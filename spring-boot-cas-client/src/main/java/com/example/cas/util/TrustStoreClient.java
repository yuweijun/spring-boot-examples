package com.example.cas.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
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
 * @author yuweijun 2016-08-12.
 */
public class TrustStoreClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrustStoreClient.class);

    public static void main(String[] args) throws Exception {
        String password = "changeit";
        ClassLoader classLoader = TrustStoreClient.class.getClassLoader();
        String truststore = classLoader.getResource("truststore.jks").getFile();
        TrustStoreClient.initHttpsURLConnection(password, truststore);

        String httpsUrl = "https://localhost:8443/";
        HttpsURLConnection urlCon = null;
        try {
            urlCon = (HttpsURLConnection) (new URL(httpsUrl)).openConnection();
            urlCon.setDoInput(true);
            urlCon.setDoOutput(true);
            urlCon.setRequestMethod("GET");
            urlCon.getOutputStream().flush();
            urlCon.getOutputStream().close();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
