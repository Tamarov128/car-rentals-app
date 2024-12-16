package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import service.Service;

import java.io.IOException;

public class StartController {

    Service service;

    public StartController(Service service) {
        this.service = service;
    }

    @FXML
    private Button CarsMenuButton;

    @FXML
    private Button ReservationsMenuButton;

    @FXML
    private Button ReportsButton;

    @FXML
    void newCarsMenu(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(GUI.class.getResource("carsMenu.fxml"));
        CarsMenuController carsMenuController = new CarsMenuController(service);
        loader.setController(carsMenuController);
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Cars menu");
        stage.show();
    }

    @FXML
    void newReservationsMenu(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(GUI.class.getResource("reservationsMenu.fxml"));
        ReservationsMenuController reservationsMenuController = new ReservationsMenuController(service);
        loader.setController(reservationsMenuController);
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Reservations menu");
        stage.show();
    }

    @FXML
    void newReports(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(GUI.class.getResource("reports.fxml"));
        ReportsController reportsController = new ReportsController(service);
        loader.setController(reportsController);
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Reports");
        stage.show();
    }
}
