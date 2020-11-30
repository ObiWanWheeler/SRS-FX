package com.obiwanwheeler;

import com.obiwanwheeler.utilities.*;
import com.obiwanwheeler.objects.Card;
import com.obiwanwheeler.objects.Deck;

import java.io.IOException;
import java.util.*;

public class Reviewer {

    private final Scanner scanner = new Scanner(System.in);
    private static String deckFilePath;
    public static String getDeckFilePath() {
        return deckFilePath;
    }
    private static int numberOfCardsLeftToBeReviewed;
    private static List<Card> cardsToReviewToday = new LinkedList<>();
    public static List<Card> getCardsToReviewToday() {
        return cardsToReviewToday;
    }
    private static Deck updatedDeck;
    public static Deck getUpdatedDeck() {
        return updatedDeck;
    }
    private static IntervalHandler intervalHandler;

    //region initialise review
    public static boolean tryInitialiseReview(){
        deckFilePath = DeckFileParser.DECK_FOLDER_PATH + "testDeck" + FileExtensions.JSON;
        Deck deckToReview = DeckFileParser.DECK_FILE_PARSER_SINGLETON.deserializeDeck(deckFilePath);
        if (deckToReview == null){
            return false;
        }
        initialiseUpdatedDeck(deckToReview);
        insertUnchangedCards(deckToReview);

        intervalHandler = new IntervalHandler(deckToReview.getOptionGroup());

        cardsToReviewToday = DeckManipulator.DECK_MANIPULATOR_SINGLETON.getCardsToReviewToday(deckToReview);
        numberOfCardsLeftToBeReviewed = cardsToReviewToday.size();
        return true;
    }

    private static void initialiseUpdatedDeck(Deck sourceDeck){
        updatedDeck = new Deck(new LinkedList<>());
        updatedDeck.setDeckName(sourceDeck.getDeckName());
        updatedDeck.setOptionGroupFilePath(sourceDeck.getOptionGroupFilePath());
    }

    private static void insertUnchangedCards(Deck sourceDeck){
        List<Card> unchangedCards = DeckManipulator.DECK_MANIPULATOR_SINGLETON.getCardsNotBeingReviewedToday(sourceDeck);
        updatedDeck.getCards().addAll(unchangedCards);
    }
    //endregion

    public static void processCardMarkedBad(Card markedCard){
        if (markedCard.getState() == Card.CardState.LEARNT){
            intervalHandler.relearnCard(markedCard);
        }
        else {
            intervalHandler.decreaseInterval(markedCard);
        }
    }

    public static void processCardMarkedGood(Card markedCard){
        intervalHandler.increaseInterval(markedCard);

        if (checkIfCardIsFinishedForSession(markedCard)){
            finishReviewingCardForSession(markedCard);
        }
    }

    public static boolean sessionIsFinished(){
        return numberOfCardsLeftToBeReviewed == 0;
    }

    private static boolean checkIfCardIsFinishedForSession(Card cardToCheck){
        return cardToCheck.getState() == Card.CardState.LEARNT;
    }

    private static void finishReviewingCardForSession(Card finishedCard){
        cardsToReviewToday.remove(finishedCard);
        numberOfCardsLeftToBeReviewed--;
        updatedDeck.getCards().add(finishedCard);
    }
}
