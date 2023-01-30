package bg.sofia.uni.fmi.mjt.sentiment.enums;

public enum ReviewNames {

    NEGATIVE("negative"), SOMEWHAT_NEGATIVE("somewhat negative"),
    NEUTRAL("neutral"), SOMEWHAT_POSITIVE("somewhat positive"),
    POSITIVE("positive"), UNKNOWN("unknown");

    private String name;

    ReviewNames(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
