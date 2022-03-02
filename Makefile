clean:
	./mvnw clean

dependency_check: clean
	./mvnw dependency-check:check

run: clean
	./mvnw spring-boot:run

test: clean
	./mvnw test -DskipITs -Dspring.profiles.active=test
	./mvnw failsafe:integration-test failsafe:verify -Dspring.profiles.active=test

verify: clean
	./mvnw verify