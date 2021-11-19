package me.brennan.tls;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jna.Native;
import me.brennan.tls.converter.Converter;
import me.brennan.tls.converter.provided.StringConverter;
import me.brennan.tls.cookie.Cookie;
import me.brennan.tls.cookie.CookieJar;
import me.brennan.tls.exception.ClientException;
import me.brennan.tls.library.TLSLibrary;
import me.brennan.tls.request.Request;
import me.brennan.tls.request.Response;
import me.brennan.tls.types.GoString;

import java.util.*;

/**
 * @author Brennan / skateboard
 * @since 11/8/2021
 **/
public class HttpClient {
    private final CookieJar cookieJar;
    private final String proxy;
    private final TLSLibrary tlsLibrary;
    private final Converter converter;
    private Browser browser;

    public HttpClient(CookieJar cookieJar, String proxy, Converter converter, Browser browser) throws ClientException {
        if (System.getProperty("tls.client.path") == null) {
            throw new ClientException("You must set the tls.client.path property to the path of the native client library!");
        }

        this.cookieJar = cookieJar;
        this.proxy = proxy == null ? "" : proxy;
        this.converter = converter;
        this.browser = browser;

        this.tlsLibrary = TLSLibrary.INSTANCE;
    }

    public HttpClient(CookieJar cookieJar, String proxy) {
        this(cookieJar, proxy, new StringConverter(), Browser.CHROME);
    }

    public Response<Object> execute(Request request) {
        return request.getMethod().equals("GET") ? executeGET(request) : executeData(request);
    }

    private Response<Object> executeData(Request request) {
        if (cookieJar != null) {
            final StringJoiner stringJoiner = new StringJoiner(";");

            for (Cookie cookie : cookieJar.loadForRequest(request.getUrl())) {
                String cookieString = cookie.getName() + "=" + cookie.getValue();

                if (cookie.isSecure()) {
                    cookieString += "; Secure";
                }

                if (cookie.getExpiresAt() != null) {
                    cookieString += "; expires=" + cookie.getExpiresAt();
                }

                if (cookie.getMaxAge() != 0) {
                    cookieString += "; max-age=" + cookie.getMaxAge();
                }

                if (cookie.getDomain() != null) {
                    cookieString += "; domain=" + cookie.getDomain();
                }

                if (cookie.getPath() != null) {
                    cookieString += "; path=" + cookie.getPath();
                }
                stringJoiner.add(cookieString);
            }

            request.getHeaders().put("Cookie", stringJoiner.toString());
        }

        final String requestResponse = TLSLibrary.INSTANCE.data_request(
                new GoString(request.getUrl()),
                new GoString(browser.getBrowserName()),
                new GoString(request.getMethod()),
                new GoString(request.getBody()),
                request.isAllowRedirect(),
                new GoString(proxy),
                request.getParameters() != null ? new GoString(request.getParametersAsString()) : new GoString(""),
                request.getHeaders() != null ? new GoString(request.getHeadersAsString()) : new GoString("")
        );
        final JsonObject requestObject = JsonParser.parseString(requestResponse).getAsJsonObject();

        final Map<String, String> responseHeaders = new HashMap<>();

        final List<Cookie> cookies = new LinkedList<>();
        for (Map.Entry<String, JsonElement> headers : requestObject.getAsJsonObject("headers").entrySet()) {

            if (cookieJar != null) {
                if (headers.getKey().equals("Set-Cookie")) {
                    final String[] rawCookieParams  = headers.getValue().getAsString().split(";");

                    String[] rawCookieNameAndValue = rawCookieParams[0].split("=");
                    if (rawCookieNameAndValue.length != 2) {
                        continue;
                    }
                    String cookieName = rawCookieNameAndValue[0].trim();
                    String cookieValue = rawCookieNameAndValue[1].trim();

                    Cookie cookie = new Cookie(cookieName, cookieValue);

                    for (int i = 1; i < rawCookieParams.length; i++) {
                        String[] rawCookieParamNameAndValue = rawCookieParams[i].trim().split("=");
                        String paramName = rawCookieParamNameAndValue[0].trim();

                        if (paramName.equalsIgnoreCase("secure")) {
                            cookie.setSecure(true);
                        } else {
                            if (rawCookieParamNameAndValue.length != 2) {
                                continue;
                            }

                            String paramValue = rawCookieParamNameAndValue[1].trim();

                            if (paramName.equalsIgnoreCase("expires")) {
                                cookie.setExpiresAt(paramValue);
                            } else if (paramName.equalsIgnoreCase("max-age")) {
                                cookie.setMaxAge(Long.parseLong(paramValue));
                            } else if (paramName.equalsIgnoreCase("domain")) {
                                cookie.setDomain(paramValue);
                            } else if (paramName.equalsIgnoreCase("path")) {
                                cookie.setPath(paramValue);
                            } else if (paramName.equalsIgnoreCase("comment")) {
                                cookie.setPath(paramValue);
                            }
                        }

                    }

                    cookies.add(cookie);
                }
            }

            responseHeaders.put(headers.getKey(), headers.getValue().getAsString());
        }

        if (cookieJar != null)
            cookieJar.saveFromRequest(request.getUrl(), cookies);

        Object object = converter.convertFrom(requestObject.get("body").getAsString());

        return new Response(request.getUrl(), requestObject.get("statusCode").getAsInt(), responseHeaders, object);
    }

