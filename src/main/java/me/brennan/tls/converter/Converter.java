package me.brennan.tls.converter;

/**
 * @author Brennan / skateboard
 * @since 11/8/2021
 **/
public interface Converter<V> {

    V convertFrom(String value);
}
