## jrebel generate

    $> mvn org.zeroturnaround:jrebel-maven-plugin:generate

## debug spring boot v1.x using maven

> By default, the run goal runs in the same process unless jvm arguments or an agent have been specified. You can enable or disable forking explicitly using the fork attribute.
> 
> If you need to fork the process and debug it you can add the necessary JVM arguments to enable remote debugging. 

    $> mvn spring-boot:run -Drun.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=8000"

## spring boot v2.0

    $> mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"

## Referrences

1. https://docs.spring.io/autorepo/docs/spring-boot/1.5.9.RELEASE/maven-plugin/examples/run-debug.html
2. https://docs.spring.io/spring-boot/docs/current/maven-plugin/examples/run-debug.html