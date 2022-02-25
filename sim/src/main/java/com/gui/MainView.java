package com.gui;

import com.model.Simulation;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class MainView {
    private int CANVAS_WIDTH = 1358;
    private int CANVAS_HEIGHT = 746;

    private String css;
    private Scene scene;
    private BorderPane root;
    private Label topIcon;
    private Label title;
    private Button exitButton;
    private Button miniButton;
    private HBox topBar;
    private VBox simView;
    private Canvas world;
    private static Label stepLabel;
    private static Label dayLabel;
    private static Label timeLabel;
    private HBox statusPane;
    private GraphicsContext gc;
    private Button resetButton;
    private Button startButton;
    private Button stopButton;
    private Button stepButton;
    private HBox buttonHolder;
    private Label sliderLabel;
    private Slider speedSlider;
    private VBox sliderHolder;
    private VBox vPanel;
    private Movement clk;
    private Simulation sim;
    
    public MainView(Image icon, EventHandler<ActionEvent> minimise, EventHandler<ActionEvent> exit) {
        clk = new Movement();
        
        stepLabel = new Label("Steps: 0");
        dayLabel = new Label("Days: 0");
        timeLabel = new Label("Time: 00:00");
        stepLabel.getStyleClass().add("status-label");
        dayLabel.getStyleClass().add("status-label");
        timeLabel.getStyleClass().add("status-label");

        statusPane = new HBox(stepLabel, timeLabel, dayLabel);
        statusPane.setPrefHeight(45);
        statusPane.setId("status-pane");
        world = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        gc = world.getGraphicsContext2D();
        gc.setFill(Color.CORNSILK);
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        simView = new VBox(statusPane, world);
       
        topIcon = new Label("", new ImageView(icon));
        topIcon.setPadding(new Insets(5, 5, 0, 5));
        title = new Label("Apocalypse Simulation");
        title.setPadding(new Insets(5, 1295, 0, 3));
        title.setId("title");
        
        FontAwesomeIconView mIcon = new FontAwesomeIconView(FontAwesomeIcon.MINUS, "1.5em");
        FontAwesomeIconView cIcon = new FontAwesomeIconView(FontAwesomeIcon.TIMES, "1.5em");
        mIcon.setFill(Color.WHITE);
        cIcon.setFill(Color.WHITE);
        miniButton = new Button("", mIcon);
        exitButton = new Button("", cIcon);
        miniButton.getStyleClass().add("topBar-button");
        exitButton.getStyleClass().add("topBar-button");

        topBar = new HBox(topIcon, title, miniButton, exitButton);
        topBar.setPrefWidth(1536);
        topBar.setPrefHeight(32);
        topBar.setId("topBar");

        FontAwesomeIconView resetIcon = new FontAwesomeIconView(FontAwesomeIcon.REPEAT, "1.6em");
        FontAwesomeIconView startIcon = new FontAwesomeIconView(FontAwesomeIcon.POWER_OFF, "1.6em");
        FontAwesomeIconView stopIcon = new FontAwesomeIconView(FontAwesomeIcon.PAUSE, "1.6em");
        FontAwesomeIconView stepIcon = new FontAwesomeIconView(FontAwesomeIcon.STEP_FORWARD, "1.6em");
        resetIcon.setFill(Color.WHITE);
        startIcon.setFill(Color.WHITE);
        stopIcon.setFill(Color.WHITE);
        stepIcon.setFill(Color.WHITE);

        resetButton = new Button("", resetIcon);
        startButton = new Button("", startIcon);
        stopButton = new Button("", stopIcon);
        stepButton = new Button("", stepIcon);

        resetButton.getStyleClass().add("command-button");
        startButton.getStyleClass().add("command-button");
        stopButton.getStyleClass().add("command-button");
        stepButton.getStyleClass().add("command-button");

        buttonHolder = new HBox(resetButton, startButton, stopButton, stepButton);
        buttonHolder.setPrefWidth(174);
        buttonHolder.setPrefHeight(45);
        buttonHolder.setAlignment(Pos.CENTER);
        buttonHolder.setSpacing(8);
        buttonHolder.getStyleClass().add("border-bottom");

        sliderLabel = new Label("Simulation Speed (x.)");
        sliderLabel.setId("slider-label");

        speedSlider = new Slider(1, 4, 2);
        speedSlider.setMajorTickUnit(1);
        speedSlider.setMinorTickCount(0);
        speedSlider.setShowTickLabels(true);
        speedSlider.setSnapToTicks(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setId("slider");

        sliderHolder = new VBox(sliderLabel, speedSlider);
        sliderHolder.setSpacing(4);
        sliderHolder.setAlignment(Pos.CENTER);
        sliderHolder.getStyleClass().add("border-bottom");

        vPanel = new VBox(buttonHolder, sliderHolder);
        vPanel.setSpacing(5);
        vPanel.setId("vpanel");
        
        root = new BorderPane();
        root.setTop(topBar);
        root.setLeft(vPanel);
        root.setCenter(simView);

        scene = new Scene(root);
        css = new String(MainView.class.getResource("View.css").toExternalForm());
        scene.getStylesheets().add(css);

        EventHandler<ActionEvent> resetEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {   
                clk.stop();
                gc.setFill(Color.CORNSILK);
                gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
                sim = new Simulation(world, 100);
            }
        };

        EventHandler<ActionEvent> startEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                clk.start();
            }
        };

        EventHandler<ActionEvent> stopEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                clk.stop();
            }
        };

        EventHandler<ActionEvent> stepEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                gc.setFill(Color.CORNSILK);
                gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
                sim.action();
                sim.draw();
            }
        };

        speedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                    clk.setFRAMES_PER_SEC(12L * new_val.intValue()/2);
            }
        });

        miniButton.setOnAction(minimise);
        exitButton.setOnAction(exit);
        resetButton.setOnAction(resetEvent);
        startButton.setOnAction(startEvent);
        stopButton.setOnAction(stopEvent);
        stepButton.setOnAction(stepEvent);
    }

    private class Movement extends AnimationTimer {
        private long FRAMES_PER_SEC = 12L;
        private long INTERVAL = 1000000000L / FRAMES_PER_SEC;
        private long last = 0;
    
        @Override
        public void handle(long now) {
            if (now - last > INTERVAL) {
                try {
                    gc.setFill(Color.CORNSILK);
                    gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
                    sim.action();
                    sim.draw();
                    last = now; 
                } catch (Exception e) {
                    System.out.println(e);
                }
                
            }
        }

        public void setFRAMES_PER_SEC(long fRAMES_PER_SEC) {
            FRAMES_PER_SEC = fRAMES_PER_SEC;
        }
    }

    public static void setTime(String steps, String time, String days) {
        stepLabel.setText(steps);
        timeLabel.setText(time);
        dayLabel.setText(days);
    }

    public Scene getScene() {
        return scene;
    }
}
