package seedu.knowitall.model.card;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.knowitall.testutil.Assert;

public class ScoreTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Score(null));
    }

    @Test
    public void constructor_invalidScore_throwsIllegalArgumentException() {
        String invalidScore = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Score(invalidScore));
    }

    @Test
    public void isValidScore() {
        // null knowitall
        Assert.assertThrows(NullPointerException.class, () -> Score.isValidScore(null));

        // invalid addresses
        assertFalse(Score.isValidScore("")); // empty string
        assertFalse(Score.isValidScore(" ")); // spaces only

        // partial score strings
        assertFalse(Score.isValidScore("0/")); // missing total attempts
        assertFalse(Score.isValidScore("/0")); // missing correct attempts
        assertFalse(Score.isValidScore("00")); // missing separator
        assertFalse(Score.isValidScore("//")); // missing numbers

        // invalid numbers
        assertFalse(Score.isValidScore("0/1.0")); // float in total
        assertFalse(Score.isValidScore("1.0/0")); // float in correct
        assertFalse(Score.isValidScore("1.0/1.0")); // float in both
        assertFalse(Score.isValidScore("-1/2")); // negative in correct
        assertFalse(Score.isValidScore("1/-2")); // negative in total

        // invalid correct and total
        assertFalse(Score.isValidScore("3/2")); // correct more than total
        assertFalse(Score.isValidScore("10/0"));


        // tests for overloaded isValidScore
        assertFalse(Score.isValidScore(10, 5)); // correct more than total
        assertFalse(Score.isValidScore(-5, 10)); // negative correct
        assertFalse(Score.isValidScore(5, -10)); // negative total

        // valid scores
        assertTrue(Score.isValidScore("0/0"));
        assertTrue(Score.isValidScore("0/10"));
        assertTrue(Score.isValidScore("1/10"));
        assertTrue(Score.isValidScore("10/10"));

        // tests for overloaded isValidScore
        assertTrue(Score.isValidScore(0, 0));
        assertTrue(Score.isValidScore(0, 10));
        assertTrue(Score.isValidScore(1, 10));
        assertTrue(Score.isValidScore(10, 10));
    }

    @Test
    public void compareTo() {
        Score noneScore = new Score(0, 0);
        Score zeroScore = new Score(0, 2);
        Score halfScore = new Score(1, 2);
        Score fullScore = new Score(2, 2);

        assertTrue(zeroScore.compareTo(noneScore) == 0);
        assertTrue(halfScore.compareTo(zeroScore) > 1);
        assertTrue(fullScore.compareTo(halfScore) > 1);
        assertTrue(fullScore.compareTo(fullScore) == 0);
    }
}
