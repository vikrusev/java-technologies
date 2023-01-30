package bg.sofia.uni.fmi.mjt.git;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class SampleRepositoryTest {

    private Repository repo;

    @Before
    public void setUp() {
        repo = new Repository();
    }

    @Test
    public void testAdd_MultipleFiles() {
        Result actual = repo.add("foo.txt", "bar.txt", "baz.txt");
        assertSuccess("added foo.txt, bar.txt, baz.txt to stage", actual);
    }

    @Test
    public void myTest() {
        repo.add("test");
        repo.commit("initial");
        repo.add("test2");
        repo.commit("second");

        repo.createBranch("nessy");
        repo.checkoutBranch("nessy");

        repo.checkoutCommit("3cb7553dc2e8212d657e8d67438ddaddd6b58932");
        Result result1 = repo.log();
        repo.checkoutCommit("ca02e785538243471a44b64fa7a77a2a8ffcbfef");
        Result result2 = repo.log();

        assertSuccess("", result2);
    }

    @Test
    public void logForNewBranch() {
        repo.add("master.txt");
        repo.commit("Initial master");
        Result result4 = repo.log();

        Result result1 = repo.createBranch("nessy");
        Result result2 = repo.createBranch("nessy");

        repo.checkoutBranch("nessy");
        Result result6 = repo.log();

        assertSuccess("created branch nessy", result1);
        assertFail("branch nessy already exists", result2);
        assertSuccess(result4.getMessage(), result6);
    }

    @Test
    public void checkoutCommitCanSwitchCommits() {

        repo.add("master");
        repo.commit("first commit");

        repo.remove("master");
        repo.commit("second commit");

        repo.add("second master", "third master");
        repo.commit("third commit");

        repo.checkoutCommit("99744fc1959444a2358d08b518bfea3cd4b0213e");
        Result result = repo.log();
        repo.checkoutCommit("cedb5799da418efb44a819092d397e996fa67d17");
        Result result2 = repo.log();

        //assertSuccess("asd", result2);
    }

    @Test
    public void removeSingleFileFromStagingArea() {

        repo.add("foo.txt");
        Result result = repo.remove("foo.txt");
        repo.commit("First commit");

        assertSuccess("added foo.txt for removal", result);
    }

    @Test
    public void removeSingleFileFromStagingArea2() {
        Result result1 = repo.add("test.txt");
        Result result2 = repo.remove("test.txt");
        Result result3 = repo.commit("test");
        assertFail("nothing to commit, working tree clean", result3);
    }

    @Test
    public void Lilyana() {
        Repository repo = new Repository();
        repo.add("foo.txt", "bar.txt");
        repo.remove("foo.txt");
        Result result = repo.commit("First commit");

        assertSuccess("1 files changed", result);
    }

    @Test
    public void logForSingleCommit() {

        repo.add("foo.txt", "bar.txt");
        repo.commit("First commit");
        Result result = repo.log();

        assertSuccess("2 files changed", result);
    }

    @Test
    public void testRemove_DoesNothingWhenAnyFileIsMissing() {
        repo.add("foo.txt", "bar.txt");

        Result actual = repo.remove("foo.txt", "baz.txt");
        assertFail("'baz.txt' did not match any files", actual);

        actual = repo.commit("After removal");
        assertSuccess("2 files changed", actual);
    }

    @Test
    public void testCheckoutBranch_CanSwitchBranches() {
        repo.add("src/Main.java");
        repo.commit("Add Main.java");

        repo.createBranch("dev");
        Result actual = repo.checkoutBranch("dev");
        assertSuccess("switched to branch dev", actual);

        repo.remove("src/Main.java");
        repo.commit("Remove Main.java");
        assertEquals("Remove Main.java", repo.getHead().getMessage());

        actual = repo.checkoutBranch("master");
        assertSuccess("switched to branch master", actual);
        assertEquals("Add Main.java", repo.getHead().getMessage());
    }

    private static void assertFail(String expected, Result actual) {
        assertFalse(actual.isSuccessful());
        assertEquals(expected, actual.getMessage());
    }

    private static void assertSuccess(String expected, Result actual) {
        assertTrue(actual.isSuccessful());
        assertEquals(expected, actual.getMessage());
    }
}