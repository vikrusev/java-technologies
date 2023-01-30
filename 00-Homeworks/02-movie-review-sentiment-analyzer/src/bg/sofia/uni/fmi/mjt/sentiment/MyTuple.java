package bg.sofia.uni.fmi.mjt.sentiment;

public class MyTuple {
    private int frequency = 0;
    private int sentimentScoreTemp = 0;
    private double sentimentScore = 0;

    public void incrFrequency() {
        this.frequency++;
    }

    public void incrSentimentScoreTemp(int value) {
        this.sentimentScoreTemp += value;
    }

    public void calc() {
        double sentimentScore = (double)this.sentimentScoreTemp / this.frequency;
        setSentimentScore(sentimentScore);
    }

    public void setSentimentScore(double value) {
        this.sentimentScore = value;
    }

    public int getFrequency() {
        return this.frequency;
    }

    public double getSentimentScore() {
        return this.sentimentScore;
    }

    /*public void print() {
        System.out.println(this.frequency);
        System.out.println(this.sentimentScore);
    }*/

}
