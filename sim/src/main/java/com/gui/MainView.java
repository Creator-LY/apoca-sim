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
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
    private static Button daylightLabel;
    private static Button rainLabel;
    private static Button lightningLabel;
    private HBox timeHolder;
    private static Label resourceCountLabel;
    private static Label humanCountLabel;
    private static Label zombieCountLabel;
    private static Label subhumanCountLabel;
    private static Label mobCountLabel;
    private static Label plantCountLabel;
    private static Label corpseCountLabel;
    private HBox amountHolder;
    private VBox statusPane;
    private GraphicsContext gc;
    private Button resetButton;
    private Button startButton;
    private Button stopButton;
    private Button stepButton;
    private HBox buttonHolder;
    private Label sliderLabel;
    private Slider speedSlider;
    private VBox sliderHolder;
    private TextField resourceAmount;
    private TextField humanAmount;
    private TextField subhumanAmount;
    private TextField zombieAmount;
    private TextField mobAmount;
    private TextField plantAmount;
    private GridPane numericInputs;
    private Button addResource;
    private Button addHuman;
    private Button addZombie;
    private Button addSubhuman;
    private Button addMob;
    private Button addPlant;
    private Button addCorpse;
    private Button remove;
    private ToolBar addTools1;
    private ToolBar addTools2;
    private ToolBar addTools3;
    private VBox vPanel;
    private Movement clk;
    private Simulation sim;
    private boolean stepComplete;
    private int editMode = -2;
    private boolean wantMutated;
    
    public MainView(Image icon, EventHandler<ActionEvent> minimise, EventHandler<ActionEvent> exit) {
        clk = new Movement();   //Step event handler

        /********************
        Windows bar
        ********************/
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
        
        /********************
        Status Display
        ********************/
        stepLabel = new Label("Steps: 0");
        dayLabel = new Label("Days: 0");
        timeLabel = new Label("Time: 00:00");

        stepLabel.setMinWidth(200);
        dayLabel.setMinWidth(200);
        timeLabel.setMinWidth(200);

        FontAwesomeIconView sunIcon = new FontAwesomeIconView(FontAwesomeIcon.SUN_ALT, "1.5em");
        daylightLabel = new Button("", sunIcon);
        daylightLabel.getStyleClass().add("button-label");

        FontAwesomeIconView rainIcon = new FontAwesomeIconView(FontAwesomeIcon.TINT, "1.5em");
        rainLabel = new Button("", rainIcon);
        rainLabel.getStyleClass().add("button-label");

        FontAwesomeIconView lightningIcon = new FontAwesomeIconView(FontAwesomeIcon.BOLT, "1.5em");
        lightningLabel = new Button("", lightningIcon);
        lightningLabel.getStyleClass().add("button-label");

        setDayWeather(false, false, false);
        daylightLabel.setPadding(new Insets(3, 0, 0, 600));

        timeHolder = new HBox(stepLabel, timeLabel, dayLabel, daylightLabel, rainLabel, lightningLabel);
        timeHolder.getStyleClass().add("status");

        resourceCountLabel = new Label("Resources: 0");
        humanCountLabel = new Label("Humans: 0");
        zombieCountLabel = new Label("Zombies: 0");
        subhumanCountLabel = new Label("Sub-Humans: 0");
        mobCountLabel = new Label("Mobs: 0");
        plantCountLabel = new Label("Plants: 0");
        corpseCountLabel = new Label("Corpses: 0");

        amountHolder = new HBox(resourceCountLabel, humanCountLabel, zombieCountLabel, subhumanCountLabel, mobCountLabel,
                                plantCountLabel, corpseCountLabel);
        amountHolder.getStyleClass().add("status");

        statusPane = new VBox(timeHolder, amountHolder);
        statusPane.setPrefHeight(45);
        statusPane.setId("status-pane");

        /********************
        World Display
        ********************/
        world = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        world.setDisable(true);
        gc = world.getGraphicsContext2D();
        gc.setFill(Color.CORNSILK);
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        simView = new VBox(statusPane, world);
       
        /********************
        Control Panel
        ********************/
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

        numericInputs = new GridPane();
        numericInputs.setId("grid-numeric");
        numericInputs.add(new Label("Resources Amount:"), 0, 0);
        numericInputs.add(new Label("Humans Amount:"), 0, 1);
        numericInputs.add(new Label("Zombies Amount:"), 0, 2);
        numericInputs.add(new Label("Sub-Humans Amount:"), 0, 3);
        numericInputs.add(new Label("Mobs Amount:"), 0, 4);
        numericInputs.add(new Label("Plants Amount:"), 0, 5);

        resourceAmount = new TextField("1000");
        humanAmount = new TextField("100");
        zombieAmount = new TextField("10");
        subhumanAmount = new TextField("0");
        mobAmount = new TextField("0");
        plantAmount = new TextField("0");

        resourceAmount.getStyleClass().add("numeric-input");
        humanAmount.getStyleClass().add("numeric-input");
        zombieAmount.getStyleClass().add("numeric-input");
        subhumanAmount.getStyleClass().add("numeric-input");
        mobAmount.getStyleClass().add("numeric-input");
        plantAmount.getStyleClass().add("numeric-input");

        numericInputs.add(resourceAmount, 1, 0);
        numericInputs.add(humanAmount, 1, 1);
        numericInputs.add(zombieAmount, 1, 2);
        numericInputs.add(subhumanAmount, 1, 3);
        numericInputs.add(mobAmount, 1, 4);
        numericInputs.add(plantAmount, 1, 5);

        Label toolsTitle = new Label("Tools");
        toolsTitle.setId("tools-title");

        addResource = new Button("Add R");
        addHuman = new Button("Add H");
        addZombie = new Button("Add Z");
        addSubhuman = new Button("Add S");
        addMob = new Button("Add M");
        addPlant = new Button("Add P");
        addCorpse = new Button("Add C");
        remove = new Button("RM");
        
        addTools1 = new ToolBar(addResource, addHuman, addZombie);
        addTools2 = new ToolBar(addSubhuman, addMob, addPlant);
        addTools3 = new ToolBar(addCorpse, remove);

        addTools1.getStyleClass().add("tool-bar");
        addTools2.getStyleClass().add("tool-bar");
        addTools3.getStyleClass().add("tool-bar");

        vPanel = new VBox(buttonHolder, sliderHolder, numericInputs, toolsTitle, addTools1, addTools2, addTools3);
        vPanel.setSpacing(5);
        vPanel.setId("vpanel");
        
        /********************
        Root BorderPane
        ********************/
        root = new BorderPane();
        root.setTop(topBar);
        root.setLeft(vPanel);
        root.setCenter(simView);

        scene = new Scene(root);
        css = new String(MainView.class.getResource("View.css").toExternalForm());  //Css handle
        scene.getStylesheets().add(css);

        /********************
        Button methods
        ********************/
        miniButton.setOnAction(minimise);
        exitButton.setOnAction(exit);

        //Control panel buttons
        resetButton.setOnAction(e -> {
            world.setDisable(false);
            clk.stop();
            sim = new Simulation(world, Integer.parseInt(resourceAmount.getText()), Integer.parseInt(humanAmount.getText()),
            Integer.parseInt(zombieAmount.getText()), Integer.parseInt(subhumanAmount.getText()), Integer.parseInt(mobAmount.getText()),
            Integer.parseInt(plantAmount.getText()));
            stepComplete = true;
        });
        startButton.setOnAction(e -> {
            clk.start();
        });
        stopButton.setOnAction(e -> {
            clk.stop();
        });
        stepButton.setOnAction(e -> {
            if (stepComplete) {
                System.out.println("step");
                sim.action();
                stepComplete = sim.draw();
            }
        });
        
        //Simulation speed slider
        speedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                    clk.setFRAMES_PER_SEC(12L * new_val.intValue()/2);
            }
        });

        //Numeric inputs
        resourceAmount.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    resourceAmount.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        humanAmount.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    humanAmount.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        zombieAmount.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    zombieAmount.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        subhumanAmount.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    subhumanAmount.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        mobAmount.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    mobAmount.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        plantAmount.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    plantAmount.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        //Editing Buttons
        addResource.setOnAction(e -> {
            setToolsInActive();
            setEditMode(0);
            setToolsActive();
        });
        addHuman.setOnAction(e -> {
            setToolsInActive();
            setEditMode(1);
            setToolsActive();
        });
        addZombie.setOnAction(e -> {
            setToolsInActive();
            setEditMode(2);
            setToolsActive();
        });
        addSubhuman.setOnAction(e -> {
            setToolsInActive();
            setEditMode(3);
            setToolsActive();
        });
        addMob.setOnAction(e -> {
            setToolsInActive();
            setEditMode(4);
            setToolsActive();
        });
        addPlant.setOnAction(e -> {
            setToolsInActive();
            setEditMode(5);
            setToolsActive();
        });
        addCorpse.setOnAction(e -> {
            if (editMode != 6) {
                setToolsInActive();
                setEditMode(6);
                setToolsActive();
            } else {
                setToolsInActive();
                wantMutated = !wantMutated;
                setToolsActive();
            }
        });
        remove.setOnAction(e -> {
            setToolsInActive();
            setEditMode(-1);
            setToolsActive();
        });

        //Canvas function
        world.setOnMouseClicked(e -> {
           if (!clk.isRunning()) {
                if (editMode != 6) {
                    sim.editWorld(editMode, e.getX(), e.getY());
                } else {
                    sim.editWorld(editMode, e.getX(), e.getY(), wantMutated);
                }
           }
        });
    }

    private class Movement extends AnimationTimer {
        private boolean running;
        private long FRAMES_PER_SEC = 12L;
        private long INTERVAL = 1000000000L / FRAMES_PER_SEC;
        private long last = 0;
    
        @Override
        public void handle(long now) {
            if (now - last > INTERVAL && stepComplete) {
                try {
                    //System.out.println("called");
                    stepComplete = false;
                    sim.action();
                    stepComplete = sim.draw();
                    last = now; 
                } catch (Exception e) {
                    System.out.println(e);
                }
                
            }
        }

        @Override
        public void start() {
            super.start();
            running = true;
        }

        @Override
        public void stop() {
            super.stop();
            running = false;
        }

        public void setFRAMES_PER_SEC(long fRAMES_PER_SEC) {
            FRAMES_PER_SEC = fRAMES_PER_SEC;
        }

        public boolean isRunning() {
            return running;
        }
    }

    public static void setTime(String steps, String time, String days) {
        stepLabel.setText(steps);
        timeLabel.setText(time);
        dayLabel.setText(days);
    }

    public static void setCount(String[] amounts) {
        resourceCountLabel.setText(amounts[0]);
        humanCountLabel.setText(amounts[1]);
        zombieCountLabel.setText(amounts[2]);
        subhumanCountLabel.setText(amounts[3]);
        mobCountLabel.setText(amounts[4]);
        plantCountLabel.setText(amounts[5]);
        corpseCountLabel.setText(amounts[6]);
    }

    public static void setDayWeather(boolean isDay, boolean isRain, boolean isLightning) {
        if (isDay) {
            daylightLabel.getGraphic().setStyle("-fx-font-family: FontAwesome; -fx-fill: YELLOW; -fx-font-size: 1.5em");
        } else {
            daylightLabel.getGraphic().setStyle("-fx-font-family: FontAwesome; -fx-fill: GREY; -fx-font-size: 1.5em");
        }
        if (isRain) {
            rainLabel.getGraphic().setStyle("-fx-font-family: FontAwesome; -fx-fill: BLUE; -fx-font-size: 1.5em");
        } else {
            rainLabel.getGraphic().setStyle("-fx-font-family: FontAwesome; -fx-fill: GREY; -fx-font-size: 1.5em");
        }
        if (isLightning) {
            lightningLabel.getGraphic().setStyle("-fx-font-family: FontAwesome; -fx-fill: YELLOW; -fx-font-size: 1.5em");
        } else {
            lightningLabel.getGraphic().setStyle("-fx-font-family: FontAwesome; -fx-fill: GREY; -fx-font-size: 1.5em");
        }   
    }

    public void setToolsActive() {
        switch (editMode) {
            case -1:
            remove.setStyle("-fx-text-fill: yellow");
            break;
            case 0:
            addResource.setStyle("-fx-text-fill: yellow");
            break;
            case 1:
            addHuman.setStyle("-fx-text-fill: yellow");
            break;
            case 2:
            addZombie.setStyle("-fx-text-fill: yellow");
            break;
            case 3:
            addSubhuman.setStyle("-fx-text-fill: yellow");
            break;
            case 4:
            addMob.setStyle("-fx-text-fill: yellow");
            break;
            case 5:
            addPlant.setStyle("-fx-text-fill: yellow");
            break;
            case 6:
            if (wantMutated) {
                addCorpse.setStyle("-fx-text-fill: red");
            } else {
                addCorpse.setStyle("-fx-text-fill: lightblue");
            }
            break;
        }
    }

    public void setToolsInActive() {
        switch (editMode) {
            case -1:
            remove.setStyle("-fx-text-fill: white");
            break;
            case 0:
            addResource.setStyle("-fx-text-fill: white");
            break;
            case 1:
            addHuman.setStyle("-fx-text-fill: white");
            break;
            case 2:
            addZombie.setStyle("-fx-text-fill: white");
            break;
            case 3:
            addSubhuman.setStyle("-fx-text-fill: white");
            break;
            case 4:
            addMob.setStyle("-fx-text-fill: white");
            break;
            case 5:
            addPlant.setStyle("-fx-text-fill: white");
            break;
            case 6:
            addCorpse.setStyle("-fx-text-fill: white");
            break;
        }
    }

    public void setEditMode(int mode) {
        editMode = mode;
    }

    public Scene getScene() {
        return scene;
    }
}
