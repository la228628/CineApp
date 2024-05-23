package be.helha.applicine.models.exceptions;

import be.helha.applicine.common.models.Movie;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class testMovie {
    @Test
    public void testMovieCreation() {
        byte[] image = new byte[10];
        Movie movie = new Movie("title", "genre", "director", 120, "synopsis", image, "imagePath");

        assertEquals("title", movie.getTitle());
        assertEquals("genre", movie.getGenre());
        assertEquals("director", movie.getDirector());
        assertEquals(120, movie.getTotalDuration());
        assertEquals("synopsis", movie.getDescription());
        assertArrayEquals(image, movie.getImage());
        assertEquals("imagePath", movie.getImagePath());
    }

    @Test
    public void testMovieCreationWithId() {
        byte[] image = new byte[10];
        Movie movie = new Movie(1, "title", "genre", "director", 120, "synopsis", image, "imagePath");

        assertEquals(1, movie.getId());
        assertEquals("title", movie.getTitle());
        assertEquals("genre", movie.getGenre());
        assertEquals("director", movie.getDirector());
        assertEquals(120, movie.getTotalDuration());
        assertEquals("synopsis", movie.getDescription());
        assertArrayEquals(image, movie.getImage());
        assertEquals("imagePath", movie.getImagePath());
    }
}
