package com.leo.test.javaFX.road;

import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.*;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.Instant;

/**
 * Created by Senchenko Victor on 12.12.2016.
 */
public class MoveOnButtons extends Application {
    protected static final int HEIGHT = 500;

    protected static final int WIDTH = 500;

    protected static final PhongMaterial material = new PhongMaterial();

    protected static final double speed = .5;

    TranslateTransition transition;

    private SimpleIntegerProperty buttons = new SimpleIntegerProperty(0);

    private SimpleIntegerProperty x = new SimpleIntegerProperty(0);

    private SimpleIntegerProperty y = new SimpleIntegerProperty(0);

    public static void main(String... args) {
        launch(args);
    }

    public void translateOnButtonPressed(Node node) {
        final long[] old = {Instant.now().toEpochMilli()};
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                long current = Instant.now().toEpochMilli();
                long delta = current - old[0];
                double deltaX = x.get() * delta * speed + node.getTranslateX();
                double deltaY = y.get() * delta * speed + node.getTranslateY();
                node.setTranslateX(deltaX < 0 ? 0 : deltaX < WIDTH ? deltaX : WIDTH);
                node.setTranslateY(deltaY < 0 ? 0 : deltaY < HEIGHT ? deltaY : HEIGHT);
                old[0] = current;
            }
        };
        timer.start();
    }

    public void translateChangeOnButtonPressed(Node node) {
        transition = new TranslateTransition(Duration.INDEFINITE, node);
        transition.byXProperty().bind(x.multiply(1000));
        transition.byYProperty().bind(y.multiply(1000));
        transition.setInterpolator(Interpolator.LINEAR);
        transition.play();
//        x.addListener(observable -> {
//            transition.pause();
//            transition.setByX(x.get() * 100);
//            transition.play();
//        });
//        y.addListener(observable -> {
//            transition.pause();
//            transition.setByY(y.get() * 100);
//            transition.play();
//        });
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.sizeToScene();
        stage.setScene(createScene());
        stage.show();
        buttons.addListener(observable -> direction((SimpleIntegerProperty) observable));
    }

    private void direction(SimpleIntegerProperty buttons) {
        int b = buttons.get();
        x.set(((b & 1) > 0 ? -1 : 0) + ((b & 4) > 0 ? 1 : 0));
        y.set(((b & 2) > 0 ? -1 : 0) + ((b & 8) > 0 ? 1 : 0));
    }

    private Scene createScene() {
        material.setDiffuseMap(new Image("/wall.jpg"));
        Group road = new Group();
        Scene scene = new Scene(road, WIDTH, HEIGHT, true);
        scene.setFill(new LinearGradient(0, 0, 0, HEIGHT, false, CycleMethod.NO_CYCLE, new Stop(0, Color.WHITE), new Stop(.6, Color.AQUA), new Stop(1, Color.LIGHTGREEN)));
        scene.setOnKeyPressed(this::keyPressed);
        scene.setOnKeyReleased(this::keyReleased);
        scene.setCursor(Cursor.NONE);

        Sphere sphere = new Sphere(20);
        PhongMaterial material = new PhongMaterial();
        material.setBumpMap(new Image("/ball.jpg"));
        material.setSpecularColor(Color.AQUA);
        sphere.setMaterial(material);
        road.getChildren().addAll(sphere, createRoad());
                translateOnButtonPressed(sphere);
//        translateChangeOnButtonPressed(sphereY);
        return scene;
    }

    private Node createRoad() {
        return new Group();
    }

    protected void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case LEFT:
            case A:
                buttons.set(buttons.get() | 1);
                break;
            case UP:
            case W:
                buttons.set(buttons.get() | 2);
                break;
            case RIGHT:
            case D:
                buttons.set(buttons.get() | 4);
                break;
            case DOWN:
            case S:
                buttons.set(buttons.get() | 8);
                break;
        }
    }

    protected void keyReleased(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case LEFT:
            case A:
                buttons.set(buttons.get() & ~1);
                break;
            case UP:
            case W:
                buttons.set(buttons.get() & ~2);
                break;
            case RIGHT:
            case D:
                buttons.set(buttons.get() & ~4);
                break;
            case DOWN:
            case S:
                buttons.set(buttons.get() & ~8);
                break;
        }
    }
}
