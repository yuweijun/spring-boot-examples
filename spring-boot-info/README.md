## 47.7 Application information

Application information exposes various information collected from all InfoContributor beans defined in your ApplicationContext. Spring Boot includes a number of auto-configured InfoContributors and you can also write your own.

### 47.7.1 Auto-configured InfoContributors

The following InfoContributors are auto-configured by Spring Boot when appropriate:

Name                        	Description
EnvironmentInfoContributor      Expose any key from the Environment under the info key.
GitInfoContributor              Expose git information if a git.properties file is available.
BuildInfoContributor            Expose build information if a META-INF/build-info.properties file is available.

[Tip]
It is possible to disable them all using the management.info.defaults.enabled property.

### 47.7.2 Custom application info information

You can customize the data exposed by the info endpoint by setting info.* Spring properties. All Environment properties under the info key will be automatically exposed. For example, you could add the following to your application.properties:

    info.app.encoding=UTF-8
    info.app.java.source=1.8
    info.app.java.target=1.8

[Tip]
Rather than hardcoding those values you could also expand info properties at build time.
Assuming you are using Maven, you could rewrite the example above as follows:

    info.app.encoding=@project.build.sourceEncoding@
    info.app.java.source=@java.version@
    info.app.java.target=@java.version@

### 47.7.3 Git commit information

Another useful feature of the info endpoint is its ability to publish information about the state of your git source code repository when the project was built. If a GitProperties bean is available, the git.branch, git.commit.id and git.commit.time properties will be exposed.

[Tip]
A GitProperties bean is auto-configured if a git.properties file is available at the root of the classpath. See Generate git information for more details.
If you want to display the full git information (i.e. the full content of git.properties), use the management.info.git.mode property:

    management.info.git.mode=full

### 47.7.4 Build information

The info endpoint can also publish information about your build if a BuildProperties bean is available. This happens if a META-INF/build-info.properties file is available in the classpath.

[Tip]
The Maven and Gradle plugins can both generate that file, see Generate build information for more details.

### 47.7.5 Writing custom InfoContributors

To provide custom application information you can register Spring beans that implement the InfoContributor interface.

The example below contributes an example entry with a single value:

```java
import java.util.Collections;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class ExampleInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("example",
                Collections.singletonMap("key", "value"));
    }

}
```

If you hit the info endpoint you should see a response that contains the following additional entry:

```json
{
    "example": {
        "key" : "value"
    }
}
```

