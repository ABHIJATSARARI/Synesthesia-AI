#!/bin/bash
set -e

SCRIPTPATH="$( cd "$(dirname "$0")" ; pwd -P )"
LIB_DIR="$SCRIPTPATH/lib/android_arm64"

mkdir -p "$LIB_DIR"
cd "$LIB_DIR"

if [ ! -f libtensorflow-lite.a ]; then
    echo "Downloading and unzipping TFLite library..."
    curl -L -o tflite.zip https://cdn.edgeimpulse.com/build-system/tflite/android64/tflite-android64.zip
    unzip -o tflite.zip
    # Move files from the known subdirectory if it exists
    if [ -d "tflite-android64" ]; then
      mv tflite-android64/* .
      rmdir tflite-android64
    fi
    rm tflite.zip
    echo "TFLite library is ready."
else
    echo "TFLite library already exists."
fi
