package com.example.spring.narayana.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Listener listens for the messages in the "update" queue and writes them to the log.
 */
@Component
public class MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    @JmsListener(destination = "updates")
    public void onUpdate(String content) {
        LOGGER.info("update ----> " + content);
    }

    @JmsListener(destination = "query")
    public void onQuery(String content) {
        LOGGER.info("query ----> " + content);
    }

}
