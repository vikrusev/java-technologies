package bg.sofia.uni.fmi.jira;

public class Component {

    private String name = null;
    private String shortName = null;

    private User creator = null;

    public Component(String name, String shortName, User creator) {
        this.name = name;
        this.shortName = shortName;
        this.creator = creator;
    }

    public String getShortName() {
        return this.shortName;
    }
}