    private Response<Object> executeGET(Request request) {
        if (cookieJar != null) {
            final StringJoiner stringJoiner = new StringJoiner(";");

            for (Cookie cookie : cookieJar.loadForRequest(request.getUrl())) {
                String cookieString = cookie.getName() + "=" + cookie.getValue();

                if (cookie.isSecure()) {
                    cookieString += "; Secure";
                }

                if (cookie.getExpiresAt() != null) {
                    cookieString += "; expires=" + cookie.getExpiresAt();
                }

                if (cookie.getMaxAge() != 0) {
                    cookieString += "; max-age=" + cookie.getMaxAge();
                }

                if (cookie.getDomain() != null) {
                    cookieString += "; domain=" + cookie.getDomain();
                }

                if (cookie.getPath() != null) {
                    cookieString += "; path=" + cookie.getPath();
                }
                stringJoiner.add(cookieString);
            }

            request.getHeaders().put("Cookie", stringJoiner.toString());
        }

        final String requestResponse = TLSLibrary.INSTANCE.get_request(
                new GoString(request.getUrl()),
                new GoString(browser.getBrowserName()),
                request.isAllowRedirect(),
                new GoString(proxy),
                request.getParameters() != null ? new GoString(request.getParametersAsString()) : new GoString(""),
                request.getHeaders() != null ? new GoString(request.getHeadersAsString()) : new GoString("")
        );
        final JsonObject requestObject = JsonParser.parseString(requestResponse).getAsJsonObject();

        final Map<String, String> responseHeaders = new HashMap<>();

        final List<Cookie> cookies = new LinkedList<>();
        for (Map.Entry<String, JsonElement> headers : requestObject.getAsJsonObject("headers").entrySet()) {

            if (cookieJar != null) {
                if (headers.getKey().equals("Set-Cookie")) {
                    final String[] rawCookieParams  = headers.getValue().getAsString().split(";");

                    String[] rawCookieNameAndValue = rawCookieParams[0].split("=");
                    if (rawCookieNameAndValue.length != 2) {
                        continue;
                    }
                    String cookieName = rawCookieNameAndValue[0].trim();
                    String cookieValue = rawCookieNameAndValue[1].trim();

                    Cookie cookie = new Cookie(cookieName, cookieValue);

                    for (int i = 1; i < rawCookieParams.length; i++) {
                        String[] rawCookieParamNameAndValue = rawCookieParams[i].trim().split("=");
                        String paramName = rawCookieParamNameAndValue[0].trim();

                        if (paramName.equalsIgnoreCase("secure")) {
                            cookie.setSecure(true);
                        } else {
                            if (rawCookieParamNameAndValue.length != 2) {
                                continue;
                            }

                            String paramValue = rawCookieParamNameAndValue[1].trim();

                            if (paramName.equalsIgnoreCase("expires")) {
                                cookie.setExpiresAt(paramValue);
                            } else if (paramName.equalsIgnoreCase("max-age")) {
                                cookie.setMaxAge(Long.parseLong(paramValue));
                            } else if (paramName.equalsIgnoreCase("domain")) {
                                cookie.setDomain(paramValue);
                            } else if (paramName.equalsIgnoreCase("path")) {
                                cookie.setPath(paramValue);
                            } else if (paramName.equalsIgnoreCase("comment")) {
                                cookie.setPath(paramValue);
                            }
                        }

                    }

                    cookies.add(cookie);
                }
            }

            responseHeaders.put(headers.getKey(), headers.getValue().getAsString());
        }

        if (cookieJar != null)
            cookieJar.saveFromRequest(request.getUrl(), cookies);

        Object object = converter.convertFrom(requestObject.get("body").getAsString());

        return new Response<>(request.getUrl(), requestObject.get("statusCode").getAsInt(), responseHeaders, object);
    }

    public void close() {
        Native.unregister(tlsLibrary.getClass());
    }

    public enum Browser {
        CHROME("chrome"),
        FIREFOX("firefox"),
        IOS("ios");

        private final String browserName;

        Browser(String browserName) {
            this.browserName = browserName;
        }

        public String getBrowserName() {
            return browserName;
        }
    }

    public static class Builder {
        private CookieJar cookieJar;
        private String proxy;
        private Converter converter;

        private Browser browser;

        public Builder browser(Browser browser) {
            this.browser = browser;

            return this;
        }

        public Builder converter(Converter converter) {
            this.converter = converter;

            return this;
        }

        public Builder proxy(String proxy) {
            this.proxy = proxy;

            return this;
        }

        public Builder cookieJar(CookieJar cookieJar) {
            this.cookieJar = cookieJar;

            return this;
        }

        public HttpClient build() {
            if (converter != null || browser != null)
                return new HttpClient(cookieJar, proxy, converter, browser);

            return new HttpClient(cookieJar, proxy);
        }
    }
}
