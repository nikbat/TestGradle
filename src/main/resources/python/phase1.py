#!/usr/bin/env python2
import argparse
import collections
import json
import logging
import os
import re
import shutil
import subprocess
import threading
import xml.dom.minidom
import xml.etree.cElementTree as ET
from datetime import datetime, timedelta
from xml.dom.minidom import Element

logger = logging.getLogger()
handler = logging.StreamHandler()
formatter = logging.Formatter(
    '%(asctime)s %(name)-12s %(levelname)-8s %(message)s')
handler.setFormatter(formatter)
logger.addHandler(handler)
logger.setLevel(logging.DEBUG)

# Named tuples
PodId = collections.namedtuple('PodId', 'name dc')


# Metatypes

class CRS(type):
    crs = {}

    def __init__(cls, name, bases, nmspc):
        super(CRS, cls).__init__(name, bases, nmspc)

    def __call__(cls, *pargs, **kwargs):
        if len(pargs) == 1 and isinstance(pargs[0], Element):
            elem = pargs[0]
            cr_id = elem.attributes['id'].value

            if cr_id in CRS.crs:
                return CRS.lookup(cr_id)

        # not in cache create new instance
        cr = super(CRS, cls).__call__(*pargs, **kwargs)
        CRS.crs[cr.id] = cr
        return CRS.lookup(cr.id)

    @staticmethod
    def lookup(cr_id):
        logger.debug("looking up %s", cr_id)
        return CRS.crs[cr_id]


class Pods(type):
    pods = {}

    def __init__(cls, name, bases, nmspc):
        super(Pods, cls).__init__(name, bases, nmspc)

    def __call__(cls, *pargs, **kwargs):
        if isinstance(pargs[0], Element):
            elem = pargs[0]
            name = elem.attributes['name'].value

            if len(pargs) == 1:
                dc = elem.attributes['DC'].value
            elif len(pargs) == 2 and isinstance(pargs[1], SuperPod):
                dc = pargs[1].dc
            else:
                dc = None

            pid = PodId(name=name, dc=dc)
            if pid in Pods.pods:
                return Pods.lookup(pid)

        # not in cache create new instance
        pod = super(Pods, cls).__call__(*pargs, **kwargs)
        pid = PodId(name=pod.name, dc=pod.dc)
        Pods.pods[pid] = pod
        return Pods.lookup(pid)

    @staticmethod
    def lookup(pod_id):
        return Pods.pods[pod_id]


class States(type):
    _states = []
    _state_file = None

    def __init__(cls, name, bases, nmspc):
        super(States, cls).__init__(name, bases, nmspc)

    def __call__(cls, *pargs, **kwargs):
        if len(pargs) == 5 and cls == State:
            state = super(States, cls).__call__(*pargs, **kwargs)
        elif len(pargs) == 1 and isinstance(pargs[0], State):
            state = pargs[0]
        else:
            state = None

        if state:
            existing = States._lookup(state)
            if existing:
                return existing

        # not in cache create new instance
        state = super(States, cls).__call__(*pargs, **kwargs)
        States._states.append(state)
        return States._lookup(state)

    @staticmethod
    def _read_states(cls):
        state_file = States._state_file
        try:
            if state_file and os.path.exists(state_file):
                with open(state_file) as f:
                    states = json.load(f)
                    all_states = []
                    for s_dict in states:
                        state = super(States, cls).__call__(**s_dict)
                        all_states.append(state)
                return all_states
        except Exception as e:
            logger.error('unable to load states %s: %s', state_file, e)

        return []

    @staticmethod
    def dump_states():
        state_str = json.dumps([s.as_json() for s in States._states], indent=2)
        try:
            state_dir = os.path.dirname(States._state_file)

            if not os.path.isdir(state_dir):
                logger.info("creating state directory %s", state_dir)
                os.makedirs(state_dir)

            with open(States._state_file, 'w+') as f:
                f.write(state_str)
        except Exception as e:
            logger.error('unable to persist states %s: %s', state_str, e)

    @staticmethod
    def _lookup(state):
        if state:
            for s in States._states:
                if s.schedule_id == state.schedule_id and s.phase == state.phase:
                    return s

    @staticmethod
    def state_file(value):
        logger.info("setting state file to %s", value)
        States._state_file = value
        States._states = States._read_states(State)


# types

class CR(object):
    __metaclass__ = CRS

    def __init__(self, *pargs):
        if len(pargs) == 1 and isinstance(pargs[0], Element):
            elem = pargs[0]
            self.id = elem.attributes['id'].value
            self.etc = self._safe_attribute(elem, 'etc')
            self.info = self._safe_attribute(elem, 'info')
            self.mode = self._safe_attribute(elem, 'mode')
            self.rel = self._safe_attribute(elem, 'rel')
            self.type = self._safe_attribute(elem, 'type')

    @staticmethod
    def _safe_attribute(element, name):
        attributes = element.attributes
        if name in attributes.keys():
            return attributes[name].value
        else:
            return None

    def __str__(self):
        return "{id=%s, etc=%s, info=%s, mode=%s, rel=%s, type=%s}" % (
            self.id, self.etc, self.info, self.mode, self.rel, self.type)

    def __repr__(self):
        return self.__str__()


