module com.example.silmarilplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.silmarilplayer to javafx.fxml;
    exports com.example.silmarilplayer;
}