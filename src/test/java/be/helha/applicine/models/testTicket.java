package be.helha.applicine.models;

import be.helha.applicine.common.models.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class testTicket {
    @Test
    public void testTicketCreation() {
        Client client = new Client("name", "email@example.com", "username", "password");
        Room room = new Room(1, 220);
        Viewable viewableMovie = new Movie(1, "title", "genre", "director", 120, "synopsis", new byte[10], "imagePath");
        MovieSession movieSession = new MovieSession(1, viewableMovie, "125", room, "3D");
        Ticket ticket = new Ticket("normal", movieSession, client);

        assertEquals("normal", ticket.getType());
        assertEquals(8.5, ticket.getPrice(), 0.01);
        assertEquals("A1", ticket.getSeat());
        assertEquals(client, ticket.getClientLinked());
        assertEquals(movieSession, ticket.getMovieSessionLinked());
        assertNotNull(ticket.getTicketVerificationCode());
    }

    @Test
    public void testTicketCreationWithId() {
        Client client = new Client("name", "email@example.com", "username", "password");
        Room room = new Room(1, 220);
        Viewable viewableMovie = new Movie(1, "title", "genre", "director", 120, "synopsis", new byte[10], "imagePath");
        MovieSession movieSession = new MovieSession(1, viewableMovie, "125", room, "3D");
        Ticket ticket = new Ticket(1, client, movieSession, "normal", "A1", 8.5, "123456789012345");

        assertEquals(1, ticket.getId());
        assertEquals("normal", ticket.getType());
        assertEquals(8.5, ticket.getPrice());
        assertEquals("A1", ticket.getSeat());
        assertEquals(client, ticket.getClientLinked());
        assertEquals(movieSession, ticket.getMovieSessionLinked());
    }
}
