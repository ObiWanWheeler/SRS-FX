package com.obiwanwheeler.fxmlControllers;

import com.obiwanwheeler.App;
import com.obiwanwheeler.CardFront;
import com.obiwanwheeler.utilities.Alerts;
import com.obiwanwheeler.utilities.FileExtensions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class BasicCardFrontController extends CardFront
{

    @FXML private void onShowAnswerButtonPressed(ActionEvent actionEvent) throws IOException {
        //switch to back side
        gotoBack(actionEvent);
    }

    private void gotoBack(ActionEvent actionEvent) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        URL path = App.class.getResource("fxmls/basicCardBack.fxml");

        if (path == null) {
            Alerts.giveLoadFailureAlert();
            return;
        }

        System.out.println(getClass());
        loader.setLocation(path);

        Parent cardBackParent = loader.load();

        BasicCardBackController basicCardBackController = loader.getController();
        basicCardBackController.initialiseCardGUI(cardToReview);

        Stage currentStage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.getScene().setRoot(cardBackParent);
    }
}
