FROM openjdk:8

RUN mkdir /opt/cdbg
RUN wget -qO- https://storage.googleapis.com/cloud-debugger/compute-java/debian-wheezy/cdbg_java_agent_gce.tar.gz | tar xvz -C /opt/cdbg

COPY target/lib/* /app/lib/
COPY target/guestbook-service-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java", "-agentpath:/opt/cdbg/cdbg_java_agent.so", "-Dcom.google.cdbg.module=guestbook-service", "-Dcom.google.cdbg.version=1.0-SNAPSHOT","-jar", "/app/app.jar"]
