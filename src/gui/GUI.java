package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.databaseRepository.CarDatabaseRepository;
import repository.databaseRepository.ReservationDatabaseRepository;
import service.Service;

public class GUI extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        CarDatabaseRepository carsRepo = new CarDatabaseRepository("data/carrentals.db");
        ReservationDatabaseRepository reservationRepo = new ReservationDatabaseRepository("data/carrentals.db");
        Service service = new Service(carsRepo, reservationRepo);

        StartController controller = new StartController(service);
        FXMLLoader loader = new FXMLLoader(GUI.class.getResource("start.fxml"));
        loader.setController(controller);

        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("App");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
