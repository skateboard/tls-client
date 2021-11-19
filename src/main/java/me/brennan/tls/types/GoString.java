package me.brennan.tls.types;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

/**
 * @author freewind
 * @url https://github.com/freewind-demos/call-go-function-from-java-demo/blob/master/src/main/java/demo/types/GoString.java
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