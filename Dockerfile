FROM openjdk:11
EXPOSE 8080
ADD target/account-service.jar account-service.jar
ENTRYPOINT ["java", "-jar","/account-service.jar" ]