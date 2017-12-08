package com.example.websocket.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by yuweijun on 2017-07-31.
 */
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    private static Logger logger = LoggerFactory.getLogger(WebSocketHandshakeInterceptor.class);

    public static final String WEBSOCKET_USERNAME = "websocket-username";

    public static final String SESSION_USERNAME = "session-username";

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession(false);
            if (session != null) {
                // 使用userName区分WebSocketHandler，以便定向发送消息
                String userName = (String) session.getAttribute(SESSION_USERNAME);
                logger.info("user name is {}", userName);
                // 将HttpSession中的userName放入WebSocketSession中
                attributes.put(WEBSOCKET_USERNAME, userName);
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception exception) {
        logger.info("after hand shake");
    }

}
