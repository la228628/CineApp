package be.helha.applicine.common.models.request;

import java.io.IOException;
import java.sql.SQLException;

public class GetSessionByMovieId extends ClientEvent{
    private int movieID;
    public GetSessionByMovieId(int movieID) {
        this.movieID = movieID;
    }
    @Override
    public void dispatchOn(RequestVisitor requestVisitor) throws IOException, SQLException {
        requestVisitor.visit(this);
    }

    public int getMovieId() {
        return movieID;
    }
}
