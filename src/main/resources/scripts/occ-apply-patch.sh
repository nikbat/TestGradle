#!/bin/bash
#set -x

# ensures that the specified file exists
# if not then exit the script
function _ensureFile() {
  file_name=$1
  message=$2

  if [[ ! -f ${file_name} ]]; then
    echo cannot locate ${file_name} ${message}
    exit 1
  fi
}

# Upload the required bits i.e.
#   * FM Schedule
#   * phase1.py
# The arguments passed to stageBits are the following:
#   * FA host FQDN
#   * FA host user (oracle)
#   * location of private key
#   * location of FM Schedule
#   * location of phase1.py
function stageBits() {
  fa_host=$1
  identity_file=$2
  fm_schedule=$3
  script_location=$4
  proxy_host=$5

  _ensureFile ${identity_file}
  _ensureFile ${fm_schedule}
  _ensureFile ${script_location}
  _proxy_host ${identity_file} ${proxy_host}

  artifacts=(${fm_schedule} ${script_location})
  for artifact in "${artifacts[@]}"
  do
    echo uploading "${artifact}" to "${fa_host}" proxy=\'"${_OCC_PROXY}"\'
    scp -i ${identity_file} ${_OCC_PROXY}  ${artifact} opc@${fa_host}:
  done
}

function _proxy_host() {
  identity_file=$1
  proxy_host=$2

  if [[ "${proxy_host}" ]]; then
    export _OCC_PROXY="-o \"ProxyCommand ssh -i ${identity_file} opc@${proxy_host} -W %h:%p\" "
  fi
}

# Trigger the preparation and scheduling of the patch application.
#
#   * FM Schedule
#   * phase1.py
# The arguments passed to triggerScript are the following:
#  * FA - host FQDN
#  * FA - host user (oracle)
#  * identity - location of private key
#
# Returns:
# JobId - The atq job id if successfully triggered patching
function triggerScript() {
  read -r fa_host fa_user identity_file fm_schedule pod_name oss_url oss_bucket oss_user oss_password stage_dir \
    extract_dir proxy <<< $(echo $@)
  fm_schedule_file=$(basename ${fm_schedule})
  _ensureFile ${identity_file}
  _proxy_host ${identity_file} ${proxy}

  ssh -tt -i ${identity_file} ${_OCC_PROXY} opc@${fa_host} <<-EOS
    sudo cp phase1.py ${fm_schedule_file} ~${fa_user}/
    sudo chown ${fa_user}:${fa_user} ~${fa_user}/phase1.py ~${fa_user}/${fm_schedule_file}
    sudo su - ${fa_user}

    nohup python phase1.py --schedule ${fm_schedule_file} --cloud-type OCC --oss-url ${oss_url} \
      --oss-bucket ${oss_bucket} --oss-user ${oss_user} --oss-password ${oss_password} \
      --pod ${pod_name} --region OCC --stage ${stage_dir} --dest ${extract_dir} &
    exit 0
    exit 0
EOS
}

# Tail the log
# The arguments passed to tailPatchingLog are the following:
#   * FA host FQDN
#   * FA host user (oracle)
#   * location of private key
function tailPatchingLog() {
  fa_host=$1
  fa_user=$2
  identity_file=$3

  _ensureFile ${identity_file}

  ssh -i ${identity_file} ${_OCC_PROXY} opc@${fa_host} <<-EOS
    sudo su - ${fa_user}
    tail -f ~/patching/patching.log
EOS
}

