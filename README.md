Nashorn port to Java  14+
=========================

Build it with Java 15+:

    ./gradlew jar

The tests can be run with:

    ./gradlew check

To run only the unit tests:

    ./gradlew test

Publishing is done with:

    ./gradlew publish

AWS credentials to the S3 bucket sqreen-ci-java are required in the usual place
(usually ~/.aws/{credentials,config}).

The `jjs` tool can be built with:

    ./gradlew jjs:jlink

Execute with:

    $ jjs/build/jpackage/jjs/bin/jjs -version
    nashorn 14.0.31.1
    jjs> 
