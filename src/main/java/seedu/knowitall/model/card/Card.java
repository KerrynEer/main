package seedu.knowitall.model.card;

import static seedu.knowitall.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.knowitall.model.hint.Hint;

/**
 * Represents a Card in the card folder.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Card {
    public static final int MAX_QUESTIONS = 1;
    public static final int MAX_ANSWERS = 1;
    public static final int MAX_HINTS = 1;
    public static final int MAX_OPTIONS = 3;

    /**
     * {@code CardType} representing the type of Card question.
     */
    public enum CardType {
        MCQ,
        SINGLE_ANSWER
    }
    private int answerIndex;
    private List<String> completeOptions;

    // Identity fields
    private final Question question;
    private final Answer answer;

    // Data fields
    private final Score score;
    private final Set<Option> options = new HashSet<>();
    private final Set<Hint> hints = new HashSet<>();
    private CardType type;

    /**
     * Every field must be present and not null.
     */
    public Card(Question question, Answer answer, Score score, Set<Option> options, Set<Hint> hints) {
        requireAllNonNull(question, answer, score, options, hints);
        this.question = question;
        this.answer = answer;
        this.score = score;
        this.options.addAll(options);
        this.hints.addAll(hints);

        this.completeOptions = new ArrayList<>();
        options.forEach(option -> completeOptions.add(option.optionValue));
        completeOptions.add(answer.fullAnswer);
        answerIndex = completeOptions.indexOf(answer.fullAnswer) + 1;

        if (options.isEmpty()) {
            this.type = CardType.SINGLE_ANSWER;
        } else {
            this.type = CardType.MCQ;
        }
    }

    public Question getQuestion() {
        return question;
    }

    public Answer getAnswer() {
        return answer;
    }

    public Score getScore() {
        return score;
    }

    /**
     * Returns an immutable {@code Hint} set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Hint> getHints() {
        return Collections.unmodifiableSet(hints);
    }

    /**
     * Returns an immutable {@code Option} set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Option> getOptions() {
        return Collections.unmodifiableSet(options);
    }

    /**
     * Returns the assigned {@code CardType} of this Card.
     */
    public CardType getCardType() {
        return this.type;
    }

    /**
     * Returns a list of options, inclusive of the answer, for MCQ cards.
     */
    public List<String> getCompleteMcqOptions() {
        return completeOptions;
    }

    public int getAnswerIndex() {
        return answerIndex;
    }

    /**
     * Randomizes the order of completeOptions list and updates answerIndex.
     */
    public void shuffleMcqOptions() {
        Collections.shuffle(completeOptions);
        answerIndex = completeOptions.indexOf(answer.fullAnswer) + 1;
        System.out.println(answerIndex);
    }

    /**
     * Returns true if both cards of the same question also have the same answer, but not necessarily the same hint.
     * This defines a weaker notion of equality between two cards.
     */
    public boolean isSameCard(Card otherCard) {
        if (otherCard == this) {
            return true;
        }

        return otherCard != null
                && otherCard.getQuestion().equals(getQuestion())
                && (otherCard.getAnswer().equals(getAnswer()));
    }

    /**
     * Returns true if both cards have the same identity and data fields.
     * This defines a stronger notion of equality between two cards.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Card)) {
            return false;
        }

        Card otherCard = (Card) other;
        return otherCard.getQuestion().equals(getQuestion())
                && otherCard.getAnswer().equals(getAnswer())
                && otherCard.getOptions().equals(getOptions())
                && otherCard.getScore().equals(getScore())
                && otherCard.getHints().equals(getHints());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(question, answer, score, hints);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getQuestion())
                .append("\nAnswer: ")
                .append(getAnswer())
                .append("\nIncorrect options: ")
                .append(getOptions())
                .append("\nScore: ")
                .append(getScore())
                .append("\nHints: ");
        getHints().forEach(builder::append);
        return builder.toString();
    }
}
