package be.helha.applicine.FileMangement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {

    /**
     * Creates the data folder in the Appdata folder.
     */
    public static void createDataFolder() {
        String getAppdata = System.getenv("APPDATA");
        Path path = Paths.get(getAppdata + "/Applicine/images/");
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Copies an image to the Appdata folder.
     * @param imagePath The path of the image to copy.
     * @return The path of the copied image.
     */

    public static String copyImageToAppdata(String imagePath) {
        Path source = Paths.get(imagePath);
        String fileSeparator = System.getProperty("file.separator");
        String imageName = imagePath.substring(imagePath.lastIndexOf(fileSeparator) + 1);
        System.out.println(imageName);
        Path destination = Paths.get(System.getenv("APPDATA") + "/Applicine/images/" + imageName);
        try {
            Files.copy(source, destination);
            return destination.toString();
        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;
    }
}