class Downtime(object):
    format = '%Y%m%d%H%M%S'

    def __init__(self, *pargs):
        if len(pargs) == 1 and isinstance(pargs[0], Element):
            elem = pargs[0]
            self.end = datetime.strptime(elem.attributes['end'].value, Downtime.format)
            self.start = datetime.strptime(elem.attributes['start'].value, Downtime.format)
            self.id = elem.attributes['id'].value
            self.mode = elem.attributes['mode'].value

    def __str__(self):
        return "{start=%s, end=%s, id=%s, mode=%s}" % (
            self.start, self.end, self.id, self.mode)

    def __repr__(self):
        return self.__str__()


class SuperPod(object):
    format = '%Y%m%d%H%M%S'

    def __init__(self, *pargs):
        if len(pargs) == 1 and isinstance(pargs[0], Element):
            elem = pargs[0]
            self.dc = elem.attributes['DC'].value
            self.cdbName = elem.attributes['cdbName'].value
            self.promisedEndTime = datetime.strptime(elem.attributes['promisedEndTime'].value, Downtime.format)

            self.crs = [CR(node) for node in elem.getElementsByTagName('CR')]
            self.pods = [Pod(node, self) for node in elem.getElementsByTagName('POD')]

    def __str__(self):
        return "{dc=%s, cdbName=%s, promisedEndTime=%s, crs=%s, pods=%s}" % (
            self.dc, self.cdbName, self.promisedEndTime, self.crs, self.pods,)

    def __repr__(self):
        return self.__str__()


class Pod(object):
    __metaclass__ = Pods

    def __init__(self, *pargs):
        if isinstance(pargs[0], Element):
            elem = pargs[0]
            self.name = elem.attributes['name'].value
            self.patchingCadence = elem.attributes['patchingCadence'].value

            if len(pargs) == 1:
                self.dc = elem.attributes['DC'].value
                self.crs = [CR(cr) for cr in elem.getElementsByTagName("CR")]
            elif len(pargs) == 2 and isinstance(pargs[1], SuperPod):
                super_pod = pargs[1]
                self.dc = super_pod.dc
                self.crs = super_pod.crs

    def __str__(self):
        return "{dc=%s, name=%s, patchingCadence=%s, crs=%s}" % (
            self.dc, self.name, self.patchingCadence, self.crs)

    def __repr__(self):
        return self.__str__()


class Schedule(object):

    def __init__(self, fm_schedule):
        self.fm_schedule = fm_schedule

        dom = xml.dom.minidom.parse(fm_schedule)
        assert dom.documentElement.tagName == "Schedule"

        for downtime in dom.documentElement.getElementsByTagName("Downtime"):
            self.downTime = Downtime(downtime)

        for node in dom.documentElement.getElementsByTagName("CRS"):
            self.crs = [cr for cr in self._parse_crs(node)]

        logger.info("parsed CRS: %s", self.crs)

        for pods in dom.documentElement.getElementsByTagName("PODS"):
            self.pods = [pod for pod in self._parse_pods(pods)]

        logger.info("parsed Pods: %s", self.pods)

    @staticmethod
    def _parse_crs(crs):
        for node in crs.getElementsByTagName("CR"):
            yield CR(node)

    @staticmethod
    def _parse_pods(pods):
        for node in pods.childNodes:
            if isinstance(node, Element):
                if node.tagName == 'SuperPOD':
                    SuperPod(node)
                elif node.tagName == 'POD':
                    yield Pod(node)
                else:
                    raise Exception("Unknown element %s" % (node.tagName,))


class OutputParser(object):
    def parse(self, output, error):
        return self


class JobIdParser(OutputParser):
    regex = re.compile(r'job (\d*) at.*')

    def __init__(self):
        self.job_id = 0

    def parse(self, output, error):
        matcher = self.regex.match(error)
        if matcher:
            self.job_id = int(matcher.group(1))
            logger.info("parsed job id %d", self.job_id)
        return self


class PodNameParser(OutputParser):
    regex = re.compile(r'pod.fact.name=(.*)')

    def __init__(self):
        self.pod_name = "UNDEFINED"

    def parse(self, output, error):
        matcher = self.regex.match(output)
        if matcher:
            self.pod_name = matcher.group(1)
            logger.info("parsed pod name %s", self.pod_name)
        return self


