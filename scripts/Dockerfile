FROM amazoncorretto:8

RUN yum -y update
RUN yum -y install git-core
RUN yum -y install python3-pip || yum -y install python-pip
RUN pip3 install --upgrade awscli || pip install --upgrade awscli

USER root

RUN groupadd --gid 992 jenkins

RUN groupadd --gid 993 docker

RUN useradd -r -u 996 -g 992 -ms /bin/bash jenkins

RUN usermod -a -G docker jenkins

USER jenkins

WORKDIR /home/jenkins