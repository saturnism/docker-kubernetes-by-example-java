FROM java:8
ENTRYPOINT ["java", "-agentpath:/opt/cdbg/cdbg_java_agent.so", "-Dcom.google.cdbg.module=helloworld-service", "-Dcom.google.cdbg.version=1.0-SNAPSHOT","-jar", "/app/app.jar"]

RUN mkdir /opt/cdbg
RUN wget -qO- https://storage.googleapis.com/cloud-debugger/compute-java/debian-wheezy/cdbg_java_agent_gce.tar.gz | tar xvz -C /opt/cdbg

COPY target/lib/* /app/lib/
COPY target/helloworld-service-0.1-opt-3.jar /app/app.jar
