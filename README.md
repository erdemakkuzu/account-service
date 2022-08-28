# Account MICROSERVICE

Microservice for handling bank account operations

* Technologies :
    * Java 11
    * Spring boot
    * Maven 3
    * Spring Data JPA with Hibernate
    * H2 Database
    * JUnit
    * Mockito
    * Slf4j
    * SwaggerUI
    * Docker

## Installation

1. Install Java 11 and Maven 3.
2. Clone the project :
  ```bash
git clone https://github.com/erdemakkuzu/account-service.git
``` 
3. Go to project folder with command line and run the following command :
```bash
mvn clean package
```
4. **account-service.jar** should have been created under  the target folder. Run the .jar file with the following command:
```bash
java -jar target/account-service.jar
```
Or if you prefer to run the application inside a container, then please  follow the steps below after creating the **wallet-service.jar** (3rd step above)

1. Make sure that your docker engine is up.
2. Execute the following command under the project folder. It will build a docker image.
```bash
docker build -t account-service.jar .
```
3. Run the following command to see the docker images. You should be able to see **account-service.jar** image.
```bash
docker image ls
```
4. And finally execute this command
```bash
docker run -p 8080:8080 account-service.jar 
```

By clicking the link below, you should be accessing the swagger API documentation.
```bash
http://localhost:8080/swagger-ui.html
```

## Database Design

H2 in memory database was used in this project.

Console URL = http://localhost:8080/h2/
* username = sa
* password = password
* data source url = jdbc:h2:file:./data/swedBankDb

There is dummy data inside that you can play with after you get the project up and running.
Data is not lost when you stop and restart the project. It persists data under the folder of
*/data/*

Relationships between tables:
1. **One account** can have **many currency accounts.**
2. **One currency account** can have **one currency.**

## Endpoints
In this chapter you can find detailed information about the APIs.
For swagger documenation of the APIs, visit the link = http://localhost:8080/swagger-ui.html

Here is some postman collection that includes sample requests. You can download and import with postman:

https://drive.google.com/file/d/1e46NxPyral3LBCDsKZNEqZ-z_BGG2Tn6/view


**1. POST Account /api/account**
* You can create a new account with this endpoint

Request body example:
```json
{
  "customer_full_name" : "Erdem's account",
  "initial_currency" : "SEK"
}
```
* Initial currency must be one of those : USD, EUR, SEK, RUB. 
* If you use docker to run this project, you need to go to database console and add currencies manually.

**2. GET Account /api/account/{accountId}**
* You can display account information with this api.

**3. POST Currency Account /api/account/{accountId}/currency-account*
* You can create currency accounts with this API by providing you main account id.

Request body example:
```json
{
  "currency_code" : "RUB"
}
```

**4. POST Add money /api/account/{accountId}/add-money*
* You can add money to your account by providing you main account id.

Request body example:
```json
{
"amount" : 100,
"currency_code" : "USD"
}
```

* amount must be bigger than 1 
* you must have the specific currency account to perform this operation.

**5. POST Debit /api/account/{accountId}/debit*
* You debit money from your account by providing you main account id.

Request body example:
```json
{
  "amount" : 55.369,
  "currency_code" : "USD"
}
```

* amount must be bigger than 1
* you must have the specific currency account to perform this operation.
* you must have enough balance to perform this operation

**6. POST Debit /api/account/{accountId}/exchange-currency*
* You can exchange currency between your currency accounts by providing you main account id.

Request body example:
```json
{
  "from" : "USD",
  "to" : "RUB",
  "amount" : 20.1
}
```

* amount must be bigger than 1
* you must have the specific currency accounts (USD and RUB for the example) to perform this operation.
* you must have enough balance to perform this operation




