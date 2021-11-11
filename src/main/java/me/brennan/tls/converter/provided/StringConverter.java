package me.brennan.tls.converter.provided;

import me.brennan.tls.converter.Converter;

/**
 * @author Brennan / skateboard
 * @since 11/10/2021
 **/
public class StringConverter implements Converter<String> {

    @Override
    public String convertFrom(String value) {
        return value;
    }
}
