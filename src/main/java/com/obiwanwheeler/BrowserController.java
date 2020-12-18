package com.obiwanwheeler;

import com.obiwanwheeler.objects.Card;
import com.obiwanwheeler.objects.Deck;
import com.obiwanwheeler.utilities.DeckFileParser;
import com.obiwanwheeler.utilities.FileExtensions;
import com.obiwanwheeler.utilities.Serializer;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class BrowserController implements Initializable {
    @FXML private ListView<String> decksListView;
    @FXML private TableView<Card> deckTableView;
    @FXML private TableColumn<Card, String> frontColumn;
    @FXML private TableColumn<Card, String> backColumn;
    @FXML private TableColumn<Card, Card.CardState> stateColumn;
    @FXML private TableColumn<Card, LocalDate> nextDateColumn;
    @FXML private TextArea frontTextArea;
    @FXML private TextArea backTextArea;
    @FXML private ImageView imageView;
    private Deck selectedDeck;
    private Card selectedCard;

    private void populateDeckList(){
        for (String deck : DeckFileParser.getAlLDeckNames()) {
            decksListView.getItems().add(deck.replace(".json", ""));
        }

        decksListView.getSelectionModel().selectedItemProperty().addListener((selectedItem, s, t1) -> {
            updateEditedCard();
            refreshDeckTable(selectedItem);
        });
    }

    private void refreshDeckTable(ObservableValue<? extends String> selectedDeck) {
        this.selectedDeck = DeckFileParser.deserializeDeck(DeckFileParser.DECK_FOLDER_PATH + selectedDeck.getValue() + FileExtensions.JSON);
        deckTableView.getItems().clear();
        ObservableList<Card> cardsObservableList = FXCollections.observableArrayList();
        cardsObservableList.addAll(this.selectedDeck.getCards());
        deckTableView.setItems(cardsObservableList);
    }

    private void onCardSelected(ObservableValue<? extends Card> observableCard){
        updateEditedCard();
        if (observableCard.getValue() != null){
            selectedCard = observableCard.getValue();
            setUI();
        }
    }

    private void setUI() {
        frontTextArea.setText(selectedCard.getTargetLanguageSentence());
        backTextArea.setText(selectedCard.getNativeLanguageTranslation());
        String imagePath = selectedCard.getImagePath();
        if (imagePath != null){
            File imageFile = new File(imagePath);
            Image image = new Image(imageFile.toURI().toString());
            imageView.setImage(image);
        }
        else {
            imageView.setImage(null);
        }
    }

    @FXML private void onSelectImageButtonPressed(){
        if (selectedCard == null)
            return;

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*jpeg"));

        File selectedImage = fileChooser.showOpenDialog(null);

        Image image = new Image(selectedImage.toURI().toString());
        imageView.setImage(image);
        updateEditedCard();
    }

    private void updateEditedCard(){
        if (selectedCard != null && selectedDeck != null){
            selectedCard.setTargetLanguageSentence(frontTextArea.getText());
            selectedCard.setNativeLanguageTranslation(backTextArea.getText());
            Image currentImage = imageView.getImage();
            if (currentImage != null){
                String imagePath = currentImage.getUrl();
                selectedCard.setImagePath(imagePath.substring(imagePath.indexOf("/") + 1));
                Serializer.SERIALIZER_SINGLETON.serializeToExisting(DeckFileParser.DECK_FOLDER_PATH + selectedDeck.getName() + FileExtensions.JSON, selectedDeck);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        frontColumn.setCellValueFactory(new PropertyValueFactory<>("targetLanguageSentence"));
        backColumn.setCellValueFactory(new PropertyValueFactory<>("nativeLanguageTranslation"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        nextDateColumn.setCellValueFactory(new PropertyValueFactory<>("nextReviewDate"));
        deckTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, card, t1) -> onCardSelected(observableValue));
        populateDeckList();
    }
}
