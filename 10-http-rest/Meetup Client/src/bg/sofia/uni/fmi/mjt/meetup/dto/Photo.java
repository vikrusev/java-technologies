package bg.sofia.uni.fmi.mjt.meetup.dto;

import com.google.gson.annotations.SerializedName;

public class Photo {

    private long id;
    @SerializedName("photo_link") private String photoLink;

    public Photo(long id, String photoLink) {
        this.id = id;
        this.photoLink = photoLink;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhotoLink() {
        return this.photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    @Override
    public String toString() {
        return "Photo id: " + this.id + System.lineSeparator() + "Photo link: " + this.photoLink;
    }
}
