#!/usr/bin/env bash

./gradlew :library:clean :library:checkDebug :library:assembleDebug -PpreDexEnable=false