# Financial Service REST API
Financial service is a web application using REST API architecture which allows an existing user to create a  new account.

## Some technical decisions made

- The user is assumed to be already existing and only account creation functionality is implemented.
- An in-memory database is used for easier testing
- A couple of users and accounts are created via schema for easier testing
- The auto-generated ids for accounts in the file data.sql are changed to 100,200 and 300, 400...etc on purpose. This is to ensure that there is 
no conflict when a new id is generated when a new account is created through the POST endpoint.

## Prerequisites
- Download and install gradle
- Download and install JDK 17

## How to build and run project

If you are on Window, first run the command:

`./gradlew clean build`

Then to run the project, either run the command

`./gradlew bootRun`

To run all unit tests in the project, use the command:

`./gradlew test`

You can also run the project by starting the application via the main method in the class 
FinancialServiceApplication or run this command under the root folder:

`java -jar ./build/libs/financial-service-0.0.1-SNAPSHOT.jar`

Then go to this link to access the endpoints:

http://localhost:8080/swagger-ui/index.html

## The available endpoints for this project are:
* Add a new account for an already existing user
* View the user information including existing and newly created accounts with transactions

Users with id 1,2 and 3 can be used for testing purposes. 