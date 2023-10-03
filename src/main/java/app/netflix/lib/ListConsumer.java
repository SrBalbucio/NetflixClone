package app.netflix.lib;

import app.netflix.model.Genre;
import app.netflix.model.Movie;

import java.util.List;

public interface ListConsumer {

    List<Movie> moreMovies(List<Movie> oldMovies);
}
