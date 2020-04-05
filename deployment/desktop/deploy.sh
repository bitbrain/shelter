#!/bin/bash

set -e
set -o pipefail

if [[ -z "${BUTLER_API_KEY}" ]]; then
  echo "Unable to deploy! No BUTLER_API_KEY environment variable specified!"
  exit 1
fi

prepare_butler() {
    echo "Preparing butler..."
    download_if_not_exist http://dl.itch.ovh/butler/linux-amd64/head/butler butler
    chmod +x butler
}

prepare_packr() {
    echo "Preparing packr..."
    download_if_not_exist https://libgdx.badlogicgames.com/ci/packr/packr.jar packr.jar
    cp desktop/build/libs/desktop-1.0.jar shelter.jar
}

# platform = $1
# packrConfig = $2
# jre = $3
# ext = $4
prepare_and_push() {
    echo "Preparing $1 build..."
    download_if_not_exist https://bitbucket.org/alexkasko/openjdk-unofficial-builds/downloads/$3 $3
    java -jar packr.jar --jdk $3 --output shelter-$1$4 -- deployment/desktop/$2

    zip -r shelter-$1.zip shelter-$1$4
    rm -rf shelter-$1

    echo "Push $1 build to itch.io..."
    ./butler push shelter-$1.zip bitbrain/shelter:$1 -i butler_creds
}

download_if_not_exist() {
    if [ ! -f $2 ]; then
        curl -L -O $1 > $2
    fi
}

# Initialisation
prepare_butler
prepare_packr

prepare_and_push "windows" "packr-windows.json" "openjdk-1.7.0-u80-unofficial-windows-i586-image.zip"
prepare_and_push "mac" "packr-mac.json" "openjdk-1.7.0-u45-unofficial-icedtea-2.4.3-macosx-x86_64-image.zip" ".app"
prepare_and_push "linux-amd32" "packr-linux-32.json" "openjdk-1.7.0-u80-unofficial-linux-amd64-image.zip"
prepare_and_push "linux-amd64" "packr-linux-64.json" "openjdk-1.7.0-u80-unofficial-linux-amd64-image.zip"

echo "Done."
exit 0
