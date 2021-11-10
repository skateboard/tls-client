package me.brennan.tls.body.provided;

import com.google.gson.JsonObject;
import me.brennan.tls.body.Body;

/**
 * @author Brennan / skateboard
 * @since 11/9/2021
 **/
public class JsonBody implements Body {
    private final String json;

    public JsonBody(JsonObject jsonObject) {
        this.json = jsonObject.toString();
    }

    @Override
    public long contentLength() {
        return json.length();
    }

    @Override
    public String contentType() {
        return "application/json; charset=utf-8";
    }

    @Override
    public String body() {
        return json;
    }
}
