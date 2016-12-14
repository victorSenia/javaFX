package com.leo.test.javaFX.maze;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.*;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Senchenko Victor on 12.12.2016.
 */
public abstract class MazeAbstract extends Application {
    protected static final int WALL_WIDTH = 5;

    protected static final int WALL_HEIGHT = 30;

    protected static final PhongMaterial material = new PhongMaterial();

    protected static final Duration duration = Duration.millis(500);

    protected static final int HEIGHT = 500;

    protected static final int WIDTH = 500;

    protected static final int MAZE = 15;

    protected Transition transition;

    protected Shape[][] field;

    protected int[] position;

    protected int[] destination;

    protected double rotationX;

    protected double rotationY;

    protected Sphere sphereY;

    protected Group sphere;

    protected SimpleIntegerProperty buttons = new SimpleIntegerProperty(0);

    protected SimpleIntegerProperty rotateZ = new SimpleIntegerProperty(0);

    private int camera = -1;

    private SimpleIntegerProperty scope = new SimpleIntegerProperty(2);

    private Scene scene;

    private List<Camera> cameras = new ArrayList<>();

    private Group maze = new Group();

    @Override
    public void start(Stage stage) throws Exception {
        stage.sizeToScene();
        stage.setScene(createScene());
        stage.show();
    }

    private void createCameras() {
        ParallelCamera parallelCamera = new ParallelCamera();
        parallelCamera.scaleXProperty().bind(scope.multiply(.3));
        parallelCamera.scaleYProperty().bind(scope.multiply(.3));
        cameras.add(parallelCamera);

        PerspectiveCamera perspectiveCamera = new PerspectiveCamera(true);
        perspectiveCamera.setFarClip(1000);
        perspectiveCamera.setNearClip(10);
        perspectiveCamera.setFieldOfView(100);
        perspectiveCamera.translateXProperty().bind(sphere.translateXProperty());
        perspectiveCamera.translateYProperty().bind(sphere.translateYProperty());
        perspectiveCamera.translateZProperty().bind(scope.multiply(-50));
        cameras.add(perspectiveCamera);

        PerspectiveCamera perspectiveCamera3D = new PerspectiveCamera(true);
        perspectiveCamera3D.setFarClip(1000);
        perspectiveCamera3D.setNearClip(10);
        perspectiveCamera3D.setFieldOfView(100);
        perspectiveCamera3D.translateXProperty().bind(sphere.translateXProperty());
        perspectiveCamera3D.translateYProperty().bind(sphere.translateYProperty());
        perspectiveCamera3D.translateZProperty().bind(scope.multiply(-50));
        Rotate rotateX = new Rotate(60, 0, 0, 0, Rotate.X_AXIS);
        rotateX.pivotZProperty().bind(scope.multiply(50));
        Rotate rotateZ = new Rotate(0, 0, 0, 0, Rotate.Z_AXIS);
        rotateZ.pivotZProperty().bind(scope.multiply(50));
        rotateZ.angleProperty().bind(this.rotateZ);
        perspectiveCamera3D.getTransforms().addAll(rotateZ, rotateX);
        cameras.add(perspectiveCamera3D);
    }

    private Scene createScene() {
        material.setDiffuseMap(new Image("/wall.jpg"));
        Group world = new Group();
        scene = new Scene(world, WIDTH, HEIGHT, true);
        scene.setFill(new LinearGradient(0, 0, 0, HEIGHT, false, CycleMethod.NO_CYCLE, new Stop(0, Color.WHITE), new Stop(.6, Color.AQUA), new Stop(1, Color.LIGHTGREEN)));
        scene.setOnKeyPressed(this::keyPressed);
        scene.setOnKeyReleased(this::keyReleased);
        scene.setOnMousePressed(this::mousePressed);
        scene.setCursor(Cursor.NONE);

        sphereY = new Sphere(20);
        PhongMaterial material = new PhongMaterial();
        material.setBumpMap(new Image("/ball.jpg"));
        material.setSpecularColor(Color.AQUA);
        sphereY.setMaterial(material);
        //        sphereY.setEffect(new DropShadow(50, 30, 30, Color.BLACK));
        sphere = new Group(sphereY);
        newGame();

        world.getChildren().addAll(sphere, maze);
        createCameras();
        changeView();
        return scene;
    }

    private void newGame() {
        rotationX = 0;
        rotationY = 0;
        if (transition != null && transition.getStatus() != Animation.Status.STOPPED)
            transition.stop();
        createMaze();
    }

    protected void keyReleased(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case LEFT:
                buttons.set(buttons.get() & ~1);
                break;
            case UP:
                buttons.set(buttons.get() & ~2);
                break;
            case RIGHT:
                buttons.set(buttons.get() & ~4);
                break;
            case DOWN:
                buttons.set(buttons.get() & ~8);
                break;
        }
    }

    private void createMaze() {
        maze.getChildren().clear();
        createField();
        position = new int[]{0, 0};
        destination = new int[]{MAZE - 1, MAZE - 1};
        makeMaze(field, position, destination);
        placeBall();
        for (int y = 0; y < field.length; y++)
            for (int x = 0; x < field[y].length; x++)
                maze.getChildren().add(createShape(field[y][x], y, x));
    }

    protected abstract void placeBall();

    /**
     * Default implementation by backtracking
     */
    protected void makeMaze(Shape[][] field, int[] position, int[] destination) {
        Random random = new Random();
        LinkedList<int[]> linkedList = new LinkedList<>();
        List<int[]> list;
        Set<Shape> shapeSet = Arrays.stream(field).flatMap(Arrays::stream).collect(Collectors.toSet());
        int[] current = position;
        shapeSet.remove(field[current[0]][current[1]]);
        while (shapeSet.size() > 0) {
            if ((list = unvisited(current, shapeSet)).size() > 0) {
                linkedList.add(current);
                current = list.get(random.nextInt(list.size()));
                shapeSet.remove(field[current[0]][current[1]]);
                removeWall(current);
            } else {
                current = linkedList.removeLast();
            }
        }
        //        field4[0][0].getWall(Direction4.UP).setClosed(false);
        //        field4[destination[0]][destination[1]].getWall(Direction4.DOWN).setClosed(false);
    }

    protected abstract void createField();

    protected abstract List<int[]> unvisited(int[] current, Set<Shape> shapeSet);

    protected abstract void removeWall(int[] current);

    protected abstract Node createShape(Shape shape, int y, int x);

    /**
     * By default defined only general buttons
     */
    protected void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case V:
                changeView();
                break;
            case N:
                if (keyEvent.isControlDown())
                    newGame();
                break;
            case ADD:
                if (scope.get() > 1)
                    scope.set(scope.get() - 1);
                break;
            case SUBTRACT:
                if (scope.get() < 10)
                    scope.set(scope.get() + 1);
                break;
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

    private void changeView() {
        if (++camera > cameras.size() - 1)
            camera = 0;
        scene.setCamera(cameras.get(camera));
    }

    /**
     * By default defined only general movements
     */
    protected void mousePressed(MouseEvent mouseEvent) {}
}
