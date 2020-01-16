package sample;

import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CustomCell<Stop> extends ListCell<Stop>{

    @Override
    public void updateItem(Stop item, boolean empty){
        super.updateItem(item, empty);
        if(item != null) {
            this.setText(this.getIndex() + ": " + item.toString());
            this.setStyle("-fx-control-inner-background: derive(yellow, 50%);" +
                            "-fx-border-style: solid;" +
                            "-fx-border-color: black;" +
                            "-fx-border-radius: 7px;" +
                            "-fx-focus-color: transparent;");
        }
    }

}

