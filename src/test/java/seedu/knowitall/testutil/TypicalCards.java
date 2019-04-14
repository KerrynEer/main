package seedu.knowitall.testutil;

import static seedu.knowitall.logic.commands.CommandTestUtil.VALID_ANSWER_1;
import static seedu.knowitall.logic.commands.CommandTestUtil.VALID_ANSWER_2;
import static seedu.knowitall.logic.commands.CommandTestUtil.VALID_HINT_FRIEND;
import static seedu.knowitall.logic.commands.CommandTestUtil.VALID_HINT_HUSBAND;
import static seedu.knowitall.logic.commands.CommandTestUtil.VALID_QUESTION_1;
import static seedu.knowitall.logic.commands.CommandTestUtil.VALID_QUESTION_2;
import static seedu.knowitall.logic.commands.CommandTestUtil.VALID_SCORE_1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import seedu.knowitall.model.CardFolder;
import seedu.knowitall.model.ReadOnlyCardFolder;
import seedu.knowitall.model.card.Card;

/**
 * A utility class containing a list of {@code Card} objects to be used in tests.
 */
public class TypicalCards {

    public static final Card ALICE = new CardBuilder().withQuestion("Alice Pauline").withAnswer("94351253")
            .withScore("0/0").withHint("friends").build();
    public static final Card BENSON = new CardBuilder().withQuestion("Benson Meier").withAnswer("98765432")
            .withScore("0/0").withHint("owesMoney").build();
    public static final Card CARL = new CardBuilder().withQuestion("Carl Kurz").withAnswer("95352563").withScore("0/0")
            .build();
    public static final Card DANIEL = new CardBuilder().withQuestion("Daniel Meier").withAnswer("87652533")
            .withScore("0/0").withHint("friends").build();
    public static final Card ELLE = new CardBuilder().withQuestion("Elle Meyer").withAnswer("9482224").withScore("0/0")
            .build();
    public static final Card FIONA = new CardBuilder().withQuestion("Fiona Kunz").withAnswer("9482427").withScore("2/3")
            .build();
    public static final Card GEORGE = new CardBuilder().withQuestion("George Best").withAnswer("9482442")
            .withScore("1/2").withOptions("an answer").build();

    // Manually added
    public static final Card HOON = new CardBuilder().withQuestion("Hoon Meier").withAnswer("8482424").withScore("0/0")
            .build();
    public static final Card IDA = new CardBuilder().withQuestion("Ida Mueller").withAnswer("8482131").withScore("0/0")
            .build();

    // Manually added - Card's details found in {@code CommandTestUtil}
    public static final Card CARD_1 = new CardBuilder().withQuestion(VALID_QUESTION_1).withAnswer(VALID_ANSWER_1)
            .withScore(VALID_SCORE_1).withHint(VALID_HINT_FRIEND).build();
    public static final Card CARD_2 = new CardBuilder().withQuestion(VALID_QUESTION_2).withAnswer(VALID_ANSWER_2)
            .withScore("0/0").withHint(VALID_HINT_HUSBAND).build();

    public static final String TYPICAL_FOLDER_ONE_NAME = "Typical Folder 1";

    public static final String TYPICAL_FOLDER_TWO_NAME = "Typical Folder 2";

    public static final String EMPTY_FOLDER_NAME = "Empty Folder";

    public static final List<Double> TYPICAL_FOLDER_SCORES = new ArrayList<>(Arrays.asList(0.5));

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalCards() {} // prevents instantiation

    public static List<ReadOnlyCardFolder> getTypicalFolderOneAsList() {
        return Collections.singletonList(getTypicalFolderOne());
    }

    /**
     * Returns a {@code CardFolder} with the cards from Typical Folder One.
     */
    public static CardFolder getTypicalFolderOne() {
        CardFolder folder = new CardFolder(getTypicalFolderOneName());
        folder.setFolderScores(TYPICAL_FOLDER_SCORES);
        for (Card card : getTypicalFolderOneCards()) {
            folder.addCard(card);
        }
        return folder;
    }

    /**
     * Returns a {@code CardFolder} with the cards from Typical Folder Two.
     */
    public static CardFolder getTypicalFolderTwo() {
        CardFolder folder = new CardFolder(getTypicalFolderTwoName());
        folder.setFolderScores(TYPICAL_FOLDER_SCORES);
        for (Card card : getTypicalFolderTwoCards()) {
            folder.addCard(card);
        }
        return folder;
    }

    public static CardFolder getEmptyCardFolder() {
        return new CardFolder(EMPTY_FOLDER_NAME);
    }

    public static List<Card> getTypicalFolderOneCards() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }

    public static List<Card> getTypicalFolderTwoCards() {
        return new ArrayList<>(Arrays.asList(ALICE, CARL, ELLE, GEORGE));
    }

    public static String getTypicalFolderOneName() {
        return TYPICAL_FOLDER_ONE_NAME;
    }

    public static String getTypicalFolderTwoName() {
        return TYPICAL_FOLDER_TWO_NAME;
    }

}
