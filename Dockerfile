FROM amazoncorretto:21 AS build

WORKDIR /app

COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle

RUN chmod +x gradlew

RUN ./gradlew dependencies --no-daemon

COPY src ./src

RUN ./gradlew clean build -x test --no-daemon

FROM amazoncorretto:21

RUN yum update -y && yum install -y jq && yum clean all

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar
COPY entrypoint.sh /entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["/entrypoint.sh"]