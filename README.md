# apis-rest [![CircleCI](https://circleci.com/gh/jessicacarneiro/apis-rest/tree/main.svg?style=svg)](https://circleci.com/gh/jessicacarneiro/apis-rest/tree/main)

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/a99723a805284b678bdd38617a8fb304)](https://app.codacy.com/gh/jessicacarneiro/apis-rest?utm_source=github.com&utm_medium=referral&utm_content=jessicacarneiro/apis-rest&utm_campaign=Badge_Grade_Settings)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/e4269cf9efab419a8235052428650679)](https://www.codacy.com/gh/jessicacarneiro/apis-rest/dashboard?utm_source=github.com&utm_medium=referral&utm_content=jessicacarneiro/apis-rest&utm_campaign=Badge_Coverage)

Project from book "APIs REST" by Alexandre Saudate

# Table of Contents
1. [Commands](#commands)
2. [JaCoCo report](#jacoco-report)
3. [Dependency check report](#dependency-check-report)
4. [To Do](#todo)
5[References](#references)

## Commands
* `make run`: Start application
* `make test`: Run unit tests
* `make verify`: Run all tests and generate JaCoCo report
* `make clean`: Clean project
* `make dependency_check`: Check for vulnerabilities in the dependencies

## JaCoCo report
Run `make verify` or `./mvnw verify` to run all tests and generate a JaCoCo report. The report will be located at
`/target/site/jacoco/index.html`

## Dependency check report
Run `make dependency_check` or `./mvnw dependency-check:check` to run the dependency check report. The report will be located at
`/target/dependency-check-report.html`

## TODO

- [ ] Create script to validate dependency check report in the pipeline
- [ ] Add roles to other controllers
- [ ] Add more tests (increase test coverage)
- [ ] Add more cases in the default error handling
- [ ] Add cache
- [x] Create interfaces with OpenAPI docs for all APIs

## References

* [Spring Test & Security: How to mock authentication?](https://stackoverflow.com/questions/15203485/spring-test-security-how-to-mock-authentication)