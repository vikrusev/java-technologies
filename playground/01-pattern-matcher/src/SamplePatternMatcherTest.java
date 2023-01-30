import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SamplePatternMatcherTest {

    @Test
    public void testMatch_WithoutSpecialChars() {
        assertTrue(PatternMatcher.match("abcdef", "de"));
    }

    @Test
    public void testMatch_WithSingleQuestionMark() {
        assertTrue(PatternMatcher.match("abcdef", "d?"));
    }

    @Test
    public void testMatch_WithSingleStarMark() {
        assertTrue(PatternMatcher.match("abcdef", "*ef"));
    }

    @Test
    public void testMatch_AllSpecialChars() {
        assertFalse(PatternMatcher.match("abcdef", "a?cd*g"));
    }

    @Test
    public void testMatch_WithLastChar() {
        assertTrue(PatternMatcher.match("test", "est"));
    }

    @Test
    public void testMatch_DoubleOccurrence() { assertTrue(PatternMatcher.match("tetestPesho", "esh")); }

    @Test
    public void testMatch_StarCountsZeroCharsAtEnd() { assertTrue(PatternMatcher.match("tetestPesho", "esho*")); }

    @Test
    public void testMatch_StarCountsZeroCharsAtBeginning() { assertTrue(PatternMatcher.match("tetestPesho", "*esho")); }

    @Test
    public void testMatch_StarCountsZeroCharsInMiddle() { assertTrue(PatternMatcher.match("tetestPesho", "P*e")); }

    @Test
    public void testMatch_DoubleOccurrenceWithLastChar() {
        assertTrue(PatternMatcher.match("tetestPesho", "esho"));
    }

    @Test
    public void testMatch_SpecialChars() {
        assertTrue(PatternMatcher.match("tetestPesho", "e*ho"));
    }

    @Test
    public void testMatch_ValidLongerSubStringOne() { assertTrue(PatternMatcher.match("pesho", "pesho*")); }

    @Test
    public void testMatch_ValidLongerSubStringN() { assertTrue(PatternMatcher.match("o", "*o*****")); }

    @Test
    public void testMatch_QuestionAtEnd() { assertFalse(PatternMatcher.match("o", "o?")); }

    @Test
    public void testMatch_EmptyFirstString() { assertTrue(PatternMatcher.match("", "*")); }

    @Test
    public void testMatch_IDEK() { assertFalse(PatternMatcher.match("al", "l?")); }

    @Test
    public void testMatch_IDEKK() { assertFalse(PatternMatcher.match("al", "???")); }

    @Test
    public void testMatch_IDEKKK() { assertFalse(PatternMatcher.match("ab", "ba")); }

    @Test
    public void testMatch_IDEKKKK() { assertFalse(PatternMatcher.match("a*acba", "a*cba")); }

}