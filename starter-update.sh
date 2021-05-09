#!/bin/bash
# scripts to get started

service_directory=myservice
service_name=my-service
servicejava_starter=MyServiceApplication

find . -name "*starterservice*" |  sed "p;s/starterservice/${service_directory}/g" | xargs -n2 mv
find . -name "StarterService.java" | xargs sed -i '' "s/StarterService/${servicejava_starter}/g"
find . -name "StarterService.java" |  sed "p;s/StarterService/${servicejava_starter}/g" | xargs -n2 mv
find . -name "*.java" | xargs sed -i '' "s/starterservice/${service_directory}/g"
sed -i '' "s/starter-service/${service_name}/g" settings.gradle
find . -name "*.groovy" | xargs sed -i '' "s/starter-service/${service_name}/g"
