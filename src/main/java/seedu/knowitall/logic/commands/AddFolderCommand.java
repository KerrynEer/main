package seedu.knowitall.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.knowitall.commons.core.Messages;
import seedu.knowitall.logic.CommandHistory;
import seedu.knowitall.logic.commands.exceptions.CommandException;
import seedu.knowitall.model.CardFolder;
import seedu.knowitall.model.Model;
import seedu.knowitall.model.Model.State;

/**
 * Adds a card folder.
 */
public class AddFolderCommand extends Command {

    public static final String COMMAND_WORD = "addfolder";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a card folder. "
            + "Parameters: "
            + "FOLDER_NAME \n"
            + "Example: " + COMMAND_WORD + " "
            + "Nervous System ";

    public static final String MESSAGE_SUCCESS = "New card folder added: %1$s";
    public static final String MESSAGE_DUPLICATE_CARD_FOLDER = "This card folder already exists";

    private final CardFolder toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Card}
     */
    public AddFolderCommand(CardFolder cardFolder) {
        requireNonNull(cardFolder);
        toAdd = cardFolder;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        if (model.getState() != State.IN_HOMEDIR) {
            throw new CommandException(Messages.MESSAGE_INVALID_COMMAND_INSIDE_FOLDER);
        }
        if (model.hasFolder(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_CARD_FOLDER);
        }

        model.addFolder(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddFolderCommand // instanceof handles nulls
                && toAdd.equals(((AddFolderCommand) other).toAdd));
    }
}
