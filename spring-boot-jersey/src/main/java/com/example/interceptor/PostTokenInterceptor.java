package com.example.interceptor;

import com.example.annotations.PreventDuplicatePostToken;
import com.example.util.CachedMutex;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

/**
 * @author yuweijun 2016-08-23
 */
public class PostTokenInterceptor implements HandlerInterceptor {

    private static final String POST_TOKEN = "postToken";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            PreventDuplicatePostToken annotation = method.getAnnotation(PreventDuplicatePostToken.class);
            if (annotation != null) {
                if ("GET".equals(request.getMethod().toUpperCase())) {
                    setPostToken(request);
                    return true;
                } else if ("POST".equals(request.getMethod().toUpperCase())) {
                	Cookie cookie = WebUtils.getCookie(request, "SESSION");
                    String sessionId = cookie == null ? "" : cookie.getValue();
                    Object mutex = CachedMutex.getMutex(sessionId);
                    synchronized (mutex) {
                    	HttpSession session = request.getSession(false);
                    	if (session == null) {
                    		return false;
                    	}
                        if (isRepeatSubmit(request)) {
                            // 中文内容：请不要重复提交
                            response.getOutputStream().print("{\"result\": -1, \"errMsg\": \"\\u8BF7\\u4E0D\\u8981\\u91CD\\u590D\\u63D0\\u4EA4\"}");
                            response.setStatus(403);
                            return false;
                        }
                        // 相同session的一个用户提交反馈之后，立即删除token，重复提交的被取消
                        session.removeAttribute(POST_TOKEN);
                    }
                    return true;
                }
            }
        }
        return true;
    }

    void setPostToken(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "SESSION");
        String sessionId = cookie == null ? "" : cookie.getValue();
        Object mutex = CachedMutex.getMutex(sessionId);
        synchronized (mutex) {
        	HttpSession session = request.getSession(false);
        	if (session == null) {
        		// favicon.ico结尾的不拦截（http://127.0.0.1:8090/favicon.ico）
        		StringBuffer requestURL = request.getRequestURL();
        		String favicon = requestURL.substring(requestURL.length() - 12);
        		if ("/favicon.ico".equals(favicon)) {
        			return;
        		}
        		session = request.getSession(true);
        	}
            // 如果session中没有此token，则设置一个，避免并发请求，造成token重置
            // 生成的token 一直到post完成才会清除
            if (session.getAttribute(POST_TOKEN) == null) {
                session.setAttribute(POST_TOKEN, System.currentTimeMillis());
            }
        }
    }

    private boolean isRepeatSubmit(HttpServletRequest request) {
        Object serverToken = request.getSession(false).getAttribute(POST_TOKEN);
        if (serverToken == null) {
            return true;
        }
        String clientToken = request.getParameter(POST_TOKEN);
        if (clientToken == null) {
            return true;
        }
        Long lastTimeMillis= Long.valueOf(clientToken);
        if (!lastTimeMillis.equals(serverToken)) {
            return true;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastTimeMillis.longValue() <= 100) {
            // 限定毫秒内的重复提交请求也不处理
            return true;
        }

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

}
