package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.util.Collections;
import java.util.Optional;

import java.awt.*;
import java.awt.ScrollPane;
import java.util.ArrayList;
import java.util.Observable;


public class Main extends Application {
    BorderPane borderPane;
    Button newBtn;
    Button saveBtn;
    Button loadBtn;
    Button tripStopsPlusBtn = new Button("+");
    Button tripStopsMinusBtn = new Button("-");
    Button possibleStopsPlusBtn = new Button("+");
    Button possibleStopsMinusBtn = new Button("-");
    Button updatePossibleStopsBtn = new Button("Update");
    final String FILE_PROTOCOL = "file:";
    final String IMAGES_PATH = "./images/";
    final String MAP_URL = FILE_PROTOCOL + IMAGES_PATH + "map.png";
    Image mapImage = loadImage(MAP_URL);
    ListView<Stop> tripStops = new ListView<Stop>();
    Trip newTrip = new Trip();
    GridPane gridPane;
    ScrollPane possibleStopsScroll;
    TextField cityText = new TextField();
    TextField stateText = new TextField();
    TextField latDegText = new TextField();
    TextField latMinText = new TextField();
    TextField longDegText = new TextField();
    TextField longMinText = new TextField();
    ScrollBar scrollBar;
    String fileName = "";
    boolean isLoaded = false;

    ObservableList<Stop> data1;

    ListView<Stop> possibleStops = new ListView<Stop>();
    ObservableList<Stop> data2;

    @Override
    public void start(Stage primaryStage) throws Exception{
        borderPane = new BorderPane();

        Trip.readPossibleStopsFromFile("possibleStops");

        Collections.sort(newTrip.stopList, new StopSorter());
        data1 = FXCollections.observableArrayList(newTrip.stopList);

        Collections.sort(Trip.possibleStops, new StopSorter());
        data2 = FXCollections.observableArrayList(Trip.possibleStops);

        getTopPane();
        getLeftPane();
        getRightPane();
        getBottomPane();

        possibleStops.setOnMouseClicked(e -> updateEditPossibleStops());
        updatePossibleStopsBtn.setOnAction(e -> updateStopInPossibleStops());
        possibleStopsPlusBtn.setOnAction(e -> addPossibleStops());
        possibleStopsMinusBtn.setOnAction(e -> deletePossibleStops());
        tripStopsPlusBtn.setOnAction(e -> addStopToTrip());
        tripStopsMinusBtn.setOnAction(e -> deleteStopFromTrip());
        newBtn.setOnAction(e -> createNewTrip());
        saveBtn.setOnAction(e -> saveTrip());
        loadBtn.setOnAction(e -> loadTrip());


        primaryStage.setTitle("Trip Planner - Criss Cross Tour");
        primaryStage.setScene(new Scene(borderPane, 870, 1000));
        primaryStage.show();
    }

    public void loadTrip(){
        Stage newStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        File loadedFile = fileChooser.showOpenDialog(newStage);
        fileName = loadedFile.getName();
        newTrip = new Trip();
        newTrip.readTripFromFile(loadedFile.getAbsolutePath());
        
        data1 = FXCollections.observableArrayList(newTrip.stopList);
        isLoaded = true;
        getRightPane();
        getLeftPane();
    }


    public void saveTrip(){
        if(isLoaded) {

            Stage newStage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName(fileName);
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT Files", "*.txt"));
            File savedFile = fileChooser.showSaveDialog(newStage);
            newStage.show();
            if (savedFile != null) {
                newTrip.writeToFile(savedFile.getAbsolutePath());
                Trip.writePossibleTripsToFile("possibleStops");
            }

        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setContentText("A trip has not been loaded or created yet");
            alert.showAndWait();
        }
    }


