package be.helha.applicine.common.models;
import java.io.Serializable;

public abstract class Viewable implements Serializable {

    private Integer id;
    private String title;
    private String genre;
    private String director;
    private Integer duration;
    private String synopsis;
    private byte[] image;
    private String imagePath;

    /**
     * Constructor of the class.
     * Used when the viewable is created.
     * @param title The title of the viewable.
     * @param genre The genre of the viewable.
     * @param director The director of the viewable.
     * @param duration The duration of the viewable.
     * @param synopsis The synopsis of the viewable.
     * @param image The image of the viewable.
     * @param imagePath The path to the image of the viewable.
     */
    public Viewable(String title, String genre, String director, int duration, String synopsis, byte[] image, String imagePath) {
        this.title = title;
        this.genre = genre;
        this.director = director;
        this.duration = duration;
        this.synopsis = synopsis;
        this.image = image;
        this.imagePath = imagePath;
    }

    /**
     * Constructor of the class.
     * Used when the viewable is modified.
     * @param id The id of the viewable.
     * @param title The title of the viewable.
     * @param genre The genre of the viewable.
     * @param director The director of the viewable.
     * @param duration The duration of the viewable.
     * @param  synopsis The synopsis of the viewable.
     * @param image The image of the viewable.
     * @param imagePath The path to the image of the viewable.
     */

    public Viewable(int id, String title, String genre, String director, int duration, String synopsis, byte[] image, String imagePath) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.director = director;
        this.duration = duration;
        this.synopsis = synopsis;
        this.image = image;
        this.imagePath = imagePath;
    }

    /**
     * Get the description of the viewable.
     * @return The description of the viewable.
     */
    public abstract String getDescription();

    /**
     * Get the total duration of the viewable.
     * @return The total duration of the viewable.
     */
    public abstract int getTotalDuration();
    public int getId() {
        return id == null ? -1 : id;
    }

    /**
     * Get the title of the viewable.
     * @return The title of the viewable.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the genre of the viewable.
     * @return The genre of the viewable.
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Get the director of the viewable.
     * @return The director of the viewable.
     */
    public String getDirector() {
        return director;
    }

    /**
     * Get the synopsis of the viewable.
     * @return The synopsis of the viewable.
     */

    public String getSynopsis() {
        return synopsis;
    }

    /**
     * Get the image of the viewable.
     * @return The image of the viewable.
     */

    public byte[] getImage() {
        return image;
    }

    /**
     * Get the duration of the viewable.
     * @return The duration of the viewable.
     */

    public int getDuration() {
        return duration;
    }

    /**
     * Set the id of the viewable.
     * @param id The id of the viewable.
     */

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set the title of the viewable.
     * @param title The title of the viewable.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set the genre of the viewable.
     * @param genre The genre of the viewable.
     */

    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Set the director of the viewable.
     * @param director The director of the viewable.
     */

    public void setDirector(String director) {
        this.director = director;
    }

    /**
     * Set the duration of the viewable.
     * @param duration The duration of the viewable.
     */

    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Set the synopsis of the viewable.
     * @param synopsis The synopsis of the viewable.
     */

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    /**
     * Set the image of the viewable.
     * @param image The image of the viewable.
     */
    public void setImage(byte[] image) {
        this.image = image;
    }

    /**
     * Get the path to the image of the viewable.
     * @return The path to the image of the viewable.
     */

    public String getImagePath() {
        return imagePath;
    }

    /**
     * Set the path to the image of the viewable.
     * @param imagePath The path to the image of the viewable.
     */

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}
