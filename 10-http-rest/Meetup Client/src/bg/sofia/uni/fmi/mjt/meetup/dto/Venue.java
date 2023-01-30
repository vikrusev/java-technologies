package bg.sofia.uni.fmi.mjt.meetup.dto;

public class Venue {

    private String name;
    private String city;

    public Venue(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Name: " + this.name + System.lineSeparator() + "City: " + this.city;
    }

}
