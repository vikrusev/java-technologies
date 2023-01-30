package bg.sofia.uni.fmi.mjt.sentiment.interfaces;

import java.util.Collection;

public interface SentimentAnalyzer {

    /**
     * @param review the text of the review
     * @return the review sentiment as a floating-point number in the interval [0.0,
     *         4.0] if known, and -1.0 if unknown
     */
    public double getReviewSentiment(String review);

    /**
     * @param review the text of the review
     * @return the review sentiment as a name: "negative", "somewhat negative",
     *         "neutral", "somewhat positive", "positive"
     */
    public String getReviewSentimentAsName(String review);

    /**
     * @param word
     * @return the review sentiment of the word as a floating-point number in the
     *         interval [0.0, 4.0] if known, and -1.0 if unknown
     */
    public double getWordSentiment(String word);

    /**
     * @param sentimentValue [0 - 4]
     * @return а review with а sentiment equal to the sentimentValue or null, if there is no such a sentiment
     */
    public String getReview(double sentimentValue);

    /**
     * Returns a collection of the n most frequent words found in the reviews.
     *
     * @throws {@link IllegalArgumentException}, if n is negative
     */
    public Collection<String> getMostFrequentWords(int n);

    /**
     * Returns a collection of the n most positive words in the reviews
     */
    public Collection<String> getMostPositiveWords(int n);

    /**
     * Returns a collection of the n most negative words in the reviews
     */
    public Collection<String> getMostNegativeWords(int n);

    /**
     * @param review The text part of the review
     * @param sentimentValue The given rating
     */
    public void appendReview(String review, int sentimentValue);

    /**
     * Returns the total number of words with known sentiment score
     */
    public int getSentimentDictionarySize();

    /**
     * Returns whether a word is a stopword
     */
    public boolean isStopWord(String word);

}