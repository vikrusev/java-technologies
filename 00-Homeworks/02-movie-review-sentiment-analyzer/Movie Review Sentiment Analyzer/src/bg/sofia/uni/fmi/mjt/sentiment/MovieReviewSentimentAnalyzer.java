package bg.sofia.uni.fmi.mjt.sentiment;

import bg.sofia.uni.fmi.mjt.sentiment.interfaces.SentimentAnalyzer;

import java.io.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static bg.sofia.uni.fmi.mjt.sentiment.enums.ReviewNames.*;

public class MovieReviewSentimentAnalyzer implements SentimentAnalyzer {

    private OutputStream reviewsOutput;

    private HashSet<String> stopwords = new HashSet<>();
    private HashMap<String, MyTuple> dictionary = new HashMap<>();

    public MovieReviewSentimentAnalyzer(InputStream stopwordsInput,
                                        InputStream reviewsInput, OutputStream reviewsOutput) {

        learn(stopwordsInput, reviewsInput, reviewsOutput);
    }

    private void learn(InputStream stopwordsInput, InputStream reviewsInput, OutputStream reviewsOutput) {

        try (BufferedReader stopwordsReader = new BufferedReader(new InputStreamReader(stopwordsInput));
             BufferedReader reviewsReader = new BufferedReader(new InputStreamReader(reviewsInput)))
        {
            this.reviewsOutput = reviewsOutput;

            fillStopwords(stopwordsReader);
            fillDictionary(reviewsReader);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillStopwords(BufferedReader stopwords) throws IOException {
        String line;
        line = stopwords.readLine();

        while (line != null) {
            this.stopwords.add(line);
            line = stopwords.readLine();
        }
    }

    private void fillDictionary(BufferedReader reviews) throws IOException {
        String line;
        line = reviews.readLine();

        while (line != null) {
            line = line.toLowerCase();

            String[] words = line.split("[^A-Za-z0-9]+");

            int reviewScore = Integer.valueOf(words[0]);

            // remove the first element which is a score (a number)
            words = Arrays.copyOfRange(words, 1, words.length);

            getFilteredWords(words)
                    .forEach(w -> {
                        if (!this.dictionary.containsKey(w)) {
                            this.dictionary.put(w, new MyTuple());
                        }
                        this.dictionary.get(w).incrFrequency();
                        this.dictionary.get(w).incrSentimentScoreTemp(reviewScore);
                    });

            line = reviews.readLine();
        }

        this.dictionary.values().forEach(MyTuple::calc);
    }

    private Stream<String> getFilteredWords(String[] words) {
        return Arrays.stream(words).filter(Predicate.not(this::isStopWord));
    }

    public HashSet<String> getStopwords() {
        return this.stopwords;
    }

    /**
     * @param review the text of the review
     * @return the review sentiment as a floating-point number in the interval [0.0,
     *         4.0] if known, and -1.0 if unknown
     */
    public double getReviewSentiment(String review) {
        boolean isUnknown = true;
        int knownWords = 0;
        double sentimentalScoreTemp = 0;

        String[] words = review.split("[^A-Za-z0-9]+");

        for (String word : words) {
            if (!isStopWord(word) && this.dictionary.containsKey(word)) {
                isUnknown = false;
                knownWords++;
                sentimentalScoreTemp += this.dictionary.get(word).getSentimentScore();
            }
        }

        return isUnknown ? -1.0 : sentimentalScoreTemp / knownWords;
    }

    /**
     * @param review the text of the review
     * @return the review sentiment as a name: "negative", "somewhat negative",
     *         "neutral", "somewhat positive", "positive", or "unknown" if == -1.0
     */
    public String getReviewSentimentAsName(String review) {
        switch((int)Math.round(getReviewSentiment(review))) {
            case 0: return NEGATIVE.getName();
            case 1: return SOMEWHAT_NEGATIVE.getName();
            case 2: return NEUTRAL.getName();
            case 3: return SOMEWHAT_POSITIVE.getName();
            case 4: return POSITIVE.getName();
            default: return UNKNOWN.getName();
        }
    }
    /**
     * @param word
     * @return the review sentiment of the word as a floating-point number in the
     *         interval [0.0, 4.0] if known, and -1.0 if unknown
     */
    public double getWordSentiment(String word) {
        return this.dictionary.containsKey(word.toLowerCase()) ? this.dictionary.get(word).getSentimentScore() : -1.0;
    }

    /**
     * @param sentimentValue [0 - 4]
     * @return а review with а sentiment equal to the sentimentValue or null, if there is no such a sentiment
     */
    public String getReview(double sentimentValue) {
        return null;
    }

    /**
     * Returns a collection of the n most frequent words found in the reviews.
     * Sorted
     *
     * @throws {@link IllegalArgumentException}, if n is negative
     */
    public Collection<String> getMostFrequentWords(int n) throws IllegalArgumentException {
        if (n < 0) throw new IllegalArgumentException();

        List<String> words = this.dictionary.entrySet().stream()
                .sorted(Comparator.comparingInt(o -> o.getValue().getFrequency()))
                .skip(this.dictionary.size() - n)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        Collections.reverse(words);
        return words;
    }

    /**
     * Returns a collection of the n most positive words in the reviews
     */
    public Collection<String> getMostPositiveWords(int n) throws IllegalArgumentException {
        if (n < 0) throw new IllegalArgumentException();

        List<String> words = this.dictionary.entrySet().stream()
                .sorted(Comparator.comparingDouble(o -> o.getValue().getSentimentScore()))
                .skip(this.dictionary.size() - n)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        Collections.reverse(words);
        return words;
    }

    /**
     * Returns a collection of the n most negative words in the reviews
     * sorted and then taken the first n words
     */
    public Collection<String> getMostNegativeWords(int n) throws IllegalArgumentException {
        if (n < 0) throw new IllegalArgumentException();

        return this.dictionary.entrySet().stream()
                .sorted(Comparator.comparingDouble(o -> o.getValue().getSentimentScore()))
                .limit(n)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * @param review The text part of the review
     * @param sentimentValue The given rating
     */
    public void appendReview(String review, int sentimentValue) {
        /*String line = sentimentValue + " " + review + System.lineSeparator();
        try {
            (new ByteArrayOutputStream(line.getBytes())).writeTo(this.reviewsOutput);
        } catch (IOException e) {
            System.err.println("Could not write to stream");
            e.printStackTrace();
        }*/
    }

    /**
     * Returns the total number of words with known sentiment score
     */
    public int getSentimentDictionarySize() {
        return this.dictionary.size();
    }

    /**
     * Returns whether a word is a stopword
     */
    public boolean isStopWord(String word) {
        return this.stopwords.contains(word);
    }

}
