# Distributed Scoreboard using Zookeeper
HW1 for CSC 591 Data Intensive Computing | distributed-scoreboard

## Getting started
These instructions will get you the project up and running on your local/cloud machine for development and testing purposes. This application is built using maven and spring-boot.

## Prerequisites
You will need Zookeeper server installed on your system. You can use following commands to install it.
- **Ubuntu** : apt-get install zookeeper
- **Mac** : brew install zookeeper

## Installation
After installation, start the zookeeper server. Follow following steps to run the applications - 
1. Clone this repository.
2. Change directory to the cloned folder distributed-scoreboard/
3. Run `make` command. This will download all the prerequisites & start the spring-boot application. Make sure to have root access.
 - ![#c5f015](https://placehold.it/15/c5f015/000000?text=+)Hello. BUILD SUCCESS .  
 This message will prompted upon successful start of the application in green color
4. Use the application with following commands
- watcher 12.34.45.87:6666 | success message prompt - `Watcher created!`
- player 12.34.45.87:6666 name | success message prompt - `Node added - Thor`
- player 12.34.45.87:6666 "first last"
- player 12.34.45.87:6666 name count delay score | success message prompt - `Score Posted by Thor`