function usage() {
  ACTIONS=$(typeset -f | awk '/ \(\) $/ && !/^_.* / {print $1}')

  echo "Usage:"
  echo "./occ-apply-patch.sh --identity <private key file> --script <phase1.py> \\"
  echo "    --extract-dir <extract location> --stage-dir <download location> \\"
  echo "    --host <fa host> --user <fa user host \(oracle\)> \\"
  echo "    --fm-schedule <FM Schedule> --pod <pod name> --oss-url <oss url> \\"
  echo "    --oss-bucket <oss bucket> --oss-user <oss user> --oss-password <oss password> \\"
  echo "  [action(s)]"
  echo
  echo "Available actions: [${ACTIONS}]"
  echo
  echo "Where:"
  echo
  echo " --identity|-i        location of the private key file"
  echo " --script|-s          location of the phase1.py script"
  echo " --fm-schedule|-fm    location of the FM schedule"
  echo " --extract-dir|-e     pod location where the e2e environment will be provisioned"
  echo " --stage-dir|-sd      pod location where the artifacts will be downloaded"
  echo " --host|-h            FA pod host"
  echo " --user|-u            user that will run the phase1.py script"
  echo " --oss-url|-url       OSS location"
  echo " --oss-bucket|-bucket OSS bucket"
  echo " --oss-user|-ou       OSS user"
  echo " --oss-password|-opwd OSS user password"
  echo
  echo "Example:"
  echo "   $./occ-apply-patch.sh -s ../servers/podmgr/src/main/resources/scripts/phase1.py --fm-schedule ../servers/podmgr/src/test/resources/schedule/18-05-downtime.xml -host hostname  stageBits"
  echo
  echo "   $./occ-apply-patch.sh --fm-schedule ../servers/podmgr/src/test/resources/schedule/18-05-downtime.xml -host hostname --user oracle  --oss-url http://oss.oracle.com --oss-bucket occartifacts --oss-user user --oss-password secret triggerScript"
  echo
  echo "   $./occ-apply-patch.sh -host hostname --user chhelin tailPatchingLog"
}

function parseArguments() {
 if [ $# -eq 0 ]
    then
      echo "No arguments supplied"
      usage
      exit 2
  fi

  # defaults
  IDENTITY_FILE=~/.ssh/id_rsa
  FA_USER=oracle
  ACTIONS=$(typeset -f | awk '/ \(\) $/ && !/^_.* / {print $1}')
  STAGE_DIR=/podscratch/downloads
  EXTRACT_DIR=/podscratch/e2e

  echo Actions ${ACTIONS}

  for ((i = 1; i <= $#; i++ )); do
    case "${!i}" in
      -i|--identity)
        i=$((i + 1))
        IDENTITY_FILE="${!i}"
      ;;

      -host|--fa-host)
        i=$((i + 1))
        FA_HOST="${!i}"
      ;;

      -p|--pod-name)
        i=$((i + 1))
        POD_NAME="${!i}"
      ;;

      -u|--user)
        i=$((i + 1))
        FA_USER="${!i}"
      ;;

      -fm|--fm-schedule)
        i=$((i + 1))
        FM_SCHEDULE="${!i}"
      ;;

      -url|--oss-url)
        i=$((i + 1))
        OSS_URL="${!i}"
      ;;

      -bucket|--oss-bucket)
        i=$((i + 1))
        OSS_BUCKET="${!i}"
      ;;

      -ou|--oss-user)
        i=$((i + 1))
        OSS_USER="${!i}"
      ;;

      -opwd|--oss-password)
        i=$((i + 1))
        OSS_PASSWORD="${!i}"
      ;;

     -s|--script)
        i=$((i + 1))
        SCRIPT="${!i}"
      ;;

     -sd|--stage-dir)
        i=$((i + 1))
        STAGE_DIR="${!i}"
      ;;

     -e|--extract-dir)
        i=$((i + 1))
        EXTRACT_DIR="${!i}"
      ;;

     -proxy|--proxy-host)
        i=$((i + 1))
        PROXY_HOST="${!i}"
      ;;

     --usage|-h|--help)
          shift
          usage
          exit 0
     ;;

     *)
       action=${!i}
       if [[ "${ACTIONS[@]}" =~ "${action}" ]]; then
          case ${action} in
            tailPatchingLog)
               tailPatchingLog ${FA_HOST} ${FA_USER} ${IDENTITY_FILE}
            ;;

            stageBits)
              stageBits ${FA_HOST} ${IDENTITY_FILE} ${FM_SCHEDULE} ${SCRIPT} ${PROXY_HOST}
            ;;

            triggerScript)
              triggerScript ${FA_HOST} ${FA_USER} ${IDENTITY_FILE} ${FM_SCHEDULE} ${POD_NAME} \
                ${OSS_URL} ${OSS_BUCKET} ${OSS_USER} ${OSS_PASSWORD} ${STAGE_DIR} ${EXTRACT_DIR} ${PROXY_HOST}
            ;;

            usage)
              usage
            ;;
          esac
       else
          echo here ${action}
         usage
         exit 1
       fi
     ;;
    esac
  done
}

parseArguments "$@"