class LCMBase(object):
    @staticmethod
    def _ensure_directory(directory):
        if not os.path.isdir(directory):
            try:
                logger.info("creating directory %s", directory)
                os.makedirs(directory)
            except OSError as e:
                logger.warn("unable to create directory %s: %s", directory, e)

    def _rm_directory(self, directory):
        if os.path.isdir(directory):
            logger.info("removing directory %s", directory)
            self._run_cmd("xargs -P64 rm -rf", std_input="find %s " % (directory,))

    def _extract(self, archive_path, dest_path):
        self._ensure_directory(dest_path)

        if archive_path.endswith('tar.gz'):
            logger.info('extracting tarball %s to %s', archive_path, dest_path)
            self._run_cmd(['tar', '-zxvf', archive_path, '-C', dest_path])
            logger.info('completed extraction of %s to %s', archive_path, dest_path)
            return dest_path

        elif archive_path.endswith(('.zip', '.gz')):
            logger.info('extracting zip %s to %s', archive_path, dest_path)
            self._run_cmd(['unzip', '-K', '-o', '-d', dest_path, archive_path])
            logger.info('completed extraction of %s to %s', archive_path, dest_path)
            return dest_path

        else:
            logger.error("not a known extract format, will just copy: '%s' to '%s'", archive_path)
            file_name = os.path.basename(archive_path)
            dest_path_file = os.path.join(dest_path, file_name)
            shutil.copyfile(archive_path, dest_path_file)
            return dest_path_file

    @staticmethod
    def _run_cmd(cmd_list, std_input=None, here_doc=None, parser=None):
        if isinstance(cmd_list, str) or isinstance(cmd_list, unicode):
            cmd_list = cmd_list.split()

        if here_doc:
            cmd_list.append(here_doc)

        logger.info('running command "%s", input: "%s", heredoc: "%s"', ' '.join(cmd_list), std_input, here_doc)
        process = subprocess.Popen(cmd_list, stdout=subprocess.PIPE, stdin=subprocess.PIPE, stderr=subprocess.PIPE)
        output, error = process.communicate(std_input)

        if parser:
            return parser.parse(output, error)
        else:
            # no custom output parser... just echo
            if output:
                logger.info(output)
            if error:
                logger.error(error)
            return process.returncode


class State(object):
    __metaclass__ = States

    def __init__(self, schedule_id, phase, steps, job_id, self_job):
        self.schedule_id = schedule_id
        self.phase = phase
        if not isinstance(steps, list):
            self.steps = []
            if steps:
                self.steps.append(steps)
        else:
            self.steps = steps
        self.job_id = int(job_id)
        self.self_job = int(self_job)

    def as_json(self):
        return json.loads(self.__str__())

    def __str__(self):
        return json.dumps(self.__dict__, indent=2)

    def __repr__(self):
        return self.__str__()

    def complete_step(self, step):
        if step in self.steps:
            logger.warn('step already completed %s', step)
            return False
        self.steps.append(step)
        States.dump_states()
        return True

    def done_step(self, step):
        return step in self.steps


class CloudType(LCMBase):
    _lcm_tools_location = ''
    _lcm_tools_archive_name = 'lcm_tools.zip'
    _lcm_tools_destination = 'lcm-tools'

    def __init__(self, cloud_type, url, oss_bucket):
        self.cloud_type = cloud_type
        self.url = url
        self.oss_bucket = oss_bucket

    def download(self, name, destination):
        logger.info('downloading "%s" to "%s"', name, destination)
        return self._download(self._append_url(self.url, name), destination)

    def _download(self, url, file_path):
        curl = 'curl -S -o {file_path} --retry 3 {url}'.format(url=url, file_path=file_path)
        logger.info(curl)
        self._run_cmd(curl)
        return file_path

    @staticmethod
    def _append_url(url, elem):
        if elem.startswith('/') and url.endswith('/'):
            return url + elem[1:]
        elif elem.startswith('/') or url.endswith('/'):
            return url + elem
        else:
            return url + '/' + elem

    def _cr_path(self, cr):
        return self.storage_prefix.format(**cr.__dict__)

    def download_artifact(self, cr, destination):
        logger.info("inside download_artifact %s, %s ", destination, cr)
        tmp_path = os.path.join(destination, cr.rel)
        self._ensure_directory(tmp_path)

        tmp_file_path = os.path.join(tmp_path, self.artifact_name.format(**cr.__dict__))
        if not os.path.exists(tmp_file_path):
            logger.info("download path exists %s", tmp_file_path)
            self.download(self._cr_path(cr), tmp_file_path)
        else:
            logger.info("artifact already downloaded %s.. skipping download", tmp_file_path)

        return tmp_file_path

    def download_tools(self, destination):
        tmp_path = os.path.join(destination, self.lcm_tools_location)
        self._ensure_directory(tmp_path)
        tmp_file_path = os.path.join(tmp_path, self.lcm_tools_archive_name)
        self._rm_directory(tmp_file_path)
        logger.info("downloading tools %s to %s", self.lcm_tools_archive_name, tmp_file_path)
        self.download(self.lcm_tools_archive_name, tmp_file_path)
        return tmp_file_path

    @property
    def lcm_tools_location(self):
        return self._lcm_tools_location

    @property
    def lcm_tools_archive_name(self):
        return self._lcm_tools_archive_name

    @property
    def lcm_tools_destination(self):
        return self._lcm_tools_destination

    @property
    def storage_prefix(self):
        raise Exception('Not defined')

    @property
    def artifact_name(self):
        raise Exception('Not defined')

    @property
    def bucket(self):
        return self.oss_bucket


