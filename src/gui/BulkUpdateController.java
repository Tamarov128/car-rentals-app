package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.Service;

public class BulkUpdateController {

    private Service service;

    public BulkUpdateController(Service service) {
        this.service = service;
    }

    public void initialize() {
        threadsChoiceBox.getItems().addAll("Traditional", "Executor service");
    }

    @FXML
    private Button bulkUpdateButton;

    @FXML
    private TextField percentageTextField;

    @FXML
    private TextField yearTextField;

    @FXML
    private TextField threadsNumberTextField;

    @FXML
    private ChoiceBox<String> threadsChoiceBox;

    @FXML
    void bulkUpdate(ActionEvent event) {
        double percentage = Double.parseDouble(percentageTextField.getText());
        int year = Integer.parseInt(yearTextField.getText());
        int threadsNumber = Integer.parseInt(threadsNumberTextField.getText());
        String threadsChoice = threadsChoiceBox.getValue();
        if (threadsChoice.equals("Traditional")) {
            service.increasePriceTraditionalThreads(percentage, year, threadsNumber);
        }
        if (threadsChoice.equals("Executor service")) {
            service.increasePriceExecutorService(percentage, year, threadsNumber);
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Updated prices of cars produced after " + year + " to " + percentage + "% more");
        alert.setContentText("Operation finished successfully");
        alert.showAndWait();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
