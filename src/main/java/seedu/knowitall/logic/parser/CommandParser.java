package seedu.knowitall.logic.parser;

import static seedu.knowitall.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.knowitall.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.knowitall.logic.commands.AddCommand;
import seedu.knowitall.logic.commands.AddFolderCommand;
import seedu.knowitall.logic.commands.AnswerCommand;
import seedu.knowitall.logic.commands.ChangeCommand;
import seedu.knowitall.logic.commands.ClearCommand;
import seedu.knowitall.logic.commands.Command;
import seedu.knowitall.logic.commands.DeleteCommand;
import seedu.knowitall.logic.commands.DeleteFolderCommand;
import seedu.knowitall.logic.commands.EditCommand;
import seedu.knowitall.logic.commands.EditFolderCommand;
import seedu.knowitall.logic.commands.EndCommand;
import seedu.knowitall.logic.commands.ExitCommand;
import seedu.knowitall.logic.commands.ExportCommand;
import seedu.knowitall.logic.commands.HelpCommand;
import seedu.knowitall.logic.commands.HistoryCommand;
import seedu.knowitall.logic.commands.ImportCommand;
import seedu.knowitall.logic.commands.ListCommand;
import seedu.knowitall.logic.commands.NextCommand;
import seedu.knowitall.logic.commands.RedoCommand;
import seedu.knowitall.logic.commands.ReportCommand;
import seedu.knowitall.logic.commands.SearchCommand;
import seedu.knowitall.logic.commands.SelectCommand;
import seedu.knowitall.logic.commands.SortCommand;
import seedu.knowitall.logic.commands.TestCommand;
import seedu.knowitall.logic.commands.UndoCommand;

import seedu.knowitall.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class CommandParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case AddFolderCommand.COMMAND_WORD:
            return new AddFolderCommandParser().parse(arguments);

        case ChangeCommand.COMMAND_WORD:
            return new ChangeCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case EditFolderCommand.COMMAND_WORD:
            return new EditFolderCommandParser().parse(arguments);

        case SelectCommand.COMMAND_WORD:
            return new SelectCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case DeleteFolderCommand.COMMAND_WORD:
            return new DeleteFolderCommandParser().parse(arguments);

        case TestCommand.COMMAND_WORD:
            return new TestCommand();

        case ReportCommand.COMMAND_WORD:
            return new ReportCommand();

        case AnswerCommand.COMMAND_WORD:
            return new AnswerCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case SearchCommand.COMMAND_WORD:
            return new SearchCommandParser().parse(arguments);

        case ExportCommand.COMMAND_WORD:
            return new ExportCommandParser().parse(arguments);

        case ImportCommand.COMMAND_WORD:
            return new ImportCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case SortCommand.COMMAND_WORD:
            return new SortCommand();

        case HistoryCommand.COMMAND_WORD:
            return new HistoryCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case EndCommand.COMMAND_WORD:
            return new EndCommand();

        case NextCommand.COMMAND_WORD:
            return new NextCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case UndoCommand.COMMAND_WORD:
            return new UndoCommand();

        case RedoCommand.COMMAND_WORD:
            return new RedoCommand();

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
