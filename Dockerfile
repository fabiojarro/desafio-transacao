FROM openjdk

WORKDIR /app

COPY target/desafio.jar /app/desafio.jar

ENTRYPOINT ["java", "-jar", "desafio.jar"]