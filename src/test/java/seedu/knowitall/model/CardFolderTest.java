package seedu.knowitall.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.knowitall.logic.commands.CommandTestUtil.VALID_HINT_HUSBAND;
import static seedu.knowitall.testutil.TypicalCards.ALICE;
import static seedu.knowitall.testutil.TypicalCards.TYPICAL_FOLDER_SCORES;
import static seedu.knowitall.testutil.TypicalCards.getTypicalFolderOne;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.knowitall.model.card.Card;
import seedu.knowitall.model.card.exceptions.DuplicateCardException;
import seedu.knowitall.testutil.CardBuilder;

public class CardFolderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final CardFolder cardFolder = new CardFolder(this.getClass().getName());

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), cardFolder.getCardList());

        CardFolder newData = getTypicalFolderOne();
        cardFolder.resetData(newData);
        assertEquals(newData.getCardList(), cardFolder.getCardList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        cardFolder.resetData(null);
    }

    @Test
    public void resetData_withValidReadOnlyCardFolder_replacesData() {
        CardFolder newData = getTypicalFolderOne();
        cardFolder.resetData(newData);
        assertEquals(newData.getCardList(), cardFolder.getCardList());
    }

    @Test
    public void resetData_withValidReadOnlyCardFolder_doesNotReplaceName() {
        CardFolder newData = getTypicalFolderOne();
        cardFolder.resetData(newData);
        assertNotEquals(newData, cardFolder);
    }

    @Test
    public void resetData_withDuplicateCards_throwsDuplicateCardException() {
        // Two cards with the same identity fields
        Card editedAlice = new CardBuilder(ALICE).withHint(VALID_HINT_HUSBAND)
                .build();
        List<Card> newCards = Arrays.asList(ALICE, editedAlice);
        CardFolderStub newData = new CardFolderStub(newCards);

        thrown.expect(DuplicateCardException.class);
        cardFolder.resetData(newData);
    }

    @Test
    public void hasCard_nullCard_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        cardFolder.hasCard(null);
    }

    @Test
    public void hasCard_cardNotInCardFolder_returnsFalse() {
        assertFalse(cardFolder.hasCard(ALICE));
    }

    @Test
    public void hasCard_cardInCardFolder_returnsTrue() {
        cardFolder.addCard(ALICE);
        assertTrue(cardFolder.hasCard(ALICE));
    }

    @Test
    public void hasCard_cardWithSameIdentityFieldsInCardFolder_returnsTrue() {
        cardFolder.addCard(ALICE);
        Card editedAlice = new CardBuilder(ALICE).withHint(VALID_HINT_HUSBAND)
                .build();
        assertTrue(cardFolder.hasCard(editedAlice));
    }

    @Test
    public void getCardList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        cardFolder.getCardList().remove(0);
    }

    @Test
    public void setFolderScores_scoresSet() {
        cardFolder.setFolderScores(TYPICAL_FOLDER_SCORES);
        assertEquals(TYPICAL_FOLDER_SCORES, cardFolder.getFolderScores());
    }

    @Test
    public void addFolderScore_scoreAdded() {
        cardFolder.addFolderScore(0.5);
        List<Double> newFolderScores = new ArrayList<>(Arrays.asList(0.5));
        assertEquals(newFolderScores, cardFolder.getFolderScores());
    }

    @Test
    public void addListener_withInvalidationListener_listenerAdded() {
        SimpleIntegerProperty counter = new SimpleIntegerProperty();
        InvalidationListener listener = observable -> counter.set(counter.get() + 1);
        cardFolder.addListener(listener);
        cardFolder.addCard(ALICE);
        assertEquals(1, counter.get());
    }

    @Test
    public void removeListener_withInvalidationListener_listenerRemoved() {
        SimpleIntegerProperty counter = new SimpleIntegerProperty();
        InvalidationListener listener = observable -> counter.set(counter.get() + 1);
        cardFolder.addListener(listener);
        cardFolder.removeListener(listener);
        cardFolder.addCard(ALICE);
        assertEquals(0, counter.get());
    }

    /**
     * A stub ReadOnlyCardFolder whose cards list can violate interface constraints.
     */
    private static class CardFolderStub implements ReadOnlyCardFolder {
        private final ObservableList<Card> cards = FXCollections.observableArrayList();

        CardFolderStub(Collection<Card> cards) {
            this.cards.setAll(cards);
        }

        @Override
        public ObservableList<Card> getCardList() {
            return cards;
        }

        @Override
        public List<Double> getFolderScores() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public String getFolderName() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasSameCards(ObservableList<Card> otherCardList) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addListener(InvalidationListener listener) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void removeListener(InvalidationListener listener) {
            throw new AssertionError("This method should not be called.");
        }
    }

}
