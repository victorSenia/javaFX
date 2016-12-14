package com.leo.test.javaFX.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;

// Collect the Money Bags!
public class MoneyBagBadExample extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        theStage.setTitle("Collect the Money Bags!");

        Group root = new Group();
        Scene theScene = new Scene(root);
        theStage.setScene(theScene);

        Canvas canvas = new Canvas(512, 512);
        root.getChildren().add(canvas);

        ArrayList<String> input = new ArrayList<String>();

        theScene.setOnKeyPressed(e -> {
            String code = e.getCode().toString();
            if (!input.contains(code))
                input.add(code);
        });

        theScene.setOnKeyReleased(e -> {
            String code = e.getCode().toString();
            input.remove(code);
        });

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Font theFont = Font.font("Helvetica", FontWeight.BOLD, 24);
        gc.setFont(theFont);
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        Sprite briefcase = new Sprite();
        briefcase.setImage("briefcase.png");
        briefcase.setPosition(200, 0);

        ArrayList<Sprite> moneybagList = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            Sprite moneybag = new Sprite();
            moneybag.setImage("moneybag.png");
            double px = 350 * Math.random() + 50;
            double py = 350 * Math.random() + 50;
            moneybag.setPosition(px, py);
            moneybagList.add(moneybag);
        }

        LongValue lastNanoTime = new LongValue(System.nanoTime());

        IntValue score = new IntValue(0);

        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                // calculate time since last update.
                double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
                lastNanoTime.value = currentNanoTime;

                // game logic
                briefcase.setVelocity(0, 0);
                if (input.contains("LEFT"))
                    briefcase.addVelocity(-50, 0);
                if (input.contains("RIGHT"))
                    briefcase.addVelocity(50, 0);
                if (input.contains("UP"))
                    briefcase.addVelocity(0, -50);
                if (input.contains("DOWN"))
                    briefcase.addVelocity(0, 50);

                briefcase.update(elapsedTime);

                // collision detection

                Iterator<Sprite> moneybagIter = moneybagList.iterator();
                while (moneybagIter.hasNext()) {
                    Sprite moneybag = moneybagIter.next();
                    if (briefcase.intersects(moneybag)) {
                        moneybagIter.remove();
                        score.value++;
                    }
                }

                // render

                gc.clearRect(0, 0, 512, 512);
                briefcase.render(gc);

                for (Sprite moneybag : moneybagList)
                    moneybag.render(gc);

                String pointsText = "Cash: $" + (100 * score.value);
                gc.fillText(pointsText, 360, 36);
                gc.strokeText(pointsText, 360, 36);
            }
        }.start();

        theStage.show();
    }

    public class Sprite {
        private Image image;

        private double positionX;

        private double positionY;

        private double velocityX;

        private double velocityY;

        private double width;

        private double height;

        public Sprite() {
            positionX = 0;
            positionY = 0;
            velocityX = 0;
            velocityY = 0;
        }

        public void setImage(Image i) {
            image = i;
            width = i.getWidth();
            height = i.getHeight();
        }

        public void setImage(String filename) {
            Image i = new Image(filename);
            setImage(i);
        }

        public void setPosition(double x, double y) {
            positionX = x;
            positionY = y;
        }

        public void setVelocity(double x, double y) {
            velocityX = x;
            velocityY = y;
        }

        public void addVelocity(double x, double y) {
            velocityX += x;
            velocityY += y;
        }

        public void update(double time) {
            positionX += velocityX * time;
            positionY += velocityY * time;
        }

        public void render(GraphicsContext gc) {
            gc.drawImage(image, positionX, positionY);
        }

        public Rectangle2D getBoundary() {
            return new Rectangle2D(positionX, positionY, width, height);
        }

        public boolean intersects(Sprite s) {
            return s.getBoundary().intersects(this.getBoundary());
        }

        public String toString() {
            return " Position: [" + positionX + "," + positionY + "]" + " Velocity: [" + velocityX + "," + velocityY + "]";
        }
    }

    public class LongValue {
        public long value;

        public LongValue(long i) {
            value = i;
        }
    }

    public class IntValue {
        public int value;

        public IntValue(int i) {
            value = i;
        }
    }
}