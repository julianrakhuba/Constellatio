package aaa;

import java.net.URISyntaxException;

/**
 * Copyright (c) 2008, 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 */
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/** 
 * An advanced area chart with sound and animation. The sound is in AAC audio format.
 *
 * @see javafx.scene.chart.AreaChart
 * @see javafx.scene.chart.Chart
 * @see javafx.scene.chart.NumberAxis
 * @see javafx.scene.chart.XYChart
 * @see javafx.scene.media.AudioSpectrumListener
 * @see javafx.scene.media.Media
 * @see javafx.scene.media.MediaPlayer
 */
public class AudioTest extends Application {
    private XYChart.Data<Number,Number>[] series1Data;
    private AudioSpectrumListener audioSpectrumListener;

    private  String AUDIO_URI; 
    private static MediaPlayer audioMediaPlayer;
    private static final boolean PLAY_AUDIO = true ; //Boolean.parseBoolean(System.getProperty("demo.play.audio","true"));
        
    private void init(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setScene(new Scene(root));
        root.getChildren().add(createChart());
        audioSpectrumListener = new AudioSpectrumListener() {
            @Override public void spectrumDataUpdate(double timestamp, double duration,
                    float[] magnitudes, float[] phases) {
                for (int i = 0; i < series1Data.length; i++) {
                    series1Data[i].setYValue(magnitudes[i] + 60);
                }
            }
        };
        
        try {
			AUDIO_URI = this.getClass().getResource("/lifelike.mp3").toURI().toString();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
    }

    public void play() {
        this.startAudio();
    }

   public void stop() {
        this.stopAudio();
    }

    @SuppressWarnings("unchecked")
	protected AreaChart<Number,Number> createChart() {
        final NumberAxis xAxis = new NumberAxis(0,128,8);
        final NumberAxis yAxis = new NumberAxis(0,50,10);
        final AreaChart<Number,Number> ac = new AreaChart<Number,Number>(xAxis,yAxis);
        // setup chart
        ac.setId("audioAreaDemo");
        ac.setLegendVisible(false);
        ac.setTitle("Live Audio Spectrum Data");
        ac.setAnimated(false);
        xAxis.setLabel("Frequency Bands");
        yAxis.setLabel("Magnitudes");
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis,null,"dB"));
        // add starting data
        XYChart.Series<Number,Number> series = new XYChart.Series<Number,Number>();
        series.setName("Audio Spectrum");
        //noinspection unchecked
        series1Data = new XYChart.Data[(int)xAxis.getUpperBound()];
        for (int i=0; i<series1Data.length; i++) {
            series1Data[i] = new XYChart.Data<Number,Number>(i,50);
            series.getData().add(series1Data[i]);
        }
        ac.getData().add(series);
        return ac;
    }

    private void startAudio() {
        if (PLAY_AUDIO) {
            getAudioMediaPlayer().setAudioSpectrumListener(audioSpectrumListener);
            getAudioMediaPlayer().play();
            
        }
    }

    private void stopAudio() {
    	if(audioMediaPlayer != null && audioMediaPlayer.getAudioSpectrumListener() == audioSpectrumListener){
    		audioMediaPlayer.pause();
        }
    }

   private  MediaPlayer getAudioMediaPlayer() {
        if (audioMediaPlayer == null) audioMediaPlayer = new MediaPlayer(new Media(AUDIO_URI));
       
        audioMediaPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
            	audioMediaPlayer.seek(Duration.ZERO);
            	audioMediaPlayer.play();
            }
        }); 
        
        return audioMediaPlayer;
    }

    @SuppressWarnings("exports")
	@Override public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();
        play();
    }
    public static void main(String[] args) { launch(args); }
}
