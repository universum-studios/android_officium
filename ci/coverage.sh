#!/usr/bin/env bash

./gradlew :library:jacocoTestReportDebug
./gradlew :library:uploadCoverageToCodacy
bash <(curl -s https://codecov.io/bash)