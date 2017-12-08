package com.example.websocket.controller;


import com.example.websocket.model.Greeting;
import com.example.websocket.model.HelloMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GreetingController.class);

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        LOGGER.info("message is {}", message);
        Thread.sleep(500); // simulated delay
        return new Greeting("Hello, " + message.getName() + "!");
    }

}
