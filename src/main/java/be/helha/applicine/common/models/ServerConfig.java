package be.helha.applicine.common.models;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class ServerConfig {
    private static Properties properties;

    static {
        properties = new Properties();
        try {
            InputStream inputStream = new FileInputStream("config/server.properties");
            properties.load(inputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getHost() {
        return properties.getProperty("server.host");
    }

    public static int getPort() {
        return Integer.parseInt(properties.getProperty("server.port"));
    }
}
