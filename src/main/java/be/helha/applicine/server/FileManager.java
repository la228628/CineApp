package be.helha.applicine.server;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class FileManager {

    /**
     * Creates the data folder in the Appdata folder.
     */
    public static void createDataFolder() throws IOException{
        String getAppdata = System.getenv("APPDATA");
        Path path = Paths.get(getAppdata + "/Applicine/images/");
        Files.createDirectories(path);
    }

    public static String createPath(String fileName) {
        return "file:" + System.getenv("APPDATA") + "/Applicine/images/" + fileName;
    }

    public static byte[] fileToByteArray(File imageFile) throws IOException {
        return Files.readAllBytes(imageFile.toPath());
    }

    public static byte[] getImageAsBytes(String imagePath) {
        try {
            imagePath = imagePath.substring(5); // Remove the "file:" scheme
            return Files.readAllBytes(Paths.get(imagePath));
        } catch (IOException e) {
            throw new RuntimeException("Error while reading image as bytes", e);
        }
    }

    public static void createImageFromBytes(byte[] imageBytes, String imagePath){
        try {
            imagePath = imagePath.substring(5); // Remove the "file:" scheme
            Files.write(Paths.get(imagePath), imageBytes);
        } catch (IOException e) {
            throw new RuntimeException("Error while creating image from bytes", e);
        }
    }
}
