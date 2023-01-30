package bg.sofia.uni.fmi.mjt.movies;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import bg.sofia.uni.fmi.mjt.movies.model.Actor;
import bg.sofia.uni.fmi.mjt.movies.model.Movie;

public class MoviesExplorer {

    private List<Movie> movies;

    /**
     * Loads the dataset from the given {@code dataInput} stream.
     */
    public MoviesExplorer(InputStream dataInput) {
        movies = new ArrayList<>();
        movies.addAll(parseData(dataInput));
    }

    private List<Movie> parseData(InputStream data) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(data));
        return reader.lines()
                .map(Movie::createMovie)
                .collect(Collectors.toList());
    }

    /**
     * Returns all the movies loaded from the dataset.
     **/
    public Collection<Movie> getMovies() {
        return this.movies;
    }

    // Other methods ...
    public int countMoviesReleasedIn(int year) {
        return (int) this.movies.stream()
                                .filter(movie -> movie.getYear() == year)
                                .count();
    }

    public Movie findFirstMovieWithTitle(String title) {
        return this.movies.stream()
                        .filter(movie -> movie.getTitle().contains(title))
                        .findFirst()
                        .orElseThrow(IllegalArgumentException::new);
    }

    public Collection<Actor> getAllActors() {
        return this.movies.stream()
                .map(Movie::getActors)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public int getFirstYear() {
        return this.movies.stream()
                .map(Movie::getYear)
                .sorted()
                .findFirst()
                .get();
    }

    public Collection<Movie> getAllMoviesBy(Actor actor) {
        return this.movies.stream()
                .filter(movie -> movie.getActors().contains(actor))
                .collect(Collectors.toSet());
    }

    public Collection<Movie> getMoviesSortedByReleaseYear() {
        return this.movies.stream()
                .sorted(Comparator.comparing(Movie::getYear))
                .collect(Collectors.toList());
    }

    public int findYearWithLeastNumberOfReleasedMovies() {
        Map<Integer, Long> result = this.movies.stream()
                                            .collect(Collectors.groupingBy(Movie::getYear, Collectors.counting()));

        return result.entrySet().stream()
                .min(Comparator.comparing(Map.Entry::getValue))
                .get().getKey();
    }

    public Movie findMovieWithGreatestNumberOfActors() {
        Map<Movie, Integer> result = this.movies.stream()
                .collect(Collectors.toMap(e -> e, e -> e.getActors().size()));

        return result.entrySet().stream()
                    .max(Comparator.comparing(Map.Entry::getValue))
                    .get().getKey();
    }
}