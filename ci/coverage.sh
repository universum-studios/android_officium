#!/usr/bin/env bash

./gradlew :library:jacocoTestReportDebug
bash <(curl -s https://codecov.io/bash)