# java-api-best-practice
Java Api best practice and core components

# usage
in build.gradle
```gradle 
    ...
    repositories {
        maven { url "https://jitpack.io" }
    }
    dependencies {
        ...
        compile 'com.github.zdix:java-api-best-practice:0.0.5'
        providedRuntime("org.springframework.boot:spring-boot-starter-tomcat:1.4.0.RELEASE")
  	}
```
in BootApplication.java (there is an example)
```java
...
@ComponentScan(basePackages = "com.dix.base")
public class BootApplication
...
```

