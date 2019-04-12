package seedu.knowitall.ui;

import static org.junit.Assert.assertEquals;
import static seedu.knowitall.ui.StatusBarFooter.STATUS_IN_FOLDER;
import static seedu.knowitall.ui.StatusBarFooter.STATUS_IN_HOME_DIRECTORY;
import static seedu.knowitall.ui.StatusBarFooter.STATUS_IN_REPORT_DISPLAY;
import static seedu.knowitall.ui.StatusBarFooter.STATUS_IN_TEST_SESSION;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.StatusBarFooterHandle;

public class StatusBarFooterTest extends GuiUnitTest {

    private StatusBarFooterHandle statusBarFooterHandle;
    private StatusBarFooter statusBarFooter;

    @Before
    public void setUp() {
        statusBarFooter = new StatusBarFooter();
        uiPartRule.setUiPart(statusBarFooter);

        statusBarFooterHandle = new StatusBarFooterHandle(statusBarFooter.getRoot());
    }

    @Test
    public void display() {
        // initial state
        assertStatusBarContent(STATUS_IN_HOME_DIRECTORY);

        // new status received
        String testFolderName = "Test";
        guiRobot.interact(() -> statusBarFooter.updateStatusBarInFolder(testFolderName));
        assertStatusBarContent(String.format(STATUS_IN_FOLDER, testFolderName));

        guiRobot.interact(() -> statusBarFooter.updateStatusBarInTestSession());
        assertStatusBarContent(STATUS_IN_TEST_SESSION);

        guiRobot.interact(() -> statusBarFooter.updateStatusBarInReportDisplay());
        assertStatusBarContent(STATUS_IN_REPORT_DISPLAY);
    }

    /**
     * Asserts that the current status of user matches that of {@code expectedCurrentStatus}.
     */
    private void assertStatusBarContent(String expectedCurrentStatus) {
        assertEquals(expectedCurrentStatus, statusBarFooterHandle.getCurrentStatus());
        guiRobot.pauseForHuman();
    }

}
