package me.brennan.tls.request;

import me.brennan.tls.converter.Converter;

import java.util.Map;

/**
 * @author Brennan / skateboard
 * @since 11/8/2021
 **/
public class Response {
    private String url;
    private int statusCode;
    private Map<String, String> headers;
    private String data;

    public Response(String url, int statusCode, Map<String, String> headers, String data) {
        this.url = url;
        this.statusCode = statusCode;
        this.headers = headers;
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getData() {
        return data;
    }
}
