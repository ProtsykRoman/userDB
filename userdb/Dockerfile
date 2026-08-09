# ЕТАП 1: Компіляція (Builder)
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Копіюємо бібліотеки та вихідний код
COPY target/lib ./lib
COPY src ./src

# Створюємо папку для результатів компіляції
RUN mkdir -p target/out

# 1. Компілюємо класи з підтримкою імен параметрів (КРИТИЧНО для Spring Boot 3+)
RUN find src/main/java -name "*.java" > sources.txt && \
    javac -parameters -d target/out -cp "lib/*" @sources.txt

# 2. Копіюємо ресурси (шаблони, конфігурації) у корінь папки з класами
RUN cp -rv src/main/resources/* target/out/

# 3. Перевірка структури (виведеться в лог збірки)
RUN ls -d target/out/templates/ && ls target/out/templates/pages/

# 4. Пакуємо все у JAR файл
RUN cd target/out && jar -cvf /app/app.jar .

# ЕТАП 2: Фінальний образ (Runtime)
FROM eclipse-temurin:17-jre
WORKDIR /app

# Копіюємо зібраний JAR та бібліотеки з першого етапу
COPY --from=builder /app/app.jar .
COPY --from=builder /app/lib ./lib

EXPOSE 8080

# Запуск програми з налаштуваннями Thymeleaf та логуванням
ENTRYPOINT ["java", "-cp", "app.jar:lib/*", \
            "-Dspring.thymeleaf.prefix=classpath:/templates/", \
            "-Dspring.thymeleaf.suffix=.html", \
            "-Dlogging.level.org.thymeleaf=DEBUG", \
            "ua.com.userdb.UserdbApplication"]
