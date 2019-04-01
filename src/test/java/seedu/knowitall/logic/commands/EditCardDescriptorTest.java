package seedu.knowitall.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.knowitall.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.knowitall.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.knowitall.logic.commands.CommandTestUtil.VALID_ANSWER_2;
import static seedu.knowitall.logic.commands.CommandTestUtil.VALID_HINT_HUSBAND;
import static seedu.knowitall.logic.commands.CommandTestUtil.VALID_OPTION_2;
import static seedu.knowitall.logic.commands.CommandTestUtil.VALID_QUESTION_2;

import org.junit.Test;

import seedu.knowitall.logic.commands.EditCommand.EditCardDescriptor;
import seedu.knowitall.testutil.EditCardDescriptorBuilder;

public class EditCardDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditCardDescriptor descriptorWithSameValues = new EditCardDescriptor(DESC_AMY);
        assertTrue(DESC_AMY.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_AMY.equals(DESC_AMY));

        // null -> returns false
        assertFalse(DESC_AMY.equals(null));

        // different types -> returns false
        assertFalse(DESC_AMY.equals(5));

        // different values -> returns false
        assertFalse(DESC_AMY.equals(DESC_BOB));

        // different question -> returns false
        EditCommand.EditCardDescriptor editedAmy =
                new EditCardDescriptorBuilder(DESC_AMY).withQuestion(VALID_QUESTION_2).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different answer -> returns false
        editedAmy = new EditCardDescriptorBuilder(DESC_AMY).withAnswer(VALID_ANSWER_2).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different option -> returns false
        editedAmy = new EditCardDescriptorBuilder(DESC_AMY).withOptions(VALID_OPTION_2).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different hint -> returns false
        editedAmy = new EditCardDescriptorBuilder(DESC_AMY).withHint(VALID_HINT_HUSBAND).build();
        assertFalse(DESC_AMY.equals(editedAmy));
    }
}
