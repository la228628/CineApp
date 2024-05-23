package be.helha.applicine.models.exceptions;

import be.helha.applicine.common.models.request.*;
import be.helha.applicine.common.models.Client;
import be.helha.applicine.common.models.Movie;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;


//Mockito sert a créer un mock de RequestVisitor. Ensuite, ils appellent dispatchOn sur l'objet de requête et vérifient que la bonne méthode visit a été appelée sur le RequestVisitor.
public class RequestTest {

    @Test
    public void testCheckLoginRequestDispatchOn() {
        String username = "testUser";
        String password = "testPassword";
        CheckLoginRequest request = new CheckLoginRequest(username, password);
        RequestVisitor visitor = mock(RequestVisitor.class);
        request.dispatchOn(visitor);
        verify(visitor).visit(request);
    }

    @Test
    public void testClientRegistrationRequestDispatchOn() {
        Client client = new Client("Michel", "testUser", "testPassword", "testEmail");
        ClientRegistrationRequest request = new ClientRegistrationRequest(client);
        RequestVisitor visitor = mock(RequestVisitor.class);
        request.dispatchOn(visitor);
        verify(visitor).visit(request);
    }

    @Test
    public void testCreateMovieRequestDispatchOn() {
        Movie movie = new Movie("testTitle", "testGenre", "testDirector", 145, "testDescription", new byte[0], "testTrailer");
        CreateMovieRequest request = new CreateMovieRequest(movie);
        RequestVisitor visitor = mock(RequestVisitor.class);
        request.dispatchOn(visitor);
        verify(visitor).visit(request);
    }


}