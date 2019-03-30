package seedu.knowitall.logic.commands;

import seedu.knowitall.logic.CommandHistory;
import seedu.knowitall.model.Model;

/**
 * Terminates the program.
 */
public class ExitCommand extends Command {

    public static final String COMMAND_WORD = "exit";

    public static final String MESSAGE_EXIT_ACKNOWLEDGEMENT = "Exiting card folder as requested ...";

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        return new CommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT, CommandResult.Type.IS_EXIT);
    }

}
