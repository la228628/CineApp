package be.helha.applicine.models;

import be.helha.applicine.common.models.Movie;
import be.helha.applicine.common.models.Saga;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class testSaga {
    @Test
    public void testSagaCreation() {
        byte[] image = new byte[10];
        Movie movie1 = new Movie("title1", "genre1", "director1", 120, "synopsis1", image, "imagePath1");
        Movie movie2 = new Movie("title2", "genre2", "director2", 100, "synopsis2", image, "imagePath2");
        ArrayList<Movie> movies = new ArrayList<>(Arrays.asList(movie1, movie2));
        Saga saga = new Saga(1, "title", "genre", "director", 220, "synopsis", image, "imagePath", movies);

        assertEquals(1, saga.getId());
        assertEquals("title", saga.getTitle());
        assertEquals("genre", saga.getGenre());
        assertEquals("director", saga.getDirector());
        assertEquals(220, saga.getTotalDuration());
        assertEquals("synopsis1\nsynopsis2\n", saga.getDescription());
        assertArrayEquals(image, saga.getImage());
        assertEquals("imagePath", saga.getImagePath());
        assertEquals(movies, saga.getMovies());
    }
}

