# apis-rest [![CircleCI](https://circleci.com/gh/jessicacarneiro/apis-rest/tree/main.svg?style=svg)](https://circleci.com/gh/jessicacarneiro/apis-rest/tree/main)

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/a99723a805284b678bdd38617a8fb304)](https://app.codacy.com/gh/jessicacarneiro/apis-rest?utm_source=github.com&utm_medium=referral&utm_content=jessicacarneiro/apis-rest&utm_campaign=Badge_Grade_Settings)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/e4269cf9efab419a8235052428650679)](https://www.codacy.com/gh/jessicacarneiro/apis-rest/dashboard?utm_source=github.com&utm_medium=referral&utm_content=jessicacarneiro/apis-rest&utm_campaign=Badge_Coverage)

Project from book "APIs REST" by Alexandre Saudate

# Table of Contents
1. [Maven commands](#maven-commands)
2. [JaCoCo report](#jacoco-report)

## Maven commands
* `./mvnw test`: Run all tests
* `./mvnw verify`: Run all tests and generate JaCoCo report

## JaCoCo report
Run `./mvnw verify` to run all tests and generate a JaCoCo report. The report will be located at `/target/site/jacoco/index.html`