package multiscale;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("./view/main.fxml"));
        primaryStage.setTitle("Multi-scale modeling");
        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("./css/main.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
//        setResizeResizable(scene, root);
    }

    public static void main(String[] args) {
        launch(args);
    }
//
//    private void setResizeResizable(Scene scene, Parent root) {
//        Pane tmpPane = null;
//        for (Object child: root.getChildrenUnmodifiable()) {
//            if (child instanceof Pane) {
//                tmpPane = (Pane) child;
//            }
//        }
//        final Pane pane = tmpPane;
//        scene.widthProperty().addListener(((observable, oldValue, newValue) -> {
//            if (pane != null) {
//                pane.setPrefWidth(newValue.doubleValue());
//            }
//        }));
//        scene.heightProperty().addListener(((observable, oldValue, newValue) -> {
//            if (pane != null) {
//                pane.setPrefHeight(newValue.doubleValue());
//            }
//        }));
//    }
}
