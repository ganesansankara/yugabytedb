package com.ganesan.utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigHelper {

    private static Properties myConfig;

    static {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        myConfig = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream("application.properties")) {
            myConfig.load(resourceStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getConfig(String key, String defValue) {
        String s= myConfig.getProperty(key);
        return ( s != null ) ? s : defValue;
    }

}
