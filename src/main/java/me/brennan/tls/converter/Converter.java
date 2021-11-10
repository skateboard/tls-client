package me.brennan.tls.converter;

/**
 * @author Brennan / skateboard
 * @since 11/8/2021
 **/
public interface Converter<V> {

    String convertTo(String value);

    V convertFrom(String value);
}
