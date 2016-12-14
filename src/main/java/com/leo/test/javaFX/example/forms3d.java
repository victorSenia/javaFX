package com.leo.test.javaFX.example;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

/**
 * Created by Senchenko Victor on 01.12.2016.
 */
public class forms3d extends Application {
    /**
     * Java main for when running without JavaFX launcher
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("forms3d");
        primaryStage.setScene(scene());
        primaryStage.show();
    }

    private Scene scene() {
        Label label = new Label("Label");
        final TextField textField = new TextField("TextField");
        Button button = new Button("Button");
        button.setOnMousePressed(event -> textField.setText("text"));
        button.disableProperty().bind(Bindings.isEmpty(textField.textProperty()));
        //        textField.
        //        Group group = new Group();
        //        ObservableList<Node> nodes = group.getChildren();
        //        nodes.addAll(new Label("Label"));
        //        Scene scene = new Scene(group, 400, 400);
        Box myBox = new Box(2, 2, 2);
        Camera camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll(new Rotate(-30, Rotate.Y_AXIS), new Rotate(-30, Rotate.X_AXIS), new Translate(0, 0, -15));
        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(50);
        light.setTranslateY(-300);
        light.setTranslateZ(-400);
        PointLight light2 = new PointLight(Color.color(0.6, 0.3, 0.4));
        light2.setTranslateX(400);
        light2.setTranslateY(0);
        light2.setTranslateZ(-400);
//        AmbientLight ambientLight = new AmbientLight(Color.color(0.2, 0.2, 0.2));
//        ambientLight.setRotationAxis(new Point3D(2, 1, 0).normalize());
//        ambientLight.setTranslateX(180);
//        ambientLight.setTranslateY(180);
        Group group = new Group( myBox, light, light2);
        SubScene subScene = new SubScene(group, 200, 200);
        subScene.setFill(Color.ALICEBLUE);
        subScene.setCamera(camera);
        group = new Group(subScene);
        Scene scene = new Scene(new FlowPane(10, 10, label, textField, button, group), 400, 300);

        return scene;
    }
    //    public Parent createContent() throws Exception {
    //
    //        // Box
    //        Box testBox = new Box(1, 1, 1);
    //        testBox.setMaterial(new PhongMaterial(Color.RED));
    //        testBox.setDrawMode(DrawMode.LINE);
    //
    //        // Create and position camera
    //        PerspectiveCamera camera = new PerspectiveCamera(true);
    //        camera.getTransforms().addAll(new Rotate(-20, Rotate.Y_AXIS), new Rotate(-20, Rotate.X_AXIS), new Translate(0, 0, -15));
    //
    //        // Build the Scene Graph
    //        Group root = new Group();
    //        root.getChildren().add(camera);
    //        root.getChildren().add(testBox);
    //
    //        // Use a SubScene
    //        SubScene subScene = new SubScene(root, 300, 300);
    //        subScene.setFill(Color.ALICEBLUE);
    //        subScene.setCamera(camera);
    //        Group group = new Group();
    //        group.getChildren().add(subScene);
    //        return group;
    //    }
    //
    //    @Override
    //    public void start(Stage primaryStage) throws Exception {
    //        primaryStage.setResizable(false);
    //        Scene scene = new Scene(createContent());
    //        primaryStage.setScene(scene);
    //        primaryStage.show();
    //    }
}
