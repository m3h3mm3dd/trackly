FROM eclipse-temurin:17-jdk

WORKDIR /app
COPY . .
RUN ./mvnw -q -DskipTests package
RUN mv target/*SNAPSHOT.jar /app/trackly.jar

CMD ["java","-jar","/app/trackly.jar"]
