#!/usr/bin/env bash

./gradlew :library:clean :library:check :library:test -PpreDexEnable=false
./gradlew :library:clean :library:check :library:connectedAndroidTest -PpreDexEnable=false