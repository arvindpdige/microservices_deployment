# Shopping Cart Cloud Native Microservice

## Prerequisites

### Java 17
Install OpenJDK ,

For Windows


## Build
Set Redis server environment variables in '.env' file. This file will not checked into Git as it holds sensitive information such as Redis password.
```
For WINDOWS,

Install Gradle Binery from
https://gradle.org/install/#manually
Then run
gradle wrapper      #This Will recreate the gradlew & gradlew.bat wrappers
gradle build

To Avoid Test Failures
gradle build -x test
```


## Run Locally
gradle bootRun


### Build Docker Image

Note: It's require to build the project first to create Docker Image

Tell Docker CLI to talk to minikube's VM.

For MacOS,
`eval $(minikube docker-env)`

For Windows,
`& minikube -p minikube docker-env --shell powershell | Invoke-Expression`

Build docker image,
`docker build -t cart:latest .`
