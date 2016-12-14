package com.leo.test.javaFX.test;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

/**
 * Created by Senchenko Victor on 01.12.2016.
 */
public class TryForm extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("TryForm");
        primaryStage.setScene(scene());
        primaryStage.setHeight(800);
        primaryStage.setWidth(1200);
        primaryStage.show();
    }

    private Scene scene() {
        Label label = new Label("addRow");
        CheckBox checkBox = new CheckBox("Start TryForm");
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                startControl();
            else
                stopControl();
        });
        TableView<String> tableSensors = new TableView();
        tableSensors.getColumns().addAll(new TableColumn<String, String>("name"));
        Button requestSensors = new Button("Sensors Status");
        requestSensors.setOnMouseReleased(event -> requestSensors(tableSensors));
        TableView<Device> tableDevices = new TableView();
        Button requestDevices = new Button("Devices Status");
        requestDevices.setOnMouseReleased(event -> requestDevices(tableDevices));
        ComboBox<String> statusesComboBox = new ComboBox(statuses());
        ComboBox<String> devicesComboBox = new ComboBox(devices());
        Button sendStatus = new Button("Send Status");
        sendStatus.disableProperty().bind(Bindings.or(Bindings.isNull(statusesComboBox.valueProperty()), Bindings.isNull(devicesComboBox.valueProperty())));
        sendStatus.setOnMousePressed(event -> sendStatus(devicesComboBox.valueProperty().get(), statusesComboBox.valueProperty().get()));
        TilePane statusPane = new TilePane(10, 10, statusesComboBox, devicesComboBox, sendStatus);
        FlowPane devices = new FlowPane(10, 10, tableDevices, requestDevices);
        devices.setOrientation(Orientation.VERTICAL);
        FlowPane sensors = new FlowPane(10, 10, tableSensors, requestSensors);
        sensors.setOrientation(Orientation.VERTICAL);
        FlowPane pane = new FlowPane(10, 10, checkBox, devices, sensors, statusPane);
        pane.setOrientation(Orientation.HORIZONTAL);
        pane.setStyle("-fx-font-size: 15pt");
        pane.setAlignment(Pos.CENTER);
        Scene scene = new Scene(pane);
        return scene;
    }

    private void requestDevices(TableView tableDevices) {
        System.out.println("here");
        tableDevices.setItems(devices());
    }

    private void requestSensors(TableView tableSensors) {
        System.out.println("here");
        tableSensors.setItems(statuses());
    }

    private ObservableList<String> devices() {
        return FXCollections.observableArrayList("device 1", "device 2", "device 3");
    }

    private ObservableList<String> statuses() {
        return FXCollections.observableArrayList("Option 1", "Option 2", "Option 3");
    }

    private void sendStatus(Object device, Object status) {
        System.out.println("Sended Status " + status + " on " + device);
    }

    private void stopControl() {
        System.out.println("stop control");
    }

    private void startControl() {
        System.out.println("start control");
    }
    private class Device{
        String name;
        String status;
    }
    private class Sensor{
        String name;
        double value;
    }
}
