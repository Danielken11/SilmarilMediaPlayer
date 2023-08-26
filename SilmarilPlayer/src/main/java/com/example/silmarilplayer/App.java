package com.example.silmarilplayer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.util.Objects;

public class App extends Application {

private double xOffset;
private double yOffset;

private boolean isMaximized = false;

@Override
public void start(Stage primaryStage) throws IOException {
    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-view.fxml")));
    Scene scene = new Scene(root, 1000, 600);

    HBox topBar = (HBox) root.lookup("#topBar");

    topBar.setOnMousePressed((MouseEvent event) -> {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    });

    topBar.setOnMouseDragged((MouseEvent event) -> {
        primaryStage.setX(event.getScreenX() - xOffset);
        primaryStage.setY(event.getScreenY() - yOffset);
    });

    Button minimizeButton = (Button) root.lookup("#minimizeButton");
    Button closeButton = (Button) root.lookup("#closeButton");
    Button maximizeButton = (Button) root.lookup("#maximizeButton");

    primaryStage.setFullScreenExitHint("");

    minimizeButton.setOnAction(event -> {
        primaryStage.setIconified(true);
    });

    maximizeButton.setOnAction(event -> {
        if (isMaximized) {
            primaryStage.setX(xOffset);
            primaryStage.setY(yOffset);
            primaryStage.setWidth(1000);
            primaryStage.setHeight(600);
        } else {
            Screen screen = Screen.getPrimary();
            primaryStage.setX(screen.getVisualBounds().getMinX());
            primaryStage.setY(screen.getVisualBounds().getMinY());
            primaryStage.setWidth(screen.getVisualBounds().getWidth());
            primaryStage.setHeight(screen.getVisualBounds().getHeight());
            xOffset = primaryStage.getX();
            yOffset = primaryStage.getY();
        }
            isMaximized = !isMaximized;
    });

    closeButton.setOnAction(event -> {
            primaryStage.close();
    });

    Image icon = new Image(getClass().getResource("/com/example/silmarilplayer/Images/silmarilPlayerIco.png").toExternalForm());

    primaryStage.getIcons().add(icon);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Silmaril Player");
    primaryStage.initStyle(StageStyle.UNDECORATED);
    primaryStage.show();

}
    public static void main(String[] args) {
        launch();
    }
}