#!/usr/bin/env bash

./gradlew :library:jacocoTestReportRelease
./gradlew :library:uploadCoverageToCodacy
bash <(curl -s https://codecov.io/bash)