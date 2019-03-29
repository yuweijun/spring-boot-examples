## References

官方示例

1. [https://spring.io/guides/gs/messaging-stomp-websocket/](https://spring.io/guides/gs/messaging-stomp-websocket/)
2. [https://docs.spring.io/spring/docs/current/spring-framework-reference/html/websocket.html](https://docs.spring.io/spring/docs/current/spring-framework-reference/html/websocket.html)

## nginx pass_proxy

```
  location /websocket/ {
    proxy_pass http://127.0.0.1:8080/;
    proxy_http_version 1.1;
    proxy_set_header Upgrade $http_upgrade;
    proxy_set_header Connection "Upgrade";
  }
```
