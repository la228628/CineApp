package be.helha.applicine.models;

import be.helha.applicine.common.models.ServerConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class testServerConfig {
    static Properties properties;
    @BeforeAll
    public static void setup() throws Exception {
        properties = new Properties();
        properties.setProperty("server.host", "testhost");
        properties.setProperty("server.port", "45752");

        OutputStream outputStream = new FileOutputStream("config/server.properties");
        properties.store(outputStream, null);
        outputStream.close();
    }

    @AfterAll
    public static void teardown() throws IOException {
        properties.setProperty("server.host", "localhost");
        OutputStream outputStream = new FileOutputStream("config/server.properties");
        properties.store(outputStream, null);
    }

    @Test
    public void testGetHost() {
        assertEquals("testhost", ServerConfig.getHost());
    }

    @Test
    public void testGetPort() {
        assertEquals(45752, ServerConfig.getPort());
    }
}
