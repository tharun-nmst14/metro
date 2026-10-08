FROM sbtscala/scala-sbt:eclipse-temurin-17.0.14_7_1.10.7_2.13.14

WORKDIR /app

COPY build.sbt ./
COPY project/ ./project/
RUN sbt update

COPY src/ ./src/
RUN sbt compile

EXPOSE 10000

CMD ["sh", "-c", "UFM_PORT=${PORT:-10000} sbt runMain com.metro.ufm.MetroUfmServer"]
