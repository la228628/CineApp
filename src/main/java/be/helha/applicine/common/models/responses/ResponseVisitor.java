package be.helha.applicine.common.models.responses;

public interface ResponseVisitor {
    void visit(FillListViewableResponse fillListViewableResponse);
    void visit(ToEventResponse toEventResponse);
}
