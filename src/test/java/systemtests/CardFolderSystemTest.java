package systemtests;

import static guitests.guihandles.WebViewUtil.waitUntilBrowserLoaded;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.knowitall.logic.commands.CommandTestUtil.TEST_FOLDER_INDEX;
import static seedu.knowitall.ui.StatusBarFooter.SYNC_STATUS_INITIAL;
import static seedu.knowitall.ui.StatusBarFooter.SYNC_STATUS_UPDATED;
import static seedu.knowitall.ui.testutil.GuiTestAssert.assertListMatching;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;

import guitests.guihandles.BrowserPanelHandle;
import guitests.guihandles.CardListPanelHandle;
import guitests.guihandles.CommandBoxHandle;
import guitests.guihandles.MainMenuHandle;
import guitests.guihandles.MainWindowHandle;
import guitests.guihandles.ResultDisplayHandle;
import guitests.guihandles.StatusBarFooterHandle;
import seedu.knowitall.TestApp;
import seedu.knowitall.commons.core.index.Index;
import seedu.knowitall.logic.commands.ChangeCommand;
import seedu.knowitall.logic.commands.ClearCommand;
import seedu.knowitall.logic.commands.ListCommand;
import seedu.knowitall.logic.commands.SearchCommand;
import seedu.knowitall.logic.commands.SelectCommand;
import seedu.knowitall.model.CardFolder;
import seedu.knowitall.model.Model;
import seedu.knowitall.testutil.TypicalCards;
import seedu.knowitall.ui.CommandBox;

/**
 * A system test class for CardFolder, which provides access to handles of GUI components and helper methods
 * for test verification.
 */
public abstract class CardFolderSystemTest {
    @ClassRule
    public static ClockRule clockRule = new ClockRule();

    private static final List<String> COMMAND_BOX_DEFAULT_STYLE = Arrays.asList("text-input", "text-field");
    private static final List<String> COMMAND_BOX_ERROR_STYLE =
            Arrays.asList("text-input", "text-field", CommandBox.ERROR_STYLE_CLASS);

    private MainWindowHandle mainWindowHandle;
    private TestApp testApp;
    private SystemTestSetupHelper setupHelper;

    @BeforeClass
    public static void setupBeforeClass() {
        SystemTestSetupHelper.initialize();
    }

    @Before
    public void setUp() {
        setupHelper = new SystemTestSetupHelper();
        testApp = setupHelper.setupApplication(this::getInitialData, getDataFileLocation());
        mainWindowHandle = setupHelper.setupMainWindowHandle();

        String command = ChangeCommand.COMMAND_WORD + " " + TEST_FOLDER_INDEX.getOneBased();
        String resultDisplayString = String.format(ChangeCommand.MESSAGE_ENTER_FOLDER_SUCCESS,
                TEST_FOLDER_INDEX.getOneBased());

        mainWindowHandle.getCommandBox().run(command);
        setupHelper.initialiseCardListPanelHandle();

        waitUntilBrowserLoaded(getBrowserPanel());
        assertApplicationStartingStateIsCorrect(resultDisplayString);
    }

    @After
    public void tearDown() {
        setupHelper.tearDownStage();
    }

    /**
     * Returns the data to be loaded into the file in {@link #getDataFileLocation()}.
     */
    protected CardFolder getInitialData() {
        return TypicalCards.getTypicalCardFolder();
    }

    /**
     * Returns the directory of the data file.
     */
    protected Path getDataFileLocation() {
        return TestApp.SAVE_LOCATION_FOR_TESTING;
    }

    public MainWindowHandle getMainWindowHandle() {
        return mainWindowHandle;
    }

    public CommandBoxHandle getCommandBox() {
        return mainWindowHandle.getCommandBox();
    }

    public CardListPanelHandle getCardListPanel() {
        return mainWindowHandle.getCardListPanel();
    }

    public MainMenuHandle getMainMenu() {
        return mainWindowHandle.getMainMenu();
    }

    public BrowserPanelHandle getBrowserPanel() {
        return mainWindowHandle.getBrowserPanel();
    }

    public StatusBarFooterHandle getStatusBarFooter() {
        return mainWindowHandle.getStatusBarFooter();
    }

    public ResultDisplayHandle getResultDisplay() {
        return mainWindowHandle.getResultDisplay();
    }

    /**
     * Executes {@code command} in the application's {@code CommandBox}.
     * Method returns after UI components have been updated.
     */
    protected void executeCommand(String command) {
        rememberStates();
        // Injects a fixed clock before executing a command so that the time stamp shown in the status bar
        // after each command is predictable and also different from the previous command.
        clockRule.setInjectedClockToCurrentTime();

        mainWindowHandle.getCommandBox().run(command);

        waitUntilBrowserLoaded(getBrowserPanel());
    }

    /**
     * Displays all cards in the card folder.
     */
    protected void showAllCards() {
        executeCommand(ListCommand.COMMAND_WORD);
        assertEquals(getModel().getActiveCardFolder().getCardList().size(), getModel().getFilteredCards().size());
    }

    /**
     * Displays all cards with any parts of their questions matching {@code keyword} (case-insensitive).
     */
    protected void showCardsWithQuestion(String keyword) {
        executeCommand(SearchCommand.COMMAND_WORD + " " + keyword);
        assertTrue(getModel().getFilteredCards().size() < getModel().getActiveCardFolder().getCardList().size());
    }

