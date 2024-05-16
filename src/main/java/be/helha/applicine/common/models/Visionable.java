package be.helha.applicine.common.models;

import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.List;

public abstract class Visionable implements Serializable {

    private Integer id;
    private String title;
    private String genre;
    private String director;
    private Integer duration;
    private String synopsis;
    private Image image;
    private String imagePath;

    public Visionable(String title, String genre, String director, int duration, String synopsis, Image image, String imagePath) {
        this.title = title;
        this.genre = genre;
        this.director = director;
        this.duration = duration;
        this.synopsis = synopsis;
        this.image = image;
        this.imagePath = imagePath;
    }

    public Visionable(int id, String title, String genre, String director, int duration, String synopsis, Image image, String imagePath) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.director = director;
        this.duration = duration;
        this.synopsis = synopsis;
        this.image = image;
        this.imagePath = imagePath;
    }

    //méthodes qui seront implémentées dans les classes filles (ovverride)
    public abstract String getDescription();
    public abstract int getTotalDuration();


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getDirector() {
        return director;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public Image getImage() {
        return image;
    }
    public int getDuration() {
        return duration;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}
