FROM eclipse-temurin:17-jdk

WORKDIR /app
COPY . .
RUN ./mvnw -q -DskipTests package
RUN mv target/*SNAPSHOT.jar /app/expensetracker.jar

CMD ["java","-jar","/app/expensetracker.jar"]
