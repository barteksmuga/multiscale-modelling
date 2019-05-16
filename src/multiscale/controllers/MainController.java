package multiscale.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static multiscale.constants.WindowConstants.CHILD_HEIGHT;
import static multiscale.constants.WindowConstants.CHILD_WIDTH;
import static multiscale.constants.WindowConstants.ELEMENTARY_MACHINE_WINDOW_PATH;
import static multiscale.constants.WindowConstants.ELEMENTARY_MACHINE_WINDOW_TITLE;

public class MainController {
    private final Logger logger = Logger.getLogger(getClass().getName());

    @FXML Button elementaryMachineButton;
    @FXML Button gameOfLifeButton;
    @FXML Button grainGrowthButton;


    public void openElementaryMachineWindow() {
        createStage(ELEMENTARY_MACHINE_WINDOW_TITLE, ELEMENTARY_MACHINE_WINDOW_PATH).show();
    }

    public void openGameOfLifeWindow() {

    }

    public void openGrainGrowthWindow() {

    }

    private Stage createStage(String title, String pathToView) {
        Stage stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource(pathToView));
            stage.setTitle(title);
            Scene scene = new Scene(root, CHILD_WIDTH, CHILD_HEIGHT);
            //todo set css stylesheet
            stage.setScene(scene);
        } catch (IOException e) {
            logger.log(Level.WARNING, String.format("Error with creating window: %s", title));
        }
        return stage;
    }

}
