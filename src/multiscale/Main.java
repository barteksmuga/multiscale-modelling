package multiscale;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static multiscale.helpers.constants.SizeConstants.MAIN_SCENE_HEIGHT;
import static multiscale.helpers.constants.SizeConstants.MAIN_SCENE_WIDTH;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("views/main.fxml"));
        primaryStage.setTitle("Multi-scale modeling");
        Scene scene = new Scene(root, MAIN_SCENE_WIDTH, MAIN_SCENE_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
