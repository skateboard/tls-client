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
                .browser(HttpClient.Browser.CHROME)
                .build();

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("test", "hi");

        Request request = new Request.Builder()
                .url("https://ja3er.com/json")
                .defaultChromeHeaders()
                .get()
                .build();
        final Response<Object> response = httpClient.execute(request);
        final JsonObject responseObject = (JsonObject) response.getData();
        System.out.println(responseObject);

        httpClient.close();
    }
}
