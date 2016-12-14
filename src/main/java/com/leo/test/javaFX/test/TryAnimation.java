package com.leo.test.javaFX.test;

import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Created by Senchenko Victor on 05.12.2016.
 */
public class TryAnimation extends Application {
    private static final int HEIGHT = 500;

    private static final int WIDTH = 800;

    @Override
    public void start(Stage stage) throws Exception {
        //make invisible and fit to scene
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.sizeToScene();
        stage.setScene(createScene());
        stage.show();
        //        //output FPS
        //        new Timer().schedule(new TimerTask() {
        //            @Override
        //            public void run() {
        //                System.out.println("FPS " + PerformanceTracker.getSceneTracker(stage.getScene()).getInstantFPS());
        //            }
        //        }, 0, 1000);
    }

    private Scene createScene() {
        Group group = new Group();
        Ellipse ellipse = new Ellipse(WIDTH / 2, HEIGHT / 2, WIDTH / 2, HEIGHT / 2);
        group.setClip(ellipse);
        Rectangle rectangle = new Rectangle(WIDTH, HEIGHT, new LinearGradient(0, 0, 0, HEIGHT, false, CycleMethod.NO_CYCLE, new Stop(0., new Color(1, 1, 1, 0)), new Stop(0.8, Color.LIGHTBLUE), new Stop(1., Color.YELLOWGREEN)));
        Text text = new Text("EXIT");
        //        text.setTextAlignment(TextAlignment.CENTER);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        text.setOpacity(0);
        text.setOnMouseClicked(event -> {
            Platform.exit();
            System.exit(0);
        });
        text.setTranslateY(HEIGHT * .2);
        text.translateXProperty().bind(rectangle.widthProperty().divide(2).add(text.wrappingWidthProperty().divide(2)));

        double radius = 30;
        Circle circle = new Circle(0, 0, radius, new RadialGradient(0, 0, 0, 0, radius, false, CycleMethod.NO_CYCLE, new Stop(0, Color.DARKRED), new Stop(.6, Color.DARKGREEN),new Stop(1, Color.BLACK)));

        Path path = createPath();

        group.getChildren().addAll(rectangle, testTransform(text), path, circle);

        final PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.seconds(4.0));
        pathTransition.setDelay(Duration.seconds(.5));
        pathTransition.setPath(path);
        pathTransition.setNode(circle);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.setAutoReverse(true);
        pathTransition.play();

        Transition transition = new Transition() {
            {
                setCycleCount(javafx.animation.Animation.INDEFINITE);
                setInterpolator(Interpolator.LINEAR);
                setCycleDuration(Duration.seconds(3));
                setAutoReverse(true);
            }

            @Override
            protected void interpolate(double frac) {
            }
        };
        transition.play();

        Scene scene = new Scene(group, Color.TRANSPARENT);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(150), text);
        fadeIn.setToValue(0.8);
        scene.setOnMouseEntered(e -> fadeIn.playFromStart());
        FadeTransition fadeOut = new FadeTransition(Duration.millis(150), text);
        fadeOut.setToValue(0);
        scene.setOnMouseExited(e -> fadeOut.play());

        scene.setOnKeyPressed(this::keyPressed);
        scene.setOnMousePressed(this::mousePressed);

        scene.setCursor(Cursor.WAIT);

        return scene;
    }

    private void mousePressed(MouseEvent mouseEvent) {
        System.out.println(mouseEvent.getX() + " " + mouseEvent.getY());
    }

    private void keyPressed(KeyEvent key) {
        System.out.println(key.getCode());
    }

    private Node testTransform(Node node) {
        //upper-left,upper-right, lower-right,lower-left
        PerspectiveTransform transform = new PerspectiveTransform(-50, -50, 45, -55, 55, 55, -40, 45);
        Reflection shadow = new Reflection(.5, 1, .4, .2);
        transform.setInput(shadow);
        node.setEffect(transform);
        return node;
    }

    private Path createPath() {
        Path path = new Path();

        MoveTo moveTo = new MoveTo();
        moveTo.setX(100.0f);
        moveTo.setY(100.0f);

        HLineTo hLineTo = new HLineTo();
        hLineTo.setX(150.0f);

        QuadCurveTo quadCurveTo = new QuadCurveTo();
        quadCurveTo.setX(120.0f);
        quadCurveTo.setY(60.0f);
        quadCurveTo.setControlX(200.0f);
        quadCurveTo.setControlY(0.0f);

        LineTo lineTo = new LineTo();
        lineTo.setX(75.0f);
        lineTo.setY(255.0f);

        ArcTo arcTo = new ArcTo();
        arcTo.setX(150.0f);
        arcTo.setY(150.0f);
        arcTo.setRadiusX(150.0f);
        arcTo.setRadiusY(150.0f);

        //        path.getElements().add(moveTo);
        //        path.getElements().add(hLineTo);
        //        path.getElements().add(quadCurveTo);
        //        path.getElements().add(lineTo);
        //        path.getElements().add(arcTo);

        double h = 100, w = 40;
        path.getElements().addAll(new MoveTo(0, 0), new QuadCurveTo(-10, -h/2, h / 4, -h), new QuadCurveTo(10, -h/2, w, 0));
        path.getTransforms().add(Transform.translate(350, 350));//move to
        path.setFill(Color.BLACK);//fill with color
        path.setStroke(null);//hide path

        return path;
    }
}
