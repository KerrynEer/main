package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalCards.getTypicalCardFolders;
import static seedu.address.testutil.TypicalCards.getTypicalFolderName;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.CardFolder;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ClearCommandTest {

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_emptyCardFolder_success() {
        Model model = new ModelManager(this.getClass().getName());
        Model expectedModel = new ModelManager(this.getClass().getName());
        expectedModel.commitActiveCardFolder();

        assertCommandSuccess(new ClearCommand(), model, commandHistory, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyCardFolder_success() throws CommandException {
        Model model = new ModelManager(getTypicalCardFolders(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalCardFolders(), new UserPrefs());
        expectedModel.resetCardFolder(new CardFolder(getTypicalFolderName()));
        expectedModel.commitActiveCardFolder();

        assertCommandSuccess(new ClearCommand(), model, commandHistory, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
