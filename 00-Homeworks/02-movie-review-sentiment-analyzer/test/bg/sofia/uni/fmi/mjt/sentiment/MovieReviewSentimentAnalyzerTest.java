package bg.sofia.uni.fmi.mjt.sentiment;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class MovieReviewSentimentAnalyzerTest {

    private static MovieReviewSentimentAnalyzer sentiment;
    private static final double delta = 0.01;

    @BeforeClass
    public static void initializeMovieReviewSentimentAnalyzer() {

        File stopwordsFile = new File("resources/stopwords.txt");
        File reviewsFile = new File("resources/reviews.txt");

        try (BufferedInputStream stopwordsReader = new BufferedInputStream(new FileInputStream(stopwordsFile));
             BufferedInputStream reviewsReader = new BufferedInputStream(new FileInputStream(reviewsFile));
             ByteArrayOutputStream output = new ByteArrayOutputStream())
        {

            sentiment = new MovieReviewSentimentAnalyzer(stopwordsReader, reviewsReader, output);

        } catch (FileNotFoundException e) {
            System.err.println("A file has not been found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Unexpected IO exception.");
            e.printStackTrace();
        }
    }

    @Test
    public void allStopWordsAreAdded() {
        final int stopwordsCount = 174;
        assertEquals("Not all stopwords are loaded", stopwordsCount,
                sentiment.getStopwords().size());
    }

    @Test
    public void wordIsAStopword() {
        assertTrue("The stopword is not known as a stopword",
                sentiment.isStopWord("a"));
    }

    @Test
    public void wordIsNotAStopword() {
        assertFalse("The non-stopword is a stopword",
                sentiment.isStopWord("RandomNonStopword"));
    }

    @Test
    public void dictionaryIsNotEmpty() {
        assertTrue("Dictionary should be filled",
                sentiment.getSentimentDictionarySize() != 0);
    }

    @Test
    public void reviewSentimentShouldBeUnknown() {
        final double expected = -1.0;
        assertEquals("Review sentimental score is not 'unknown'", expected,
                sentiment.getReviewSentiment("This is an unknown review"), delta);
    }

    @Test
    public void reviewSentimentShouldBeAValue() {
        final double expected = 2.5;
        assertEquals("Review sentimental score is 'unknown'", expected,
                sentiment.getReviewSentiment("series quiet quiet fans"), delta);
    }

    @Test
    public void reviewNameShouldBeUnknown() {
        assertEquals("Review sentimental name is not 'unknown'", "unknown",
                sentiment.getReviewSentimentAsName("Unknown review"));
    }

    @Test
    public void reviewNameShouldBeKnown() {
        assertEquals("Review sentimental name is not correct 'unknown'", "positive",
                sentiment.getReviewSentimentAsName("The performances are an absolute joy ."));
    }

    @Test
    public void sentimentScoreOfWordShouldBeNegative() {
        final double expected = -1.0;
        assertEquals("Sentiment score of 'unknown' word is not -1.0", expected,
                sentiment.getWordSentiment("Stoyo"), delta);
    }

    @Test
    public void sentimentScoreOfWordShouldBeNumber() {
        final double expected = 1.66;
        assertEquals("Sentiment score of 'known' word is wrong", expected,
                sentiment.getWordSentiment("good"), delta);
    }

    @Test (expected = IllegalArgumentException.class)
    public void getMostFrequentWordsShouldThrowException() {
        final int n = -1;
        sentiment.getMostFrequentWords(n);
    }

    @Test
    public void getMostFrequentWordsShouldReturnMostFrequentWords() {
        final int n = 3;

        String[] expected = {"s", "like", "good"};

        Assert.assertArrayEquals("Wrong most frequent words", expected,
                sentiment.getMostFrequentWords(n).toArray());
    }

    @Test
    public void getMostPositiveWordsShouldReturnMostPositiveWords() {
        final int n = 3;

        String[] expected = {"proportions", "performance", "seeking"};

        Assert.assertArrayEquals("Wrong most frequent words", expected,
                sentiment.getMostPositiveWords(n).toArray());
    }

    @Test
    public void getMostNegativeWordsShouldReturnMostNegativeWords() {
        final int n = 3;

        String[] expected = {"reason", "big", "none"};

        Assert.assertArrayEquals("Wrong most frequent words", expected,
                sentiment.getMostNegativeWords(n).toArray());
    }

    @Test
    public void test() {
        //sentiment.appendReview("testtttt", 2);
    }

}
