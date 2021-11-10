package me.brennan.tls.types;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

/**
 * @author Brennan / skateboard
 * @since 11/8/2021
 **/
public class GoString extends Structure implements Structure.ByValue {
    public String p;
    public long n;

    public GoString() {
    }

    public GoString(String content) {
        this.p = content;
        this.n = p.length();
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("p", "n");
    }

    @Override
    public String toString() {
        return p;
    }
}