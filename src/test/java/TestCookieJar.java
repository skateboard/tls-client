import me.brennan.tls.cookie.Cookie;
import me.brennan.tls.cookie.CookieJar;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Brennan / skateboard
 * @since 11/8/2021
 **/
public class TestCookieJar implements CookieJar {
    private final List<Cookie> cookies = new LinkedList<>();

    @Override
    public void saveFromRequest(String url, List<Cookie> cookies) {
        this.cookies.addAll(cookies);
    }

    @Override
    public List<Cookie> loadForRequest(String url) {
        return cookies;
    }

    public List<Cookie> getCookies() {
        return cookies;
    }
}
