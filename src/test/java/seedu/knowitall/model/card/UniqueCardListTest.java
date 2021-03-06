package seedu.knowitall.model.card;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.knowitall.logic.commands.CommandTestUtil.VALID_HINT_HUSBAND;
import static seedu.knowitall.testutil.TypicalCards.ALICE;
import static seedu.knowitall.testutil.TypicalCards.CARD_2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.knowitall.model.card.exceptions.CardNotFoundException;
import seedu.knowitall.model.card.exceptions.DuplicateCardException;
import seedu.knowitall.testutil.CardBuilder;

public class UniqueCardListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final UniqueCardList uniqueCardList = new UniqueCardList();

    @Test
    public void contains_nullCard_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueCardList.contains(null);
    }

    @Test
    public void contains_cardNotInList_returnsFalse() {
        assertFalse(uniqueCardList.contains(ALICE));
    }

    @Test
    public void contains_cardInList_returnsTrue() {
        uniqueCardList.add(ALICE);
        assertTrue(uniqueCardList.contains(ALICE));
    }

    @Test
    public void contains_cardWithSameIdentityFieldsInList_returnsTrue() {
        uniqueCardList.add(ALICE);
        Card editedAlice = new CardBuilder(ALICE).withHint(VALID_HINT_HUSBAND)
                .build();
        assertTrue(uniqueCardList.contains(editedAlice));
    }

    @Test
    public void add_nullCard_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueCardList.add(null);
    }

    @Test
    public void add_duplicateCard_throwsDuplicateCardException() {
        uniqueCardList.add(ALICE);
        thrown.expect(DuplicateCardException.class);
        uniqueCardList.add(ALICE);
    }

    @Test
    public void setCard_nullTargetCard_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueCardList.setCard(null, ALICE);
    }

    @Test
    public void setCard_nullEditedCard_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueCardList.setCard(ALICE, null);
    }

    @Test
    public void setCard_targetCardNotInList_throwsCardNotFoundException() {
        thrown.expect(CardNotFoundException.class);
        uniqueCardList.setCard(ALICE, ALICE);
    }

    @Test
    public void setCard_editedCardIsSameCard_success() {
        uniqueCardList.add(ALICE);
        uniqueCardList.setCard(ALICE, ALICE);
        UniqueCardList expectedUniqueCardList = new UniqueCardList();
        expectedUniqueCardList.add(ALICE);
        assertEquals(expectedUniqueCardList, uniqueCardList);
    }

    @Test
    public void setCard_editedCardHasSameIdentity_success() {
        uniqueCardList.add(ALICE);
        Card editedAlice = new CardBuilder(ALICE).withHint(VALID_HINT_HUSBAND)
                .build();
        uniqueCardList.setCard(ALICE, editedAlice);
        UniqueCardList expectedUniqueCardList = new UniqueCardList();
        expectedUniqueCardList.add(editedAlice);
        assertEquals(expectedUniqueCardList, uniqueCardList);
    }

    @Test
    public void setCard_editedCardHasDifferentIdentity_success() {
        uniqueCardList.add(ALICE);
        uniqueCardList.setCard(ALICE, CARD_2);
        UniqueCardList expectedUniqueCardList = new UniqueCardList();
        expectedUniqueCardList.add(CARD_2);
        assertEquals(expectedUniqueCardList, uniqueCardList);
    }

    @Test
    public void setCard_editedCardHasNonUniqueIdentity_throwsDuplicateCardException() {
        uniqueCardList.add(ALICE);
        uniqueCardList.add(CARD_2);
        thrown.expect(DuplicateCardException.class);
        uniqueCardList.setCard(ALICE, CARD_2);
    }

    @Test
    public void remove_nullCard_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueCardList.remove(null);
    }

    @Test
    public void remove_cardDoesNotExist_throwsCardNotFoundException() {
        thrown.expect(CardNotFoundException.class);
        uniqueCardList.remove(ALICE);
    }

    @Test
    public void remove_existingCard_removesCard() {
        uniqueCardList.add(ALICE);
        uniqueCardList.remove(ALICE);
        UniqueCardList expectedUniqueCardList = new UniqueCardList();
        assertEquals(expectedUniqueCardList, uniqueCardList);
    }

    @Test
    public void setCards_nullUniqueCardList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueCardList.setCards((UniqueCardList) null);
    }

    @Test
    public void setCards_uniqueCardList_replacesOwnListWithProvidedUniqueCardList() {
        uniqueCardList.add(ALICE);
        UniqueCardList expectedUniqueCardList = new UniqueCardList();
        expectedUniqueCardList.add(CARD_2);
        uniqueCardList.setCards(expectedUniqueCardList);
        assertEquals(expectedUniqueCardList, uniqueCardList);
    }

    @Test
    public void setCards_nullList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueCardList.setCards((List<Card>) null);
    }

    @Test
    public void setCards_list_replacesOwnListWithProvidedList() {
        uniqueCardList.add(ALICE);
        List<Card> cardList = Collections.singletonList(CARD_2);
        uniqueCardList.setCards(cardList);
        UniqueCardList expectedUniqueCardList = new UniqueCardList();
        expectedUniqueCardList.add(CARD_2);
        assertEquals(expectedUniqueCardList, uniqueCardList);
    }

    @Test
    public void setCards_listWithDuplicateCards_throwsDuplicateCardException() {
        List<Card> listWithDuplicateCards = Arrays.asList(ALICE, ALICE);
        thrown.expect(DuplicateCardException.class);
        uniqueCardList.setCards(listWithDuplicateCards);
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        uniqueCardList.asUnmodifiableObservableList().remove(0);
    }
}
