package com.example.websocket.controller;

import com.example.websocket.handler.SystemWebSocketHandler;
import com.example.websocket.interceptor.WebSocketHandshakeInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * Created by yuweijun on 2017-07-31.
 */
@Controller
public class MessageController {

    static Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private SystemWebSocketHandler socketHandler;

    private static final String TEST_USER = "test";

    @RequestMapping(value = "/message")
    public String message(HttpSession session) {
        logger.info("user test login.");
        session.setAttribute(WebSocketHandshakeInterceptor.SESSION_USERNAME, TEST_USER);
        return "message";
    }

    @RequestMapping(value = "/message/send", method = RequestMethod.GET)
    @ResponseBody
    public String send() {
        socketHandler.sendMessageToUser(TEST_USER, new TextMessage("test message in " + this.getClass().getName() + " at " + new Date()));
        return "message has been sent to " + TEST_USER;
    }

    @RequestMapping(value = "/message/send/all", method = RequestMethod.GET)
    @ResponseBody
    public String sendToAll() {
        socketHandler.sendMessageToUsers(new TextMessage("test message for all user " + new Date()));
        return "message has been sent to all";
    }

}
