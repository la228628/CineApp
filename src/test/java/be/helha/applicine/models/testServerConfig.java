package be.helha.applicine.models;

import be.helha.applicine.common.models.ServerConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class testServerConfig {
    @BeforeAll
    public static void setup() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("server.host", "testhost");
        properties.setProperty("server.port", "45752");

        OutputStream outputStream = new FileOutputStream("config/server.properties");
        properties.store(outputStream, null);
        outputStream.close();
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
