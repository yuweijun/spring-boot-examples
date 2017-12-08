package com.example.websocket.config;

import com.example.websocket.handler.SystemWebSocketHandler;
import com.example.websocket.interceptor.WebSocketHandshakeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketHandlerConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(systemWebSocketHandler(), "/webSocketServer")
                .addInterceptors(new WebSocketHandshakeInterceptor());
        registry.addHandler(systemWebSocketHandler(), "/sockjs/webSocketServer")
                .addInterceptors(new WebSocketHandshakeInterceptor()).withSockJS();
    }

    @Bean
    public SystemWebSocketHandler systemWebSocketHandler() {
        return new SystemWebSocketHandler();
    }

}
