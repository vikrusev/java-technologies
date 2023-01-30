package bg.sofia.uni.fmi.mjt.meetup.dto;

public class Event {

    private String id;
    private String name;
    private String status;
    private long duration;

    private Venue venue;
    private String description;
    private Group group;

    public Event(String name, Venue venue) {
        this.name = name;
        this.venue = venue;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Venue getVenue() {
        return this.venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public Group getGroup() {
        return this.group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getDesc() {
        return this.description;
    }

    public void setDesc(String desc) {
        this.description = desc;
    }

    @Override
    public String toString() {
        System.out.println(venue);
        return "Name: " + this.name + System.lineSeparator() + "Duration: " + this.duration;
    }

}
