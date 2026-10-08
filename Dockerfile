FROM sbtscala/scala-sbt:eclipse-temurin-17_1.x

WORKDIR /app

COPY build.sbt ./
COPY project/ ./project/
RUN sbt update

COPY src/ ./src/
RUN sbt compile

EXPOSE 10000

CMD ["sh", "-c", "UFM_PORT=${PORT:-10000} sbt 'runMain com.metro.ufm.MetroUfmServer'"]