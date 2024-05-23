package be.helha.applicine.common.models.responses;

import be.helha.applicine.common.models.Viewable;

import java.util.ArrayList;

public class FillListViewableResponse {

    ArrayList <Viewable> viewables;

    public FillListViewableResponse( ArrayList <Viewable> viewables ){
        this.viewables = viewables;
    }

    public  ArrayList<Viewable> getViewables(){
        return this.viewables;
    }

}
