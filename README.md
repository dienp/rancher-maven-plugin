# rancher-maven-plugin
Rancher maven plugin for automatic upgrade services

1. Installation
```xml
<pluginRepositories>
    <pluginRepository>
        <id>ossrh</id>
        <name>Sonatype Public Plugin Repository</name>
        <url>https://oss.sonatype.org/content/groups/public/</url>
		</pluginRepository>
</pluginRepositories>
```
```xml
<plugin>
    <groupId>com.github.ptdien</groupId>
    <artifactId>rancher-maven-plugin</artifactId>
    <version>0.0.3</version>
</plugin>
```

2. Usage:

```text
mvn -Drancher.root=string 
-Dservice.upgrade.timeout=long 
-Drancher.username=string 
-Ddocker.image=string 
-Drancher.password=string 
rancher:run
```

What are those above?
```text
-Drancher.root: Rancher service url (no protocol). Ex: 0.0.0.0:8080\v2-beta\projects\<projectId>\services\<serviceId>?action=.
-Dservice.upgrade.timeout: Timeout if upgrading task takes too long. Ex: 60000 (ms).
-Drancher.username: Your environment accessKey. (API -> Keys -> Add evironment API key).
-Drancher.password: Your environment secretKey
-Ddocker.image: Which docker image is used for upgrading this service? Ex: 0.0.0.0:9000/dockerimage:1.0.0
```