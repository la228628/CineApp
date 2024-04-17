package be.helha.applicine.models;

public abstract class Visionable {

    private int id;
    private String title;
    private String genre;
    private String director;
    private int duration;
    private String synopsis;
    private String imagePath;

    public Visionable(String title, String genre, String director, int duration, String synopsis, String imagePath) {
        this.title = title;
        this.genre = genre;
        this.director = director;
        this.duration = duration;
        this.synopsis = synopsis;
        this.imagePath = imagePath;
    }

    public Visionable(int id, String title, String genre, String director, int duration, String synopsis, String imagePath) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.director = director;
        this.duration = duration;
        this.synopsis = synopsis;
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

    public String getImagePath() {
        return imagePath;
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

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}
