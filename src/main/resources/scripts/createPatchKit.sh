#!/usr/bin/env bash

#
# This script creates an archive consisting of terraform scripts to provision a control plane
#

createPatchKit() {
  if [ -d "$OUT_DIR" ]; then
    mkdir -p ${OUT_DIR}/falcmoci-patchkit
    rm -rf ${OUT_DIR}/falcmoci-patchkit/*

    cp -r ../provisioning ${OUT_DIR}/falcmoci-patchkit/
    mkdir -p ${OUT_DIR}/falcmoci-patchkit/provisioning/staging/configs/podmgr
    mkdir -p ${OUT_DIR}/falcmoci-patchkit/provisioning/staging/configs/saasmgr
    mkdir -p ${OUT_DIR}/falcmoci-patchkit/provisioning/staging/templates/podmgr
    mkdir -p ${OUT_DIR}/falcmoci-patchkit/provisioning/staging/templates/saasmgr

    cp -r ../config/test/tenancy_example/.oci ${OUT_DIR}/falcmoci-patchkit/provisioning/staging/configs/podmgr/
    cp -r ../config/test/tenancy_example/.psmoci ${OUT_DIR}/falcmoci-patchkit/provisioning/staging/configs/podmgr/
    cp -r ../config/test/tenancy_example/.ssh ${OUT_DIR}/falcmoci-patchkit/provisioning/staging/configs/podmgr/
    cp -r ../config/test/tenancy_example/config.properties ${OUT_DIR}/falcmoci-patchkit/provisioning/staging/configs/podmgr/


    cp -r ../config/test/tenancy_example/.ssh ${OUT_DIR}/falcmoci-patchkit/provisioning/staging/configs/saasmgr/
    cp -r ../config/test/tenancy_example/config.properties ${OUT_DIR}/falcmoci-patchkit/provisioning/staging/configs/saasmgr/

    cp -r ../servers/podmgr/src/test/resources/configs/.falcm ${OUT_DIR}/falcmoci-patchkit/provisioning/staging/templates/podmgr/
    cp -r ../servers/podmgr/src/test/resources/configs/.oci ${OUT_DIR}/falcmoci-patchkit/provisioning/staging/templates/podmgr/
    cp -r ../servers/podmgr/src/test/resources/configs/.psmoci ${OUT_DIR}/falcmoci-patchkit/provisioning/staging/templates/podmgr/

    cp -r ../servers/saasmgr/src/test/resources/configs/.falcm ${OUT_DIR}/falcmoci-patchkit/provisioning/staging/templates/saasmgr/


    sed -i -e 's#config_dir=""#config_dir="../configs/podmgr"#' ${OUT_DIR}/falcmoci-patchkit/provisioning/staging/lcm-docker/env-var.template
    sed -i -e 's#config_dir=""#config_dir="../configs/saasmgr"#' ${OUT_DIR}/falcmoci-patchkit/provisioning/staging/ssm-docker/env-var.template
    sed -i -e 's#template_dir=""#template_dir="../templates/podmgr"#' ${OUT_DIR}/falcmoci-patchkit/provisioning/staging/lcm-docker/env-var.template
    sed -i -e 's#template_dir=""#template_dir="../templates/saasmgr"#' ${OUT_DIR}/falcmoci-patchkit/provisioning/staging/ssm-docker/env-var.template


    cd ${OUT_DIR}
    if [ -s "falcmoci-patchkit.tar.gz" ]; then
      echo "Removing old archive falcmoci-patchkit.tar.gz"
      rm falcmoci-patchkit.tar.gz
    fi
    tar --exclude='.terraform' -zcvf falcmoci-patchkit.tar.gz falcmoci-patchkit
    echo "Created archive : ${OUT_DIR}/falcmoci-patchkit.tar.gz"
    rm -rf ${OUT_DIR}/falcmoci-patchkit
  else
    echo "$OUT_DIR does not exist, please create it"
    exit 1
  fi
}

usage() {
  echo "usage: $0"
  echo "  -o <OUT_DIR>: Out directory where PatchKit needs to be created"
  echo "examples: "
  echo "  Create a PatchKit under /tmp/kit"
  echo "    $0 -o /tmp/kit"
}

if [ $# -eq 0 ]; then
  usage
  exit 1
fi

while getopts "o:" opt; do
  case $opt in
    o)
      OUT_DIR=${OPTARG}
      createPatchKit
      ;;
    ?)
      usage
      exit 1
      ;;
  esac
done
