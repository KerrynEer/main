package seedu.knowitall.model.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.knowitall.model.CardFolder;
import seedu.knowitall.model.ReadOnlyCardFolder;
import seedu.knowitall.model.card.Answer;
import seedu.knowitall.model.card.Card;
import seedu.knowitall.model.card.Option;
import seedu.knowitall.model.card.Question;
import seedu.knowitall.model.card.Score;
import seedu.knowitall.model.hint.Hint;

/**
 * Contains utility methods for populating {@code CardFolder} with sample data.
 */
public class SampleDataUtil {
    public static Card[] getSampleCards() {
        return new Card[] {
            new Card(new Question("Alex Yeoh"), new Answer("87438807"),
                    new Score(5, 10), Collections.emptySet(),
                    getHintSet("friends")),
            new Card(new Question("Bernice Yu"), new Answer("99272758"),
                    new Score(10, 60), Collections.emptySet(),
                    getHintSet("colleagues", "friends")),
            new Card(new Question("Charlotte Oliveiro"), new Answer("93210283"),
                    new Score(0, 24), Collections.emptySet(),
                    getHintSet("neighbours")),
            new Card(new Question("David Li"), new Answer("91031282"),
                    new Score(69, 420), Collections.emptySet(),
                    getHintSet("family")),
            new Card(new Question("Irfan Ibrahim"), new Answer("92492021"),
                    new Score(9, 99), Collections.emptySet(),
                    getHintSet("classmates")),
            new Card(new Question("Roy Balakrishnan"), new Answer("92624417"),
                    new Score(120, 500), Collections.emptySet(),
                    getHintSet("colleagues"))
        };
    }

    public static ReadOnlyCardFolder getSampleCardFolder() {
        CardFolder sampleAb = new CardFolder(getSampleFolderName());
        sampleAb.setFolderScores(getSampleFolderScore());
        for (Card sampleCard : getSampleCards()) {
            sampleAb.addCard(sampleCard);
        }
        return sampleAb;
    }

    public static String getSampleFolderName() {
        return "Sample Folder";
    }

    public static String getSampleFolderFileName() {
        return "Sample Folder.json";
    }

    public static List<Double> getSampleFolderScore() {
        return new ArrayList<>(Arrays.asList(0.5, 0.6, 0.7));
    }

    /**
     * Returns a hint set containing the list of strings given.
     */
    public static Set<Hint> getHintSet(String... strings) {
        return Arrays.stream(strings)
                .map(Hint::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns an option set containing the list of strings given.
     */
    public static Set<Option> getOptionSet(String... strings) {
        return Arrays.stream(strings)
                .map(Option::new)
                .collect(Collectors.toSet());
    }

}
