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
public class Maze6 extends MazeAbstract {
    private static final double h = 50 / 2, w = 50 * Math.cos(Math.toRadians(30));

    protected List<int[]> unvisited(int[] current, Set<Shape> hexagonSet) {
        List<int[]> list = new ArrayList<>();
        for (Direction6 direction : Direction6.values()) {
            try {
                double xT = direction.getX(), yT = direction.getY();
                int x = current[1] + (xT < 0 ? -1 : 1) + (yT != 0 ? (xT < 0 ? current[0] % 2 : -(current[0] + 1) % 2) : 0);
                int y = current[0] + (yT < 0 ? -1 : yT > 0 ? 1 : 0);
                if (hexagonSet.contains(field[y][x]))
                    list.add(new int[]{y, x, direction.ordinal()});
            } catch (IndexOutOfBoundsException e) {
                // no need to check, it is normal work
            }
        }
        return list;
    }

    protected void placeBall() {
        sphere.setTranslateX(position[0] * 2 * w + w);
        sphere.setTranslateY(position[1] * 4 * h + h * 2);
    }

    @Override
    protected void createField() {
        field = new Hexagon(null, null, null, null, null, null).field(MAZE, MAZE);
    }

    protected void removeWall(int[] current) {
        Direction6 direction = Direction6.values()[current[2]];
        field[current[0]][current[1]].getWall(direction.opposite()).setClosed(false);
    }

    protected Node createShape(Shape hexagon, int y, int x) {
        Group group = new Group();
        if (hexagon.getWall(Direction6.UP_LEFT).isClosed())
            group.getChildren().add(moveBox(w / 2, h / 2, 60));
        if (hexagon.getWall(Direction6.UP_RIGHT).isClosed())
            group.getChildren().add(moveBox(w * 1.5, h / 2, -60));
        if (hexagon.getWall(Direction6.DOWN_LEFT).isClosed())
            group.getChildren().add(moveBox(w / 2, h * 3.5, -60));
        if (hexagon.getWall(Direction6.DOWN_RIGHT).isClosed())
            group.getChildren().add(moveBox(w * 1.5, h * 3.5, 60));
        if (hexagon.getWall(Direction6.LEFT).isClosed())
            group.getChildren().add(moveBox(0, h * 2, 0));
        if (hexagon.getWall(Direction6.RIGHT).isClosed())
            group.getChildren().add(moveBox(w * 2, h * 2, 0));
        group.setTranslateX(x * w * 2 + (y % 2) * w);
        group.setTranslateY(y * h * 3);
        return group;
    }

    private Box moveBox(double x, double y, double rotate) {
        return moveBox(x, y, rotate, WALL_WIDTH, 50, WALL_HEIGHT);
    }

    private Box moveBox(double x, double y, double rotate, double width, double height, double depth) {
        Box box = new Box(width, height, depth);
        box.setMaterial(material);
        box.setTranslateX(x);
        box.setTranslateY(y);
        box.setRotate(rotate);
        return box;
    }

    protected void keyPressed(KeyEvent keyEvent) {
        super.keyPressed(keyEvent);
        if (transition == null || transition.getStatus() == Animation.Status.STOPPED)
            moveBall();
    }

    private void moveBall() {
        switch (buttons.get()) {
            case 1:
                moveBall(Direction6.LEFT);
                rotateZ.set(270);
                break;
            case 4:
                moveBall(Direction6.RIGHT);
                rotateZ.set(90);
                break;
            case 9:
                moveBall(Direction6.DOWN_LEFT);
                rotateZ.set(210);
                break;
            case 12:
                moveBall(Direction6.DOWN_RIGHT);
                rotateZ.set(150);
                break;
            case 3:
                moveBall(Direction6.UP_LEFT);
                rotateZ.set(330);
                break;
            case 6:
                moveBall(Direction6.UP_RIGHT);
                rotateZ.set(30);
        }
    }

    private void moveBall(Direction6 direction) {
        if (!field[position[0]][position[1]].getWall(direction).isClosed()) {
            double x = direction.getX(), y = direction.getY();
            TranslateTransition translate = new TranslateTransition(duration, sphere);
            translate.setToX(x * w * 2 + sphere.getTranslateX());
            translate.setToY(y * h * 4 + sphere.getTranslateY());
            translate.setInterpolator(Interpolator.LINEAR);
            double rotate = 50 * 180 / Math.PI / 20;
            RotateTransition rotateX = new RotateTransition(duration, sphere);
            rotateX.setInterpolator(Interpolator.LINEAR);
            rotateX.setAxis(Rotate.X_AXIS);
            rotateX.setFromAngle(rotationX);
            switch (direction) {
                case UP_LEFT:
                case UP_RIGHT:
                    rotateX.setToAngle(rotationX += rotate);
                    break;
                case DOWN_RIGHT:
                case DOWN_LEFT:
                    rotateX.setToAngle(rotationX -= rotate);
                    break;
                default:
            }
            RotateTransition rotateY = new RotateTransition(duration, sphereY);
            rotateY.setInterpolator(Interpolator.LINEAR);
            rotateY.setAxis(Rotate.Y_AXIS);
            rotateY.setFromAngle(rotationY);
            switch (direction) {
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
            position[1] += (x < 0 ? -1 : 1) + (y != 0 ? (x < 0 ? position[0] % 2 : -(position[0] + 1) % 2) : 0);
            position[0] += y < 0 ? -1 : y > 0 ? 1 : 0;
        }
    }
}
