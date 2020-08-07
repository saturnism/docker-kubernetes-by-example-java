# HelloWorld Service - Option 4

In this section we would see another way for building docker image quickly for HelloWorld Service 
and creating and applying Kubernetes manifests onto Kubernetes Cluster. We would be using a project
called [Eclipse JKube](https://github.com/eclipse/jkube), it provides integration of Kubernetes
and Docker inside maven with which you can use maven goals for your Kubernetes workloads.

To start, this project has Eclipse JKube's `kubernetes-maven-plugin` in plugins section:
```
<plugin>
  <groupId>org.eclipse.jkube</groupId>
  <artifactId>kubernetes-maven-plugin</artifactId>
  <version>${jkube.version}</version>
  <executions>
    <execution>
      <goals>
        <goal>resource</goal>
        <goal>build</goal>
        <goal>deploy</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```

## Building Docker Image (k8s:build)
Eclipse JKube can build both opinionated docker images or custom based on `Dockerfile`. This project
has `Dockerfile` defined in the root directory. Eclipse JKube would pick this and build a docker 
image for the project using `k8s:build` goal:
```
~/work/repos/docker-kubernetes-by-example-java/examples/helloworld-service-opt-4 : $ mvn k8s:build
[INFO] Scanning for projects...
[INFO]
[INFO] ---------------< com.google.examples:helloworld-service >---------------
[INFO] Building helloworld-service 0.1-opt-4
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- kubernetes-maven-plugin:1.0.0-rc-1:build (default-cli) @ helloworld-service ---
[INFO] k8s: Running in Kubernetes mode
[INFO] k8s: Building Docker image in Kubernetes mode
[INFO] k8s: [examples/helloworld-service:0.1-opt-4]: Created docker-build.tar in 3 seconds
[INFO] k8s: [examples/helloworld-service:0.1-opt-4]: Built image sha256:4a227
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  12.250 s
[INFO] Finished at: 2020-08-07T20:36:13+05:30
[INFO] ------------------------------------------------------------------------
~/work/repos/docker-kubernetes-by-example-java/examples/helloworld-service-opt-4 : $ docker images | grep helloworld-service
examples/helloworld-service                                      0.1-opt-4                     4a22712e21e1        8 seconds ago       656MB
~/work/repos/docker-kubernetes-by-example-java/examples/helloworld-service-opt-4 : $
```

In order to make the generated images available to the Kubernetes cluster the generated images need to be pushed to a registry with the goal `k8s:push`.

## Generating Kubernetes Manifests (k8s:resource)
Eclipse JKube can build opinionated kubernetes manifests which can be customized using XML properties
or resource fragments in `src/main/jkube` directory. In order to generate Kubernetes manifests we just
need to issue `k8s:resource` goal:

But before that, I've added two properties in order to customize generated resources:
```
    <properties>
        # To add Service Port as 8080
        <jkube.enricher.jkube-service.port>8080</jkube.enricher.jkube-service.port>
        # To get a NodePort type Service
        <jkube.enricher.jkube-service.type>NodePort</jkube.enricher.jkube-service.type>
    </properties>

```

Now we can issue `k8s:resource` goal which would generate a Deployment and Service manifest
for us:

```
~/work/repos/docker-kubernetes-by-example-java/examples/helloworld-service-opt-4 : $ mvn k8s:resource
[INFO] Scanning for projects...
[INFO]
[INFO] ---------------< com.google.examples:helloworld-service >---------------
[INFO] Building helloworld-service 0.1-opt-4
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- kubernetes-maven-plugin:1.0.0-rc-1:resource (default-cli) @ helloworld-service ---
[INFO] k8s: jkube-controller: Adding a default Deployment
[INFO] k8s: jkube-service: Adding a default service 'helloworld-service' with ports [8080]
[INFO] k8s: jkube-revision-history: Adding revision history limit to 2
[INFO] k8s: validating /home/rohaan/work/repos/docker-kubernetes-by-example-java/examples/helloworld-service-opt-4/target/classes/META-INF/jkube/kubernetes/helloworld-service-deployment.yml resource
[INFO] k8s: validating /home/rohaan/work/repos/docker-kubernetes-by-example-java/examples/helloworld-service-opt-4/target/classes/META-INF/jkube/kubernetes/helloworld-service-service.yml resource
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  3.172 s
[INFO] Finished at: 2020-08-07T20:39:25+05:30
[INFO] ------------------------------------------------------------------------
~/work/repos/docker-kubernetes-by-example-java/examples/helloworld-service-opt-4 : $ ls target/classes/META-INF/jkube/
kubernetes  kubernetes.yml
~/work/repos/docker-kubernetes-by-example-java/examples/helloworld-service-opt-4 : $ ls target/classes/META-INF/jkube/kubernetes
helloworld-service-deployment.yml  helloworld-service-service.yml
~/work/repos/docker-kubernetes-by-example-java/examples/helloworld-service-opt-4 : $
```

## Applying Kubenetes Manifests (k8s:deploy / k8s:apply)
Once we have generated Kubernetes manifests, we can go ahead and apply them onto Kuberntes Cluster.
We would only need to issue `k8s:deploy` or `k8s:apply` goal:
```
~/work/repos/docker-kubernetes-by-example-java/examples/helloworld-service-opt-4 : $ mvn k8s:apply
[INFO] Scanning for projects...
[INFO] 
[INFO] ---------------< com.google.examples:helloworld-service >---------------
[INFO] Building helloworld-service 0.1-opt-4
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- kubernetes-maven-plugin:1.0.0-rc-1:apply (default-cli) @ helloworld-service ---
[INFO] k8s: Using Kubernetes at https://192.168.39.145:8443/ in namespace default with manifest /home/rohaan/work/repos/docker-kubernetes-by-example-java/examples/helloworld-service-opt-4/target/classes/META-INF/jkube/kubernetes.yml 
[INFO] k8s: Using namespace: default
[INFO] k8s: Creating a Service from kubernetes.yml namespace default name helloworld-service
[INFO] k8s: Created Service: target/jkube/applyJson/default/service-helloworld-service-1.json
[INFO] k8s: Creating a Deployment from kubernetes.yml namespace default name helloworld-service
[INFO] k8s: Created Deployment: target/jkube/applyJson/default/deployment-helloworld-service-1.json
[INFO] k8s: HINT: Use the command `kubectl get pods -w` to watch your pods start up
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  7.098 s
[INFO] Finished at: 2020-08-07T20:43:28+05:30
[INFO] ------------------------------------------------------------------------
```

Now we can check our application deployed inside our Kubernetes Cluster:
```
~/work/repos/docker-kubernetes-by-example-java/examples/helloworld-service-opt-4 : $ kubectl get pods
NAME                                          READY   STATUS      RESTARTS   AGE
helloworld-service-b67957756-w468b            1/1     Running     0          8s
~/work/repos/docker-kubernetes-by-example-java/examples/helloworld-service-opt-4 : $ kubectl get svc
NAME                         TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
helloworld-service           NodePort    10.106.12.97     <none>        8080:30668/TCP   16s
~/work/repos/docker-kubernetes-by-example-java/examples/helloworld-service-opt-4 : $ MINIKUBE_IP=`minikube ip`
`/work/repos/docker-kubernetes-by-example-java/examples/helloworld-service-opt-4 : $ HELLO_SVC_PORT=`kubectl get svc helloworld-service -o jsonpath="{.spec.ports[0].nodePort}"
~/work/repos/docker-kubernetes-by-example-java/examples/helloworld-service-opt-4 : $ curl $MINIKUBE_IP:$HELLO_SVC_PORT/
Greetings from Spring Boot!
```

## Cleaning up (k8s:undeploy)
Once you're done, you can undeploy your application from Kubenetes Cluster using `k8s:undeploy` goal:
```
~/work/repos/docker-kubernetes-by-example-java/examples/helloworld-service-opt-4 : $ mvn k8s:undeploy
[INFO] Scanning for projects...
[INFO]
[INFO] ---------------< com.google.examples:helloworld-service >---------------
[INFO] Building helloworld-service 0.1-opt-4
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- kubernetes-maven-plugin:1.0.0-rc-1:undeploy (default-cli) @ helloworld-service ---
[INFO] k8s: Using Kubernetes at https://192.168.39.145:8443/ in namespace default with manifest /home/rohaan/work/repos/docker-kubernetes-by-example-java/examples/helloworld-service-opt-4/target/classes/META-INF/jkube/kubernetes.yml
[INFO] k8s: Using namespace: default
[INFO] k8s: Deleting resource Deployment default/helloworld-service
[INFO] k8s: Deleting resource Service default/helloworld-service
[INFO] k8s: HINT: Use the command `kubectl get pods -w` to watch your pods start up
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  4.241 s
[INFO] Finished at: 2020-08-07T20:49:00+05:30
[INFO] ------------------------------------------------------------------------
~/work/repos/docker-kubernetes-by-example-java/examples/helloworld-service-opt-4 : $
```
