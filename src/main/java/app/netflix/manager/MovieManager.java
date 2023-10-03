package app.netflix.manager;

import app.netflix.AppInfo;
import app.netflix.Main;
import app.netflix.model.Genre;
import app.netflix.model.Movie;
import app.netflix.utils.SanitizerUtils;
import balbucio.responsivescheduler.ResponsiveScheduler;
import balbucio.sqlapi.model.ConditionValue;
import balbucio.sqlapi.model.Conditional;
import balbucio.sqlapi.model.Operator;
import balbucio.sqlapi.sqlite.HikariSQLiteInstance;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.Discover;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import lombok.Getter;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MovieManager {

    private HikariSQLiteInstance sqlite = Main.sqlite;
    private TmdbApi api = Main.tmdbApi;
    private Random random = new Random();
    @Getter
    private List<Movie> popularToday = new ArrayList<>();

    public MovieManager() {
        sqlite.createTable("genries", "id INTEGER(255) PRIMARY KEY UNIQUE, name VARCHAR(255)");
        sqlite.createTable("movies",
                "id INTEGER(255) PRIMARY KEY UNIQUE, name TEXT, description TEXT, genre INTEGER(255), popularity FLOAT, poster TEXT, backdrop TEXT, data TEXT");
    }

    public void loadAll(){
        Main.window.getLoadView().setLoadText("Loading database...");
        api.getGenre().getMovieGenreList("pt-BR").forEach(g -> sqlite.replace("id, name", "'" + g.getId() + "', '" + g.getName() + "'", "genries"));
        Main.window.getLoadView().setLoadText("Loading trending films...");
        loadPopularToday(1, true);
        Main.window.getLoadView().setLoadText("Loading the best films...");
        getGenries().forEach(this::discoverNewMovies);
    }

    public void loadPopularToday(int page, boolean next){
        try {
            MovieResultsPage results = api.getMovies().getPopularMovies("pt-BR", page);
            results.forEach(m -> {
                if (!m.getOverview().isEmpty() && !m.getTitle().isEmpty()) {
                    JSONObject data = new JSONObject();
                    data.put("release", m.getReleaseDate());
                    data.put("vote", m.getVoteAverage());
                    data.put("voteCount", m.getVoteCount());
                    data.put("originalName", SanitizerUtils.clean(m.getOriginalTitle()));
                    Movie movie = new Movie(m.getId(), SanitizerUtils.clean(m.getTitle()), SanitizerUtils.clean(m.getOverview()), -1, m.getPopularity(), m.getPosterPath(), m.getBackdropPath(), data.toString());
                    popularToday.add(movie);
                }
            });
            if (next) {
                ResponsiveScheduler.run(() -> {
                    for (int i = 0; i < 3; i++) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {}
                        loadPopularToday(random.nextInt(results.getTotalPages()), false);
                    }
                });
            }
        } catch (Exception e){}
    }

    public void discoverNewMovies(Genre genre) {
        discoverNewMovies(genre, -1, true);
    }

    public void discoverNewMovies(Genre genre, int page, boolean next) {
        try {
            Discover discover = new Discover();
            discover.includeAdult(true).language("pt-BR").withGenres(String.valueOf(genre.getId()));
            if (page != -1) {
                discover.page(page);
            }
            MovieResultsPage results = api.getDiscover().getDiscover(discover);
            results.getResults().forEach(m -> {
                if (!m.getOverview().isEmpty() && !m.getTitle().isEmpty()) {
                    JSONObject data = new JSONObject();
                    data.put("release", m.getReleaseDate());
                    data.put("vote", m.getVoteAverage());
                    data.put("voteCount", m.getVoteCount());
                    data.put("originalName", SanitizerUtils.clean(m.getOriginalTitle()));
                    sqlite.replace("id, name, description, genre, popularity, poster, backdrop, data",
                            "'" + m.getId() + "', '" + SanitizerUtils.clean(m.getTitle()) + "', '" + SanitizerUtils.clean(m.getOverview()) + "', '" + genre.getId() + "', '" + m.getPopularity() + "', '" + m.getPosterPath() + "', '" + m.getBackdropPath() + "', '" + data.toString() + "'", "movies");
                }
            });
            if(next) {
                ResponsiveScheduler.run(() -> {
                    for (int i = 0; i < 3; i++) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {}
                        discoverNewMovies(genre, random.nextInt(results.getTotalPages()), false);
                    }
                });
            }
        } catch (Exception e){}
    }

    public List<Movie> getMovies(Genre genre) {
        List<Movie> movies = new ArrayList<>();
        sqlite.getAllValuesFromColumns("movies", new ConditionValue[]{
                new ConditionValue("genre", Conditional.EQUALS, genre.getId(), Operator.NULL)
        }).forEach(r -> {
            if (movies.size() <= 10) {
                movies.add(new Movie(r.asInt("id"), r.asString("name"), r.asString("description"), r.asInt("genre"), Float.parseFloat(String.valueOf(r.get("popularity"))), r.asString("poster"), r.asString("backdrop"), r.asString("data")));
            }
        });
        return movies;
    }

    public List<Movie> getMoviesAndExclude(Genre genre, List<Movie> toExclude) {
        List<Movie> movies = new ArrayList<>();
        ConditionValue[] cv = new ConditionValue[toExclude.size() + 1];
        cv[0] = new ConditionValue("genre", Conditional.EQUALS, genre.getId(), Operator.NULL);
        for (int i = 1; i < cv.length; i++) {
            cv[i] = new ConditionValue("id", Conditional.EQUALS, toExclude.get(i).getId(), Operator.NOT);
        }
        sqlite.getAllValuesFromColumns("movies", cv).forEach(r -> {
            if (movies.size() <= 2) {
                movies.add(new Movie(r.asInt("id"), r.asString("name"), r.asString("description"), r.asInt("genre"), Float.parseFloat(String.valueOf(r.get("popularity"))), r.asString("poster"), r.asString("backdrop"), r.asString("data")));
            }
        });
        return movies;
    }

    public List<Genre> getGenries() {
        List<Genre> genries = new ArrayList<>();
        sqlite.getAllValuesFromColumns("genries").forEach(resultValue -> {
            genries.add(new Genre(resultValue.asInt("id"), resultValue.asString("name")));
        });
        return genries;
    }
}
