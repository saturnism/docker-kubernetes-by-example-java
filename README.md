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

1. Tag it: `docker tag -f helloworld gcr.io/${PROJECT_ID}/helloworld`
2. Push it to Google Container Registry, a private repository: `gcloud docker push gcr.io/${PROJECT_ID}/helloworld` 
3. Deploy it in Kubernetes: `kubectl run helloworld --image=gcr.io/${RPOJECT_ID}/helloworld -l app=hellworld,visualize=true` (the label "visualize" is for demo visualization purposes. You can use whatever labels you like).
4. Scale it: `kubectl scale rc helloworld --replicas=3`
5. Expose it as an external service: `kubectl expose rc helloworld --port=8080 --target-port=8080 --type=LoadBalancer`

### Rolling Update
1. Make changes to `Helloworld.groovy`
2. Build the container as v2: `docker build -t helloworld:v2 .`
3. Tag it: `docker tag -f helloworld:v2 gcr.io/${PROJECT_ID}/helloworld:v2`
4. Push it: `gcloud docker push gcr.io/${PROJECT_ID}/helloworld:v2`
5. Rolling update: `kubectl rolling-update frontend --image=gcr.io/${PROJECT_ID}/helloworld:v2 --update-periods=5s`

