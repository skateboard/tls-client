package me.brennan.tls.body;

/**
 * @author Brennan / skateboard
 * @since 11/8/2021
 **/
public interface Body {

    long contentLength();

    String contentType();

    String body();

}
