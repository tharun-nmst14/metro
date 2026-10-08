FROM sbtscala/scala-sbt:eclipse-temurin-17_1.x

WORKDIR /app

COPY build.sbt ./
COPY project/ ./project/

RUN sbt update

COPY src/ ./src/

RUN sbt compile
RUN sbt assembly

RUN cp target/scala-2.13/metro-ufm-demo-assembly-*.jar /app/metro-ufm-demo.jar

EXPOSE 10000

CMD ["sh", "-c", "exec UFM_PORT=${PORT:-10000} java -Xms64m -Xmx256m -XX:MaxMetaspaceSize=128m -jar /app/metro-ufm-demo.jar"]