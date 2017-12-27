package com.example.spring.boot.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SessionInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionInterceptor.class);

    private final static String SESSION_KEY = "sessionId";

    @Autowired
    private HttpSession httpSession;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        MDC.remove(SESSION_KEY);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null) {
            // favicon.ico结尾的不拦截（http://127.0.0.1:8080/favicon.ico）
            StringBuffer requestURL = request.getRequestURL();
            String url = requestURL.toString();
            if (url.contains("favicon.ico")) {
                LOGGER.warn("request favicon.ico without session");
                return true;
            } else if (url.contains("/static/")) {
                LOGGER.warn("request /static/ without session");
                return true;
            }
            session = request.getSession(true);
        }

        String requestedSessionId = session.getId();
        if (requestedSessionId == null) {
            MDC.put(SESSION_KEY, "");
        } else {
            MDC.put(SESSION_KEY, requestedSessionId);
        }
        LOGGER.info(request.getRequestURI());
        if (request.getRequestURI().equals("/login")) {
            return true;
        } else {
            return login(request, response, handler);
        }
    }

    private boolean login(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        Object user = httpSession.getAttribute("user");
        if (user == null) {
            LOGGER.info("redirect to user login page");
            response.sendRedirect(request.getContextPath() + "/login");
            response.setStatus(301);
            return false;
        }

        return true;
    }

}
