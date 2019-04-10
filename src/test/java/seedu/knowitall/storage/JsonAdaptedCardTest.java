package seedu.knowitall.storage;

import static org.junit.Assert.assertEquals;
import static seedu.knowitall.storage.JsonAdaptedCard.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.knowitall.testutil.TypicalCards.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import seedu.knowitall.commons.exceptions.IllegalValueException;
import seedu.knowitall.model.card.Answer;
import seedu.knowitall.model.card.Question;
import seedu.knowitall.testutil.Assert;

public class JsonAdaptedCardTest {
    private static final String INVALID_QUESTION = " ";
    private static final String INVALID_ANSWER = " ";
    private static final String INVALID_OPTION = " ";
    private static final String INVALID_HINT = " ";

    private static final String VALID_QUESTION = BENSON.getQuestion().toString();
    private static final String VALID_ANSWER = BENSON.getAnswer().toString();
    private static final String VALID_SCORE = BENSON.getScore().toString();
    private static final List<JsonAdaptedOption> VALID_OPTION = BENSON.getOptions().stream()
            .map(JsonAdaptedOption::new)
            .collect(Collectors.toList());
    private static final List<JsonAdaptedHint> VALID_HINT = BENSON.getHints().stream()
            .map(JsonAdaptedHint::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validCardDetails_returnsCard() throws Exception {
        JsonAdaptedCard card = new JsonAdaptedCard(BENSON);
        assertEquals(BENSON, card.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedCard card = new JsonAdaptedCard(INVALID_QUESTION, VALID_ANSWER, VALID_SCORE, VALID_OPTION,
                VALID_HINT);
        String expectedMessage = Question.MESSAGE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, card::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedCard card = new JsonAdaptedCard(null, VALID_ANSWER, VALID_SCORE, VALID_OPTION, VALID_HINT);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Question.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, card::toModelType);
    }

    @Test
    public void toModelType_invalidAnswer_throwsIllegalValueException() {
        JsonAdaptedCard card = new JsonAdaptedCard(VALID_QUESTION, INVALID_ANSWER, VALID_SCORE, VALID_OPTION,
                VALID_HINT);
        String expectedMessage = Answer.MESSAGE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, card::toModelType);
    }

    @Test
    public void toModelType_nullAnswer_throwsIllegalValueException() {
        JsonAdaptedCard card = new JsonAdaptedCard(VALID_QUESTION, null, VALID_SCORE, VALID_OPTION, VALID_HINT);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Answer.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, card::toModelType);
    }

    @Test
    public void toModelType_invalidHint_throwsIllegalValueException() {
        List<JsonAdaptedHint> invalidHint = new ArrayList<>(VALID_HINT);
        invalidHint.add(new JsonAdaptedHint(INVALID_HINT));
        JsonAdaptedCard card = new JsonAdaptedCard(VALID_QUESTION, VALID_ANSWER,
                VALID_SCORE, VALID_OPTION, invalidHint);
        Assert.assertThrows(IllegalValueException.class, card::toModelType);
    }

    @Test
    public void toModelType_invalidOption_throwsIllegalValueException() {
        List<JsonAdaptedOption> invalidOption = new ArrayList<>(VALID_OPTION);
        invalidOption.add(new JsonAdaptedOption(INVALID_OPTION));
        JsonAdaptedCard card = new JsonAdaptedCard(VALID_QUESTION, VALID_ANSWER, VALID_SCORE, invalidOption,
                VALID_HINT);
        Assert.assertThrows(IllegalValueException.class, card::toModelType);
    }

}
