package com.ganesan.config;

import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class Config {
    

    private final static Map<String, Object> myConf;

     static {

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try (InputStream resourceStream = loader.getResourceAsStream("ganesanconfig.yaml")) {
            myConf = new Yaml().load(resourceStream);
        } catch (Exception e) {
                             myConf = null;
            e.printStackTrace();
        }
    }


    public static Map<String, Object> getConfig(String sectionName) {
        return (Map<String, Object>) myConf.get(sectionName);
    }
}