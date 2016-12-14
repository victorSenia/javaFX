package com.leo.test.javaFX.test;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Created by Senchenko Victor on 06.12.2016.
 */
public class StartAnimation extends Application {
    private static final int HEIGHT = 500;

    private static final int WIDTH = 800;

    final Group world = new Group();

    Box box;

    Group xform;

    private Transition transition;

    @Override
    public void start(Stage stage) throws Exception {
        stage.sizeToScene();
        stage.setScene(createScene());
        stage.show();
    }

    private Scene createScene() {
        xform = new Group();
        world.getChildren().add(xform);
        Scene scene = new Scene(new Group(world), WIDTH, HEIGHT, true);
        scene.setFill(new LinearGradient(0, 0, 0, HEIGHT, false, CycleMethod.NO_CYCLE, new Stop(0, Color.WHITE), new Stop(.6, Color.AQUA), new Stop(1, Color.LIGHTGREEN)));
        scene.setOnKeyPressed(this::keyPressed);
        scene.setOnMousePressed(this::mousePressed);
        scene.setCursor(Cursor.NONE);
        box = new Box(20, 20, 20);
        box.setMaterial(new PhongMaterial(Color.AQUA));
        box.setEffect(new DropShadow(50, 30, 30, Color.BLACK));
        //        box.getTransforms().add(new Translate(0, 0, 20));

        //                box.setDrawMode(DrawMode.LINE);

        Camera camera = new PerspectiveCamera(true);
        camera.setFarClip(1000);
        camera.setNearClip(.1);
        camera.getTransforms().addAll(new Rotate(-30, Rotate.Y_AXIS), new Rotate(-30, Rotate.X_AXIS), new Translate(0, 0, -150));
        scene.setCamera(camera);

        PointLight pointLight = new PointLight(Color.LIGHTSALMON);
        pointLight.getTransforms().add(new Translate(0, 0, -300));

        Sphere sphere = new Sphere(15);
        sphere.setMaterial(new PhongMaterial(Color.ANTIQUEWHITE));
        sphere.setEffect(new DropShadow(50, 30, 30, Color.BLACK));
        xform.getChildren().addAll(sphere, box, new Cylinder(1, 30), pointLight);

        return scene;
    }

    private void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case LEFT:
                newAnimation(25, -25);
                break;
            case RIGHT:
                newAnimation(-25, 25);
                break;
            case SPACE:
                if (transition != null)
                    if (transition.getStatus() == Animation.Status.PAUSED)
                        transition.play();
                    else
                        transition.pause();
        }
    }

    private void newAnimation(int from, int to) {
        stopAnnimation();
        transition = createTranslateTransition(from, to);
        //        transition = createPathTransition(from, to);
        transition.play();
    }

    private TranslateTransition createTranslateTransition(double from, double to) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(3), box);
        transition.setAutoReverse(true);
        transition.setCycleCount(Animation.INDEFINITE);
        transition.setFromZ(from);
        transition.setToZ(to);
        transition.setInterpolator(Interpolator.EASE_BOTH);
        return transition;
    }

    private PathTransition createPathTransition(int from, int to) {
        Path path = new Path(new MoveTo(from, 0), new ArcTo(3 * 2, 3 * 2, 0, to, 0, false, from > 0));
        //        xform.getChildren().add(path);
        PathTransition transition = new PathTransition(Duration.seconds(3), path, box);
        transition.setAutoReverse(true);
        transition.setCycleCount(Animation.INDEFINITE);
        transition.setInterpolator(Interpolator.EASE_BOTH);
        return transition;
    }

    private void stopAnnimation() {
        if (transition != null)
            transition.stop();
    }

    private void mousePressed(MouseEvent mouseEvent) {

    }
}
