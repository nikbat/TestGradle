import argparse
import os
import logging
import ntpath
import subprocess

LCM_TOOLS='lcm_tools'
LCM_VERTEX='vertex'
OCI_REGIONS=['us-phoenix-1', 'us-ashburn-1', 'eu-frankfurt-1']
PATCHING_ARTIFACT='PATCHING'
PROV_ARTIFACT='PROV'


logger = logging.getLogger()
handler = logging.StreamHandler()
formatter = logging.Formatter(
    '%(asctime)s %(name)-12s %(levelname)-8s %(message)s')
handler.setFormatter(formatter)
logger.addHandler(handler)
logger.setLevel(logging.DEBUG)

class Component(object):
    def __init__(self, artifact_file,artifact_name,oci_profile, oci_namespace):
        self.artifact_file = artifact_file
        self.oci_profile = oci_profile
        self.artifact_name = artifact_name
        self.oci_namespace = oci_namespace

    def upload(self):
        for region in OCI_REGIONS:
            os_cmd = 'oci --profile {profile} --region {region} os object put -bn {bucket} --force --name {filename} --namespace {namespace} --file {file_path}' \
                .format(profile=self.oci_profile, region=region, bucket=self._bucket, filename=self.artifact_name, namespace = self.oci_namespace, file_path=self.artifact_file)
            logger.info('running command %s', os_cmd)
            self._run_cmd(os_cmd)


    def _run_cmd(self, cmd_list, input=None):
        logger.info('running command %s', cmd_list)
        process = subprocess.Popen(cmd_list, stdout=subprocess.PIPE, shell=True)
        output, error = process.communicate(input)
        if output:
            logger.info(output)
        if error:
            logger.error(error)



class PatchingComponent(Component):
    _bucket = 'LCM_Patching'

    def __init__(self, artifact_file, artifact_version, artifact_type, artifact_name, oci_profile, oci_namespace):
        self.artifact_file = artifact_file
        self.artifact_version = artifact_version
        self.oci_profile = oci_profile
        self.artifact_type = artifact_type
        self.oci_namespace = oci_namespace

        if (artifact_name.startswith(LCM_TOOLS) or artifact_name.startswith(LCM_VERTEX)):
            self.artifact_name = artifact_name
        else:
            self.artifact_name = artifact_version+"/"+artifact_name

        Component.__init__(self, self.artifact_file, self.artifact_name, self.oci_profile, self.oci_namespace)


class ProvComponent(Component):
    _bucket = 'PaaSProvisioning'

    def __init__(self, artifact_file, artifact_version, artifact_type, artifact_name, oci_profile, oci_namespace):
        self.artifact_file = artifact_file
        self.artifact_version = artifact_version
        self.oci_profile = oci_profile
        self.artifact_type = artifact_type
        self.oci_namespace = oci_namespace
        self.artifact_name = "ERP/"+artifact_version+"/"+artifact_name

        Component.__init__(self, self.artifact_file, self.artifact_name, self.oci_profile, self.oci_namespace)


class ComponentManager(object):
    def __init__(self, artifact_file, artifact_version, artifact_type, oci_profile_name, oci_namespace):
        self.artifact_name = self.__get_artifact_name(artifact_file)
        self.artifact_file = artifact_file
        self.artifact_version = artifact_version
        self.oci_profile_name = oci_profile_name
        self.oci_namespace = oci_namespace
        self.artifact_type = artifact_type
        self.artifact_name = self.__get_artifact_name(artifact_file)

        if self.artifact_type == PATCHING_ARTIFACT:
            self.component = PatchingComponent(self.artifact_file,self.artifact_version,self.artifact_type,self.artifact_name,self.oci_profile_name, self.oci_namespace)
        else:
            self.component = ProvComponent(self.artifact_file,self.artifact_version,self.artifact_type,self.artifact_name,self.oci_profile_name, self.oci_namespace)

    def __validate(self):
        self.__validate_oci_config()
        self.__validate_artifact_version()

    def __get_artifact_name(self, artifact_file):

        if not os.path.exists(artifact_file):
            logger.info('artifact file to be staged does not exist %s', artifact_file)
            quit()

        if not os.path.isfile(artifact_file):
            logger.info('artifact file to be staged %s, is not a file', artifact_file)
            quit()

        #self.artifact_name = os.path.basename(self.artifact_file)
        #TODO: Do we really need ntpath
        return ntpath.basename(artifact_file)

    def __validate_oci_config(self):
        if not os.path.isfile(os.getenv("HOME")+'/.oci/config'):
            logger.info('oci config file does not exist %s', self.oci_config)
            quit()

    def __validate_artifact_version(self):
        if not (self.artifact_name.startswith(LCM_TOOLS) or self.artifact_name.startswith(LCM_VERTEX)):
            if self.artifact_version is None:
                logger.info('artifact version is not defined')
                quit()
            else:
                pass
                #TODO: validate version using reg expression

    def upload(self):
        self.component.upload()


def setup_file_logger(log_file, log_level):
    logger.info("logging to %s [%s]", log_file, log_level)
    log_dir = os.path.dirname(log_file)

    if not os.path.isdir(log_dir):
        logger.info("creating logging dir %s", log_dir)
        os.makedirs(log_dir)

    fh = logging.FileHandler(log_file)
    fh.setLevel(logging._levelNames[log_level])
    fh.setFormatter(formatter)
    return fh


def parse_arguments():
    parser = argparse.ArgumentParser(description='upload artifacts to OSS')
    parser.add_argument('--file','-f', help='artifact-file to be staged')
    parser.add_argument('--version','-v', help='artifact version')
    parser.add_argument('--oci-profile-name','-pn', help='oci profile name to use in /.oci/config, defaults to DEFAULT', default='DEFAULT')
    parser.add_argument('--oci-namespace','-ns', help='oci namespace')
    parser.add_argument('--artifact-type','-at', help='artifact type', choices=['PROV', 'PATCHING'])
    #parser.add_argument('--log-file', '-lf', help='Specifies the log location, defaults to /var/log/falcm/compmgr/compmgr.log', default='/var/log/falcm/compmgr/compmgr.log')
    #parser.add_argument('--log-level', '-ll', help='Specifies the log level, defaults to DEBUG', choices=['ERROR', 'WARNING', 'INFO', 'DEBUG'], default='DEBUG')

    return parser.parse_args()

def dump_args(args):
    params = ''
    for k, v in vars(args).items():
        params = params + ", " + k + "=" + str(v)

    return params


if __name__ == '__main__':
    args = parse_arguments()
    #logger.addHandler(setup_file_logger(args.log_file, args.log_level))
    logger.info("new compmgr.py invocation [%s]", dump_args(args))
    comp_mgr = ComponentManager(args.file, args.version, args.artifact_type, args.oci_profile_name, args.oci_namespace)
    comp_mgr.upload()