    public void createNewTrip(){
        TextInputDialog dialog = new TextInputDialog("Trip Name");
        dialog.setTitle("New Trip");
        dialog.setContentText("Please enter the name of your new trip: ");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(tripName -> {
            fileName = tripName;
            newTrip = new Trip();
            data1 = FXCollections.observableArrayList(newTrip.stopList);
            getLeftPane();
            getRightPane();
            getBottomPane();
            getLeftPane();
        });

        isLoaded = true;
    }

    public void deleteStopFromTrip() {
        if (isLoaded) {

            Stop deletedStop = tripStops.getSelectionModel().getSelectedItem();
            for (int i = 0; i < newTrip.stopList.size(); i++) {
                if (deletedStop.getCity().equalsIgnoreCase(newTrip.stopList.get(i).getCity())) {
                    newTrip.stopList.remove(i);
                }
            }

            data1 = FXCollections.observableArrayList(newTrip.stopList);
            getRightPane();
            getLeftPane();

        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setContentText("A trip has not been loaded or created yet");
            alert.showAndWait();
        }
    }

    public void addStopToTrip(){
        if(isLoaded) {
            boolean alreadyExists = false;
            Stop newStop = possibleStops.getSelectionModel().getSelectedItem();
            for(int i = 0; i < newTrip.stopList.size(); i++){
                if(newStop.getCity().equalsIgnoreCase(newTrip.stopList.get(i).getCity())){
                    alreadyExists = true;
                }
            }

            if(!alreadyExists) {
                newTrip.stopList.add(newStop);
                data1 = FXCollections.observableArrayList(newTrip.stopList);
                getRightPane();
                getLeftPane();
            } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setContentText("This stop already exists in this trip");
                alert.showAndWait();
            }


        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setContentText("A trip has not been loaded or created yet");
            alert.showAndWait();
        }

    }


    public void deletePossibleStops(){
        Stop stop = possibleStops.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Are you sure you delete this stop?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){

            for(int i = 0; i < Trip.possibleStops.size(); i++){
                if(stop.getCity().equalsIgnoreCase(Trip.possibleStops.get(i).getCity())){
                    Trip.possibleStops.remove(i);
                }
            }

            cityText.setText("");
            stateText.setText("");
            latDegText.setText("");
            latMinText.setText("");
            longDegText.setText("");
            longMinText.setText("");

            Collections.sort(Trip.possibleStops, new StopSorter());
            data2 = FXCollections.observableArrayList(Trip.possibleStops);
            getBottomPane();
            getLeftPane();

        } else {

        }


    }

    public void addPossibleStops(){
        boolean alreadyExists = false;
        for(int i = 0; i < Trip.possibleStops.size(); i++){
            if(Trip.possibleStops.get(i).getCity().equalsIgnoreCase(cityText.getText())){
                alreadyExists = true;
            }
        }

        if(!alreadyExists) {
            String city = cityText.getText();
            String state = stateText.getText();
            int latDeg = Integer.parseInt(latDegText.getText());
            int latMin = Integer.parseInt(latMinText.getText());
            int longDeg = Integer.parseInt(longDegText.getText());
            int longMin = Integer.parseInt(longMinText.getText());
            Trip.possibleStops.add(new Stop(city, state, latDeg, latMin, longDeg, longMin));
            Collections.sort(Trip.possibleStops, new StopSorter());
            data2 = FXCollections.observableArrayList(Trip.possibleStops);
            getBottomPane();
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setContentText("This city already exists in the possible stops");
            alert.showAndWait();
        }
    }


    public void updateStopInPossibleStops() {
        if (possibleStops.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setContentText("No possible stop has been selected");
            alert.showAndWait();
        } else {
            Stop stop = possibleStops.getSelectionModel().getSelectedItem();
            for (int i = 0; i < Trip.possibleStops.size(); i++) {
                if (Trip.possibleStops.get(i).getCity().equalsIgnoreCase(stop.getCity())) {
                    Trip.possibleStops.get(i).setCity(cityText.getText());
                    Trip.possibleStops.get(i).setState(stateText.getText());
                    Trip.possibleStops.get(i).setLatDeg(Integer.parseInt(latDegText.getText()));
                    Trip.possibleStops.get(i).setLatMin(Integer.parseInt(latMinText.getText()));
                    Trip.possibleStops.get(i).setLongDeg(Integer.parseInt(longDegText.getText()));
                    Trip.possibleStops.get(i).setLongMin(Integer.parseInt(longMinText.getText()));
                }
            }
        }

        getBottomPane();
    }


    public void updateEditPossibleStops() {

            Stop stop = possibleStops.getSelectionModel().getSelectedItem();
            cityText.setText(stop.getCity());
            stateText.setText(stop.getState());
            latDegText.setText(stop.getLatDeg() + "");
            latMinText.setText(stop.getLatMin() + "");
            longDegText.setText(stop.getLongDeg() + "");
            longMinText.setText(stop.getLongMin() + "");

    }


    private Image loadImage(String imageFileURL) {
        Image image = new Image(imageFileURL);
        if (!image.isError()) {
            return image;
        }
        else
            return null;
    }

    public static void main(String[] args) {
        launch(args);
    }



    private void getTopPane(){

        HBox hBox = new HBox();

        newBtn = new Button("New");
        newBtn.prefWidthProperty().bind(borderPane.widthProperty().divide(7));
        saveBtn = new Button("Save");
        saveBtn.prefWidthProperty().bind(borderPane.widthProperty().divide(7));
        loadBtn = new Button("Load");
        loadBtn.prefWidthProperty().bind(borderPane.widthProperty().divide(7));

        hBox.setSpacing(5);

        hBox.setStyle("-fx-border-style: solid;" +
                        "-fx-border-color: black;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-padding: 10px 10px 10px 10px;");
        hBox.getChildren().addAll(newBtn, saveBtn,loadBtn);

        borderPane.setTop(hBox);

    }

    public void getLeftPane(){
        AnchorPane anchorPane = new AnchorPane();
        ImageView mapImageView = new ImageView();
        mapImageView.setImage(mapImage);
        mapImageView.setPreserveRatio(true);
        mapImageView.setFitWidth(600);
        mapImageView.setFitHeight(400);

//        mapImageView.fitHeightProperty().bind(borderPane.heightProperty().divide(2));
//        mapImageView.fitWidthProperty().bind(borderPane.widthProperty().divide(1.5));
        anchorPane.getChildren().add(mapImageView);
        for(int i = 0; i < newTrip.stopList.size(); i++){
            double pixelsPerLong = mapImageView.boundsInParentProperty().get().getWidth() / (125.0-65.0);
            double pixelsPerLat = mapImageView.boundsInLocalProperty().get().getHeight() / (50.0-25.0);
            double y = (mapImageView.boundsInLocalProperty().get().getHeight() - ((newTrip.stopList.get(i).getLatDeg() - 25)  * pixelsPerLat));
            double x = (mapImageView.boundsInParentProperty().get().getWidth() - ((newTrip.stopList.get(i).getLongDeg() - 65) * pixelsPerLong));
            Circle newCircle = new Circle();
            newCircle.setCenterX(x);
            newCircle.setCenterY(y);
            newCircle.setRadius(2);
            anchorPane.getChildren().addAll(newCircle);
        }

        for(int i = 0; i < newTrip.stopList.size() - 1; i++){
            int j = i + 1;
            double pixelsPerLong = mapImageView.boundsInParentProperty().get().getWidth() / (125.0-65.0);
            double pixelsPerLat = mapImageView.boundsInLocalProperty().get().getHeight() / (50.0-25.0);
            double startX = (mapImageView.boundsInParentProperty().get().getWidth() - ((newTrip.stopList.get(i).getLongDeg() - 65) * pixelsPerLong));
            double startY = (mapImageView.boundsInLocalProperty().get().getHeight() - ((newTrip.stopList.get(i).getLatDeg() - 25)  * pixelsPerLat));
            double endX = (mapImageView.boundsInParentProperty().get().getWidth() - ((newTrip.stopList.get(j).getLongDeg() - 65) * pixelsPerLong));
            double endY = (mapImageView.boundsInLocalProperty().get().getHeight() - ((newTrip.stopList.get(j).getLatDeg() - 25)  * pixelsPerLat));
            Line line = new Line(startX, startY, endX, endY);
            anchorPane.getChildren().addAll(line);
        }

        borderPane.setLeft(anchorPane);
    }

    public void getRightPane(){
        HBox tripStopsHbox = new HBox();
        Text text = new Text("Trip Stops");
        tripStopsHbox.setSpacing(5);
        tripStopsHbox.getChildren().addAll(text, tripStopsPlusBtn, tripStopsMinusBtn);
        VBox tripStopsVbox = new VBox();
        Text distanceText = new Text("Total Milage:   "  + newTrip.calculateDistance());
        tripStopsVbox.getChildren().addAll(tripStopsHbox, tripStops, distanceText);
        tripStops.setItems(data1);
        tripStops.setCellFactory(new Callback<ListView<Stop>, ListCell<Stop>>() {
            @Override
            public ListCell<Stop> call(ListView<Stop> param) {
                return new CustomCell<>();
            }
        });

        tripStopsVbox.prefHeightProperty().bind(borderPane.prefHeightProperty().divide(5));
        tripStopsVbox.setStyle("-fx-border-style: solid;" +
                "-fx-border-color: black;" +
                "-fx-border-radius: 10px;" +
                "-fx-padding: 5px 5px 5px 5px;");
        borderPane.setRight(tripStopsVbox);

    }

    public void getBottomPane(){
        VBox possibleStopsVbox = new VBox();
        Text text = new Text("Possible stops");
        possibleStops.setItems(data2);
        possibleStops.setCellFactory(new Callback<ListView<Stop>, ListCell<Stop>>() {
            @Override
            public ListCell<Stop> call(ListView<Stop> param) {
                return new CustomCell();
            }
        });

        HBox hBox = new HBox();

        possibleStopsVbox.getChildren().addAll(text, possibleStops);
        possibleStopsVbox.setStyle("-fx-padding: 10px;");

        HBox addPossibleCityBtns = new HBox();


        addPossibleCityBtns.getChildren().addAll(possibleStopsPlusBtn, possibleStopsMinusBtn);
        VBox editPossibleStops = new VBox();

        gridPane = new GridPane();
        gridPane.add(new Text("City: "), 0,0);
        gridPane.add(cityText, 1, 0);
        gridPane.add(new Text("State: "), 0, 1);
        gridPane.add(stateText, 1, 1);
        gridPane.add(new Text("Latitude Degrees: "), 0, 2);
        gridPane.add(latDegText, 1, 2);
        gridPane.add(new Text("Latitude Minutes: "),  0, 3);
        gridPane.add(latMinText, 1, 3);
        gridPane.add(new Text("Longitude Degrees: "), 0, 4);
        gridPane.add(longDegText, 1, 4);
        gridPane.add(new Text("Longitude Minutes: "), 0, 5);
        gridPane.add(longMinText, 1, 5);


        gridPane.add(updatePossibleStopsBtn, 0, 6);

        gridPane.setStyle("-fx-border-style: solid;" +
                "-fx-border-color: black;" +
                "-fx-border-radius: 10px;" +
                "-fx-padding: 10px;");

        editPossibleStops.getChildren().addAll(addPossibleCityBtns, gridPane);

        editPossibleStops.setStyle("-fx-padding: 10px;");

        HBox possibleStopsHbox = new HBox();
        possibleStopsHbox.getChildren().addAll(possibleStopsVbox, editPossibleStops);

        possibleStopsHbox.setStyle("-fx-border-style: solid;" +
                                    "-fx-border-color: black;" +
                                    "-fx-border-radius: 10px;" +
                                    "-fx-padding: 10px;");

        borderPane.setBottom(possibleStopsHbox);

    }

}