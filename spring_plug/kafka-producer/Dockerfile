# Используем Maven для сборки приложения
FROM maven:3.8.6-openjdk-11 AS builder
WORKDIR /app
# Копируем pom.xml и src
COPY ./spring_plug/kafka-producer/pom.xml .
COPY ./spring_plug/kafka-producer/src ./src
# Собираем проект
RUN mvn clean package -DskipTests

# Используем OpenJDK для запуска приложения
FROM openjdk:11-jre-slim
WORKDIR /app
# Копируем скомпилированный JAR файл
COPY --from=builder /app/target/kafka-producer-0.0.1-SNAPSHOT.jar app.jar
# Указываем, как запускать приложение
ENTRYPOINT ["java", "-jar", "app.jar"]