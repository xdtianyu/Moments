#!/bin/bash

gradle clean assembleDebug

cp app/build/outputs/apk/app-debug.apk $HOME/Moments-debug-v$VERSION.apk

gradle clean assembleRelease

ls -lR app/build/outputs

cp app/build/outputs/apk/app-release.apk $HOME/Moments-release-v$VERSION.apk
