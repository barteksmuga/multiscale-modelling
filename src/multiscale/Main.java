package multiscale;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static multiscale.constants.WindowConstants.CHILD_HEIGHT;
import static multiscale.constants.WindowConstants.CHILD_WIDTH;
import static multiscale.constants.WindowConstants.MAIN_HEIGHT;
import static multiscale.constants.WindowConstants.MAIN_WIDTH;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("views/main.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, MAIN_WIDTH, MAIN_HEIGHT));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
