package com.example.cas;

import com.example.cas.util.TrustStoreClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CasClientApplication {

	public static void main(String[] args) throws Exception {
		String password = "changeit";
		ClassLoader classLoader = TrustStoreClient.class.getClassLoader();
		String truststore = classLoader.getResource("truststore.jks").getFile();
		// 加入本地自制的HTTPS证书，否则连接HTTPS服务会抛出以下错误
		// keytool -import -trustcacerts -file ~/.localhost.crt -alias localhost.ubuntu -keystore truststore.jks －storepass changeit
		// Caused by: java.security.cert.CertificateException: No name matching localhost found
		TrustStoreClient.initHttpsURLConnection(password, truststore);

		SpringApplication.run(CasClientApplication.class, args);
	}

}