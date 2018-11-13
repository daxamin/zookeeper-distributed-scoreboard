#!/bin/sh

# To get the latest package lists
sudo apt-get update
echo "Installing JDK"
sudo apt-get install default-jdk -y
echo "Installed JDK"
echo "Installing maven"
sudo apt-get install maven -y
echo "Installed maven"

mvn clean install
mvn spring-boot:run