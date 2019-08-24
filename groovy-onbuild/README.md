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
