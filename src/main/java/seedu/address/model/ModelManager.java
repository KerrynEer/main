package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.InvalidationListenerManager;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.card.Answer;
import seedu.address.model.card.Card;
import seedu.address.model.card.exceptions.CardNotFoundException;
import seedu.address.storage.csvmanager.CsvFile;
import seedu.address.storage.csvmanager.CsvManager;
import seedu.address.storage.csvmanager.exceptions.CsvManagerNotInitialized;

/**
 * Represents the in-memory model of the card folder data.
 */
public class ModelManager implements Model {

    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);
    private final InvalidationListenerManager invalidationListenerManager = new InvalidationListenerManager();

    private final UserPrefs userPrefs;

    //Card related
    private final SimpleObjectProperty<Card> selectedCard = new SimpleObjectProperty<>();

    // CardFolder related
    private ObservableList<VersionedCardFolder> folders;
    private final FilteredList<VersionedCardFolder> filteredFolders;
    private final List<FilteredList<Card>> filteredCardsList;
    private int activeCardFolderIndex;
    private boolean inFolder;

    // Test Session related
    private final SimpleObjectProperty<Card> currentTestedCard = new SimpleObjectProperty<>();
    private ObservableList<Card> currentTestedCardFolder;
    private int currentTestedCardIndex;
    private boolean isInsideTestSession = false;
    private boolean isCardAlreadyAnswered = false;
    private int numAnsweredCorrectly = 0;

    // Report display related
    private boolean inReportDisplay = false;

    // Export related
    private CsvManager csvManager;
    {
        try {
            csvManager = new CsvManager();
        } catch (IOException e) {
            csvManager = null;
            logger.warning("Unable to carry out import and export of card folders");
        }
    }

    /**
     * Initializes a ModelManager with the given cardFolders and userPrefs.
     */
    public ModelManager(List<ReadOnlyCardFolder> cardFolders, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(cardFolders, userPrefs);

        logger.fine("Initializing with card folder: " + cardFolders + " and user prefs " + userPrefs);

        List<VersionedCardFolder> versionedCardFolders = new ArrayList<>();
        for (ReadOnlyCardFolder cardFolder : cardFolders) {
            versionedCardFolders.add(new VersionedCardFolder(cardFolder));
        }
        folders = FXCollections.observableArrayList(versionedCardFolders);
        filteredFolders = new FilteredList<>(folders);
        this.userPrefs = new UserPrefs(userPrefs);

        filteredCardsList = new ArrayList<>();
        for (int i = 0; i < filteredFolders.size(); i++) {
            FilteredList<Card> filteredCards = new FilteredList<>(filteredFolders.get(i).getCardList());
            filteredCardsList.add(filteredCards);
            filteredCards.addListener(this::ensureSelectedCardIsValid);
        }


        // ModelManager initialises to first card folder
        activeCardFolderIndex = 0;
        inFolder = true;
    }

    public ModelManager(String newFolderName) {
        this(Collections.singletonList(new CardFolder(newFolderName)), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getcardFolderFilesPath() {
        return userPrefs.getcardFolderFilesPath();
    }

    @Override
    public void setcardFolderFilesPath(Path cardFolderFilesPath) {
        requireNonNull(cardFolderFilesPath);
        userPrefs.setcardFolderFilesPath(cardFolderFilesPath);
    }

    //=========== CardFolder ================================================================================

    @Override
    public void resetCardFolder(ReadOnlyCardFolder cardFolder) {
        VersionedCardFolder versionedCardFolder = getActiveVersionedCardFolder();
        versionedCardFolder.resetData(cardFolder);
    }

    @Override
    public ReadOnlyCardFolder getActiveCardFolder() {
        return getActiveVersionedCardFolder();
    }

    @Override
    public List<ReadOnlyCardFolder> getCardFolders() {
        return new ArrayList<>(filteredFolders);
    }

    @Override
    public boolean hasCard(Card card) {
        requireNonNull(card);

        VersionedCardFolder versionedCardFolder = getActiveVersionedCardFolder();
        return versionedCardFolder.hasCard(card);
    }

    @Override
    public void deleteCard(Card target) {
        VersionedCardFolder versionedCardFolder = getActiveVersionedCardFolder();
        versionedCardFolder.removeCard(target);
    }

    @Override
    public void addCard(Card card) {
        VersionedCardFolder versionedCardFolder = getActiveVersionedCardFolder();
        versionedCardFolder.addCard(card);
        updateFilteredCard(PREDICATE_SHOW_ALL_CARDS);
    }

    @Override
    public void setCard(Card target, Card editedCard) {
        requireAllNonNull(target, editedCard);

        VersionedCardFolder versionedCardFolder = getActiveVersionedCardFolder();
        versionedCardFolder.setCard(target, editedCard);
    }

    @Override
    public boolean hasFolder(CardFolder cardFolder) {
        requireNonNull(cardFolder);

        return hasFolderWithName(cardFolder.getFolderName());
    }

    @Override
    public boolean hasFolderWithName(String name) {
        requireNonNull(name);

        return folders.stream().anyMatch(folder -> folder.getFolderName().equals(name));
    }

    @Override
    public void deleteFolder(int index) {
        folders.remove(index);
        filteredCardsList.remove(index);
        indicateModified();
    }

    @Override
    public void addFolder(CardFolder cardFolder) {
        VersionedCardFolder versionedCardFolder = new VersionedCardFolder(cardFolder);
        folders.add(versionedCardFolder);
        FilteredList<Card> filteredCards = new FilteredList<>(versionedCardFolder.getCardList());
        filteredCardsList.add(filteredCards);
        filteredCards.addListener(this::ensureSelectedCardIsValid);
        indicateModified();
    }

    @Override
    public void renameFolder(int index, String newName) {
        CardFolder folderToRename = folders.get(index);
        folderToRename.rename(newName);
        indicateModified();
    }

    @Override
    public void enterFolder(int index) {
        inFolder = true;
        activeCardFolderIndex = index;
    }

    @Override
    public void exitFolderToHome() {
        inFolder = false;
        removeSelectedCard();
    }

    @Override
    public boolean isInFolder() {
        return inFolder;
    }

    @Override
    public void addListener(InvalidationListener listener) {
        invalidationListenerManager.addListener(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        invalidationListenerManager.removeListener(listener);
    }

    @Override
    public int getActiveCardFolderIndex() {
        return activeCardFolderIndex;
    }

    /**
     * Returns the active {@code CardFolder}
     */
    private VersionedCardFolder getActiveVersionedCardFolder() {
        return folders.get(activeCardFolderIndex);
    }

    /**
     * Returns the filtered list of cards from the active {@code CardFolder}
     */
    private FilteredList<Card> getActiveFilteredCards() {
        return filteredCardsList.get(activeCardFolderIndex);
    }

    /**
     * Notifies listeners that the list of card folders has been modified.
     */
    private void indicateModified() {
        invalidationListenerManager.callListeners(this);
    }

    //=========== Filtered Card List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Card} backed by the internal list of
     * {@code filteredFolders}
     */
    @Override
    public ObservableList<Card> getFilteredCards() {
        return getActiveFilteredCards();
    }

    @Override
    public ObservableList<VersionedCardFolder> getFilteredFolders() {
        return filteredFolders;
    }

    @Override
    public void updateFilteredCard(Predicate<Card> predicate) {
        requireNonNull(predicate);
        FilteredList<Card> filteredCards = getActiveFilteredCards();
        filteredCards.setPredicate(predicate);
    }
    @Override
    public void sortFilteredCard(Comparator<Card> cardComparator) {
        requireNonNull(cardComparator);
        folders.get(activeCardFolderIndex).sortCards(cardComparator);
    }

    //=========== Undo/Redo =================================================================================

    @Override
    public boolean canUndoActiveCardFolder() {
        VersionedCardFolder versionedCardFolder = getActiveVersionedCardFolder();
        return versionedCardFolder.canUndo();
    }

    @Override
    public boolean canRedoActiveCardFolder() {
        VersionedCardFolder versionedCardFolder = getActiveVersionedCardFolder();
        return versionedCardFolder.canRedo();
    }

    @Override
    public void undoActiveCardFolder() {
        VersionedCardFolder versionedCardFolder = getActiveVersionedCardFolder();
        versionedCardFolder.undo();
    }

    @Override
    public void redoActiveCardFolder() {
        VersionedCardFolder versionedCardFolder = getActiveVersionedCardFolder();
        versionedCardFolder.redo();
    }

    @Override
    public void commitActiveCardFolder() {
        VersionedCardFolder versionedCardFolder = getActiveVersionedCardFolder();
        versionedCardFolder.commit();
    }

    //=========== Report Displayed =======================================================================
    @Override
    public boolean inReportDisplay() {
        return inReportDisplay;
    }

    @Override
    public void enterReportDisplay() {
        inReportDisplay = true;
    }

    @Override
    public void exitReportDisplay() {
        inReportDisplay = false;
    }



    //=========== Test Session ===========================================================================

    @Override
    public void testCardFolder() {
        currentTestedCardFolder = getActiveCardFolder().getCardList();
        if (currentTestedCardFolder.isEmpty()) {
            throw new EmptyCardFolderException();
        }

        sortFilteredCard(COMPARATOR_ASC_SCORE_CARDS);

        currentTestedCardIndex = 0;
        Card cardToTest = currentTestedCardFolder.get(currentTestedCardIndex);
        setCurrentTestedCard(cardToTest);
        isInsideTestSession = true;
        numAnsweredCorrectly = 0;
    }

    @Override
    public void setCurrentTestedCard(Card card) {
        if (card != null && !getActiveFilteredCards().contains(card)) {
            throw new CardNotFoundException();
        }
        currentTestedCard.setValue(card);
    }

    @Override
    public Card getCurrentTestedCard() {
        return currentTestedCard.getValue();
    }

    @Override
    public void endTestSession() {
        getActiveVersionedCardFolder()
                .addFolderScore((double) numAnsweredCorrectly / getActiveCardFolder().getCardList().size());
        getActiveVersionedCardFolder().commit();
        isInsideTestSession = false;
        setCardAsNotAnswered();
        numAnsweredCorrectly = 0;
        setCurrentTestedCard(null);
        currentTestedCardFolder = null;
    }

    @Override
    public boolean markAttemptedAnswer(Answer attemptedAnswer) {
        Answer correctAnswer = currentTestedCard.getValue().getAnswer();
        String correctAnswerInCapitals = correctAnswer.toString().toUpperCase();
        String attemptedAnswerInCapitals = attemptedAnswer.toString().toUpperCase();

        if (correctAnswerInCapitals.equals(attemptedAnswerInCapitals)) {
            numAnsweredCorrectly++;
            return true;
        }
        return false;
    }

    @Override
    public void setCardAsAnswered() {
        isCardAlreadyAnswered = true;
    }

    private void setCardAsNotAnswered() {
        isCardAlreadyAnswered = false;
    }

    @Override
    public boolean checkIfCardAlreadyAnswered() {
        return isCardAlreadyAnswered;
    }

    @Override
    public boolean checkIfInsideTestSession() {
        return isInsideTestSession;
    }

    @Override
    public boolean testNextCard() {
        currentTestedCardIndex += 1;
        if (currentTestedCardIndex == currentTestedCardFolder.size()) {
            return false;
        }
        Card cardToTest = currentTestedCardFolder.get(currentTestedCardIndex);
        setCurrentTestedCard(cardToTest);
        setCardAsNotAnswered();
        return true;
    }

    //=========== Selected card ===========================================================================

    @Override
    public ReadOnlyProperty<Card> selectedCardProperty() {
        return selectedCard;
    }

    @Override
    public Card getSelectedCard() {
        return selectedCard.getValue();
    }

    @Override
    public void setSelectedCard(Card card) {
        if (card != null && !getActiveFilteredCards().contains(card)) {
            throw new CardNotFoundException();
        }
        selectedCard.setValue(card);
    }

    @Override
    public void removeSelectedCard() {
        selectedCard.setValue(null);
    }

    /**
     * Ensures {@code selectedCard} is a valid card in {@code filteredCardsList}.
     */
    private void ensureSelectedCardIsValid(ListChangeListener.Change<? extends Card> change) {
        while (change.next()) {
            if (selectedCard.getValue() == null) {
                // null is always a valid selected card, so we do not need to check that it is valid anymore.
                return;
            }

            boolean wasSelectedCardReplaced = change.wasReplaced() && change.getAddedSize() == change.getRemovedSize()
                    && change.getRemoved().contains(selectedCard.getValue());
            if (wasSelectedCardReplaced) {
                // Update selectedCard to its new value.
                int index = change.getRemoved().indexOf(selectedCard.getValue());
                selectedCard.setValue(change.getAddedSubList().get(index));
                continue;
            }

            boolean wasSelectedCardRemoved = change.getRemoved().stream()
                    .anyMatch(removedCard -> selectedCard.getValue().isSameCard(removedCard));
            if (wasSelectedCardRemoved) {
                // Select the card that came before it in the list,
                // or clear the selection if there is no such card.
                selectedCard.setValue(change.getFrom() > 0 ? change.getList().get(change.getFrom() - 1) : null);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return filteredFolders.equals(other.filteredFolders)
                && userPrefs.equals(other.userPrefs)
                && filteredCardsList.equals(other.filteredCardsList)
                && Objects.equals(selectedCard.get(), other.selectedCard.get())
                && isInsideTestSession == other.isInsideTestSession
                && currentTestedCardIndex == other.currentTestedCardIndex
                && isCardAlreadyAnswered == other.isCardAlreadyAnswered
                && activeCardFolderIndex == other.activeCardFolderIndex
                && inFolder == other.inFolder;
    }



    //=========== Export / Import card folders ========================================================================
    @Override
    public void exportCardFolders(List<Integer> cardFolderExports) throws IOException, CsvManagerNotInitialized {
        if (csvManager == null) {
            throw new CsvManagerNotInitialized(Messages.MESSAGE_CSV_MANAGER_NOT_INITIALIZED);
        }
        List<ReadOnlyCardFolder> cardFolders = returnValidCardFolders(cardFolderExports);
        csvManager.writeFoldersToCsv(cardFolders);
    }

    @Override
    public void importCardFolders(CsvFile csvFile) throws IOException, CommandException {
        CardFolder cardFolder = csvManager.readFoldersToCsv(csvFile);
        addFolder(cardFolder);
    }

    @Override
    public void setTestCsvPath() throws IOException {
        csvManager.setTestDefaultPath();
    }

    @Override
    public String getDefaultPath() {
        return csvManager.getDefaultPath();
    }

    /**
     * returns the corresponding {@code List<ReadOnlyCardFolder>} from the list of integer indexes
     */
    private List<ReadOnlyCardFolder> returnValidCardFolders(List<Integer> cardFolderExports) {
        List<ReadOnlyCardFolder> readOnlyCardFolders = new ArrayList<>();
        List<Index> indexList = cardFolderExports.stream().map(Index::fromOneBased).collect(Collectors.toList());
        for (Index index : indexList) {
            try {
                ReadOnlyCardFolder cardFolder = filteredFolders.get(index.getZeroBased());
                readOnlyCardFolders.add(cardFolder);
            } catch (IndexOutOfBoundsException e) {
                throw new CardFolderNotFoundException(index.displayIndex());
            }
        }
        return readOnlyCardFolders;
    }
}
