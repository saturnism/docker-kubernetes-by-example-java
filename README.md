This project contains Dockerfile that creates a base image to run Spring Boot applications.

This is not an official Google product.

How to use this image
=====================

Groovy Applications
-------------------
Create a Dockerfile in your project directory:

    FROM saturnism/spring-boot:1.2.3-jdk-8-groovy-2.4.3
    ADD . $SRC_DIR

You can then build and run the image:

    docker build -t myapp
    docker run -ti myapp

You'll notice that everytime the container starts, it will resolve all the dependencies.
To avoid this, you can also pre-compile your Groovy application using the `onbuild` image.

In the Dockerfile, use:

    FROM saturnism/spring-boot:1.2.3jdk-8-groovy-2.4.3-onbuild

You can then build and run the image just like the previous method:

    docker build -t myapp
    docker run -ti myapp


Examples
--------
You can find examples under the [Examples](examples/) directory.

Full Demo with Kubernetes
-------------------------
### Videos
Ray used this repository for many of his demos during his talks around the world in conferences. You can find a list of Ray's videos on how to run the demos in his [YouTube playlist](https://www.youtube.com/playlist?list=PL4uYfigiauVYH4OwOyq8FGbPQOn-JueEf).

But specifically, checkout the one from [Jfokus](https://www.youtube.com/watch?v=R2l-tL_1els&index=6&list=PL4uYfigiauVYH4OwOyq8FGbPQOn-JueEf).

### Creating a microservice
1. Create a new directory, and change into it: `mkdir hello-live && cd hello-live`
2. Create `Helloworld.groovy`
    ```
    @RestController
    class Helloworld {
      @RequestMapping("/mul/{x}/{y}")
      def mul(@PathVariable int x, @PathVariable int y) {
        [ x: x, y: y, result: x * y ]
      }
    }
    ```

3. Run it: `spring run .`
4. Build it: `spring jar ~/app.jar .`
5. Run the jar: `java -jar ~/app.jar`

### Containerize the service
1. Create a `Dockerfile`
2. Build the container `docker build -t helloworld .`
3. Run it: `docker run -ti --rm -p 8080:8080 helloworld`

### Deploying to Kubernetes on Google Container Engine
This assumes that you have a Google Cloud Platform account, a Container Engine managed Kubernetes cluster, and the associated Project ID.

1. Set the PROJECT_ID: `export PROJECT_ID=$(gcloud config get-value core/project)`
2. Tag it: `docker tag helloworld gcr.io/${PROJECT_ID}/helloworld`
3. Push it to Google Container Registry, a private repository: `gcloud docker -- push gcr.io/${PROJECT_ID}/helloworld`
4. Deploy it in Kubernetes: `kubectl run helloworld --image=gcr.io/${RPOJECT_ID}/helloworld -l app=hellworld,visualize=true` (the label "visualize" is for demo visualization purposes. You can use whatever labels you like).
5. Scale it: `kubectl scale rc helloworld --replicas=3`
6. Expose it as an external service: `kubectl expose rc helloworld --port=8080 --target-port=8080 --type=LoadBalancer`

### Rolling Update
1. Make changes to `Helloworld.groovy`
2. Build the container as v2: `docker build -t helloworld:v2 .`
3. Tag it: `docker tag helloworld:v2 gcr.io/${PROJECT_ID}/helloworld:v2`
4. Push it: `gcloud docker -- push gcr.io/${PROJECT_ID}/helloworld:v2`
5. Rolling update: `kubectl rolling-update frontend --image=gcr.io/${PROJECT_ID}/helloworld:v2 --update-periods=5s`

### Running the examples in the Google Container Engine

1. Build the docker images from the examples for the projects helloworld-service, guestbook-service and helloworld-ui
2. Get the project id from above: `echo ${PROJECT_ID}`
3. In the examples/kubernetes-1.6 directory run the following commands to deploy the examples to Google Container Engine.
4. Modify the helloworldservice-deployment-v1.yaml to point to the docker image you pushed above.  In the yaml file modify image to be `image: gcr.io/${PROJECT_ID}/helloworld` (replacing ${PROJECT_ID} with the actual project id).
5. Get the external IP of helloworld-service: `kubectl get services`
6. Browse to http://EXTERNAL_IP:8080/hello/world
7. Deploy redis: `kubectl apply -f redis-deployment.yaml -f redis-service.yaml`
8. Deploy mysql: `kubectl apply -f mysql-pvc.yaml -f mysql-deployment.yaml -f mysql-service.yaml`
9. Deploy helloworld-service by running `kubectl apply -f helloworldservice-deployment-v1.yaml -f helloworldservice-service.yaml`
10. Repeat the same thing to deploy the guestbook service by modifying and applying guestbookservice-deployment.yaml and guestbookservice-service.yaml files.
11. The helloworld ui calls the guestbook service on startup, so wait until the guest book service has a status of Running by calling: `kubectl get pods` and looking for the pod guestbook-service-*
12. Repeat the same thing to deploy the helloworld ui by modifying and applying  helloworldui-deployment-v1.yaml and helloworldui-service.yaml files.
11. Get the external IP of helloworld ui by running `kubectl get services` and browse to the EXTERNAL IP
12. View the status of the services by getting the name of the pod: `kubectl get pods` and then browsing the logs of the pod: `kubectl logs -f helloworld-ui-3415022828-1h37t `
