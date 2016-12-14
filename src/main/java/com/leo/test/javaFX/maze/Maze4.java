package com.leo.test.javaFX.maze;

import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by Senchenko Victor on 06.12.2016.
 */
public class Maze4 extends MazeAbstract {

    @Override
    protected List<int[]> unvisited(int[] current, Set<Shape> shapeSet) {
        List<int[]> list = new ArrayList<>();
        for (Direction4 direction4 : Direction4.values()) {
            try {
                if (shapeSet.contains(field[current[0] + direction4.getY()][current[1] + direction4.getX()]))
                    list.add(new int[]{current[0] + direction4.getY(), current[1] + direction4.getX(), direction4.ordinal()});
            } catch (IndexOutOfBoundsException e) {
                // no need to check, it is normal work
            }
        }
        return list;
    }

    @Override
    protected void removeWall(int[] current) {
        Direction4 direction4 = Direction4.values()[current[2]];
        field[current[0]][current[1]].getWall(direction4.opposite()).setClosed(false);
    }

    protected void createField() {
        field = new Square(null, null, null, null).field(MAZE, MAZE);
    }

    @Override
    protected Node createShape(Shape square, int y, int x) {
        Group group = new Group();
        group.getChildren().addAll(moveCorner(0, 0), moveCorner(0, 50), moveCorner(50, 0), moveCorner(50, 50));
        if (square.getWall(Direction4.UP).isClosed())
            group.getChildren().add(moveBox(50 / 2, 0, 50 - WALL_WIDTH, WALL_WIDTH, WALL_HEIGHT));
        if (square.getWall(Direction4.DOWN).isClosed())
            group.getChildren().add(moveBox(50 / 2, 50, 50 - WALL_WIDTH, WALL_WIDTH, WALL_HEIGHT));
        if (square.getWall(Direction4.LEFT).isClosed())
            group.getChildren().add(moveBox(0, 50 / 2, WALL_WIDTH, 50 - WALL_WIDTH, WALL_HEIGHT));
        if (square.getWall(Direction4.RIGHT).isClosed())
            group.getChildren().add(moveBox(50, 50 / 2, WALL_WIDTH, 50 - WALL_WIDTH, WALL_HEIGHT));
        group.setTranslateX(x * 50);
        group.setTranslateY(y * 50);
        return group;
    }

    private Box moveCorner(double x, double y) {
        return moveBox(x, y, WALL_WIDTH, WALL_WIDTH, WALL_HEIGHT);
    }

    private Box moveBox(double x, double y, double width, double height, double depth) {
        Box box = new Box(width, height, depth);
        box.setMaterial(material);
        box.setTranslateX(x + WALL_WIDTH / 2);
        box.setTranslateY(y + WALL_WIDTH / 2);
        return box;
    }

    protected void placeBall() {
        sphere.setTranslateX(position[0] * 50 + 25 + WALL_WIDTH / 2);
        sphere.setTranslateY(position[1] * 50 + 25 + WALL_WIDTH / 2);
    }

    @Override
    protected void keyPressed(KeyEvent keyEvent) {
        super.keyPressed(keyEvent);
        if (transition == null || transition.getStatus() == Animation.Status.STOPPED)
            moveBall();
    }

    private void moveBall() {
        switch (buttons.get()) {
            case 1:
                moveBall(Direction4.LEFT);
                rotateZ.set(270);
                break;
            case 4:
                moveBall(Direction4.RIGHT);
                rotateZ.set(90);
                break;
            case 2:
                moveBall(Direction4.UP);
                rotateZ.set(0);
                break;
            case 8:
                moveBall(Direction4.DOWN);
                rotateZ.set(180);
                break;
        }
    }

    private void moveBall(Direction4 direction4) {
        double x = direction4.getX(), y = direction4.getY();
        if (!field[position[0]][position[1]].getWall(direction4).isClosed()) {
            TranslateTransition translate = new TranslateTransition(duration, sphere);
            translate.setToX(x * 50 + sphere.getTranslateX());
            translate.setToY(y * 50 + sphere.getTranslateY());
            translate.setInterpolator(Interpolator.LINEAR);
            double rotate = 50 * 180 / Math.PI / 20;
            RotateTransition rotateX = new RotateTransition(duration, sphere);
            rotateX.setInterpolator(Interpolator.LINEAR);
            rotateX.setAxis(Rotate.X_AXIS);
            rotateX.setFromAngle(rotationX);
            switch (direction4) {
                case DOWN:
                    rotateX.setToAngle(rotationX += rotate);
                    break;
                case UP:
                    rotateX.setToAngle(rotationX -= rotate);
                    break;
                default:
            }

            RotateTransition rotateY = new RotateTransition(duration, sphereY);
            rotateY.setInterpolator(Interpolator.LINEAR);
            rotateY.setAxis(Rotate.Y_AXIS);
            rotateY.setFromAngle(rotationY);
            switch (direction4) {
                case LEFT:
                    rotateY.setToAngle(rotationY += rotate);
                    break;
                case RIGHT:
                    rotateY.setToAngle(rotationY -= rotate);
                    break;
                default:
            }
            this.transition = new ParallelTransition(translate, rotateX, rotateY);
            this.transition.play();
            this.transition.setOnFinished(event -> {
                if (Arrays.equals(position, destination))
                    System.out.println("you win");
                else if (buttons.get() != 0)
                    moveBall();
            });
            position[0] += y;
            position[1] += x;
        }
    }
}
