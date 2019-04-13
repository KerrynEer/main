package seedu.knowitall.ui;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import seedu.knowitall.model.card.Card;

/**
 * An UI component when user enters a test session.
 */
public class TestSession extends UiPart<Region> {

    private static final String FXML = "TestSession.fxml";
    private static final String MESSAGE_CORRECT_ANSWER = "\"Correct answer, good job!\"";
    private static final String MESSAGE_WRONG_ANSWER = "\"Wrong answer, better luck next time!\"";
    private static final String MESSAGE_REVEAL_ANSWER = "\"Answer revealed, you can do better!\"";

    private Card cardToTest;

    @FXML
    private GridPane testSessionPage;
    @FXML
    private Label testCardQuestion;
    @FXML
    private Label testCardHint;
    @FXML
    private Label testCardAnswer;
    @FXML
    private Label testCardOptions;
    @FXML
    private Label testMessage;

    public TestSession() {
        super(FXML);
    }
    public TestSession(Card cardToTest) {
        super(FXML);
        this.cardToTest = cardToTest;
        displayCard(cardToTest);
    }

    /**
     * Updates the UI to show test session page with the question of the specified card
     * @param cardToTest card to be tested in this page of test session
     */
    public void displayCard(Card cardToTest) {
        testSessionPage.getChildren().clear();
        testCardQuestion.setText(cardToTest.getQuestion().fullQuestion);
        testCardHint.setText("");
        if (!cardToTest.getHints().isEmpty() && cardToTest.getHints().size() <= 1) {
            System.out.println("got hint");
            cardToTest.getHints().forEach(hintVal -> testCardHint.setText("Hint: " + hintVal.hintName));
        }
        testCardOptions.setText("");
        if (cardToTest.getCardType() == Card.CardType.MCQ) {
            cardToTest.shuffleMcqOptions();
            List<String> completeOptions = cardToTest.getCompleteMcqOptions();
            for (int i = 1; i <= completeOptions.size(); i++) {
                testCardOptions.setText(testCardOptions.getText() + i + ") " + completeOptions.get(i - 1) + "\n");
            }
        }
        testCardAnswer.setText("Correct answer:\n" + cardToTest.getAnswer().fullAnswer);
        testSessionPage.getChildren().addAll(testCardQuestion, testCardHint, testCardOptions);
    }

    /**
     * Updates the UI to show the answer for a correctly answered card.
     */
    public void handleCorrectAnswer() {
        testSessionPage.setStyle("-fx-background-color: #90ee90;");
        testMessage.setText(MESSAGE_CORRECT_ANSWER);
        testSessionPage.getChildren().addAll(testCardAnswer, testMessage);
    }

    /**
     * Updates the UI to show the answer for a wrongly answered card.
     */
    public void handleWrongAnswer() {
        testSessionPage.setStyle("-fx-background-color: #ef6262;");
        testMessage.setText(MESSAGE_WRONG_ANSWER);
        testSessionPage.getChildren().addAll(testCardAnswer, testMessage);
    }

    /**
     * Updates the UI to show the answer for a request to reveal the answer to the current card.
     */
    public void handleRevealAnswer() {
        testSessionPage.setStyle("-fx-background-color: #ffde64 ;");
        testMessage.setText(MESSAGE_REVEAL_ANSWER);
        testSessionPage.getChildren().addAll(testCardAnswer, testMessage);
    }
}
