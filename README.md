# Financial Service REST API
Financial service is a web application using REST API architecture which allows an existing user to create a  new account.

## Some technical decisions made

- The user is assumed to be already existing and only account creation functionality is implemented.
- An in-memory database is used for easier testing
- A couple of users and accounts are created via schema for easier testing

## Prerequisites
- Download and install gradle
- Download and install JDK 17

## How to build and run project

If you are on Window, run the command:

`./gradlew bootRun`

To run all unit tests in the project, use the command:

`./gradlew test`

If gradle command is not working, you can also run the project by starting the application via the main method in the class 
FinancialServiceApplication or run this command under the root folder:

`java -jar ./build/libs/financial-service-0.0.1-SNAPSHOT.jar`

Then go to this link to access the endpoints:

http://localhost:8080/swagger-ui/index.html

## The available endpoints for this project are:
* Add a new account for an already existing user
* View the user information