package org.autumframework.context;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyLoader {

    private static Properties                       properties       = new Properties();
    private static Map<String, Map<String, String>> configProperties = new HashMap<>();

    static {
        try (InputStream input = PropertyLoader.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);
                loadConfigurationProperties();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void loadConfigurationProperties() {
        for (String key : properties.stringPropertyNames()) {
            String[] parts = key.split("\\.");
            if (parts.length > 1) {
                String prefix = parts[0];
                String propertyName = key.substring(prefix.length() + 1);
                configProperties.computeIfAbsent(prefix, k -> new HashMap<>()).put(propertyName, properties.getProperty(key));
            }
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    public static String getConfigProperty(String prefix, String key) {
        return configProperties.getOrDefault(prefix, new HashMap<>()).get(key);
    }
}
