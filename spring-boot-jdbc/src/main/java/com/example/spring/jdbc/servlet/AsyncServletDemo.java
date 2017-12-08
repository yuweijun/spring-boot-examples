package com.example.spring.jdbc.servlet;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * https://www.ibm.com/developerworks/cn/java/j-lo-servlet30/
 * http://blog.csdn.net/catoop/article/details/50501686
 *
 * @author yuweijun 2016-12-22
 */
@WebServlet(urlPatterns = "/index", asyncSupported = true)
public class AsyncServletDemo extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<p>进入Servlet的时间：" + new Date() + ".");
        out.flush();

        //在子线程中执行业务调用，并由其负责输出响应，主线程退出
        AsyncContext ctx = req.startAsync();
        ctx.addListener(new AsyncListener() {

            public void onComplete(AsyncEvent asyncEvent) throws IOException {
                // 做一些清理工作或者其他
                System.out.println("onComplete");
            }

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
                System.out.println("onTimeout");
            }

            @Override
            public void onError(AsyncEvent event) throws IOException {
                System.out.println("onError");
            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
                System.out.println("onStartAsync");
            }

        });

        new Thread(new AsyncBusinessExecutor(ctx)).start();

        out.println("<p>结束Servlet的时间：" + new Date() + ".");
        out.flush();
    }

}

class AsyncBusinessExecutor implements Runnable {

    private AsyncContext ctx = null;

    public AsyncBusinessExecutor(AsyncContext ctx) {
        this.ctx = ctx;
    }

    public void run() {
        try {
            //等待十秒钟，以模拟业务方法的执行
            Thread.sleep(10000);
            PrintWriter out = ctx.getResponse().getWriter();
            out.println("<p>业务处理完毕的时间：" + new Date() + ".");
            out.flush();
            ctx.complete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
