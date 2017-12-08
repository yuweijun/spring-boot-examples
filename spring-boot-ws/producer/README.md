## 初始化

在项目目录下运行：

> mvn clean compile

根据wsdl/xsd文件创建java源码在src/main/java目录下。

## 启动服务

> mvn springboot:run

## 测试

可运行测试用例：ApplicationIntegrationTests.java

## 命令行测试

Test the application

Now that the application is running, you can test it. Create a file request.xml containing the following SOAP request:

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
				  xmlns:gs="http://spring.io/guides/gs-producing-web-service">
   <soapenv:Header/>
   <soapenv:Body>
      <gs:getCountryRequest>
         <gs:name>Spain</gs:name>
      </gs:getCountryRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

The are a few options when it comes to testing the SOAP interface. You can use something like SoapUI or just use command line tools if you are on a *nix/Mac system as shown below.

```bash
$ curl --header "content-type: text/xml" -d @request.xml http://localhost:8080/ws
```
