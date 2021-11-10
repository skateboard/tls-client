package me.brennan.tls.cookie;

import java.util.List;

/**
 * @author Brennan / skateboard
 * @since 11/8/2021
 **/
public interface CookieJar {

    void saveFromRequest(String url, List<Cookie> cookies);

    List<Cookie> loadForRequest(String url);

}
