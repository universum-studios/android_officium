#!/usr/bin/env bash

./gradlew :library:clean :library:check :library:createDebugCoverageReport -PpreDexEnable=false