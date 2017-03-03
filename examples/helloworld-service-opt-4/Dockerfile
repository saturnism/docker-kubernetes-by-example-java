FROM java:8
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

COPY target/lib/* /app/lib/
COPY target/helloworld-service-0.1-opt-3.jar /app/app.jar
