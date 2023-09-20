package aaa;
import java.net.URISyntaxException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.scene.chart.LineChart;

public class AudioSpectrumAnalyzer extends Application {

    private MediaPlayer mediaPlayer;

    @Override
    public void start(@SuppressWarnings("exports") Stage primaryStage) {
        primaryStage.setTitle("Audio Spectrum Analyzer");

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Frequency (Hz)");
        yAxis.setLabel("Magnitude");
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Audio Spectrum");
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        lineChart.getData().add(series);

        primaryStage.setScene(new Scene(lineChart, 800, 400));
        primaryStage.show();

        String audioFilePath = "your_audio_file.mp3"; // Replace with your audio file path
        try {
        	audioFilePath = this.getClass().getResource("/01 Lay-la.m4a").toURI().toString();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
        
        Media media = new Media(audioFilePath);
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setOnReady(() -> {
            mediaPlayer.setAudioSpectrumListener(new AudioSpectrumListener() {
                @Override
                public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
                    series.getData().clear();
                    for (int i = 0; i < magnitudes.length; i++) {
                        double frequency = i * mediaPlayer.getAudioSpectrumInterval(); // Calculate frequency
                        double magnitude = magnitudes[i];
                        series.getData().add(new XYChart.Data<>(frequency, magnitude));
                    }
                }
            });

            mediaPlayer.play();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}

