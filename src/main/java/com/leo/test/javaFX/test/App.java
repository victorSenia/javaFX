package com.leo.test.javaFX.test;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Created by Senchenko Victor on 05.12.2016.
 */
public class App extends Application {
    private static final int SCENE_WIDTH = 1000;

    private static final int SCENE_HEIGHT = 800;

    private Group rootContent;

    public static void main(String... args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("TryAnimation");
        primaryStage.setScene(createScene());
        //make invisible and fit to scene
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.sizeToScene();

        //close application
        final Text close = new Text("Click to close");
        close.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        close.setStyle("-fx-background-color:transparent;-fx-text-fill:gray;");
        close.setOpacity(0);
        close.setOnMouseClicked(e -> {
            Platform.exit();
            System.exit(0);
        });
        StackPane stackPane = new StackPane(close);
        stackPane.setTranslateY(80);

        stackPane.translateXProperty().bind(stackPane.widthProperty().divide(2).negate().add(SCENE_WIDTH / 2));

        rootContent.getChildren().add(stackPane);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(150), close);
        fadeIn.setToValue(0.7);
        primaryStage.getScene().setOnMouseEntered(e -> fadeIn.playFromStart());
        FadeTransition fadeOut = new FadeTransition(Duration.millis(150), close);
        fadeOut.setToValue(0);
        primaryStage.getScene().setOnMouseExited(e -> fadeOut.play());
        primaryStage.show();
    }

    private Scene createScene() {
        rootContent = new Group();
        rootContent.setClip(new Ellipse(SCENE_WIDTH / 2, SCENE_HEIGHT / 2, SCENE_WIDTH / 2, SCENE_HEIGHT / 2)); //Scene shape and size

        Rectangle background = new Rectangle(SCENE_WIDTH, SCENE_HEIGHT);
        background.setFill(new LinearGradient(0, 0, 0, SCENE_HEIGHT, false, CycleMethod.NO_CYCLE, new Stop(1., Color.YELLOWGREEN), new Stop(0.7, Color.LIGHTBLUE), new Stop(0, new Color(1, 1, 1, 0)))); //background color
        rootContent.getChildren().add(background);
        //        rootContent.getChildren().add(treeContent = new Group()); // tree layout
        //        rootContent.getChildren().add(grassContent = new Group()); // grass layout

        return new Scene(rootContent, SCENE_WIDTH, SCENE_HEIGHT, Color.TRANSPARENT);
    }
}
