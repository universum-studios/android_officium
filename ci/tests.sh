#!/usr/bin/env bash

./gradlew :library:clean :library:check :library:connectedAndroidTest -PpreDexEnable=false