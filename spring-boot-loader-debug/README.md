## Jar Application

1. IDEA配置好Jar Application需要的一些参数，jar包的路径，搜索类文件的路径即工程路径。
    > /data/github.com/spring-boot-examples/spring-boot-loader-debug/target/spring-boot-loader-debug.jar
2. Search Sources using module's classpath : select spring-boot-loader-debug module.

## maven pom.xml

然后再在工程的pom文件中引入spring-boot-loader的依赖(因为IDEA会在工程里查找源文件，所以必须主动引入才行)，在JarLauncher类上打上断点，就可以愉快地进行debug了。

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-loader</artifactId>
</dependency>
```

## Thread stack

	  at com.example.boot.SpringBootLoaderDebugApplication.main(SpringBootLoaderDebugApplication.java:13)
	  at sun.reflect.NativeMethodAccessorImpl.invoke0(NativeMethodAccessorImpl.java:-1)
	  at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	  at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	  at java.lang.reflect.Method.invoke(Method.java:498)
	  at org.springframework.boot.loader.MainMethodRunner.run(MainMethodRunner.java:48)
	  at org.springframework.boot.loader.Launcher.launch(Launcher.java:87)
	  at org.springframework.boot.loader.Launcher.launch(Launcher.java:50)
	  at org.springframework.boot.loader.JarLauncher.main(JarLauncher.java:51)
