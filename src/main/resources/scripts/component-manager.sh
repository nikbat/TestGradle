#! /bin/bash
#
# This script is designed as a helper to upload patch related data
# to OSS. Prerequisites include a properly setup ~/.oci directory
#
# Bucket name
DESTINATION_BUCKET=LCM_Patching
PROV_BUCKET=PaaSProvisioning
REGIONS=(us-phoenix-1 us-ashburn-1 eu-frankfurt-1)
LCM_TOOLS=lcm_tools*
LCM_VERTEX=vertex*
TYPE_PATCHING=PATCHING
TYPE_PROV=PROV

function upload_file(){
  upload_path=${1:?"--file [file] is not defined"}
  #echo up=${upload_path}

  upload_version=$2
  #echo uv=${upload_version}

  upload_filename=$(basename ${upload_path})
  #echo uf=${upload_filename}

  upload_namespace=${3:?"--namespace [namespace] is not defined"}
  #echo ns=${upload_namespace}

  artifact_type=${4:?"--type [PROV|PATCHING] is not defined"}
  #echo at=${artifact_type}

  oci_profile=${5:?"--profilename [profilename] is not defined"}

  if [ -z "$upload_version" ]
    then
      if [[ ${upload_filename} == ${LCM_TOOLS} || ${upload_filename} == ${LCM_VERTEX} ]]
        then
            echo "uploading lcm_tools/vertex without version prefix"
      else
            echo "--v [version] is not defined"
            usage
            exit 2
      fi
  fi


  rm component-manager.log
# Patching artifacts upload
  if [[ ${artifact_type} == $TYPE_PATCHING ]]
      then
    for region in ${REGIONS[@]}; do
      echo uploading ${upload_filename} to ${region}
      if [[ ${upload_filename} == ${LCM_TOOLS} || ${upload_filename} == ${LCM_VERTEX} ]]
        then
        oci --profile ${oci_profile} --region ${region} os object put -bn ${DESTINATION_BUCKET} \
        --force --name ${upload_filename} --namespace ${upload_namespace} \
        --file ${upload_path} >> component-manager.log &
      else
        oci --profile ${oci_profile} --region ${region} os object put -bn ${DESTINATION_BUCKET} \
        --force --name ${upload_version}/${upload_filename} --namespace ${upload_namespace} \
        --file ${upload_path} >> component-manager.log &
      fi
      uploads[${i}]=$!
    done

    echo waiting for the uploads to complete
    for pid in ${uploads[*]}; do
        wait $pid
    done
  fi

# Provisioning artifacts upload
  if [[ $artifact_type = $TYPE_PROV ]]
      then
    for region in ${REGIONS[@]}; do

      echo uploading ${upload_filename} to ${region}

      oci --profile ${oci_profile} --region ${region} os object put -bn ${PROV_BUCKET} \
        --force --name ERP/${upload_version}/${upload_filename} --namespace ${upload_namespace} \
        --file ${upload_path} >> component-manager.log &

      uploads[${i}]=$!

    done

      echo waiting for the uploads to complete
      for pid in ${uploads[*]}; do
          wait $pid
      done
  fi
}

function check_oci_config() {
  if [ ! -d ~/.oci/config ]; then
    echo "Please refer to https://docs.us-phoenix-1.oraclecloud.com/Content/API/Concepts/sdkconfig.htm for more information"
    exit 1
  fi
}

function check_file() {
  upload_file="$1"
  if [ ! -f "${upload_file}" ]; then
    echo File ${upload_file} cannot be found
    exit 3
  fi
}

# Check that the version is properly
function check_version() {
  version=${1:?"--v [version] is not defined"}
}

# Check that the namespace is properly
function check_namespace() {
  upload_namespace=${1:?"--ns [namespace] is not defined"}
}

# Check that the oci profile is properly
function check_ociprofile() {
  upload_ociprofile=${1:?"--pn [profilename] is not defined"}
}

function check_artifact_type() {
  artifact_type=${1:?"--type [PROV|PATCHING] is not defined"}

  case "$artifact_type" in
  "${TYPE_PROV}"|"${TYPE_PATCHING}")
      ;;
  *)
      echo "-at artifact type must be either PATCHING|PROV."
      exit 3
    ;;
  esac
}

function usage() {
  echo
  echo "Command line:"
  echo "    ./component-manager.sh -f|--file [file] -v|--version [version] -u|--usage -ns|--namespace [namepsace] -pn|--profilename [profilename] -at|--artifacttype [PROV|PATCHING]"
  echo
  echo " where:"
  echo "    file:           file that should be staged"
  echo "    version:        version of the file to be staged in the format <major>.<minor>.<micro>-<MM>-<WW>-<OO>"
  echo "                    MM - is the monthly patch bundle number"
  echo "                    WW - is the weekly patch bundle number"
  echo "                    OO - is the one of patch bundle number"
  echo "    profilename:        .oci/config profile name"
  echo "    namespace:      oci namespace"
  echo "    artifacttype:   PROV|PATCHING"
  echo
}

function parse_cli() {

  if [ $# -eq 0 ]
    then
      echo "No arguments supplied"
      usage
      exit 2
  fi

  for ((i = 1; i <= $#; i++ )); do
    case "${!i}" in
      -f|--file)
        i=$((i + 1))
        UPLOAD_FILE="${!i}"
              check_file "${!i}"
            ;;

      -v|--version)
        i=$((i + 1))
        UPLOAD_VERSION="${!i}"
              check_version "${!i}"
            ;;

            -ns|--namespace)
        i=$((i + 1))
        NAMESPACE="${!i}"
        check_namespace "${!i}"
            ;;

            -pn|--profilename)
        i=$((i + 1))
        OCI_PROFILE="${!i}"
        check_ociprofile "${!i}"
            ;;

            -at|--artifacttype)
        i=$((i + 1))
        ARTIFACT_TYPE="${!i}"
        check_artifact_type "${!i}"
            ;;

       -u|--usage)
            shift
            usage
            exit 0
       ;;

          *)
        echo Internal error!
        usage
        exit 1
      ;;
    esac
  done
}

parse_cli "$@"
upload_file "${UPLOAD_FILE}" "${UPLOAD_VERSION}" "${NAMESPACE}" "${ARTIFACT_TYPE}" "${OCI_PROFILE}"
