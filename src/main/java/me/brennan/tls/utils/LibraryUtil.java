package me.brennan.tls.utils;

import me.brennan.tls.HttpClient;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Brennan / skateboard
 * @since 11/9/2021
 **/
public enum LibraryUtil {
    INSTANCE;

    public String getLibrary() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File path1 = Paths.get(classLoader.getResource("native_client.dll").toURI()).toFile();

            return path1.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
