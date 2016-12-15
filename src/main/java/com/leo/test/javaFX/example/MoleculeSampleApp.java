/*
 * Copyright (c) 2013, 2014 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.leo.test.javaFX.example;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author cmcastil
 */
public class MoleculeSampleApp extends Application {

    final Group root = new Group();
    final Xform axisGroup = new Xform();
    final Xform moleculeGroup = new Xform();
    final Xform world = new Xform();
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    final Xform cameraXform = new Xform();
    final Xform cameraXform2 = new Xform();
    final Xform cameraXform3 = new Xform();
    private static final double CAMERA_INITIAL_DISTANCE = -450;
    private static final double CAMERA_INITIAL_X_ANGLE = 70.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 320.0;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;
    private static final double AXIS_LENGTH = 250.0;
    private static final double HYDROGEN_ANGLE = 104.5;
    private static final double CONTROL_MULTIPLIER = 0.1;
    private static final double SHIFT_MULTIPLIER = 10.0;
    private static final double MOUSE_SPEED = 0.1;
    private static final double ROTATION_SPEED = 2.0;
    private static final double TRACK_SPEED = 0.3;
    
    double mousePosX;
    double mousePosY;
    double mouseOldX;
    double mouseOldY;
    double mouseDeltaX;
    double mouseDeltaY;
    
    //   private void buildScene() {
    //       root.getChildren().add(world);
    //   }
    private void buildCamera() {
        System.out.println("buildCamera()");
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(cameraXform3);
        cameraXform3.getChildren().add(camera);
        cameraXform3.setRotateZ(180.0);

        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
        cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
        cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
    }

    private void buildAxes() {
        System.out.println("buildAxes()");
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);