class OCC(CloudType):

    def __init__(self, oss_user, oss_password, oss_url, oss_bucket):
        self.oss_user = oss_user
        self.oss_password = oss_password
        self.oss_url = oss_url
        self.oss_bucket = oss_bucket
        CloudType.__init__(self, 'OCC', self._localize_url(), oss_bucket)

    @property
    def storage_prefix(self):
        return '/%s' % (self.artifact_name,)

    @property
    def artifact_name(self):
        return '{info}.zip'

    def _download(self, url, file_path):
        curl = 'curl -S -o {file_path} --retry 3 -H --auth-no-challenge --user {user}:{password} {url}'.format(
            user=self.oss_user, password=self.oss_password, url=url, file_path=file_path)
        logger.info(curl)
        self._run_cmd(curl)
        return file_path

    def _localize_url(self):
        return self._append_url(self.oss_url, self.oss_bucket)


class OCI(CloudType):
    default_region = 'us-phoenix-1'

    localized_urls = {
        'eu-frankfurt-1': 'swiftobjectstorage.eu-frankfurt-1.oraclecloud.com',
        'uk-london-1': 'swiftobjectstorage.uk-london-1.oraclecloud.com',
        'us-phoenix-1': 'swiftobjectstorage.us-phoenix-1.oraclecloud.com',
        'us-ashburn-1': 'swiftobjectstorage.us-ashburn-1.oraclecloud.com',
    }

    base_url = 'https://{localized_host}/v1/oraclefaonbm/{oss_bucket}'

    def __init__(self, region, oss_bucket):
        self.region = region
        CloudType.__init__(self, 'OCI', self._localize_url(oss_bucket), oss_bucket)

    def _localize_url(self, oss_bucket):
        if self.region in self.localized_urls:
            region_url = self.localized_urls[self.region]
        else:
            logger.warn("cannot localize %s will use default url", self.region)
            region_url = self.localized_urls[self.default_region]
        return self.base_url.format(localized_host=region_url, oss_bucket=oss_bucket)

    @property
    def storage_prefix(self):
        return '/{rel}/%s' % (self.artifact_name,)

    @property
    def artifact_name(self):
        return '{info}.tar.gz'


