package multiscale.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController {
    private final Logger logger = Logger.getLogger(getClass().getName());

    @FXML Button elementaryMachineButton;
    @FXML Button gameOfLifeButton;

    private final String elementaryMachineView = "../view/elementaryMachines.fxml";
    private final String gameOfLifeView = "../view/gameOfLife.fxml";

    public void openElementaryMachineWindow(ActionEvent actionEvent) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource(elementaryMachineView));
            Stage stage = new Stage();
            stage.setTitle("Elementary machines");
            Scene scene = new Scene(root, 1200, 800);
            scene.getStylesheets().add(getClass().getResource("../css/main.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Creating new window failed");
        }
    }

    public void openGameOfLifeWindow(ActionEvent actionEvent) {

    }
}
