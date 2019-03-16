package seedu.address.logic.commands;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_INSIDE_TEST_SESSION;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalCards.getTypicalCardFolders;

import org.junit.Test;

import seedu.address.logic.AnswerCommandResultType;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.card.Card;
import seedu.address.testutil.TypicalIndexes;

/**
 * Contains integration tests (interaction with the Model) and junit tests for {@code TestCommand}.
 */
public class TestCommandTest {
    private Model model = new ModelManager(getTypicalCardFolders(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalCardFolders(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validTestCommand_success() {
        TestCommand testCommand = new TestCommand(TypicalIndexes.INDEX_FIRST_CARD_FOLDER);
        expectedModel.setActiveCardFolderIndex(TypicalIndexes.INDEX_FIRST_CARD_FOLDER.getZeroBased());
        expectedModel.testCardFolder(TypicalIndexes.INDEX_FIRST_CARD_FOLDER.getZeroBased());
        Card cardToTest = expectedModel.getCurrentTestedCard();

        CommandResult expectedCommandResult = new CommandResult(TestCommand.MESSAGE_ENTER_TEST_FOLDER_SUCCESS, false,
                false, false, cardToTest, false, AnswerCommandResultType.NOT_ANSWER_COMMAND);
        assertCommandSuccess(testCommand, model, commandHistory, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_invalidTestCommandInsideTestSession_fail() {
        TestCommand testCommand = new TestCommand(TypicalIndexes.INDEX_FIRST_CARD_FOLDER);
        model.setActiveCardFolderIndex(TypicalIndexes.INDEX_FIRST_CARD_FOLDER.getZeroBased());
        model.testCardFolder(TypicalIndexes.INDEX_FIRST_CARD_FOLDER.getZeroBased());
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_INSIDE_TEST_SESSION);
        assertCommandFailure(testCommand, model, commandHistory, expectedMessage);
    }
}
