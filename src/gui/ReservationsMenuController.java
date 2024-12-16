package gui;

import domain.Car;
import domain.Reservation;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import service.Service;
import utils.exceptions.IDNotValidException;
import utils.exceptions.ReservationNotValidException;

import java.util.ArrayList;
import java.util.List;

public class ReservationsMenuController {

    private ObservableList<Reservation> reservations;
    private Service service;

    public ReservationsMenuController(Service service) {
        this.service = service;
    }

    public void initialize() {
        List<Reservation> reservationsList = new ArrayList<>();
        for (Reservation reservation : service.getAllReservations()) {
            reservationsList.add(reservation);
        }
        this.reservations = FXCollections.observableList(reservationsList);

        reservationsTableView.setItems(reservations);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        carIdColumn.setCellValueFactory(cellData -> {
            Car car = cellData.getValue().getRentedCar();
            return new SimpleObjectProperty<>(car != null ? car.getId() : null);
        });

        filterByChoiceBox.getItems().addAll("Name", "Minimum Price", "Maximum Price");
    }

    @FXML
    private TableView<Reservation> reservationsTableView;
    @FXML
    private TableColumn<Reservation, Integer> idColumn;
    @FXML
    private TableColumn<Reservation, String> nameColumn;
    @FXML
    private TableColumn<Reservation, Float> priceColumn;
    @FXML
    private TableColumn<Reservation, Integer> carIdColumn;

    @FXML
    private TextField idTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private TextField carIdTextField;

    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button modifyButton;
    @FXML
    private Button clearButton;

    @FXML
    private ChoiceBox<String> filterByChoiceBox;
    @FXML
    private Button filterButton;
    @FXML
    private Button restoreButton;

    @FXML
    void reservationsTableViewClicked(MouseEvent event) {
        Reservation selectedReservation = reservationsTableView.getSelectionModel().getSelectedItem();
        if (selectedReservation != null) {
            idTextField.setText(String.valueOf(selectedReservation.getId()));
            nameTextField.setText(selectedReservation.getName());
            priceTextField.setText(String.valueOf(selectedReservation.getPrice()));
            carIdTextField.setText(String.valueOf(selectedReservation.getRentedCar().getId()));
        }
    }

    private void displayError(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid input");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    private void displayWarning(String warning) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Warning");
        alert.setContentText(warning);
    }

    @FXML
    void addReservation(ActionEvent event) {
        try {
            int id = Integer.parseInt(idTextField.getText());
            String name = nameTextField.getText();
            int carId = Integer.parseInt(carIdTextField.getText());
            Reservation reservation = service.constructReservation(id, name, carId);
            service.addReservation(reservation);
            reservations.add(reservation);
        }
        catch (IllegalArgumentException | ReservationNotValidException | IDNotValidException e) {
            displayError(e);
        }
    }

    @FXML
    void deleteReservation(ActionEvent event) {
        try {
            int id = Integer.parseInt(idTextField.getText());
            service.removeReservation(id);
            Reservation reservationToRemove = null;
            for (Reservation reservation : reservations) {
                if (reservation.getId() == id) {
                    reservationToRemove = reservation;
                    break;
                }
            }
            if (reservationToRemove != null) {
                reservations.remove(reservationToRemove);
            }
        }
        catch (IllegalArgumentException | IDNotValidException e) {
            displayError(e);
        }
    }

    @FXML
    void modifyReservation(ActionEvent event) {
        try {
            int id = Integer.parseInt(idTextField.getText());
            String name = nameTextField.getText();
            int carId = Integer.parseInt(carIdTextField.getText());
            Reservation modifiedReservation = service.constructReservation(id, name, carId);
            service.updateReservation(id, modifiedReservation);
            for (int i = 0; i < reservations.size(); i++) {
                Reservation reservation = reservations.get(i);
                if (reservation.getId() == id) {
                    reservations.set(i, modifiedReservation);
                    break;
                }
            }
        }
        catch (IllegalArgumentException | ReservationNotValidException | IDNotValidException e) {
            displayError(e);
        }
    }

    @FXML
    void clearFields(ActionEvent event) {
        idTextField.clear();
        nameTextField.clear();
        priceTextField.clear();
        carIdTextField.clear();
    }

    @FXML
    void filterByReservations(ActionEvent event) {
        try {
            String selection = filterByChoiceBox.getSelectionModel().getSelectedItem();
            switch (selection) {
                case "Name":
                    try {
                        String name = nameTextField.getText();
                        List<Reservation> filteredReservations = service.getReservationsByName(name);
                        reservations.setAll(filteredReservations);
                    }
                    catch (IllegalArgumentException e) {
                        displayError(e);
                    }
                    break;
                case "Minimum Price":
                    try {
                        float price = Float.parseFloat(priceTextField.getText());
                        List<Reservation> filteredReservations = service.getReservationsByMinPrice(price);
                        reservations.setAll(filteredReservations);
                    }
                    catch (IllegalArgumentException e) {
                        displayError(e);
                    }
                    break;
                case "Maximum Price":
                    try {
                        float price = Float.parseFloat(priceTextField.getText());
                        List<Reservation> filteredReservations = service.getReservationsByMaxPrice(price);
                        reservations.setAll(filteredReservations);
                    }
                    catch (IllegalArgumentException e) {
                        displayError(e);
                    }
                    break;
                default:
                    displayWarning("No filter selected");
                    break;
            }
        }
        catch (Exception e) {
            displayWarning("No filter selected");
        }
    }

    @FXML
    void restoreReservations(ActionEvent event) {
        List<Reservation> reservationsList = new ArrayList<>();
        for (Reservation reservation : service.getAllReservations()) {
            reservationsList.add(reservation);
        }
        reservations.setAll(reservationsList);
    }
}

