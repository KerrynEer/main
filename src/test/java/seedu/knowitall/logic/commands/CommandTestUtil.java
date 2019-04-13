package seedu.knowitall.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.knowitall.logic.parser.CliSyntax.PREFIX_ANSWER;
import static seedu.knowitall.logic.parser.CliSyntax.PREFIX_FILENAME;
import static seedu.knowitall.logic.parser.CliSyntax.PREFIX_FOLDERNAME;
import static seedu.knowitall.logic.parser.CliSyntax.PREFIX_HINT;
import static seedu.knowitall.logic.parser.CliSyntax.PREFIX_OPTION;
import static seedu.knowitall.logic.parser.CliSyntax.PREFIX_QUESTION;

import java.util.Arrays;
import java.util.List;

import javafx.collections.transformation.FilteredList;
import seedu.knowitall.commons.core.index.Index;
import seedu.knowitall.logic.CommandHistory;
import seedu.knowitall.logic.commands.exceptions.CommandException;
import seedu.knowitall.model.Model;
import seedu.knowitall.model.card.Card;
import seedu.knowitall.model.card.QuestionContainsKeywordsPredicate;
import seedu.knowitall.testutil.EditCardDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final Index TEST_FOLDER_INDEX = Index.fromOneBased(1);

    public static final String VALID_QUESTION_1 = "Sample Question 1";
    public static final String VALID_QUESTION_2 = "Sample Question 2";
    public static final String VALID_ANSWER_1 = "Sample Answer 1";
    public static final String VALID_ANSWER_2 = "Sample Answer 2";
    public static final String VALID_SCORE_1 = "0/0";
    public static final String VALID_OPTION_1 = "Sample Option 1";
    public static final String VALID_OPTION_2 = "Sample Option 2";
    public static final String VALID_HINT_HUSBAND = "husband";
    public static final String VALID_HINT_FRIEND = "friend";
    public static final String VALID_FOLDER_NAME_1 = "Sample Folder 1";
    public static final String VALID_FOLDER_NAME_2 = "Sample Folder 2";
    public static final String VALID_FILENAME = "sample_folder.csv";
    public static final String INVALID_FILENAME = "sample_folder.json";

    public static final String QUESTION_DESC_SAMPLE_1 = " " + PREFIX_QUESTION + VALID_QUESTION_1;
    public static final String QUESTION_DESC_SAMPLE_2 = " " + PREFIX_QUESTION + VALID_QUESTION_2;
    public static final String ANSWER_DESC_SAMPLE_1 = " " + PREFIX_ANSWER + VALID_ANSWER_1;
    public static final String ANSWER_DESC_SAMPLE_2 = " " + PREFIX_ANSWER + VALID_ANSWER_2;
    public static final String OPTION_DESC_SAMPLE_1 = " " + PREFIX_OPTION + VALID_OPTION_1;
    public static final String OPTION_DESC_SAMPLE_2 = " " + PREFIX_OPTION + VALID_OPTION_2;
    public static final String HINT_DESC_FRIEND = " " + PREFIX_HINT + VALID_HINT_FRIEND;
    public static final String HINT_DESC_HUSBAND = " " + PREFIX_HINT + VALID_HINT_HUSBAND;
    public static final String FOLDER_DESC_SAMPLE_1 = " " + PREFIX_FOLDERNAME + VALID_FOLDER_NAME_1;
    public static final String FOLDER_DESC_SAMPLE_2 = " " + PREFIX_FOLDERNAME + VALID_FOLDER_NAME_2;
    public static final String FILENAME_DESC_SAMPLE = " " + PREFIX_FILENAME + VALID_FILENAME;
    public static final String INVALID_FILENAME_EXT = " " + PREFIX_FILENAME + INVALID_FILENAME;

    public static final String INVALID_QUESTION_DESC = " " + PREFIX_QUESTION; // empty string not allowed for questions
    public static final String INVALID_ANSWER_DESC = " " + PREFIX_ANSWER; // empty string not allowed for answers
    public static final String INVALID_OPTION_DESC = " " + PREFIX_OPTION; // empty string not allowed for options
    public static final String INVALID_OPTION_SAME_AS_ANSWER_1 = " " + PREFIX_OPTION + VALID_ANSWER_1;
    public static final String INVALID_HINT_DESC = " " + PREFIX_HINT; // empty string not allowed for hints
    public static final String INVALID_FOLDER_DESC = " " + PREFIX_FOLDERNAME;
    public static final String INVALID_FILENAME_DESC = " " + PREFIX_FILENAME;


    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditCardDescriptor DESC_AMY;
    public static final EditCommand.EditCardDescriptor DESC_BOB;



    static {
        DESC_AMY = new EditCardDescriptorBuilder().withQuestion(VALID_QUESTION_1)
                .withAnswer(VALID_ANSWER_1).withOptions(VALID_OPTION_1).withHint(VALID_HINT_FRIEND).build();
        DESC_BOB = new EditCardDescriptorBuilder().withQuestion(VALID_QUESTION_2)
                .withAnswer(VALID_ANSWER_2).withOptions(VALID_OPTION_1, VALID_OPTION_2)
                .withHint(VALID_HINT_HUSBAND, VALID_HINT_FRIEND).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel} <br>
     * - the {@code actualCommandHistory} remains unchanged.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandHistory actualCommandHistory,
            CommandResult expectedCommandResult, Model expectedModel) {
        CommandHistory expectedCommandHistory = new CommandHistory(actualCommandHistory);
        try {
            CommandResult result = command.execute(actualModel, actualCommandHistory);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
            assertEquals(expectedCommandHistory, actualCommandHistory);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandHistory, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandHistory actualCommandHistory,
            String expectedMessage, Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, actualCommandHistory, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the card folder, filtered card list and selected card in {@code actualModel} remain unchanged <br>
     * - {@code actualCommandHistory} remains unchanged.
     */
    public static void assertCommandFailure(Command command, Model actualModel, CommandHistory actualCommandHistory,
            String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        int expectedActiveCardFolderIndex = actualModel.getActiveCardFolderIndex();
        List<FilteredList<Card>> expectedFilteredCardsList = actualModel.getFilteredCardsList();
        Card expectedSelectedCard = actualModel.getSelectedCard();
        Model.State expectedState = actualModel.getState();

        CommandHistory expectedCommandHistory = new CommandHistory(actualCommandHistory);

        try {
            command.execute(actualModel, actualCommandHistory);
            throw new AssertionError("The expected CommandException was not thrown.");
        } catch (CommandException e) {
            assertEquals(expectedMessage, e.getMessage());
            assertEquals(expectedState, actualModel.getState());
            assertEquals(expectedActiveCardFolderIndex, actualModel.getActiveCardFolderIndex());
            assertEquals(expectedFilteredCardsList, actualModel.getFilteredCardsList());
            assertEquals(expectedSelectedCard, actualModel.getSelectedCard());
            assertEquals(expectedCommandHistory, actualCommandHistory);
        }
    }

    /**
     * Updates {@code model}'s filtered list to show only the card at the given {@code targetIndex} in the
     * {@code model}'s card folder.
     */
    public static void showCardAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getActiveFilteredCards().size());

        Card card = model.getActiveFilteredCards().get(targetIndex.getZeroBased());
        final String[] splitQuestion = card.getQuestion().fullQuestion.split("\\s+");
        model.updateFilteredCard(new QuestionContainsKeywordsPredicate(Arrays.asList(splitQuestion[0])));

        assertEquals(1, model.getActiveFilteredCards().size());
    }

    /**
     * Deletes the first card in {@code model}'s filtered list from {@code model}'s card folder.
     */
    public static void deleteFirstCard(Model model) {
        Card firstCard = model.getActiveFilteredCards().get(0);
        model.deleteCard(firstCard);
        model.commitActiveCardFolder();
    }

}
