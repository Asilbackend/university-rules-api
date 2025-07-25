# Base image
FROM eclipse-temurin:21-jre

# ffprobe (ffmpeg ichida) o‘rnatish
RUN apt-get update && \
    apt-get install -y ffmpeg --no-install-recommends && \
    rm -rf /var/lib/apt/lists/*


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
ENTRYPOINT ["java", "-Xms256m", "-Xmx512m", "-XX:MaxMetaspaceSize=128m", "-XX:+UseSerialGC", "-Xss512k", "-jar", "/app/unirule.jar"]

# CMD emas, ENTRYPOINT docker-compose ichida yozilgan


## Base image
#FROM eclipse-temurin:21-jdk
#
## ffprobe (ffmpeg) ni o‘rnatish
#RUN apt-get update && \
#    apt-get install -y ffmpeg --no-install-recommends && \
#    rm -rf /var/lib/apt/lists/*
#
#
## App directory
#WORKDIR /app
#
## Fayllarni copy qilish (templates ichidagi fayllarni saqlash uchun)
#COPY src/main/resources/templates/otherFiles /app/src/main/resources/templates/otherFiles
#COPY src/main/resources/templates/pictures /app/src/main/resources/templates/pictures
#COPY src/main/resources/templates/videos /app/src/main/resources/templates/videos
#COPY src/main/resources/templates/documents /app/src/main/resources/templates/documents
#COPY src/main/resources/templates/audios /app/src/main/resources/templates/audios
#
#
## Jar faylni copy qilish
#COPY build/libs/unirule.jar /app/unirule.jar
#
#
## Expose port (optional, container ichida)
#EXPOSE 8082
#ENTRYPOINT ["java", "-jar", "/app/unirule.jar"]
#
## CMD emas, ENTRYPOINT docker-compose ichida yozilgan
