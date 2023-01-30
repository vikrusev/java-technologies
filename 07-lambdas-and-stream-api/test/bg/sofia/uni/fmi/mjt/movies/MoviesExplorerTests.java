package bg.sofia.uni.fmi.mjt.movies;

import bg.sofia.uni.fmi.mjt.movies.model.Actor;
import bg.sofia.uni.fmi.mjt.movies.model.Movie;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class MoviesExplorerTests {

    private MoviesExplorer movies;

    @Before
    public void initMovies() {
        String str = "First(1992)/Viktor, Rusev1/Alex, Rusev1/Viktor, Rusev2/Alex, Rusev2/Viktor, Rusev3/"
                    + "Armstrong, Darrell/Beatty, Warren\nSecond(1992)/Armstrong, Darrell/Beatty, Warren\n"
                    + "Third (1991)/Alex, Rusev3\nFourth (1991)\nFifth (11)/Brady";
        ByteArrayInputStream input = new ByteArrayInputStream(str.getBytes());

        movies = new MoviesExplorer(input);
    }

    @Test
    public void getMovies() {
        movies.getMovies();
    }

    @Test
    public void countMoviesReleasedIn() {
        final int year = 1991;
        final int exp = 2;
        assertEquals(exp, movies.countMoviesReleasedIn(year));
    }

    @Test
    public void findFirstMovieWithTitle() {

        assertEquals(Movie.createMovie("Fourth(1991)"), movies.findFirstMovieWithTitle("Fourth"));
    }

    @Test
    public void getFirstYear() {
        final int exp = 11;
        assertEquals(exp, movies.getFirstYear());
    }

    @Test
    public void getAllMoviesBy() {
        Set<Movie> expected = new HashSet<>();
        expected.add(Movie.createMovie("Fifth (11)/Brady"));

        assertEquals(expected, movies.getAllMoviesBy(new Actor("", "Brady")));
    }

    @Test
    public void getMoviesSortedByReleaseYear() {
        List<Movie> expected = new ArrayList<>();
        expected.add(Movie.createMovie("Fifth (11)/Brady"));
        expected.add(Movie.createMovie("Third (1991)/Alex, Rusev3"));
        expected.add(Movie.createMovie("Fourth (1991)"));
        expected.add(Movie.createMovie("First(1992)/Viktor, Rusev1/Alex, Rusev1/Viktor, Rusev2/Alex, Rusev2/Viktor, "
                + "Rusev3/Armstrong, Darrell/Beatty, Warren"));
        expected.add(Movie.createMovie("Second(1992)/Armstrong, Darrell/Beatty, Warren"));

        assertEquals(expected, movies.getMoviesSortedByReleaseYear());
    }

    @Test
    public void findYearWithLeastNumberOfReleasedMovies() {
        final int exp = 11;
        assertEquals(exp, movies.findYearWithLeastNumberOfReleasedMovies());
    }

    @Test
    public void getAllActors() {

        Set<Actor> expected = new HashSet<>();
        expected.add(new Actor("", "Brady"));
        expected.add(new Actor("Darrell", "Armstrong"));
        expected.add(new Actor("Rusev3", "Alex"));
        expected.add(new Actor("Warren", "Beatty"));
        expected.add(new Actor("Rusev1", "Viktor"));
        expected.add(new Actor("Rusev1", "Alex"));
        expected.add(new Actor("Rusev2", "Alex"));
        expected.add(new Actor("Rusev2", "Viktor"));
        expected.add(new Actor("Rusev3", "Viktor"));


        assertEquals(expected, movies.getAllActors());
    }

    @Test
    public void findMovieWithGreatestNumberOfActors() {

        assertEquals(Movie.createMovie("First(1992)/Viktor, Rusev1/Alex, Rusev1/Viktor, Rusev2/Alex, Rusev2/Viktor, "
                + "Rusev3/Armstrong, Darrell/Beatty, Warren"), movies.findMovieWithGreatestNumberOfActors());
    }
}
