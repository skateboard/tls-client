import com.google.gson.JsonObject;
import com.sun.jna.Native;
import me.brennan.tls.HttpClient;
import me.brennan.tls.body.provided.FormBody;
import me.brennan.tls.body.provided.JsonBody;
import me.brennan.tls.cookie.Cookie;
import me.brennan.tls.request.Request;
import me.brennan.tls.request.Response;
import me.brennan.tls.utils.LibraryUtil;

/**
 * @author Brennan / skateboard
 * @since 11/8/2021
 **/
public class TestMain {

    public static void main(String[] args) {
        System.setProperty("tls.client.path", LibraryUtil.INSTANCE.getLibrary());


        final TestCookieJar testCookieJar = new TestCookieJar();
        final HttpClient httpClient = new HttpClient.Builder()
                .cookieJar(testCookieJar)
                .build();

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("test", "hi");

        Request request = new Request.Builder()
                .url("https://httpbin.org/anything")
                .defaultChromeHeaders()
                .post(new JsonBody(jsonObject))
                .build();
        final Response response = httpClient.execute(request);

        System.out.println(response.getStatusCode());
        System.out.println(response.getData());

        for (Cookie cookie : testCookieJar.getCookies()) {
            System.out.println(cookie.getName() + " = " + cookie.getValue());
        }

        httpClient.close();
    }

}
