package com.example.spring.jdbc.test.util;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author yuweijun 2016-06-12.
 */
public class PageFilter implements Filter {

    public PageFilter() {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        PaginationContext.setPageNum(getPageNum(httpRequest));
        PaginationContext.setPageSize(getPageSize(httpRequest));

        try {
            chain.doFilter(request, response);
        } finally {
            // 使用完Threadlocal，将其删除。使用finally确保一定将其删除
            PaginationContext.removePageNum();
            PaginationContext.removePageSize();
        }
    }

    /**
     * 获得pager.offset参数的值
     *
     * @param request
     * @return
     */
    protected int getPageNum(HttpServletRequest request) {
        int pageNum = 1;
        try {
            String pageNums = request.getParameter("pageNum");
            if (pageNums != null && isNumeric(pageNums)) {
                pageNum = Integer.parseInt(pageNums);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return pageNum;
    }

    /**
     * 设置默认每页大小
     *
     * @return
     */
    protected int getPageSize(HttpServletRequest request) {
        int pageSize = 10;    // 默认每页10条记录
        try {
            String pageSizes = request.getParameter("pageSize");
            if (pageSizes != null && isNumeric(pageSizes)) {
                pageSize = Integer.parseInt(pageSizes);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return pageSize;
    }

    private boolean isNumeric(String number) {
        Matcher matcher = Pattern.compile("^\\d+$").matcher(number);
        return matcher.find();
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
    }

}