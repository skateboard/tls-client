package me.brennan.tls.request;

import me.brennan.tls.body.Body;
import me.brennan.tls.library.TLSLibrary;
import me.brennan.tls.types.GoString;
import me.brennan.tls.utils.RandomUserAgent;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author Brennan / skateboard
 * @since 11/8/2021
 **/
public class Request {
    private final String url;
    private Map<String, String> parameters;
    private HashMap<String, String> headers;
    private boolean allowRedirect;
    private String method;

    public Body body;

    public Request(String url, String method, boolean allowRedirect, Map<String, String> parameters, HashMap<String, String> headers) {
        this.url = url;
        this.allowRedirect = allowRedirect;
        this.parameters = parameters;
        this.headers = headers;
        this.method = method;
    }

    public Request(String url,String method, boolean allowRedirect, Map<String, String> parameters, HashMap<String, String> headers, Body body) {
        this.url = url;
        this.parameters = parameters;
        this.headers = headers;
        this.allowRedirect = allowRedirect;
        this.method = method;
        this.body = body;
    }

    public String getBody() {
        return body.body();
    }

    public String getHeadersAsString() {
        final StringJoiner stringJoiner = new StringJoiner("&");

        for (Map.Entry<String, String> parm : headers.entrySet()) {
            stringJoiner.add(parm.getKey() + "=" + parm.getValue());
        }

        return stringJoiner.toString();
    }

    public String getParametersAsString() {
        final StringJoiner stringJoiner = new StringJoiner("&");

        for (Map.Entry<String, String> parm : parameters.entrySet()) {
            stringJoiner.add(parm.getKey() + "=" + parm.getValue());
        }

        return stringJoiner.toString();
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public boolean isAllowRedirect() {
        return allowRedirect;
    }

    public static class Builder {
        private String url;
        private Map<String, String> parameters;
        private HashMap<String, String> headers;
        private boolean allowRedirect = false;

        private String method;
        private Body body;

        public Builder get() {
            this.method = "GET";

            return this;
        }

        public Builder post(Body body) {
            this.method = "POST";
            this.body = body;

            this.addHeader("Content-Type", body.contentType());

            return this;
        }

        public Builder patch(Body body) {
            this.method = "PATCH";
            this.body = body;

            this.addHeader("Content-Type", body.contentType());

            return this;
        }

        public Builder delete(Body body) {
            this.method = "DELETE";
            this.body = body;

            this.addHeader("Content-Type", body.contentType());

            return this;
        }

        public Builder setHeaders(HashMap<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder defaultChromeHeaders() {
            return defaultChromeHeaders(RandomUserAgent.getRandomUserAgent("Chrome"));
        }

        public Builder defaultChromeHeaders(String userAgent) {
            addHeader("Sec-Ch-Ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
            addHeader("Sec-Ch-Ua-Arch", "\"x86\"");
            addHeader("Sec-Ch-Ua-Full-Version", "\"95.0.4638.69\"");
            addHeader("Sec-Ch-Ua-Mobile", "?0");
            addHeader("Sec-Ch-Ua-Platform", "Windows");
            addHeader("Sec-Ch-Ua-Platform-Version", "\"10.0.0\"");
            addHeader("Sec-Fetch-Dest", "document");
            addHeader("Sec-Fetch-Mode", "navigate");
            addHeader("Sec-Fetch-Site", "none");
            addHeader("Sec-Fetch-User", "?1");
            addHeader("Upgrade-Insecure-Requests", "1");
            addHeader("User-Agent", userAgent);

            return this;
        }

        public Builder addHeader(String key, String value) {
            if (headers == null) {
                headers = new HashMap<>();
            }

            headers.put(key, value);
            return this;
        }

        public Builder setParameters(Map<String, String> parameters) {
            this.parameters = parameters;
            return this;
        }


        public Builder addParameter(String key, String value) {
            if (parameters == null) {
                parameters = new HashMap<>();
            }

            parameters.put(key, value);
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder allowRedirect(boolean allowRedirect) {
            this.allowRedirect = allowRedirect;
            return this;
        }

        public Request build() {
            return method.equals("GET") ? new Request(url, method, allowRedirect, parameters, headers) : new Request(url, method, allowRedirect, parameters, headers, body);
        }

    }
}