    /**
     * Selects the card at {@code index} of the displayed list.
     */
    protected void selectCard(Index index) {
        executeCommand(SelectCommand.COMMAND_WORD + " " + index.getOneBased());
        assertEquals(index.getZeroBased(), getCardListPanel().getSelectedCardIndex());
    }

    /**
     * Deletes all cards in the card folder.
     */
    protected void deleteAllCards() {
        executeCommand(ClearCommand.COMMAND_WORD);
        assertEquals(0, getModel().getActiveCardFolder().getCardList().size());
    }

    /**
     * Asserts that the {@code CommandBox} displays {@code expectedCommandInput}, the {@code ResultDisplay} displays
     * {@code expectedResultMessage}, the storage contains the same card objects as {@code expectedModel}
     * and the card list panel displays the cards in the model correctly.
     */
    protected void assertApplicationDisplaysExpected(String expectedCommandInput, String expectedResultMessage,
            Model expectedModel) {
        assertEquals(expectedCommandInput, getCommandBox().getInput());
        assertEquals(expectedResultMessage, getResultDisplay().getText());
        assertEquals(new CardFolder(expectedModel.getActiveCardFolder()), testApp.readFirstStorageCardFolder());
        assertListMatching(getCardListPanel(), expectedModel.getFilteredCards());
    }

    /**
     * Calls {@code BrowserPanelHandle}, {@code CardListPanelHandle} and {@code StatusBarFooterHandle} to remember
     * their current state.
     */
    private void rememberStates() {
        StatusBarFooterHandle statusBarFooterHandle = getStatusBarFooter();
        getBrowserPanel().rememberQuestion();
        statusBarFooterHandle.rememberSaveLocation();
        statusBarFooterHandle.rememberSyncStatus();
        getCardListPanel().rememberSelectedCardCard();
    }

    /**
     * Asserts that the previously selected card is now deselected and the browser's url is now displaying the
     * default page.
     * @see BrowserPanelHandle#isQuestionChanged()
     */
    protected void assertSelectedCardDeselected() {
        assertEquals("", getBrowserPanel().getCurrentQuestion());
        assertFalse(getCardListPanel().isAnyCardSelected());
    }

    /**
     * Asserts that the browser's url is changed to display the details of the card in the card list panel at
     * {@code expectedSelectedCardIndex}, and only the card at {@code expectedSelectedCardIndex} is selected.
     * @see BrowserPanelHandle#isQuestionChanged()
     * @see CardListPanelHandle#isSelectedCardCardChanged()
     */
    protected void assertSelectedCardChanged(Index expectedSelectedCardIndex) {
        getCardListPanel().navigateToCard(getCardListPanel().getSelectedCardIndex());
        String expectedCardQuestion = getCardListPanel().getHandleToSelectedCard().getQuestion();
        assertEquals(expectedCardQuestion, getBrowserPanel().getCurrentQuestion());

        assertEquals(expectedSelectedCardIndex.getZeroBased(), getCardListPanel().getSelectedCardIndex());
    }

    /**
     * Asserts that the browser's url and the selected card in the card list panel remain unchanged.
     * @see BrowserPanelHandle#isQuestionChanged()
     * @see CardListPanelHandle#isSelectedCardCardChanged()
     */
    protected void assertSelectedCardUnchanged() {
        assertFalse(getBrowserPanel().isQuestionChanged());
        assertFalse(getCardListPanel().isSelectedCardCardChanged());
    }

    /**
     * Asserts that the command box's shows the default style.
     */
    protected void assertCommandBoxShowsDefaultStyle() {
        assertEquals(COMMAND_BOX_DEFAULT_STYLE, getCommandBox().getStyleClass());
    }

    /**
     * Asserts that the command box's shows the error style.
     */
    protected void assertCommandBoxShowsErrorStyle() {
        assertEquals(COMMAND_BOX_ERROR_STYLE, getCommandBox().getStyleClass());
    }

    /**
     * Asserts that the entire status bar remains the same.
     */
    protected void assertStatusBarUnchanged() {
        StatusBarFooterHandle handle = getStatusBarFooter();
        assertFalse(handle.isSaveLocationChanged());
        assertFalse(handle.isSyncStatusChanged());
    }

    /**
     * Asserts that only the sync status in the status bar was changed to the timing of
     * {@code ClockRule#getInjectedClock()}, while the save location remains the same.
     */
    protected void assertStatusBarUnchangedExceptSyncStatus() {
        StatusBarFooterHandle handle = getStatusBarFooter();
        String timestamp = new Date(clockRule.getInjectedClock().millis()).toString();
        String expectedSyncStatus = String.format(SYNC_STATUS_UPDATED, timestamp);
        assertEquals(expectedSyncStatus, handle.getSyncStatus());
        assertFalse(handle.isSaveLocationChanged());
    }

    /**
     * Asserts that the starting state of the application is correct.
     * @param resultDisplayString is the string that result display should show.
     */
    private void assertApplicationStartingStateIsCorrect(String resultDisplayString) {
        assertEquals("", getCommandBox().getInput());
        assertEquals(resultDisplayString, getResultDisplay().getText());
        assertListMatching(getCardListPanel(), getModel().getFilteredCards());
        assertEquals("", getBrowserPanel().getCurrentQuestion());
        assertEquals(Paths.get(".").resolve(testApp.getStorageSaveLocation()).toString(),
                getStatusBarFooter().getSaveLocation());
        assertEquals(SYNC_STATUS_INITIAL, getStatusBarFooter().getSyncStatus());
    }

    /**
     * Returns a defensive copy of the current model.
     */
    protected Model getModel() {
        return testApp.getModel();
    }
}
