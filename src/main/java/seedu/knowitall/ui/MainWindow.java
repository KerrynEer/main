package seedu.knowitall.ui;

import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.knowitall.commons.core.GuiSettings;
import seedu.knowitall.commons.core.LogsCenter;
import seedu.knowitall.logic.Logic;
import seedu.knowitall.logic.commands.CommandResult;
import seedu.knowitall.logic.commands.exceptions.CommandException;
import seedu.knowitall.logic.parser.exceptions.ParseException;
import seedu.knowitall.model.card.Card;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private BrowserPanel browserPanel;
    private FolderListPanel folderListPanel;
    private CardListPanel cardListPanel;
    private ReportDisplay reportDisplay;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;
    private CardMainScreen cardMainScreen;
    private TestSession testSession;
    private StatusBarFooter statusBarFooter;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    @FXML
    private StackPane fullScreenPlaceholder;

    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        statusBarFooter = new StatusBarFooter();
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand, logic.getHistory());
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());

        browserPanel = new BrowserPanel(logic.selectedCardProperty());
        folderListPanel = new FolderListPanel(logic.getFilteredCardFolders());
        cardListPanel = new CardListPanel(logic.getFilteredCards(), logic.selectedCardProperty(),
                logic::setSelectedCard);
        cardMainScreen = new CardMainScreen(cardListPanel, browserPanel);
        fullScreenPlaceholder.getChildren().add(folderListPanel.getRoot());
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    /**
     * Starts test session UI.
     */
    private void handleStartTestSession(Card card) {
        testSession = new TestSession(card);
        Region testSessionRegion = (testSession).getRoot();
        fullScreenPlaceholder.getChildren().add(testSessionRegion);
        statusBarFooter.updateStatusBarInTestSession();
    }

    /**
     * Display the next card in this test session.
     */
    private void handleNextCardTestSession(Card card) {
        fullScreenPlaceholder.getChildren().remove(fullScreenPlaceholder.getChildren().size() - 1);
        testSession = new TestSession(card);
        Region testSessionRegion = (testSession).getRoot();
        fullScreenPlaceholder.getChildren().add(testSessionRegion);
    }

    /**
     * Ends test session and display back card main screen.
     */
    private void handleEndTestSession() {
        fullScreenPlaceholder.getChildren().remove(fullScreenPlaceholder.getChildren().size() - 1);
        statusBarFooter.updateStatusBarInFolder();
    }

    /**
     * Refreshes the side panel to display the contents of the new active folder.
     */
    private void handleEnterFolder() {
        cardListPanel = new CardListPanel(logic.getFilteredCards(), logic.selectedCardProperty(),
                logic::setSelectedCard);
        cardMainScreen = new CardMainScreen(cardListPanel, browserPanel);
        fullScreenPlaceholder.getChildren().add(cardMainScreen.getRoot());
        statusBarFooter.updateStatusBarInFolder();
    }

    /**
     * Refreshes the side panel to display all folders.
     */
    private void handleExitFolder() {
        fullScreenPlaceholder.getChildren().remove(fullScreenPlaceholder.getChildren().size() - 1);
        folderListPanel.refreshContent();
        statusBarFooter.updateStatusBarInHomeDirectory();
    }

    /**
     * Refreshes the side panel to display updated information of all folders.
     */
    private void handleEditFolder() {
        folderListPanel.refreshContent();
    }

    /**
     * Start a report display by displaying the report page to the user.
     */
    private void handleReport() {
        reportDisplay = new ReportDisplay(logic.getCardFolder());
        Region reportRegion = (reportDisplay).getRoot();
        fullScreenPlaceholder.getChildren().add(reportRegion);
        statusBarFooter.updateStatusBarInReportDisplay();
    }

    /**
     * Ends report display and display back card main screen.
     */
    private void handleEndReport() {
        fullScreenPlaceholder.getChildren().remove(fullScreenPlaceholder.getChildren().size() - 1);
        statusBarFooter.updateStatusBarInFolder();
    }


    /**
     * Show the page with correct answer.
     */
    private void handleCorrectAnswer() {
        assert testSession != null;
        testSession.handleCorrectAnswer();
    }

    /**
     * Show the page with wrong answer.
     */
    private void handleWrongAnswer() {
        assert testSession != null;
        testSession.handleWrongAnswer();
    }

    /**
     * Show the page with revealed answer.
     */
    private void handleRevealAnswer() {
        assert testSession != null;
        testSession.handleRevealAnswer();
    }

    private void updateCardListPanel() {
        fullScreenPlaceholder.getChildren();
    }

    public CardListPanel getCardListPanel() {
        return cardListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.knowitall.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            switch (commandResult.getType()) {
            case SHOW_HELP:
                handleHelp();
                break;
            case IS_EXIT:
                handleExit();
                break;
            case ENTERED_FOLDER:
                handleEnterFolder();
                break;
            case EXITED_FOLDER:
                handleExitFolder();
                break;
            case EDITED_FOLDER:
                handleEditFolder();
                break;
            case START_TEST_SESSION:
                handleStartTestSession(commandResult.getTestSessionCard());
                break;
            case END_TEST_SESSION:
                handleEndTestSession();
                break;
            case ENTERED_REPORT:
                handleReport();
                break;
            case EXITED_REPORT:
                handleEndReport();
                break;
            case SHOW_NEXT_CARD:
                handleNextCardTestSession(commandResult.getTestSessionCard());
                break;
            case ANSWER_CORRECT:
                handleCorrectAnswer();
                break;
            case ANSWER_WRONG:
                handleWrongAnswer();
                break;
            default:
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("Invalid command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }
}
