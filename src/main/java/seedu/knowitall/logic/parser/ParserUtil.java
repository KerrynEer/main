package seedu.knowitall.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.knowitall.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.util.Pair;
import seedu.knowitall.commons.core.Messages;
import seedu.knowitall.commons.core.index.Index;
import seedu.knowitall.commons.exceptions.IllegalValueException;
import seedu.knowitall.commons.util.StringUtil;
import seedu.knowitall.logic.commands.EditFolderCommand;
import seedu.knowitall.logic.parser.exceptions.ParseException;
import seedu.knowitall.model.ReadOnlyCardFolder;
import seedu.knowitall.model.card.Answer;
import seedu.knowitall.model.card.Option;
import seedu.knowitall.model.card.Question;
import seedu.knowitall.model.hint.Hint;
import seedu.knowitall.storage.csvmanager.CsvFile;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String MESSAGE_INDEX_LESS_THAN_ZERO = "Index is less than zero";
    public static final String HOME_SYMBOL = "..";


    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses {@code String args} into an {@code Index} and {@code String} and returns it as a {@code Pair} object.
     * @throws ParseException if the specified index or folder name is invalid.
     */
    public static Pair<Index, String> parseIndexAndFolderName(String args) throws ParseException {
        String trimmedArgs = args.trim();

        int spaceIndex = trimmedArgs.indexOf(' ');
        if (spaceIndex == -1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditFolderCommand.MESSAGE_USAGE));
        }

        String trimmedIndex = trimmedArgs.substring(0, spaceIndex);
        String trimmedFolderName = trimmedArgs.substring(spaceIndex).trim();

        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }

        if (trimmedFolderName.isEmpty()) {
            throw new ParseException(ReadOnlyCardFolder.MESSAGE_CONSTRAINTS);
        }

        Index index = Index.fromOneBased(Integer.parseInt(trimmedIndex));
        return new Pair<>(index, trimmedFolderName);
    }

    /**
     * Parses {@code String args} and checks whether it is equal to {@code HOME_SYMBOL}
     */
    public static boolean parseHomeSymbol(String args) {
        String trimmedArgs = args.trim();
        return (trimmedArgs.equals(HOME_SYMBOL));
    }

    /**
     * Parses a {@code String question} into a {@code Question}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code question} is invalid.
     */
    public static Question parseQuestion(String question) throws ParseException {
        requireNonNull(question);
        String trimmedQuestion = question.trim();
        if (!Question.isValidQuestion(trimmedQuestion)) {
            throw new ParseException(Question.MESSAGE_CONSTRAINTS);
        }
        return new Question(trimmedQuestion);
    }

    /**
     * Parses a {@code String answer} into a {@code Answer}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code answer} is invalid.
     */
    public static Answer parseAnswer(String answer) throws ParseException {
        requireNonNull(answer);
        String trimmedAnswer = answer.trim();
        if (!Answer.isValidAnswer(trimmedAnswer)) {
            throw new ParseException(Answer.MESSAGE_CONSTRAINTS);
        }
        return new Answer(trimmedAnswer);
    }

    /**
     * Parses a {@code String hint} into a {@code Hint}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code hint} is invalid.
     */
    public static Hint parseHint(String hint) throws ParseException {
        requireNonNull(hint);
        String trimmedHint = hint.trim();
        if (!Hint.isValidHintName(trimmedHint)) {
            throw new ParseException(Hint.MESSAGE_CONSTRAINTS);
        }
        return new Hint(trimmedHint);
    }

    /**
     * Parses {@code Collection<String> hints} into a {@code Set<Hint>}.
     * Restrict to at most the last hint from {@code Collection<String> hints}, or none if the Collection is empty.
     */
    public static Set<Hint> parseHints(Collection<String> hints) throws ParseException {
        requireNonNull(hints);
        List<String> hintList = new ArrayList<>(hints);
        final Set<Hint> hintSet = new HashSet<>();
        if (!hintList.isEmpty()) {
            hintSet.add(parseHint(hintList.get(hintList.size() - 1)));
        }
        return hintSet;
    }

    /**
     * Parses a {@code String option} into a {@code Option}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code option} is invalid.
     */
    public static Option parseOption(String option) throws ParseException {
        requireNonNull(option);
        String trimmedOption = option.trim();
        if (!Option.isValidOption(trimmedOption)) {
            throw new ParseException(Option.MESSAGE_CONSTRAINTS);
        }
        return new Option(trimmedOption);
    }

    /**
     * Parses {@code Collection<String> options} into a {@code Set<Option>}.
     */
    public static Set<Option> parseOptions(Collection<String> options) throws ParseException {
        requireNonNull(options);
        final Set<Option> optionSet = new HashSet<>();
        for (String optionValue : options) {
            optionSet.add(parseOption(optionValue));
        }
        return optionSet;
    }


    public static Integer stringToInt(String element) throws NumberFormatException {
        return Integer.parseInt(element);
    }

    /**
     * Parses a user input of a string of integers into a {@code List<Integer>}
     */
    public static List<Integer> parseFolderIndex(String folderIndexes) throws NumberFormatException,
            IllegalValueException {
        folderIndexes = folderIndexes.trim();
        List<Integer> indexList = Arrays.stream(folderIndexes.split(" "))
                .map(ParserUtil::stringToInt)
                .collect(Collectors.toList());

        List<Integer> invalidIndexList = indexList.stream().filter(i -> i < 0).collect(Collectors.toList());
        if (invalidIndexList.size() > 0) {
            throw new IllegalValueException(MESSAGE_INDEX_LESS_THAN_ZERO);
        }
        return indexList;
    }

    /**
     * Parses a {@Code String filename} into a {@Code CsvFile}
     */
    public static CsvFile parseFileName(String filename) throws ParseException {
        requireNonNull(filename);
        if (!CsvFile.isValidFileName(filename)) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    CsvFile.MESSAGE_CONSTRAINTS));
        }
        return new CsvFile(filename);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    public static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
