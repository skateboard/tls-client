package me.brennan.tls.body.provided;

import me.brennan.tls.body.Body;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author Brennan / skateboard
 * @since 11/8/2021
 **/
public class FormBody implements Body {
    private Map<String, String> form;

    public FormBody(Map<String, String> form) {
        this.form = form;
    }

    @Override
    public long contentLength() {
        return formart().length();
    }

    @Override
    public String contentType() {
        return "application/x-www-form-urlencoded";
    }

    @Override
    public String body() {
        return formart();
    }

    private String formart() {
        final StringJoiner joiner = new StringJoiner("&");

        for (Map.Entry<String, String> formEntry : form.entrySet()) {
            joiner.add(formEntry.getKey() + "=" + formEntry.getValue());
        }

        return joiner.toString();
    }

    public static class Builder {
        private Map<String, String> form;

        public Builder add(String key, String value) {
            if (form == null) {
                form = new HashMap<>();
            }

            form.put(key, value);
            return this;
        }

        public FormBody build() {
            return new FormBody(form);
        }
    }
}