        final Box xAxis = new Box(AXIS_LENGTH, 1, 1);
        final Box yAxis = new Box(1, AXIS_LENGTH, 1);
        final Box zAxis = new Box(1, 1, AXIS_LENGTH);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);

        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        axisGroup.setVisible(false);
        world.getChildren().addAll(axisGroup);
    }

    private void handleMouse(Scene scene, final Node root) {
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseOldX = me.getSceneX();
                mouseOldY = me.getSceneY();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseDeltaX = (mousePosX - mouseOldX); 
                mouseDeltaY = (mousePosY - mouseOldY); 
                
                double modifier = 1.0;
                
                if (me.isControlDown()) {
                    modifier = CONTROL_MULTIPLIER;
                } 
                if (me.isShiftDown()) {
                    modifier = SHIFT_MULTIPLIER;
                }     
                if (me.isPrimaryButtonDown()) {
                    cameraXform.ry.setAngle(cameraXform.ry.getAngle() - mouseDeltaX*MOUSE_SPEED*modifier*ROTATION_SPEED);  
                    cameraXform.rx.setAngle(cameraXform.rx.getAngle() + mouseDeltaY*MOUSE_SPEED*modifier*ROTATION_SPEED);  
                }
                else if (me.isSecondaryButtonDown()) {
                    double z = camera.getTranslateZ();
                    double newZ = z + mouseDeltaX*MOUSE_SPEED*modifier;
                    camera.setTranslateZ(newZ);
                }
                else if (me.isMiddleButtonDown()) {
                    cameraXform2.t.setX(cameraXform2.t.getX() + mouseDeltaX*MOUSE_SPEED*modifier*TRACK_SPEED);  
                    cameraXform2.t.setY(cameraXform2.t.getY() + mouseDeltaY*MOUSE_SPEED*modifier*TRACK_SPEED);  
                }
            }
        });
    }
    
    private void handleKeyboard(Scene scene, final Node root) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case Z:
                        cameraXform2.t.setX(0.0);
                        cameraXform2.t.setY(0.0);
                        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
                        cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
                        cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
                        break;
                    case X:
                        axisGroup.setVisible(!axisGroup.isVisible());
                        break;
                    case V:
                        moleculeGroup.setVisible(!moleculeGroup.isVisible());
                        break;
                }
            }
        });
    }
    
    private void buildMolecule() {
        //======================================================================
        // THIS IS THE IMPORTANT MATERIAL FOR THE TUTORIAL
        //======================================================================

        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial whiteMaterial = new PhongMaterial();
        whiteMaterial.setDiffuseColor(Color.WHITE);
        whiteMaterial.setSpecularColor(Color.LIGHTBLUE);

        final PhongMaterial greyMaterial = new PhongMaterial();
        greyMaterial.setDiffuseColor(Color.DARKGREY);
        greyMaterial.setSpecularColor(Color.GREY);

        // Molecule Hierarchy
        // [*] moleculeXform
        //     [*] oxygenXform
        //         [*] oxygenSphere
        //     [*] hydrogen1SideXform
        //         [*] hydrogen1Xform
        //             [*] hydrogen1Sphere
        //         [*] bond1Cylinder
        //     [*] hydrogen2SideXform
        //         [*] hydrogen2Xform
        //             [*] hydrogen2Sphere
        //         [*] bond2Cylinder
        Xform moleculeXform = new Xform();
        Xform oxygenXform = new Xform();
        Xform hydrogen1SideXform = new Xform();
        Xform hydrogen1Xform = new Xform();
        Xform hydrogen2SideXform = new Xform();
        Xform hydrogen2Xform = new Xform();

        Sphere oxygenSphere = new Sphere(40.0);
        oxygenSphere.setMaterial(redMaterial);

        Sphere hydrogen1Sphere = new Sphere(30.0);
        hydrogen1Sphere.setMaterial(whiteMaterial);
        hydrogen1Sphere.setTranslateX(0.0);

        Sphere hydrogen2Sphere = new Sphere(30.0);
        hydrogen2Sphere.setMaterial(whiteMaterial);
        hydrogen2Sphere.setTranslateZ(0.0);

        Cylinder bond1Cylinder = new Cylinder(5, 100);
        bond1Cylinder.setMaterial(greyMaterial);
        bond1Cylinder.setTranslateX(50.0);
        bond1Cylinder.setRotationAxis(Rotate.Z_AXIS);
        bond1Cylinder.setRotate(90.0);

        Cylinder bond2Cylinder = new Cylinder(5, 100);
        bond2Cylinder.setMaterial(greyMaterial);
        bond2Cylinder.setTranslateX(50.0);
        bond2Cylinder.setRotationAxis(Rotate.Z_AXIS);
        bond2Cylinder.setRotate(90.0);

        moleculeXform.getChildren().add(oxygenXform);
        moleculeXform.getChildren().add(hydrogen1SideXform);
        moleculeXform.getChildren().add(hydrogen2SideXform);
        oxygenXform.getChildren().add(oxygenSphere);
        hydrogen1SideXform.getChildren().add(hydrogen1Xform);
        hydrogen2SideXform.getChildren().add(hydrogen2Xform);
        hydrogen1Xform.getChildren().add(hydrogen1Sphere);
        hydrogen2Xform.getChildren().add(hydrogen2Sphere);
        hydrogen1SideXform.getChildren().add(bond1Cylinder);
        hydrogen2SideXform.getChildren().add(bond2Cylinder);

        hydrogen1Xform.setTx(100.0);
        hydrogen2Xform.setTx(100.0);
        hydrogen2SideXform.setRotateY(HYDROGEN_ANGLE);

        moleculeGroup.getChildren().add(moleculeXform);

        world.getChildren().addAll(moleculeGroup);
    }

    @Override
    public void start(Stage primaryStage) {
        
       // setUserAgentStylesheet(STYLESHEET_MODENA);
        System.out.println("start()");

        root.getChildren().add(world);
        root.setDepthTest(DepthTest.ENABLE);

        // buildScene();
        buildCamera();
        buildAxes();
        buildMolecule();

        Scene scene = new Scene(root, 1024, 768, true);
        scene.setFill(Color.GREY);
        handleKeyboard(scene, world);
        handleMouse(scene, world);

        primaryStage.setTitle("Molecule Sample Application");
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.setCamera(camera);
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public static class Xform extends Group {

        public enum RotateOrder {
            XYZ, XZY, YXZ, YZX, ZXY, ZYX
        }

        public Translate t  = new Translate();
        public Translate p  = new Translate();
        public Translate ip = new Translate();
        public Rotate rx = new Rotate();
        { rx.setAxis(Rotate.X_AXIS); }
        public Rotate ry = new Rotate();
        { ry.setAxis(Rotate.Y_AXIS); }
        public Rotate rz = new Rotate();
        { rz.setAxis(Rotate.Z_AXIS); }
        public Scale s = new Scale();

        public Xform() {
            super();
            getTransforms().addAll(t, rz, ry, rx, s);
        }

        public Xform(RotateOrder rotateOrder) {
            super();
            // choose the order of rotations based on the rotateOrder
            switch (rotateOrder) {
            case XYZ:
                getTransforms().addAll(t, p, rz, ry, rx, s, ip);
                break;
            case XZY:
                getTransforms().addAll(t, p, ry, rz, rx, s, ip);
                break;
            case YXZ:
                getTransforms().addAll(t, p, rz, rx, ry, s, ip);
                break;
            case YZX:
                getTransforms().addAll(t, p, rx, rz, ry, s, ip);  // For Camera
                break;
            case ZXY:
                getTransforms().addAll(t, p, ry, rx, rz, s, ip);
                break;
            case ZYX:
                getTransforms().addAll(t, p, rx, ry, rz, s, ip);
                break;
            }
        }

        public void setTranslate(double x, double y, double z) {
            t.setX(x);
            t.setY(y);
            t.setZ(z);
        }

        public void setTranslate(double x, double y) {
            t.setX(x);
            t.setY(y);
        }

        // Cannot override these methods as they are final:
        // public void setTranslateX(double x) { t.setX(x); }
        // public void setTranslateY(double y) { t.setY(y); }
        // public void setTranslateZ(double z) { t.setZ(z); }
        // Use these methods instead:
        public void setTx(double x) { t.setX(x); }
        public void setTy(double y) { t.setY(y); }
        public void setTz(double z) { t.setZ(z); }

        public void setRotate(double x, double y, double z) {
            rx.setAngle(x);
            ry.setAngle(y);
            rz.setAngle(z);
        }

        public void setRotateX(double x) { rx.setAngle(x); }
        public void setRotateY(double y) { ry.setAngle(y); }
        public void setRotateZ(double z) { rz.setAngle(z); }
        public void setRx(double x) { rx.setAngle(x); }
        public void setRy(double y) { ry.setAngle(y); }
        public void setRz(double z) { rz.setAngle(z); }

        public void setScale(double scaleFactor) {
            s.setX(scaleFactor);
            s.setY(scaleFactor);
            s.setZ(scaleFactor);
        }

        public void setScale(double x, double y, double z) {
            s.setX(x);
            s.setY(y);
            s.setZ(z);
        }

        // Cannot override these methods as they are final:
        // public void setScaleX(double x) { s.setX(x); }
        // public void setScaleY(double y) { s.setY(y); }
        // public void setScaleZ(double z) { s.setZ(z); }
        // Use these methods instead:
        public void setSx(double x) { s.setX(x); }
        public void setSy(double y) { s.setY(y); }
        public void setSz(double z) { s.setZ(z); }

        public void setPivot(double x, double y, double z) {
            p.setX(x);
            p.setY(y);
            p.setZ(z);
            ip.setX(-x);
            ip.setY(-y);
            ip.setZ(-z);
        }

        public void reset() {
            t.setX(0.0);
            t.setY(0.0);
            t.setZ(0.0);
            rx.setAngle(0.0);
            ry.setAngle(0.0);
            rz.setAngle(0.0);
            s.setX(1.0);
            s.setY(1.0);
            s.setZ(1.0);
            p.setX(0.0);
            p.setY(0.0);
            p.setZ(0.0);
            ip.setX(0.0);
            ip.setY(0.0);
            ip.setZ(0.0);
        }

        public void resetTSP() {
            t.setX(0.0);
            t.setY(0.0);
            t.setZ(0.0);
            s.setX(1.0);
            s.setY(1.0);
            s.setZ(1.0);
            p.setX(0.0);
            p.setY(0.0);
            p.setZ(0.0);
            ip.setX(0.0);
            ip.setY(0.0);
            ip.setZ(0.0);
        }

        @Override public String toString() {
            return "Xform[t = (" +
                               t.getX() + ", " +
                               t.getY() + ", " +
                               t.getZ() + ")  " +
                               "r = (" +
                               rx.getAngle() + ", " +
                               ry.getAngle() + ", " +
                               rz.getAngle() + ")  " +
                               "s = (" +
                               s.getX() + ", " +
                               s.getY() + ", " +
                               s.getZ() + ")  " +
                               "p = (" +
                               p.getX() + ", " +
                               p.getY() + ", " +
                               p.getZ() + ")  " +
                               "ip = (" +
                               ip.getX() + ", " +
                               ip.getY() + ", " +
                               ip.getZ() + ")]";
        }
    }
}
