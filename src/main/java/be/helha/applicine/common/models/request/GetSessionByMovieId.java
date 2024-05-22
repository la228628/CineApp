package be.helha.applicine.common.models.request;

public class GetSessionByMovieId extends ClientEvent{
    private int movieID;
    public GetSessionByMovieId(int movieID) {
        this.movieID = movieID;
    }
    @Override
    public void dispatchOn(RequestVisitor requestVisitor){
        requestVisitor.visit(this);
    }

    public int getMovieId() {
        return movieID;
    }
}
