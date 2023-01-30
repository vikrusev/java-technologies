package bg.sofia.uni.fmi.mjt.meetup.dto;

public class Group {
    private String urlname;

    public String getUrlname() {
        return this.urlname;
    }

    @Override
    public String toString() {
        return "group urlname: " + this.urlname;
    }
}
