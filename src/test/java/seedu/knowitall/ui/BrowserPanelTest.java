package seedu.knowitall.ui;

import static org.junit.Assert.assertEquals;
import static seedu.knowitall.testutil.TypicalCards.ALICE;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.BrowserPanelHandle;
import javafx.beans.property.SimpleObjectProperty;
import seedu.knowitall.model.card.Card;

public class BrowserPanelTest extends GuiUnitTest {
    private SimpleObjectProperty<Card> selectedCard = new SimpleObjectProperty<>();
    private BrowserPanel browserPanel;
    private BrowserPanelHandle browserPanelHandle;

    @Before
    public void setUp() {
        guiRobot.interact(() -> browserPanel = new BrowserPanel(selectedCard));
        uiPartRule.setUiPart(browserPanel);

        browserPanelHandle = new BrowserPanelHandle(browserPanel.getRoot());
    }

    @Test
    public void display() throws Exception {
        // default (empty) card page
        assertEquals("", browserPanelHandle.getCurrentQuestion());

        // associated web page of a card
        guiRobot.interact(() -> selectedCard.set(ALICE));

        String expectedCardQuestion = ALICE.getQuestion().fullQuestion;
        assertEquals(expectedCardQuestion, browserPanelHandle.getCurrentQuestion());
    }
}
