package bg.sofia.uni.fmi.mjt.meetup;

import bg.sofia.uni.fmi.mjt.meetup.dto.Event;
import bg.sofia.uni.fmi.mjt.meetup.dto.Photo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class MeetupClient {

    private HttpClient client;
    private String apiKey; //23f7e5276333b247422225b4454d22

    private static final String API_URI = "https://api.meetup.com/";
    private static final String EVENTS_URI = "find/events";
    private static final String PHOTO_ALBUMS = "/photo_albums/";
    private static final int NOT_FOUND = 404;

    public static void main(String[] args) {
        HttpClient client = HttpClient.newHttpClient();
        String apiKey = "23f7e5276333b247422225b4454d22";

        MeetupClient meetup = new MeetupClient(client, apiKey);

        List<Event> eventsNearby = meetup.getEventsNearby();
        List<Event> eventsNearbyWithVenueName = meetup.getEventsWithVenueName(eventsNearby.get(0).getName());
        List<Event> eventsWithKeywords = meetup.getEventsWithKeywords(List.of("test", "one", "two"));
        Event eventMaxDuration = meetup.getEventWithMaxDuration();
        Event eventUrlnameAndId = meetup.getEvent("Outdooraholics", "255666687");

        List<Photo> photos = meetup.getAlbumPhotos("Cloud-Native-Computing-Bulgaria", "29473987");
        meetup.downloadPhotoAlbum("Cloud-Native-Computing-Bulgaria", "29473987", Path.of("downloads/"));
    }

    public MeetupClient(HttpClient client, String apiKey) {
        this.client = client;
        this.apiKey = apiKey;
    }

    /**
     * Fetches the nearby events.
     */
    public List<Event> getEventsNearby() {

        try {
            String url = API_URI + EVENTS_URI + "?key=" + this.apiKey;
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

            HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());

            Type type = new TypeToken<List<Event>>() { }
                            .getType();

            return new Gson().fromJson(response.body(), type);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Fetches the nearby events with the given venue name. The comparison is case
     * insensitive.
     *
     * @param venueName
     *            - the event venue name
     */
    public List<Event> getEventsWithVenueName(String venueName) {

        List<Event> allEvents = this.getEventsNearby();

        allEvents.removeIf(e -> {
            if (e.getVenue() == null) {
                return true;
            }

            return !e.getVenue().getName().equalsIgnoreCase(venueName);
        });

        return allEvents;
    }

    /**
     * Fetches the nearby events whose descriptions contains all of the given
     * keywords. The comparison is case insensitive.
     *
     * @param keywords
     *            - the keywords to search in the event description
     */
    public List<Event> getEventsWithKeywords(Collection<String> keywords) {

        List<Event> allEvents = this.getEventsNearby();

        allEvents.removeIf(e -> !hasAllKeywords(e, keywords));

        return allEvents;
    }

    private boolean hasAllKeywords(Event event, Collection<String> keywords) {
        if (event.getDesc() == null) {
            return false;
        }

        String desc = event.getDesc().toLowerCase();

        for (String word : keywords) {
            if (!desc.contains(word.toLowerCase())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Fetches the nearby event with max duration. Returns null when no events are
     * found.
     */
    public Event getEventWithMaxDuration() {

        return this.getEventsNearby().stream()
                .max(Comparator.comparing(Event::getDuration))
                .orElse(null);
    }

    /**
     * Fetches an event by group urlname and event id. Returns null in case of a
     * missing event.
     *
     * @param urlname
     *            - the event group urlname
     * @param id
     *            - the event id
     */
    Event getEvent(String urlname, String id) {

        try {
            String url = API_URI + urlname + "/events/" + id + "?key=" + this.apiKey;
            Type type = new TypeToken<Event>() { }
                            .getType();

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

            HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == NOT_FOUND) {
                return null;
            }

            return new Gson().fromJson(response.body(), type);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }

        /*List<Event> allEvents = this.getEventsNearby();

        allEvents.removeIf(e -> !(e.getId().equals(id) && e.getGroup().getUrlname().equals(urlname)));

        return allEvents.size() != 0 ? allEvents.get(0) : null;*/
    }

    /**
     * Fetches photos for the photo album with the given id. Returns null in case of
     * a missing photo album.
     *
     * @param urlname
     *            - the photo album group urlname
     * @param id
     *            - the photo album id
     */
    public List<Photo> getAlbumPhotos(String urlname, String id) {

        try {
            String url = API_URI + urlname + PHOTO_ALBUMS + id + "/photos?key=" + this.apiKey;
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

            HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.body().isEmpty()) {
                return null;
            }

            Type type = new TypeToken<List<Photo>>() { }
                            .getType();

            if (response.statusCode() == NOT_FOUND) {
                return null;
            }

            return new Gson().fromJson(response.body(), type);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Performs a parallel download of the photos from the given photo album to the
     * given target directory. A folder with the album id is being created in the
     * target directory. The photos are downloaded in the newly created album
     * directory. The file name of each photo is its id.
     *
     * @param urlname
     *            - the photo album group urlname
     * @param albumId
     *            - the photo album id
     * @param target
     *            - the target directory to save the photo album
     * @throws IOException - da
     */
    public void downloadPhotoAlbum(String urlname, String albumId, Path target) {

        String path = target.toString() + File.separator + albumId;
        createDir(path);

        List<Photo> photos = this.getAlbumPhotos(urlname, albumId);

        if (photos == null) {
            System.err.println("Photos not found!");
            return;
        }

        downloadPhotos(path, photos);
    }

    private void createDir(String path) {

        File targetDir = new File(path);
        if (!targetDir.exists()) {
            if (!targetDir.mkdirs()) {
                System.err.println("Directory was not created!");
                System.exit(0);
            }
        }
    }

    private void downloadPhotos(String path, List<Photo> photos) {
        for (Photo photo : photos) {

            try (InputStream in = new URL(photo.getPhotoLink()).openStream()) {

                String photoLink = photo.getPhotoLink();
                String photoExt = photoLink.substring(photoLink.lastIndexOf('.'));

                String photoName = String.valueOf(photo.getId());
                String photoPath = path + File.separator + photoName + photoExt;

                File p = new File(photoPath);

                if (!p.exists()) {
                    Files.copy(in, Paths.get(photoPath));
                }

            } catch (IOException e) {
                System.err.println("Downloading a photo problem!");
                e.printStackTrace();
            }
        }
    }
}