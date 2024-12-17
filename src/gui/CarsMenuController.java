package gui;

import domain.Car;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import service.Service;
import utils.exceptions.CarNotValidException;
import utils.exceptions.IDNotValidException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CarsMenuController {

    private ObservableList<Car> cars;
    private Service service;

    public CarsMenuController(Service service) {
        this.service = service;
    }

    public void initialize() {
        List<Car> carsList = new ArrayList<>();
        for (Car car : service.getAllCars()) {
            carsList.add(car);
        }
        this.cars = FXCollections.observableList(carsList);

        carsTableView.setItems(cars);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        makeColumn.setCellValueFactory(new PropertyValueFactory<>("make"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        rentalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("rentalPrice"));
        manufacturingYearColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturingYear"));
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));

        filterByChoiceBox.getItems().addAll("Availability", "Make", "Year");
    }

    @FXML
    private TableView<Car> carsTableView;
    @FXML
    private TableColumn<Car, Integer> idColumn;
    @FXML
    private TableColumn<Car, String> makeColumn;
    @FXML
    private TableColumn<Car, String> modelColumn;
    @FXML
    private TableColumn<Car, Float> rentalPriceColumn;
    @FXML
    private TableColumn<Car, Integer> manufacturingYearColumn;
    @FXML
    private TableColumn<Car, Boolean> availableColumn;

    @FXML
    private TextField idTextField;
    @FXML
    private TextField makeTextField;
    @FXML
    private TextField modelTextField;
    @FXML
    private TextField rentalPriceTextField;
    @FXML
    private TextField manufacturingYearTextField;
    @FXML
    private TextField availableTextField;

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
    private TextField entitiesNumberTextField;

    @FXML
    private Button populateTableButton;

    @FXML
    private Button increasePriceButton;

    @FXML
    void carsTableViewClicked(MouseEvent event) {
        Car selectedCar = carsTableView.getSelectionModel().getSelectedItem();
        if (selectedCar != null) {
            idTextField.setText(String.valueOf(selectedCar.getId()));
            makeTextField.setText(selectedCar.getMake());
            modelTextField.setText(selectedCar.getModel());
            rentalPriceTextField.setText(String.valueOf(selectedCar.getRentalPrice()));
            manufacturingYearTextField.setText(String.valueOf(selectedCar.getManufacturingYear()));
            availableTextField.setText(String.valueOf(selectedCar.isAvailable()));
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
    void addCar(ActionEvent event) {
        try {
            int id = Integer.parseInt(idTextField.getText());
            String make = makeTextField.getText();
            String model = modelTextField.getText();
            float rentalPrice = Float.parseFloat(rentalPriceTextField.getText());
            int manufacturingYear = Integer.parseInt(manufacturingYearTextField.getText());
            boolean available = Boolean.parseBoolean(availableTextField.getText());
            if (!available) {
                throw new IOException("Non-available car can't be added");
            }
            Car car = new Car(id, make, model, rentalPrice, manufacturingYear);
            service.addCar(car);
            cars.add(car);
        }
        catch (IOException | IllegalArgumentException | IDNotValidException | CarNotValidException e) {
            displayError(e);
        }
    }

    @FXML
    void deleteCar(ActionEvent event) {
        try {
            int id = Integer.parseInt(idTextField.getText());
            service.removeCar(id);
            Car carToRemove = null;
            for (Car car : cars) {
                if (car.getId() == id) {
                    carToRemove = car;
                    break;
                }
            }
            if (carToRemove != null) {
                cars.remove(carToRemove);
            }
        }
        catch (IllegalArgumentException | IDNotValidException e) {
            displayError(e);
        }
    }

    @FXML
    void modifyCar(ActionEvent event) {
        try {
            int id = Integer.parseInt(idTextField.getText());
            String make = makeTextField.getText();
            String model = modelTextField.getText();
            float rentalPrice = Float.parseFloat(rentalPriceTextField.getText());
            int manufacturingYear = Integer.parseInt(manufacturingYearTextField.getText());
            boolean available = Boolean.parseBoolean(availableTextField.getText());
            Car modifiedCar = new Car(id, make, model, rentalPrice, manufacturingYear);
            modifiedCar.setAvailable(available);
            service.updateCar(id, modifiedCar);
            for (int i = 0; i < cars.size(); i++) {
                Car car = cars.get(i);
                if (car.getId() == id) {
                    cars.set(i, modifiedCar);
                    break;
                }
            }
        }
        catch (IllegalArgumentException | IDNotValidException | CarNotValidException e) {
            displayError(e);
        }
    }

    @FXML
    void clearFields(ActionEvent event) {
        idTextField.clear();
        makeTextField.clear();
        modelTextField.clear();
        rentalPriceTextField.clear();
        manufacturingYearTextField.clear();
        availableTextField.clear();
    }

    @FXML
    void filterByCars(ActionEvent event) {
        try {
            String selection = filterByChoiceBox.getSelectionModel().getSelectedItem();
            switch (selection) {
                case "Availability":
                    try {
                        boolean available = Boolean.parseBoolean(availableTextField.getText());
                        List<Car> filteredCars = service.getCarsByAvailability(available);
                        cars.setAll(filteredCars);
                    }
                    catch (NumberFormatException e) {
                        displayError(e);
                    }
                    break;
                case "Make":
                    try {
                        String make = makeTextField.getText();
                        List<Car> filteredCars = service.getCarsByMake(make);
                        cars.setAll(filteredCars);
                    }
                    catch (NumberFormatException e) {
                        displayError(e);
                    }
                    break;
                case "Year":
                    try {
                        int year = Integer.parseInt(manufacturingYearTextField.getText());
                        List<Car> filteredCars = service.getCarsByManufacturingYear(year);
                        cars.setAll(filteredCars);
                    }
                    catch (NumberFormatException e) {
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
    void restoreCars(ActionEvent event) {
        List<Car> carsList = new ArrayList<>();
        for (Car car : service.getAllCars()) {
            carsList.add(car);
        }
        cars.setAll(carsList);
    }

    @FXML
    void populateTable(ActionEvent event) {
        int entitiesNumber = Integer.parseInt(entitiesNumberTextField.getText());
        if (entitiesNumber <= 0) {
            displayWarning("No entities selected");
        }
        else {
            try {
                service.populateCars(entitiesNumber);
            }
            catch (CarNotValidException | IDNotValidException e) {
                displayError(e);
            }
        }

    }

    @FXML
    void increasePrice(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(GUI.class.getResource("bulkUpdate.fxml"));
        BulkUpdateController controller = new BulkUpdateController(service);
        loader.setController(controller);
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Bulk Update");
        stage.showAndWait();
        // update cars list
        List<Car> carsList = new ArrayList<>();
        for (Car car : service.getAllCars()) {
            carsList.add(car);
        }
        cars.setAll(carsList);
    }
}
