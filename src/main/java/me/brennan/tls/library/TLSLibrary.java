package me.brennan.tls.library;

import com.sun.jna.Library;
import com.sun.jna.Native;
import me.brennan.tls.types.GoString;

/**
 * @author Brennan / skateboard
 * @since 11/8/2021
 **/
public interface TLSLibrary extends Library {
    TLSLibrary INSTANCE = Native.load(System.getProperty("tls.client.path"), TLSLibrary.class);

    String data_request(GoString requesturl, GoString method, GoString requestbody, boolean allowredirect, GoString proxy, GoString addedQuery, GoString headers);

    String get_request(GoString requesturl, boolean allowredirect, GoString proxy, GoString addedQuery, GoString headers);
}