class E2EEnv(LCMBase):
    _e2e_properties = {
        "INSTANCE_SUFFIX": "E2EOCI",
        "SHARED_E2E_LOCATION": "{EXTRACT_DIR}/{INSTANCE_SUFFIX}",
        "ORCH_LOCATION": "{SHARED_E2E_LOCATION}/rel-generic/orchestration",
        "TLO_HOME": "{SHARED_E2E_LOCATION}/rel-generic/TLO_HOME",
        "FLO_HOME": "{SHARED_E2E_LOCATION}/FLO_HOME",
        "RUPLITE_FLO_LOCATION": "{FLO_HOME}",
        "RELEASE": "{RELEASE}",
        "PATCH_BASE_DIR": "{EXTRACT_DIR}/patches",
        "TLO_PATCHING_HOME": "{SHARED_E2E_LOCATION}/rel-generic/TLO_HOME",
        "HLO_PATCHING_HOME": "{SHARED_E2E_LOCATION}/rel-generic/orchestration",
        "RATS_LIB_DIR": "{EXTRACT_DIR}/rats/lib",
        "E2E_TRANSPORT_ENABLED": "True",
        "ENABLE_NEW_QUIESCE_MODEL": "false",
        "FM_SCHEDULE_DIR": "{EXTRACT_DIR}/schedules",
        "FM_SCHEDULE_ARCHIVE_DIR": "{EXTRACT_DIR}/schedule-archive",
        "DEFAULT_NONROOT_GROUP": "oracle",
        "DEFAULT_NONROOT_UID": "501",
        "DEFAULT_JAVA_HOME": "/u01/APPLTOP/fusionapps/jdk",
        "EMAIL_TO": "test@oracle.com",
        "EMAIL_ADDR": "test@oracle.com",
        "WALLET_LOCATION": "/fsnadmin/saas/lcm/zfs/lcm_wallet.sso",
        "ZFS_WALLET_LOC": "/fsnadmin/saas/lcm/zfs/lcm_wallet.sso",
        "SSH_PRIVATE_KEY_PATH": "/foo/bar/.ssh/id_rsa",
        "DB_BACKUP_SCRIPT": "{lcm-tools}/lcm-psm-services/backup/lcm_db_backup.py",
        "EM_SET_BLACKOUT_SCRIPT": "su - emcadm -c \"/podscratch/cloudem/FA_create_app_blackout e2e-bk1-`hostname -s`\"",
        "EM_REMOVE_BLACKOUT_SCRIPT": "su - emcadm -c \"/podscratch/cloudem/stop_blackout e2e-bk1-`hostname -s`\"",
        "EM_BLACKOUT_SCRIPT": "/podscratch/cloudem/cloudEmServiceBlackoutOpOcm.sh",
        "WLSDIAGNOSTICS_SHOULD_EXECUTE": "false",
        "FEEDBACK_UTIL_LOC": "{SHARED_E2E_LOCATION}/lcmtools/rel-generic/feedback_util",
        "LOG_LOCATION": "/u01/APPLTOP/instance/lcm/logs/e2ewrapper",
        "OCM_META_DATA_PROVIDER": "abc",
        "TURN_OFF_UNIVERSAL_BACKUP": "true"
    }

    _e2e_extract_locations = {
        "fasaas-e2egeneric": "{SHARED_E2E_LOCATION}",
        "fainst-e2ewrapper": "{SHARED_E2E_LOCATION}",
        "p2t-snapshot": "{ORCH_LOCATION}/scripts/p2t/snapshot",
        "hc-override": "{SHARED_E2E_LOCATION}/rel-generic/orchruntime/tmp_upgrade_work_area/healthchecker/common",
        "ojvm-control": "{SHARED_E2E_LOCATION}/lcmtools/rel-generic/ojvmcontrol",
        "lcm-feedback": "{SHARED_E2E_LOCATION}/lcmtools/rel-generic",
        "lcm-tools": "{SHARED_E2E_LOCATION}/lcmtools/rel-generic"
    }

    _e2e_config_files = {
        "e2ewrapper_config": "{SHARED_E2E_LOCATION}/e2ewrapper/config/e2ewrapper.properties",
        "tlo_config": "{TLO_HOME}/config/tlo.properties",
        "pod_properties_template_config": "{ORCH_LOCATION}/config/patching/pod.properties.template",
        "flo_config": "{FLO_HOME}/metadata/env.properties",
        "flo_installer_config": "{FLO_HOME}/metadata/installer.properties",
        "hlo_properties": "{HLO_PATCHING_HOME}/config/env.properties",
        "podmetadata_properties": "{FLO_HOME}/podmetadata/podmetadata.properties",
        "tlo_env_props": "{TLO_HOME}/config/env.properties"
    }

    _e2e_inject_properties = {
        "podmetadata_properties": "OCM_META_DATA_PROVIDER"
    }

    def __init__(self, stage_dir, extract_dir, release):
        self.stage_dir = stage_dir
        self.extract_dir = extract_dir
        self.release = release
        self.e2e_env = self._interpolate_e2e()

    def configure_e2e(self, schedule, pod_name):
        logger.info("configuring e2e tools into place")

        for cf_tokenized in self._e2e_config_files:
            cf = self.e2e_env.get(cf_tokenized)
            logger.info("fixing %s", cf)
            cf_properties = self._read_properties(cf)
            shutil.move(cf, cf + ".o")
            self._write_java_properties(cf, cf_properties, cf_tokenized)

        self._ensure_directory(self.e2e_env["FM_SCHEDULE_DIR"])
        self._ensure_directory(self.e2e_env["FM_SCHEDULE_ARCHIVE_DIR"])
        self._ensure_directory(self.e2e_env["PATCH_BASE_DIR"])
        self._copy_schedule(schedule.fm_schedule, self.e2e_env["FM_SCHEDULE_DIR"], pod_name)
        logger.info("e2e has been configured")

    def _copy_schedule(self, schedule_file, dest, pod_name):
        logger.info("logical pod name %s", pod_name)
        introspection_cmd = "/u01/lcm/podmetadata/bin/get_prop.py pod.fact.name"
        parser = PodNameParser()
        self._run_cmd(introspection_cmd, parser=parser)
        physical_pod_name = parser.pod_name

        ns_v1 = '{http://xmlns.oracle.com/falcm/flo/downtimeschedule/V1.0}'
        ns_v2 = '{http://xmlns.oracle.com/falcm/flo/downtimeschedule/V2.0}'

        tree = ET.parse(schedule_file)
        root = tree.getroot()
        ns = reduce(lambda a, ns: a or root.findall(ns + 'Downtime') and ns, [ns_v1, ns_v2], "") or ""

        pod_list = root.findall('.//' + ns + 'POD')
        for pod_elem in pod_list:
            p_name = pod_elem.attrib['name']
            if p_name == pod_name:
                logger.info("changing %s to %s", pod_name, parser.pod_name)
                pod_elem.set("name", physical_pod_name)
                break
        tree.write(os.path.join(dest, os.path.basename(schedule_file)))

    def __getitem__(self, item):
        return self.e2e_env[item]

    @property
    def e2e_extract_locations(self):
        return self._e2e_extract_locations

    def __str__(self):
        return str(self.e2e_env)

    def __repr__(self):
        return self.__str__()

    def _interpolate_e2e(self):
        logger.info('interpolating E2E environment for %s', self.release)
        symbols = {"DOWNLOAD_DIR": self.stage_dir,
                   "EXTRACT_DIR": self.extract_dir,
                   "RELEASE": self.release}

        symbols.update(self._e2e_properties)
        symbols.update(self._e2e_config_files)
        symbols.update(self._e2e_extract_locations)

        symbols = self._interpolate(symbols)
        logging.info("finished interpolating %s", symbols)
        return symbols

    def _write_java_properties(self, cf, cf_properties, cf_tokenized):
        logger.info("writing property file '%s'", cf)
        with open(cf, 'w') as f:
            for key, value in cf_properties.items():
                f.write("%s=%s\n" % (key, value if key not in self.e2e_env else self.e2e_env[key]))

            if cf_tokenized in self._e2e_inject_properties:
                key = self._e2e_inject_properties.get(cf_tokenized)
                prop = "%s=%s\n" % (key, self.e2e_env[key])
                logger.debug('adding %s to %s', prop, cf)
                f.write(prop)

    @staticmethod
    def _read_properties(file_name):
        return dict((l.rstrip().split('=', 1) + [None])[:2]
                    for l in open(file_name)
                    if l.strip() and not l.startswith("#") and not l.startswith("\n"))

    def _interpolate(self, variables):
        logger.info("will interpolate %s", variables)

        while True:
            interpolated, missing, symbols = self._replace_variables(variables)
            logger.info("interpolated %d, missing %d symbols %s", interpolated, missing, symbols)

            if missing > 0 and interpolated == 0:
                raise Exception('Unable to interpolate %s' % (variables,))

            if missing == 0 and interpolated == 0:
                return variables

    @staticmethod
    def _replace_variables(variables):
        inter = 0
        miss = 0

        for (key, value) in variables.items():
            try:
                logger.debug("interpolating %s -> %s", key, value)
                val = value.format(**variables)

                variables[key] = val
                if val != value:
                    logger.debug("interpolated %s -> %s (%d)", key, val, inter)
                    inter += 1

            except (KeyError, TypeError):
                miss += 1
                logger.info('cannot find specified key %s for "%s" (%d)', key, value, miss)

        return inter, miss, variables


