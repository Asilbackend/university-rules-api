# Base image
FROM eclipse-temurin:21-jdk

# App directory
WORKDIR /app

# Fayllarni copy qilish (templates ichidagi fayllarni saqlash uchun)
COPY src/main/resources/templates/otherFiles /app/src/main/resources/templates/otherFiles
COPY src/main/resources/templates/pictures /app/src/main/resources/templates/pictures
COPY src/main/resources/templates/videos /app/src/main/resources/templates/videos
COPY src/main/resources/templates/documents /app/src/main/resources/templates/documents
COPY src/main/resources/templates/audios /app/src/main/resources/templates/audios

# Jar faylni copy qilish
COPY build/libs/unirule.jar /app/unirule.jar


# Expose port (optional, container ichida)
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/unirule.jar"]

# CMD emas, ENTRYPOINT docker-compose ichida yozilgan
