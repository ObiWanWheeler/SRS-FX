package com.obiwanwheeler;

import com.obiwanwheeler.creators.Creator;
import com.obiwanwheeler.creators.OptionGroupCreator;
import com.obiwanwheeler.utilities.FileExtensions;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxmls/reviewStart.fxml"));

        stage.setTitle("Wurmwell SRS");
        stage.setScene(new Scene(root));stage.show();
    }

    public static void changeSceneOnWindow(Stage targetStage, String targetFXML){
        try {
            Parent cardFrontParent = FXMLLoader.load(App.class.getResource("fxmls/" + targetFXML + FileExtensions.FXML));
            Scene cardFront = new Scene(cardFrontParent);
            targetStage.setScene(cardFront);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        if (Reviewer.tryInitialiseReview()){
            launch(args);
        }
    }
}