class Phase1(LCMBase):
    lcm_tools_location = ''
    lcm_tools_archive_name = 'lcm_tools.zip'
    lcm_tools_destination = 'lcm-tools'

    def __init__(self, arguments):
        self.stage_dir = os.path.expanduser(arguments.stage)
        self.extract_dir = os.path.expanduser(arguments.dest)
        self.pod_name = arguments.pod
        self.region = arguments.region
        self.cloud_env = arguments.cloud_type
        self.pod_id = PodId(self.pod_name, self.region)
        self.oss_user = arguments.oss_user
        self.oss_password = arguments.oss_password
        self.oss_url = arguments.oss_url
        self.oss_bucket = arguments.oss_bucket
        self.keep_downloads = arguments.keep_downloads

        log_handler = self._setup_file_logger(os.path.expanduser(arguments.log_file), arguments.log_level)
        if log_handler:
            logger.addHandler(log_handler)

        logger.info("new phase1.py invocation [%s]", self._dump_args(args))
        if self.cloud_env is None or self.cloud_env == 'OCI':
            self.cloud_type = OCI(self.region, self.oss_bucket)
        elif self.cloud_env == 'OCC':
            self._check_parameter(self.oss_user, "No OSS User specified")
            self._check_parameter(self.oss_password, "No OSS Password specified")
            self._check_parameter(self.oss_url, "No OSS URL specified")
            self._check_parameter(self.oss_bucket, "No OSS Bucket specified")
            self.cloud_type = OCC(self.oss_user, self.oss_password, self.oss_url, self.oss_bucket)
        else:
            logger.error('unknown cloud environment specified "%s"', self.cloud_env)
            exit(1)

        self.fm_schedule = os.path.expanduser(args.schedule)
        if self.fm_schedule and os.path.exists(self.fm_schedule):
            logger.info('reading schedule %s', self.fm_schedule)
            self.schedule = Schedule(self.fm_schedule)
        else:
            logger.error('no FM Schedule specified or cannot be found (%s)', self.fm_schedule)
            exit(1)

        # Make sure the output directories exists
        self._check_dir_parameter(self.extract_dir, "no extract location specified")
        self._check_dir_parameter(self.stage_dir, "no staging location specified")

        release = self._find_crs()[0].rel
        self.e2e_env = E2EEnv(self.stage_dir, self.extract_dir, release)
        self.state = State(self.schedule.downTime.id, self.schedule.downTime.mode, [], -1, -1)
        logger.info("current state %s", self.state)
        self.state.complete_step('init')

    def download_tools(self):
        if not self.state.done_step('download_tools'):
            logger.info("downloading tools")
            tmp_file_path = self.cloud_type.download_tools(self.stage_dir)
            dest_path = os.path.join(self.extract_dir, self.cloud_type.lcm_tools_destination)
            self._rm_directory(dest_path)
            self._ensure_directory(dest_path)
            self._extract(tmp_file_path, dest_path)
            logger.info('completed download of tools to: "%s"', dest_path)
            self._copy_tools(dest_path)
            self.state.complete_step('download_tools')
        else:
            logger.info("tools already downloaded")

    def configure_e2e(self):
        if not self.state.done_step('configure_e2e'):
            self.e2e_env.configure_e2e(self.schedule, self.pod_name)
            self.state.complete_step('configure_e2e')
        else:
            logger.info('already performed step "configure_e2e"')

        if not self.keep_downloads:
            logger.info("instructed not to retain downloads; deleting %s", self.stage_dir)
            self._rm_directory(self.stage_dir)

    def schedule_phase1(self):
        schedule_id = self.schedule.downTime.id
        delta = timedelta(hours=6)
        when = self.schedule.downTime.start - delta

        if not self.state.done_step('schedule_phase1'):
            now = datetime.now()
            if when < now:
                logger.error('schedule time: %s predates now %s', when, now)
                raise Exception('unable to schedule since the schedule time: %s is in the past, current time %s' % (when, now))

            logger.info("scheduling phase 1")

            here_doc = """python {script} --schedule {schedule_file} --pod {pod} --region {region} --stage {stage} --dest {dest}""".format(
                script=os.path.realpath(__file__), schedule_file=self.fm_schedule, pod=self.pod_name,
                region=self.region, stage=self.stage_dir, dest=self.extract_dir)
            when = when.strftime("%Y%m%d%H%M")
            bash_command = "at -t {when}".format(when=when)

            logger.info('scheduling phase 1 to run at %s: "%s"', when, bash_command)
            # TODO: Here we should send in a parser to parse the work id..
            parser = JobIdParser()
            self._run_cmd(bash_command, std_input=here_doc, parser=parser)
            self.state.self_job = parser.job_id
            logger.info('job %s at %s scheduled [%s]', self.state.self_job, when, schedule_id)
            self.state.complete_step('schedule_phase1')
        else:
            logger.info('already performed step "schedule_phase1"')
            logger.info('job %s at %s already scheduled [%s]', self.state.self_job, when, schedule_id)

    def schedule_phase2(self):
        schedule_id = self.schedule.downTime.id
        when = self.schedule.downTime.start.strftime("%Y%m%d%H%M")

        if not self.state.done_step('schedule_phase2'):
            logger.info("scheduling phase 2")

            e2e_start = os.path.join(self.e2e_env["FLO_HOME"], "public_scripts", "fm_flo_startdrive.sh")
            here_doc = """sudo {e2e} """.format(e2e=e2e_start)
            bash_command = "at -t {when}".format(when=when)

            logger.info('scheduling phase 2 to run at %s: "%s"', when, bash_command)
            # TODO: Here we should send in a parser to parse the work id..
            parser = JobIdParser()
            self._run_cmd(bash_command, std_input=here_doc, parser=parser)
            self.state.job_id = parser.job_id

            logger.info('job %s at %s scheduled [%s]', self.state.job_id, when, schedule_id)
            self.state.complete_step('schedule_phase2')
        else:
            logger.info('already performed step "schedule_phase2"')
            logger.info('job %s at %s already scheduled [%s]', self.state.job_id, when, schedule_id)

    def download_artifacts(self):
        if not self.state.done_step('download_artifacts'):
            logger.info('will download artifact(s) for %s to %s', self.pod_id, self.stage_dir)
            threads = [threading.Thread(target=self._download_artifact, args=(cr,)) for cr in self._find_crs()]
            for thread in threads:
                thread.start()

            for thread in threads:
                thread.join()
            # now persist the 'download_artifacts' state
            self.state.complete_step('download_artifacts')
        else:
            logger.info('already performed step "download_artifacts"')

    def setup_file_logger(self, log_file, log_level):
        try:
            logger.info("logging to %s [%s]", log_file, log_level)
            log_dir = os.path.dirname(log_file)

            if not os.path.isdir(log_dir):
                logger.info("creating logging directory %s", log_dir)
                self._ensure_directory(log_dir)

            fh = logging.FileHandler(log_file)
            fh.setLevel(logging._levelNames[log_level])
            fh.setFormatter(formatter)
            return fh
        except Exception as e:
            logger.error("cannot setup log file %s: e", log_file, e)

    def _copy_tools(self, extract_dir):
        logger.info("copying tools into place: %s", extract_dir)
        for tool in os.listdir(extract_dir):
            keys = filter(lambda x: tool.startswith(x), self.e2e_env.e2e_extract_locations)
            if keys:
                dest_path = self.e2e_env[keys[0]]
                self._extract(os.path.join(extract_dir, tool), dest_path)

    @staticmethod
    def _restructure_p4fa(p4fa_path):
        logger.info("restructuring P4FA patch bundle %s", p4fa_path)
        patch_work_dirs = map(lambda x: os.path.join(p4fa_path, x, "patch_work_dir"), os.listdir(p4fa_path))

        for p in patch_work_dirs:
            try:
                dirs_to_move = os.listdir(p)
                dest_dir = os.path.split(p)[0]
                for d in dirs_to_move:
                    f = os.path.join(p, d)
                    logger.debug('moving %s to %s' % (f, dest_dir))
                    shutil.move(f, dest_dir)
            except Exception as e:
                logger.warn('issue moving %s: %s', p, e)

    @staticmethod
    def _workaround_falcmoci_1554(fapb_path, cr_name):
        file_name = '/u01/FM/PATCH/podinfo.txt'
        logger.warn("workaround for FALCMOCI-1554")
        try:
            fa_path = os.path.abspath(os.path.join(fapb_path, cr_name))
            with open(file_name, 'w') as f:
                variable = "export FSNADMIN_LOC=%s/scripts/fsnadmin" % (fa_path,)
                logger.info('adding "%s" to: %s', variable, file_name)
                f.write(variable)
        except Exception as e:
            logger.error("unable to modify %s: %s", file_name, e)

    def _find_crs(self):
        try:
            pod = Pods.pods[self.pod_id]
        except KeyError:
            raise Exception("Cannot 'self' pod %s in %s" % (self.pod_id, Pods.pods.keys()))

        return pod.crs

    @staticmethod
    def _dump_args(args):
        return ", ".join([k + "=" + str(v) for k, v in vars(args).items()])

    def _download_artifact(self, cr):
        logger.info("_download_artifact %s", cr)
        tmp_file_path = self.cloud_type.download_artifact(cr, self.stage_dir)

        cr_type = cr.type.replace("PREDOWN_", "")
        dest_path = os.path.join(self.extract_dir, "patches", cr.rel, cr_type) \
            if not (cr_type == "P4FA") else os.path.join(self.extract_dir, "patch_work_dir")

        if os.path.exists(dest_path):
            logger.warn('component %s already extracted %s', cr_type, dest_path)
            if not self.keep_downloads:
                logger.info('removing previous %s extract %s', cr_type, dest_path)
                self._rm_directory(dest_path)

        if not os.path.exists(dest_path):
            self._ensure_directory(dest_path)
            self._extract(tmp_file_path, dest_path)

        if cr_type == "P4FA":
            self._restructure_p4fa(dest_path)
        elif cr_type == "FAPB":
            self._workaround_falcmoci_1554(dest_path, cr.info)

    @staticmethod
    def _check_parameter(param, message):
        if not param:
            logger.error(message)
            exit(1)

    def _check_dir_parameter(self, param, message):
        if param:
            self._ensure_directory(param)
        else:
            logger.error(message)
            exit(1)

    @staticmethod
    def _setup_file_logger(log_file, log_level):
        try:
            logger.info("logging to %s [%s]", log_file, log_level)
            log_dir = os.path.dirname(log_file)

            if not os.path.isdir(log_dir):
                logger.info("creating logging directory %s", log_dir)
                LCMBase._ensure_directory(log_dir)

            fh = logging.FileHandler(log_file)
            fh.setLevel(logging._levelNames[log_level])
            fh.setFormatter(formatter)
            return fh
        except:
            logger.error("cannot setup log file %s", log_file)


