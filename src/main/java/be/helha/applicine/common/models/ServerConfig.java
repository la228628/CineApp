package be.helha.applicine.common.models;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class represents the server configuration.
 */
public class ServerConfig {
    private static final Properties properties;

    static {
        properties = new Properties();
        try {
            InputStream inputStream = new FileInputStream("config/server.properties");
            properties.load(inputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the host of the server.
     * @return The host of the server.
     */
    public static String getHost() {
        return properties.getProperty("server.host");
    }

    /**
     * Get the port of the server.
     * @return The port of the server.
     */
    public static int getPort() {
        return Integer.parseInt(properties.getProperty("server.port"));
    }
}
