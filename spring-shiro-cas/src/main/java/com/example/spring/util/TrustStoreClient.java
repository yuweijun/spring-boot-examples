package com.example.spring.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

/**
 * 使用truststore.jks和服务器单向验证
 * <p>
 * 单向验证SSL证书的HttpClient（tomcat中clientAuth="false"）
 * <p>
 * <Connector port="8443" protocol="HTTP/1.1" SSLEnabled="true"
 * maxThreads="150" scheme="https" secure="true"
 * keystoreFile="${catalina.home}/conf/keystore.jks"
 * truststorePass="changeit"
 * clientAuth="true" sslProtocol="TLS" />
 *
 * 如果遇到以下异常，一般是证书导入错误造成的，请尝试重新导入，如果还是不行，有可能是运行应用的JDK和安装数字证书的JDK不是同一个造成的:
 * 因为证书先在mac下导入了一个，又在ubuntu下导入了一个证书，所以抛出如下异常。
 * $> keytool -import -v -trustcacerts -alias localhost -file ~/.localhost.crt -keystore ~/github.com/learning-programming/tomcat/src/main/resources/cacerts.jks -keypass changeit -storepass changeit
 *
 * javax.net.ssl.SSLHandshakeException: sun.security.validator.ValidatorException:
 * PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException:
 * unable to find valid certification path to requested target
 *
 * @author yuweijun 2016-08-12.
 */
public class TrustStoreClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrustStoreClient.class);

    public static void init() throws Exception {
        String password = "changeit";
        ClassLoader classLoader = TrustStoreClient.class.getClassLoader();
        String truststore = classLoader.getResource("cacerts.jks").getFile();
        TrustStoreClient.initHttpsURLConnection(password, truststore);
    }

    public static void initHttpsURLConnection(String password, String trustStorePath) throws Exception {
        SSLContext sslContext = null;

        // 实例化主机名验证接口
        HostnameVerifier hnv = new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                LOGGER.info("hostname is : {}", s);
                return true;
            }
        };

        try {
            sslContext = getSSLContext(password, trustStorePath);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        if (sslContext != null) {
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        }
        HttpsURLConnection.setDefaultHostnameVerifier(hnv);
    }

    public static SSLContext getSSLContext(String password, String trustStorePath) throws Exception {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        KeyStore trustStore = getKeyStore(password, trustStorePath);
        trustManagerFactory.init(trustStore);
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, trustManagerFactory.getTrustManagers(), null);

        return ctx;
    }

    public static KeyStore getKeyStore(String password, String keyStorePath) throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");
        FileInputStream is = new FileInputStream(keyStorePath);
        ks.load(is, password.toCharArray());
        is.close();
        return ks;
    }

}
