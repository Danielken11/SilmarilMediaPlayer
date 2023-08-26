package com.example.silmarilplayer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.util.Duration;
import java.io.File;

public class Controller {

//Main layers...
@FXML
    BorderPane mainBorder;

//Top custom bar buttons...
@FXML
    Button minimizeButton;
@FXML
    Button maximizeButton;
@FXML
    Button closeButton;

//Main media ui elements...
@FXML
    Button mediaSelectorButton;
@FXML
    ToggleButton playPauseButton;
@FXML
    Button backButton;
@FXML
    Button forwardButton;
@FXML
    ToggleButton soundButton;
@FXML
    Button speedButton;
@FXML
    ProgressBar mediaSlider;
@FXML
    ImageView playPauseImageView;

//Info of the playing media...
@FXML
    Button fileNameButton;
//@FXML
//    TextField fileNameLabel;
@FXML
    Label timeLabel;
@FXML
    Label currentTimeLabel;
@FXML

private MediaPlayer mediaPlayer;
private Boolean ispressed = false;
private Boolean isFinished = false;

private void cursorInteraction(Button button){
    button.setOnMouseEntered(event -> {
        button.setCursor(Cursor.HAND);
    });

    button.setOnMouseExited(event -> {
        button.setCursor(Cursor.DEFAULT);
    });
}
private void toggleCursorInteraction(ToggleButton button){
    button.setOnMouseEntered(event -> {
        button.setCursor(Cursor.HAND);
    });

    button.setOnMouseExited(event -> {
        button.setCursor(Cursor.DEFAULT);
    });
}
private void scheduleLabelDisappearance(Label label) {
    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
        label.setVisible(false);
        label.setManaged(false);
    }));
    timeline.play();
}
private String getCurrentTimeValue (double currentTime){
    double totalMilliseconds = currentTime;
    String durationString;
        long totalSeconds = (long) (totalMilliseconds / 1000);
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

    if (hours > 0){
        durationString = String.format("%d:%02d:%02d",hours,minutes,seconds);
        return durationString;
    }
    else{
        durationString =  String.format("%02d:%02d",minutes,seconds);
        return durationString;
    }
}

public void initialize(){
    cursorInteraction(minimizeButton); cursorInteraction(maximizeButton);
    cursorInteraction(closeButton); cursorInteraction(mediaSelectorButton);
    cursorInteraction(backButton); cursorInteraction(forwardButton);
    cursorInteraction(speedButton); cursorInteraction(fileNameButton);

    toggleCursorInteraction(soundButton);
    toggleCursorInteraction(playPauseButton);

    mediaSlider.setVisible(false);
    playPauseButton.setDisable(true);
    backButton.setDisable(true);
    forwardButton.setDisable(true);
    speedButton.setVisible(false);
    soundButton.setVisible(false);
    fileNameButton.setVisible(false);

}

