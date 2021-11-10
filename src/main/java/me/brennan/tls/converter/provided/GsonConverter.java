package me.brennan.tls.converter.provided;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.brennan.tls.converter.Converter;

/**
 * @author Brennan / skateboard
 * @since 11/8/2021
 **/
public class GsonConverter implements Converter<JsonElement> {
    private final Gson GSON;

    public GsonConverter() {
        this.GSON = new GsonBuilder().create();
    }

    @Override
    public String convertTo(String value) {
        try {
            return GSON.toJson(value);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public JsonElement convertFrom(String value) {
        try {
            return GSON.fromJson(value, JsonElement.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
