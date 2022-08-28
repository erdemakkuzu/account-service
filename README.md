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