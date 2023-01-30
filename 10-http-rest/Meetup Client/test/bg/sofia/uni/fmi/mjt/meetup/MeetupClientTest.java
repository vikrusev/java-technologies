package bg.sofia.uni.fmi.mjt.meetup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.gson.Gson;

import bg.sofia.uni.fmi.mjt.meetup.dto.Event;
import bg.sofia.uni.fmi.mjt.meetup.dto.Venue;
import bg.sofia.uni.fmi.mjt.meetup.dto.Photo;

@RunWith(MockitoJUnitRunner.Silent.class)
public class MeetupClientTest {

    private static final int STATUS_OK = 200;

    @Mock
    private HttpClient httpClientMock;

    @Mock
    private HttpResponse<String> httpResponseMock;

    private MeetupClient client;

    @Before
    public void setUp() {
        client = new MeetupClient(httpClientMock, null);
    }

    @Test
    public void testGetEventsNearbyWithEmptyList() throws Exception {
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);
        when(httpResponseMock.body()).thenReturn("[]");

        List<Event> actual = client.getEventsNearby();
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testGetEventsWithVenueNameWithTwoEvents() throws Exception {
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);

        List<Event> events = List.of(new Event("Java meetup", new Venue("Office foo", "Sofia")),
                new Event("Java meetup #2", new Venue("Cool office", "Sofia")));
        String json = new Gson().toJson(events);
        when(httpResponseMock.body()).thenReturn(json);

        List<Event> actual = client.getEventsWithVenueName("Office foo");

        assertEquals(1, actual.size());
        assertEquals("Java meetup", actual.get(0).getName());
    }

    @Test
    public void testGetEventWithKeywords() throws Exception {
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);

        List<Event> events = List.of(new Event("Java meetup", new Venue("Office foo", "Sofia")),
                new Event("Java meetup #2", new Venue("Cool office", "Sofia")));

        events.get(0).setDesc("test | one two this is not a keyword");
        events.get(1).setDesc("test | one two");

        String json = new Gson().toJson(events);
        when(httpResponseMock.body()).thenReturn(json);
        when(httpResponseMock.statusCode()).thenReturn(STATUS_OK);

        List<Event> actual = client.getEventsWithKeywords(List.of("test", "this", "one"));
        assertEquals(1, actual.size());
        assertEquals("Java meetup", actual.get(0).getName());
        assertEquals("Office foo", actual.get(0).getVenue().getName());
    }

    @Test
    public void testGetEventWithMaxDuration() throws Exception {
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);

        List<Event> events = List.of(new Event("Java meetup", new Venue("Office foo", "Sofia")),
                new Event("Java meetup #2", new Venue("Cool office", "Sofia")));

        final int lowDuration = 1000;
        final int longDuration = 10000;
        events.get(0).setDuration(lowDuration);
        events.get(1).setDuration(longDuration);

        String json = new Gson().toJson(events);
        when(httpResponseMock.body()).thenReturn(json);
        when(httpResponseMock.statusCode()).thenReturn(STATUS_OK);

        Event actual = client.getEventWithMaxDuration();
        assertEquals("Java meetup #2", actual.getName());
        assertEquals("Cool office", actual.getVenue().getName());
    }

    @Test
    public void testGetEventReturnsEvent() throws Exception {
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);

        Event event = new Event("Java meetup #2", new Venue("Office foo", "Sofia"));
        String json = new Gson().toJson(event);
        when(httpResponseMock.body()).thenReturn(json);
        when(httpResponseMock.statusCode()).thenReturn(STATUS_OK);

        Event actual = client.getEvent("foo", "bar");
        assertEquals("Java meetup #2", actual.getName());
        assertEquals("Office foo", actual.getVenue().getName());
    }

    @Test
    public void testGetAlbumPhotos() throws Exception {
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);

        final int firstId = 1234567890;
        final int secondId = 12345;
        List<Photo> event = List.of(new Photo(firstId, "first url"),
                new Photo(secondId, "second url"));

        String json = new Gson().toJson(event);

        when(httpResponseMock.body()).thenReturn(json);
        when(httpResponseMock.statusCode()).thenReturn(STATUS_OK);

        List<Photo> actual = client.getAlbumPhotos("some%20urlname", "1234567890");
        assertEquals(2, actual.size());
        assertEquals(firstId, actual.get(0).getId());
        assertEquals("second url", actual.get(1).getPhotoLink());
    }
}