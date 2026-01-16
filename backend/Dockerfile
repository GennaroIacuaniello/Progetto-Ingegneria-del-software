FROM maven:3.9.6-eclipse-temurin-21 AS build

# Impostiamo la directory di lavoro
WORKDIR /app

# Copiamo il file pom.xml e scarichiamo le dipendenze
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamo il codice sorgente
COPY src ./src

# Compiliamo il progetto saltando i test per velocizzare la build
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app

# Copiamo il file JAR generato dallo stage di build
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

# Esponiamo la porta 8080
EXPOSE 8080

# Comando di avvio
ENTRYPOINT ["java", "-jar", "app.jar"]