package com.ganesan.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class Config {
    private static Config INSTANCE = null;
    private static String CONFIG_FILE = "/Users/sankaraganesan/MyOwn/exasol-yugabytedb/scripts/python/ganesanconfig.yaml";

    public static Config getInstance() throws FileNotFoundException {
        if (INSTANCE == null) {
            synchronized (Config.class) {
                if (INSTANCE == null) {
                }
                INSTANCE = new Config();
                INSTANCE.load(CONFIG_FILE);
            }
        }
        return (INSTANCE);
    }

    private Map<String, Object> myConf;

    private void load(final String configFileName) throws FileNotFoundException {
        final Yaml yaml = new Yaml();
        FileInputStream in = null;
        try {

            in = new FileInputStream(configFileName);
            myConf = yaml.load(in);
            //System.out.println(myConf.toString());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (final IOException e) {
                }
            }
        }
    }

    public Map<String, Object> getConfig() {
        return myConf;
    }
}