def parse_arguments():
    parser = argparse.ArgumentParser(description='Perform phase 1 tasks.')
    parser.add_argument('--schedule', '-fs', help='the location of the Fleet Manager schedule')
    parser.add_argument('--stage', '-s', help='location where to stage the bits', default='/podscratch/downloads')
    parser.add_argument('--dest', '-d', help='location where the expanded bits goes', default='/podscratch/e2e')
    parser.add_argument('--pod', '-p', help='name of the pod')
    parser.add_argument('--region', '-r', help='region of the pod, required for OCI environment',
                        choices=['eu-frankfurt-1', 'uk-london-1', 'us-phoenix-1', 'us-ashburn-1', 'OCC'],
                        default='us-phoenix-1')
    parser.add_argument('--cloud-type', '-ct', help='environment of the pod', choices=['OCI', 'OCC'], default='OCI')
    parser.add_argument('--oss-user', '-ou', help='OSS user name, required for OCC environment')
    parser.add_argument('--oss-password', '-op', help='OSS password, required for OCC environment')
    parser.add_argument('--oss-url', '-ourl', help='OSS url, with domain name, for OCC environment')
    parser.add_argument('--oss-bucket', '-obucket',
                        help='OSS bucket where patching artifacts are staged for OCC environment', default='LCM_Patching')
    parser.add_argument('--log-file', '-lf',
                        help='Specifies the log location, defaults to ~/patching/patching.log',
                        default='~/patching/patching.log')
    parser.add_argument('--log-level', '-ll', help='Specifies the log level, defaults to INFO',
                        choices=['ERROR', 'WARNING', 'INFO', 'DEBUG'], default='DEBUG')
    parser.add_argument('--state-file', '-sf',
                        help='Specifies the state file location, defaults to ~/patching/patching-states.json',
                        default='~/patching/patching-states.json')
    parser.add_argument('--keep-downloads', '-kd',
                        help='If specified on the command line then keep downloaded artifacts, otherwise all downloads'
                             'will be removed at completion of extraction', action='store_true')
    parser.add_argument('actions', help='actions to be invoked (in order)', nargs='?',
                        default=['download_artifacts', 'download_tools', 'configure_e2e', 'schedule_phase2'],
                        choices=['download_artifacts', 'download_tools', 'configure_e2e', 'schedule_phase1',
                                 'schedule_phase2'])
    return parser.parse_args()


def trigger_actions(phase, actions):
    if actions and not isinstance(actions, list):
        actions = [actions]

    logger.info('will perform actions %s', actions)
    for action in actions:
        logger.info('invoking %s', action)
        getattr(phase, action)()


if __name__ == '__main__':
    args = parse_arguments()
    States.state_file(os.path.expanduser(args.state_file))
    phase = Phase1(args)
    actions = args.actions
    trigger_actions(phase, args.actions)