public void selectFile(){
    Stage selectStage = new Stage();
    FileChooser chooser = new FileChooser();

    chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Media","*mp4","*mp3"));

    File file = chooser.showOpenDialog(selectStage);

    if(file != null){
        playPauseButton.setDisable(false);
        backButton.setDisable(false);
        forwardButton.setDisable(false);
        speedButton.setVisible(true);
        soundButton.setVisible(true);
        fileNameButton.setVisible(true);

        String fileName = file.getName();
        String mediaNameLabelText = fileName.substring(fileName.lastIndexOf("\\") + 1,file.getName().length());
        fileNameButton.setText(mediaNameLabelText);
       // fileNameLabel.setText(mediaNameLabelText);

        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, file.getName().length());

        if(fileExtension.matches("mp4")){
            mediaSlider.setCursor(Cursor.HAND);
            mediaSlider.setVisible(true);
            Media mediaVideo = new Media(new File(String.valueOf(file)).toURI().toString());
            mediaPlayer = new MediaPlayer(mediaVideo);

            mediaPlayer.setOnEndOfMedia(()->{
                mediaPlayer.stop();
                isFinished = true;

                playPauseButton.setSelected(false);
                Image playImage = new Image(getClass().getResource("/com/example/silmarilplayer/Images/playImage.png").toExternalForm());
                playPauseImageView.setImage(playImage);
            });
//Main functions for media manipulation...
            mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                double currentTime = newValue.toMillis();
                double totalDuration = mediaPlayer.getTotalDuration().toMillis();
                double progress = currentTime / totalDuration;
                mediaSlider.setProgress(progress);
                currentTimeLabel.setText(getCurrentTimeValue(currentTime));
                timeLabel.setText(getCurrentTimeValue(totalDuration));
            });
            mediaSlider.setOnMouseClicked(event -> {
                double totalDuration = mediaPlayer.getTotalDuration().toMillis();
                double newPosition = event.getX() / mediaSlider.getWidth();
                mediaPlayer.seek(new Duration(totalDuration * newPosition));
            });
            mediaSlider.setOnMouseDragged(event -> {
                double totalDuration = mediaPlayer.getTotalDuration().toMillis();
                double newPosition = event.getX() / mediaSlider.getWidth();
                mediaPlayer.seek(new Duration(totalDuration * newPosition));
            });

            MediaView mediaView = new MediaView();
            mediaView.setFitWidth(1000);
            mediaView.setFitHeight(492);
            mainBorder.setCenter(mediaView);
            mediaView.setMediaPlayer(mediaPlayer);

        }else if (fileExtension.matches("mp3")){

            mediaSlider.setCursor(Cursor.HAND);
            mediaSlider.setVisible(true);
            playPauseButton.setDisable(false);
            backButton.setDisable(false);
            forwardButton.setDisable(false);
            speedButton.setVisible(true);
            soundButton.setVisible(true);

            Media mediaSound = new Media(new File(String.valueOf(file)).toURI().toString());
            mediaPlayer = new MediaPlayer(mediaSound);
            mediaPlayer.setOnEndOfMedia(()->{
                mediaPlayer.stop();
                isFinished = true;

                playPauseButton.setSelected(false);
                Image playImage = new Image(getClass().getResource("/com/example/silmarilplayer/Images/playImage.png").toExternalForm());
                playPauseImageView.setImage(playImage);
            });

            mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                double currentTime = newValue.toMillis();
                currentTimeLabel.setText(String.valueOf(currentTime));
                double totalDuration = mediaPlayer.getTotalDuration().toMillis();
                double progress = currentTime / totalDuration;
                mediaSlider.setProgress(progress);

                currentTimeLabel.setText(getCurrentTimeValue(currentTime));
                timeLabel.setText(getCurrentTimeValue(totalDuration));
            });

            mediaSlider.setOnMouseClicked(event -> {
                double totalDuration = mediaPlayer.getTotalDuration().toMillis();
                double newPosition = event.getX() / mediaSlider.getWidth();
                mediaPlayer.seek(new Duration(totalDuration * newPosition));
            });

            mediaSlider.setOnMouseDragged(event -> {
                double totalDuration = mediaPlayer.getTotalDuration().toMillis();
                double newPosition = event.getX() / mediaSlider.getWidth();
                mediaPlayer.seek(new Duration(totalDuration * newPosition));
            });

            Image soundWaveGif = new Image(getClass().getResource("/com/example/silmarilplayer/Images/soundWave.gif").toExternalForm());
            ImageView sounWaveView = new ImageView();
            sounWaveView.setImage(soundWaveGif);
            mainBorder.setCenter(sounWaveView);
        }else{
            System.out.println("Error of the file extension...");
        }
    }else{
       System.out.println("No file...");
    }

    playPauseButton.setOnAction(event -> {
        if(playPauseButton.isSelected()){
            Image pauseImage = new Image(getClass().getResource("/com/example/silmarilplayer/Images/pauseImage.png").toExternalForm());
            playPauseImageView.setImage(pauseImage);
            mediaPlayer.play();
        }else{
            Image playImage = new Image(getClass().getResource("/com/example/silmarilplayer/Images/playImage.png").toExternalForm());
            playPauseImageView.setImage(playImage);
            mediaPlayer.pause();
        }
    });
}
//The skip control of the media will be set ti 10 seconds for skipBack and moveForward buttons...
public void skipBack(){
    Duration currentPosition = mediaPlayer.getCurrentTime();
    Duration newPosition = currentPosition.subtract(Duration.seconds(10));
    mediaPlayer.seek(newPosition);
}

public void moveForward(){
    Duration currentPosition = mediaPlayer.getCurrentTime();
    Duration newPosition = currentPosition.add(Duration.seconds(10));
    mediaPlayer.seek(newPosition);
}

public void soundControl(){

}

public void speedControl(){

    }
}