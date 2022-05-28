# Caused by: javax.net.ssl.SSLHandshakeException: Received fatal alert: protocol_version

    url: jdbc:mysql://localhost/test?characterEncoding=utf8&useSSL=false

add `userSSL=false` in jdbc connection url.

# Unable to load authentication plugin 'caching_sha2_password'.

    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.29</version>
    </dependency>

## create user

    --mysql> CREATE USER 'dbuser'@'%' IDENTIFIED BY 'dbpass';
    --mysql> GRANT ALL PRIVILEGES ON *.* TO 'dbuser'@'%' WITH GRANT OPTION;
    --mysql> FLUSH PRIVILEGES;

# move resources/migration to resources/db/migration