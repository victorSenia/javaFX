package com.leo.test.javaFX.example;

import com.sun.javafx.geom.Vec2f;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.Random;

import static java.lang.Math.*;

/**
 * Created by Senchenko Victor on 01.12.2016.
 */
public class MovableLabels extends Application {
    private int windowWidth = 600;

    private int windowHeight = 600;

    private Random random = new Random(); // нам нужен рандом!!

    private Node node;   // ссылка на объект, который будем перетаскивать

    private Vec2f delta; // координаты указателя мыши относительно элемента

    private boolean isRotate;
    // нужны для того, чтобы не было резкого рывка вначале движения

    public static void main(String... args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Simple Application");
        primaryStage.setScene(createScene());
        primaryStage.show();
    }

    private Scene createScene() {
        Group root = new Group();
        for (int i = 0; i < 13; i++) {
            root.getChildren().add(createLabel());
        }
        return new Scene(root, windowWidth, windowHeight);
    }

    private Label createLabel() {
        Label label = LabelBuilder.create()    // создание билдера для Label
                .text("SomeText")             // текстовое значение
                .prefWidth(100)                // возможная ширина
                .prefHeight(50)                // возможная высота
                .alignment(Pos.CENTER)         // выравнивание содержимого по центру
                .layoutX(random.nextInt(windowWidth - 100)) // задание  коорд. Х
                .layoutY(random.nextInt(windowHeight - 50))  // задание  коорд. Y
                .style("-fx-background-color: orange;")  // зарисуем фон в оранжевый
                .build(); // создадим из билдера сам объект класса Label
        addTranslateListenerRotate(label);
        return label;
    }

    private void addTranslateListener(final Node node) {
        node.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                delta = new Vec2f((float) (mouseEvent.getSceneX() - node.getLayoutX()), (float) (mouseEvent.getSceneY() - node.getLayoutY()));
                // вичисление координат относительно элемeнта
                MovableLabels.this.node = node;   // сохраняем ссылку на объект
            }
        });
        node.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                MovableLabels.this.node = null;  // обнулям ссылку на объект
            }
        });
        node.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                if (node != null) {
                    // если есть что перетаскивать, перетаскиваем
                    node.setLayoutX(mouseEvent.getSceneX() - delta.x);
                    node.setLayoutY(mouseEvent.getSceneY() - delta.y);
                }
            }
        });
    }
    private void addTranslateListenerRotate(final Node node) {
        node.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                delta = new Vec2f((float) (mouseEvent.getSceneX() - node.getLayoutX()),
                        (float) (mouseEvent.getSceneY() - node.getLayoutY()));
                MovableLabels.this.node = node;
                if (mouseEvent.getButton() == MouseButton.SECONDARY){
                    isRotate = true;
                }
            }
        });
        node.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                MovableLabels.this.node = null;
                isRotate = false;
            }
        });
        node.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                if (node != null ) {
                    if (isRotate) {
                        // подсчет нового угла основуется на подсчета угла между двома векторами
                        // это несложная математика, и поэтому тут должно быть все понятно)
                        double dx1 = mouseEvent.getSceneX() - node.getLayoutX();
                        double dy1 = mouseEvent.getSceneY() - node.getLayoutY();
                        double l = Math.sqrt(dx1 *dx1 + dy1 *dy1);
                        dx1 /= l; // нормализация
                        dy1 /= l; // вектора

                        double angle = PI/2; // за второй вектор было взято
                        double dx2 = sin(angle);      // единичный вектор,
                        double dy2 = cos(angle);      // который повернутый на 90 градусов

                        double cosA = dx1 * dx2 + dy1 * dy2;
                        angle = acos(cosA);

                        if (dy1 < 0) angle = PI - angle ;
                        node.setRotate(angle / PI * 180); // из радиан в градусы
                    }
                    else {
                        node.setLayoutX(mouseEvent.getSceneX() - delta.x);
                        node.setLayoutY(mouseEvent.getSceneY() - delta.y);
                    }
                }
            }
        });
    }
}
