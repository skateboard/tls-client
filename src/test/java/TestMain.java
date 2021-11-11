import com.google.gson.JsonObject;
import me.brennan.tls.HttpClient;
import me.brennan.tls.body.provided.FormBody;
import me.brennan.tls.body.provided.JsonBody;
import me.brennan.tls.converter.provided.GsonConverter;
import me.brennan.tls.cookie.Cookie;
import me.brennan.tls.request.Request;
import me.brennan.tls.request.Response;

/**
 * @author Brennan / skateboard
 * @since 11/8/2021
 **/
public class TestMain {

    public static void main(String[] args) {
        System.setProperty("tls.client.path", "native_client.dll");


        final TestCookieJar testCookieJar = new TestCookieJar();
        final HttpClient httpClient = new HttpClient.Builder()
                .cookieJar(testCookieJar)
                .converter(new GsonConverter())
                .build();

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("test", "hi");

        FormBody body = new FormBody.Builder()
                .add("test", "hi")
                .build();

        Request request = new Request.Builder()
                .url("https://httpbin.org/anything")
                .defaultChromeHeaders()
                .post(new JsonBody(jsonObject))
                .build();
        final Response<Object> response = httpClient.execute(request);
        final JsonObject responseObject = (JsonObject) response.getData();

        System.out.println(responseObject.get("json"));

        for (Cookie cookie : testCookieJar.getCookies()) {
            System.out.println(cookie.getName() + " = " + cookie.getValue());
        }

        httpClient.close();
    }

}
