package be.helha.applicine.controllers.managercontrollers;

import org.junit.jupiter.api.Test;
import be.helha.applicine.models.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManagerControllerTest {

    @Test
    void getMovieFrom() {
        int id = 0;
        ManagerController managerController = new ManagerController();
        managerController.getMovieFrom(id);
        try {
            assertNotEquals(0, managerController.getMovieFrom(id).getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    void fullFieldMovieListFromDB() {
        ManagerController managerController = new ManagerController();
        try {
            List<Visionable> list = managerController.fullFieldMovieListFromDB();
            System.out.println(list.size());
            assertNotEquals(0, list.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}