package gui;

import domain.Car;
import domain.Reservation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.databaseRepository.CarDatabaseRepository;
import repository.databaseRepository.ReservationDatabaseRepository;
import repository.memoryRepository.CarRepository;
import repository.memoryRepository.ReservationRepository;
import service.Service;
import utils.exceptions.IDNotValidException;

public class GUI extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // initialise database repositories
        CarDatabaseRepository carsDatabaseRepo = new CarDatabaseRepository("data/carrentals.db");
        ReservationDatabaseRepository reservationsDatabaseRepo = new ReservationDatabaseRepository("data/carrentals.db");

        // initialize memory repositories
        CarRepository carsRepo = new CarRepository();
        ReservationRepository reservationsRepo = new ReservationRepository();

        // extract data from database repositories into memory repositories
        try {
            for (Car car : carsDatabaseRepo.getAll()) {
                carsRepo.add(car.getId(), car);
            }
            for (Reservation reservation : reservationsDatabaseRepo.getAll()) {
                reservationsRepo.add(reservation.getId(), reservation);
            }
        }
        catch (IDNotValidException e) {
            throw new RuntimeException(e);
        }

        Service service = new Service(carsRepo, reservationsRepo);

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
