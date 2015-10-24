#!/bin/bash

export VERSION=$(cat app/build.gradle |grep "versionName"|cut -d'"' -f 2)
