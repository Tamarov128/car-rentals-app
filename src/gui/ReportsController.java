package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import service.Service;

import java.util.InputMismatchException;

public class ReportsController {

    private Service service;

    public ReportsController(Service service) {
        this.service = service;
    }

    public void initialize() {
        reportTextArea.setEditable(false);
    }

    @FXML
    private Button carsYearReportButton;
    @FXML
    private TextField yearTextField;

    @FXML
    private Button carsAveragePriceReportButton;

    @FXML
    private Button carsPriceSortReportButton;

    @FXML
    private Button reservationsNameReportButton;
    @FXML
    private TextField nameTextField1;

    @FXML
    private Button reservationsPriceNameReportButton;
    @FXML
    private TextField nameTextField2;

    @FXML
    private TextArea reportTextArea;
    @FXML
    private Button clearButton;

    private void displayError(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid input");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    @FXML
    void getCarYearReport(ActionEvent event) {
        try {
            int year = Integer.parseInt(yearTextField.getText());
            reportTextArea.clear();
            String report = service.reportCarsManufacturedAfterYear(year);
            reportTextArea.appendText(report);
        }
        catch (InputMismatchException e) {
            displayError(e);
        }
    }

    @FXML
    void getCarAveragePriceReport(ActionEvent event) {
        reportTextArea.clear();
        String report = service.reportCarsAveragePrice();
        reportTextArea.appendText(report);
    }

    @FXML
    void getCarPriceSortReport(ActionEvent event) {
        reportTextArea.clear();
        String report = service.reportCarsAvailableSortedByPrice();
        reportTextArea.appendText(report);
    }

    @FXML
    void getReservationNameReport(ActionEvent event) {
        String name = nameTextField1.getText();
        reportTextArea.clear();
        String report = service.reportReservationsByName(name);
        reportTextArea.appendText(report);
    }

    @FXML
    void getReservationPriceNameReport(ActionEvent event) {
        String name = nameTextField2.getText();
        reportTextArea.clear();
        String report = service.reportReservationsByName(name);
        reportTextArea.appendText(report);
    }

    @FXML
    void clearTextArea(ActionEvent event) {
        yearTextField.clear();
        nameTextField1.clear();
        nameTextField2.clear();
        reportTextArea.clear();
    }
}

