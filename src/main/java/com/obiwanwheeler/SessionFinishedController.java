package com.obiwanwheeler;

import com.obiwanwheeler.utilities.Serializer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SessionFinishedController implements Initializable {

    @FXML private void onBackButtonPressed(ActionEvent actionEvent){
        Stage currentStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        App.changeSceneOnWindow(currentStage, "mainMenu");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Serializer.SERIALIZER_SINGLETON.serializeToExisting(Reviewer.getDeckFilePath(), Reviewer.getUpdatedDeck());
    }
}
