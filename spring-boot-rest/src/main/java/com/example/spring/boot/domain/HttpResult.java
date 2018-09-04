package com.example.spring.boot.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class HttpResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String message = "OK";

    private int status = 200;

    private T data;

    public String getMessage() {
        return this.message;
    }

    public HttpResult<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return this.data;
    }

    public HttpResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    public int getStatus() {
        return this.status;
    }

    public HttpResult<T> setStatus(int code) {
        this.status = code;
        return this;
    }

    public static <E> HttpResult<E> ofObject() {
        return new HttpResult<>();
    }

    public static HttpResult<Integer> ofInteger() {
        return HttpResult.<Integer>ofObject().setData(0);
    }

    public static HttpResult<Boolean> ofBoolean() {
        return HttpResult.<Boolean>ofObject().setData(false);
    }

    /**
     * 类似 HttpResult.&lt;List&lt;String&gt;&gt;ofObject() 的简写
     */
    public static <E> HttpResult<List<E>> ofList() {
        return HttpResult.ofObject();
    }

    /**
     * 类似 HttpResult.&lt;Map&lt;String, String&gt;&gt;ofObject() 的简写
     */
    public static <K, V> HttpResult<Map<K, V>> ofMap() {
        return HttpResult.ofObject();
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "message='" + message + '\'' +
                ", status=" + status +
                ", data=" + data +
                '}';
    }

}