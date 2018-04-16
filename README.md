# rancher-maven-plugin 
Upgrade a single service in specific environment
## 1. Installation
```xml
<plugin>
    <groupId>com.github.ptdien</groupId>
    <artifactId>rancher-maven-plugin</artifactId>
    <version>0.0.8</version>
</plugin>
```

## 2. Usage:

```text
mvn 
-Drancher.host=<The rancher host. Ex: 127.0.0.1>
-Drancher.port=<The rancher port. Ex: 8080>
-Drancher.project.name= <The environment name>
-Drancher.stack.name= <The stack name>
-Drancher.service.name= <The service name that you want to upgrade or create if it's not already existed on stack.>
-Dservice.action.timeout=60000 <60 seconds>
-Drancher.username= <The environment access key>
-Drancher.password= <The evironment secret key>
-Ddocker.image.name= <The docker image name. Ex: 127.0.0.1:9000/dockerimage:1.0.0>
rancher:run
```
