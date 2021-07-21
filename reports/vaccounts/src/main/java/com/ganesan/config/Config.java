package com.ganesan.config;

import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class Config {

    private final static Map<String, Object> myConf;

    static {
        InputStream resourceStream = null;
        String cfile = "ganesanconfig.yaml";

        resourceStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(cfile);
        if (resourceStream != null) {
            myConf = new Yaml().load(resourceStream);
        } else {
            myConf = null;
            System.out.printf("ERROR LOADING CONFIG FILE:%s%n", cfile);
        }
        if (resourceStream != null) {

            try {
                resourceStream.close();
                resourceStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Map<String, Object> getConfig(String sectionName) {
        return (Map<String, Object>) myConf.get(sectionName);
    }
}