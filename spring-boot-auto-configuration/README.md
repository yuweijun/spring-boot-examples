## build-info and git info

    $ mvn clean package

## 访问静态资源或者jsp页面时抛出如下错误:

Whitelabel Error Page

This application has no explicit mapping for /error, so you are seeing this as a fallback.

Thu Dec 07 14:09:55 CST 2017
There was an unexpected error (type=Not Found, status=404).
No message available

/WEB-INF/jsp/index.jsp

## 或者是

> {"status":404,"error":"Not Found","message":"No message available","timeStamp":"Sun May 28 20:18:50 CST 2017","trace":null}

# IDEA下要注意spring-boot静态资源加载不成功或者jsp访问不成功的问题

1. 必须在pom.xml中加入以下依赖

```xml
    <dependency>
        <groupId>org.apache.tomcat.embed</groupId>
        <artifactId>tomcat-embed-jasper</artifactId>
        <scope>provided</scope>
    </dependency>
```    

如果加了这个依赖，并且用命令行运行：

    mvn spring-boot:run

2. 在IDEA中运行则最重要是在 Run - Edit Configurations - Working directory 中设置为当前maven模块所在目录，或者是：
    
    $MODULE_DIR$ 
    
还是不能解析jsp抛出错误，则把上面依赖中的`<scope>provided</scope>`删除掉。
    
# References

1. [spring-boot-sample-web-jsp](https://github.com/spring-projects/spring-boot/tree/v1.5.9.RELEASE/spring-boot-samples/spring-boot-sample-web-jsp)    
    
