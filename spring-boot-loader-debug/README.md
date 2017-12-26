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

## debug from vscode

	$ java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000 -jar target/spring-boot-loader-debug.jar

在vscode中在`SpringBootLoaderDebugApplication`类main方法中打上断点，在vscode中按F5，并attach到端口8000上，程序运行后会进入断点。

## 解压存档

一些PaaS实现可能选择在运行前先解压存档。例如,Cloud Foundry就是这样操作的。你可以运行一个解压的存档,只需简单的启动合适的启动器:

    $ unzip -q myapp.jar
    $ java org.springframework.boot.loader.JarLauncher

    .
    ├── BOOT-INF
    │   ├── classes
    │   │   ├── application.yml
    │   │   ├── com
    │   │   │   └── example
    │   │   │       └── spring
    │   │   │           └── jsp
    │   │   │               ├── ExampleInfoContributor.class
    │   │   │               └── SpringBootInfoApplication.class
    │   │   ├── git.properties
    │   │   └── logback-spring.xml
    │   └── lib
    │       ├── classmate-1.3.4.jar
    │       ├── hibernate-validator-5.3.6.Final.jar
    │       ├── jackson-annotations-2.8.0.jar
    │       ├── jackson-core-2.8.10.jar
    │       ├── jackson-databind-2.8.10.jar
    │       ├── jboss-logging-3.3.1.Final.jar
    │       ├── jcl-over-slf4j-1.7.25.jar
    │       ├── jul-to-slf4j-1.7.25.jar
    │       ├── log4j-over-slf4j-1.7.25.jar
    │       ├── logback-classic-1.1.11.jar
    │       ├── logback-core-1.1.11.jar
    │       ├── slf4j-api-1.7.25.jar
    │       ├── snakeyaml-1.17.jar
    │       ├── spring-aop-4.3.13.RELEASE.jar
    │       ├── spring-beans-4.3.13.RELEASE.jar
    │       ├── spring-boot-1.5.9.RELEASE.jar
    │       ├── spring-boot-actuator-1.5.9.RELEASE.jar
    │       ├── spring-boot-autoconfigure-1.5.9.RELEASE.jar
    │       ├── spring-boot-starter-1.5.9.RELEASE.jar
    │       ├── spring-boot-starter-actuator-1.5.9.RELEASE.jar
    │       ├── spring-boot-starter-logging-1.5.9.RELEASE.jar
    │       ├── spring-boot-starter-tomcat-1.5.9.RELEASE.jar
    │       ├── spring-boot-starter-web-1.5.9.RELEASE.jar
    │       ├── spring-context-4.3.13.RELEASE.jar
    │       ├── spring-core-4.3.13.RELEASE.jar
    │       ├── spring-expression-4.3.13.RELEASE.jar
    │       ├── spring-web-4.3.13.RELEASE.jar
    │       ├── spring-webmvc-4.3.13.RELEASE.jar
    │       ├── tomcat-annotations-api-8.5.23.jar
    │       ├── tomcat-embed-core-8.5.23.jar
    │       ├── tomcat-embed-el-8.5.23.jar
    │       ├── tomcat-embed-websocket-8.5.23.jar
    │       └── validation-api-1.1.0.Final.jar
    ├── classes
    │   ├── application.yml
    │   ├── com
    │   │   └── example
    │   │       └── spring
    │   │           └── jsp
    │   │               ├── ExampleInfoContributor.class
    │   │               └── SpringBootInfoApplication.class
    │   ├── git.properties
    │   ├── logback-spring.xml
    │   └── META-INF
    │       └── build-info.properties
    ├── generated-sources
    │   └── annotations
    ├── maven-archiver
    │   └── pom.properties
    ├── maven-status
    │   └── maven-compiler-plugin
    │       ├── compile
    │       │   └── default-compile
    │       │       ├── createdFiles.lst
    │       │       └── inputFiles.lst
    │       └── testCompile
    │           └── default-testCompile
    │               └── inputFiles.lst
    ├── META-INF
    │   ├── build-info.properties
    │   ├── MANIFEST.MF
    │   └── maven
    │       └── com.example.spring
    │           └── spring-boot-info
    │               ├── pom.properties
    │               └── pom.xml
    ├── org
    │   └── springframework
    │       └── boot
    │           └── loader
    │               ├── archive
    │               │   ├── Archive.class
    │               │   ├── Archive$Entry.class
    │               │   ├── Archive$EntryFilter.class
    │               │   ├── ExplodedArchive$1.class
    │               │   ├── ExplodedArchive.class
    │               │   ├── ExplodedArchive$FileEntry.class
    │               │   ├── ExplodedArchive$FileEntryIterator.class
    │               │   ├── ExplodedArchive$FileEntryIterator$EntryComparator.class
    │               │   ├── JarFileArchive.class
    │               │   ├── JarFileArchive$EntryIterator.class
    │               │   └── JarFileArchive$JarFileEntry.class
    │               ├── data
    │               │   ├── ByteArrayRandomAccessData.class
    │               │   ├── RandomAccessData.class
    │               │   ├── RandomAccessDataFile.class
    │               │   ├── RandomAccessDataFile$DataInputStream.class
    │               │   ├── RandomAccessDataFile$FilePool.class
    │               │   └── RandomAccessData$ResourceAccess.class
    │               ├── ExecutableArchiveLauncher$1.class
    │               ├── ExecutableArchiveLauncher.class
    │               ├── jar
    │               │   ├── AsciiBytes.class
    │               │   ├── Bytes.class
    │               │   ├── CentralDirectoryEndRecord.class
    │               │   ├── CentralDirectoryFileHeader.class
    │               │   ├── CentralDirectoryParser.class
    │               │   ├── CentralDirectoryVisitor.class
    │               │   ├── FileHeader.class
    │               │   ├── Handler.class
    │               │   ├── JarEntry.class
    │               │   ├── JarEntryFilter.class
    │               │   ├── JarFile$1.class
    │               │   ├── JarFile$2.class
    │               │   ├── JarFile$3.class
    │               │   ├── JarFile.class
    │               │   ├── JarFileEntries$1.class
    │               │   ├── JarFileEntries.class
    │               │   ├── JarFileEntries$EntryIterator.class
    │               │   ├── JarFile$JarFileType.class
    │               │   ├── JarURLConnection$1.class
    │               │   ├── JarURLConnection.class
    │               │   ├── JarURLConnection$JarEntryName.class
    │               │   └── ZipInflaterInputStream.class
    │               ├── JarLauncher.class
    │               ├── LaunchedURLClassLoader$1.class
    │               ├── LaunchedURLClassLoader.class
    │               ├── Launcher.class
    │               ├── MainMethodRunner.class
    │               ├── PropertiesLauncher$1.class
    │               ├── PropertiesLauncher$ArchiveEntryFilter.class
    │               ├── PropertiesLauncher.class
    │               ├── PropertiesLauncher$PrefixMatchingArchiveFilter.class
    │               ├── util
    │               │   └── SystemPropertyUtils.class
    │               └── WarLauncher.class
    ├── spring-boot-info.jar
    └── spring-boot-info.jar.original

