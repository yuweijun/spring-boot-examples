package com.example.websocket.handler;

import com.example.websocket.interceptor.WebSocketHandshakeInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by yuweijun on 2017-07-31.
 */
public class SystemWebSocketHandler implements WebSocketHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final ArrayList<WebSocketSession> users = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("connect to the websocket success......");
        users.add(session);
        String userName = (String) session.getAttributes().get(WebSocketHandshakeInterceptor.WEBSOCKET_USERNAME);
        logger.info("username is {}", userName);

        if (userName != null) {
            int count = 100;
            session.sendMessage(new TextMessage("未读消息数: " + count));
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        logger.info("handleMessage for websocket");
        sendMessageToUsers(new TextMessage("handleMessage"));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        logger.info("websocket connection closed......");
        users.remove(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        logger.info("websocket connection closed......");
        users.remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 给所有在线用户发送消息
     *
     * @param message
     */
    public void sendMessageToUsers(TextMessage message) {
        for (WebSocketSession user : users) {
            sendMessage(message, user);
        }
    }

    private void sendMessage(TextMessage message, WebSocketSession user) {
        Object websocketUserName = user.getAttributes().get(WebSocketHandshakeInterceptor.WEBSOCKET_USERNAME);
        if (websocketUserName == null) {
            return;
        } else {
            logger.info("send message to user : {}", websocketUserName);
            try {
                if (user.isOpen()) {
                    user.sendMessage(message);
                } else {
                    logger.info("websocket for user {} has been closed", websocketUserName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 给某个用户发送消息
     *
     * @param userName
     * @param message
     */
    public void sendMessageToUser(String userName, TextMessage message) {
        logger.info("send message to user : {}, message : {}", userName, message);
        for (WebSocketSession user : users) {
            Object websocketUserName = user.getAttributes().get(WebSocketHandshakeInterceptor.WEBSOCKET_USERNAME);
            if (websocketUserName == null) {
                logger.info("WEBSOCKET_USERNAME is null");
                return;
            } else {
                logger.info("web socket user name : {}", websocketUserName);
                if (websocketUserName.equals(userName)) {
                    sendMessage(message, user);
                    return;
                }
            }
        }
    }
}
