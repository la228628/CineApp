package be.helha.applicine.controllers.managercontrollers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagerControllerTest {

    @Test
    void getMovieFrom() {
        int id = 0;
        ManagerController managerController = new ManagerController(null);
        managerController.getMovieFrom(id);
        try {
            assertEquals(1, managerController.getMovieFrom(id).getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    void fullFieldMovieListFromDB() {
        ManagerController managerController = new ManagerController(null);
        managerController.fullFieldMovieListFromDB();
        try {
            assertEquals(1, managerController.getMovieFrom(0).getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}