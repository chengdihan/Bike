# Starter Service

This Service is a template service to provide the basic structure for any new microservice. In this template, 
there is a sample greeting API and an example API that connects to the Lothric database. These can be 
modified/deleted as required.

## IntelliJ

##### Import Google Style Guide to Intellij.

https://github.com/HPI-Information-Systems/Metanome/wiki/Installing-the-google-styleguide-settings-in-intellij-and-eclipse

##### Install Checkstyle Plugin for Intellij and Google Style Checks XML for Intellij to ensure you get errors/warnings(configurable) when you don't meet standards.

http://www.practicesofmastery.com/post/intellij-checkstyle-google-java-style-guide/

Open project at the root of the repository.

##### Enable annotation processing.

Preferences -> Compiler -> Annotation Processors -> Enable Annotation Processing.

## Build, Debug and Run

```bash
# build and run tests
./gradlew clean build

# build without running tests (Useful if you need to run the service when tests are broken because you are working on them)
./gradlew clean build -x test

# Run start the service
./gradlew bootRun

# To start the service and attach a debugger later
./gradlew bootRun -Pargs=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5010

# Or run directly
./build/libs/<press-TAB> -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5010
```

Use the [generated swagger](http://localhost:9200/starter/swagger-ui.html) docs to make requests to your service.
Or, use [Postman](https://www.postman.com/): 

```bash
brew cask install postman
```

The OpenApi 3.0 spec provided by the service at runtime is located here: http://localhost:9200/starter/swagger/v3/api-docs

## Tag Release and Publish Jar to Maven Repo

```bash
./gradlew release
./gradlew clean build
./gradlew publish
```

## Setup the CI Build and QA Build in Jenkins

Follow the steps in https://newengen.atlassian.net/wiki/spaces/EN/pages/975011841/Jenkins to set up the pipeline project. 
Some examples are `tagging-service` and `data-access-service`. 

Be sure to replace the project name in the scripts/Jenkinsfile_qatest.groovy to match the project

## Convenient Scripts

After you have cloned your new repository from github, the name `starter-service` still remains in some code and configuration files. 
The `starter-update.sh` script automates replacing these with the name of your service. Edit `starter-update.sh` and change the 
variable values at the top of the script before running it. 

**This script has been tested on MacOS. you may have to adjust if you use different OS**

## Useful links

API Standard : https://newengen.atlassian.net/wiki/spaces/EN/pages/679051411/API+Standards

Monitoring : https://newengen.atlassian.net/wiki/spaces/EN/pages/1019904147/Monitoring+Prometheus

Coding Style: https://newengen.atlassian.net/wiki/spaces/EN/pages/417923075/Coding+Style+Guide

Git related : https://newengen.atlassian.net/wiki/spaces/EN/pages/613941322/Git+Branching+and+PR+Process

Testing : https://newengen.atlassian.net/wiki/spaces/EN/pages/1013189433/Testings

Java Service Infrastructure and Deployment : https://newengen.atlassian.net/wiki/spaces/EN/pages/783942553/PSI+Tools+and+Infrastructure+for+Java+Service+Applications

Karla : https://newengen.atlassian.net/wiki/spaces/EN/pages/925597704/Karla+-+Infrastructure+Management+Helper
