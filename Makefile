build_run:
	mvn clean package && java -cp target/threads-1.0-SNAPSHOT.jar david.training.App

build:
	mvn clean package

test:
	mvn clean test

run:
	java -cp target/threads-1.0-SNAPSHOT.jar david.training.App
