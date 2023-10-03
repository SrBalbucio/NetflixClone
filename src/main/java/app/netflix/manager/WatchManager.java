package app.netflix.manager;

import app.netflix.Main;
import app.netflix.model.Movie;
import balbucio.sqlapi.model.ConditionValue;
import balbucio.sqlapi.model.Conditional;
import balbucio.sqlapi.model.Operator;
import balbucio.sqlapi.sqlite.HikariSQLiteInstance;

import java.util.ArrayList;
import java.util.List;

public class WatchManager {

    private HikariSQLiteInstance sqlite = Main.sqlite;

    public WatchManager(){
        sqlite.createTable("watched", "userid VARCHAR(255) PRIMARY KEY, movieid INTEGER(255)");
    }

    public void addWatched(String userid, String movieid){
        sqlite.replace("userid, movieid", "'"+userid+"', '"+movieid+"'","watched");
    }

    public List<Integer> getWatched(String userid){
        List<Integer> movies = new ArrayList<>();
        sqlite.getAllValuesFromColumns("watched", new ConditionValue[]{ new ConditionValue("userid", Conditional.EQUALS, userid, Operator.NULL)})
                .forEach(r -> movies.add(r.asInt("movieid")));
        return movies;
    }

    public List<Movie> getWatchedMovies(String userid){
        List<Movie> movies = new ArrayList<>();
        sqlite.getAllValuesFromColumns("watched", new ConditionValue[]{ new ConditionValue("userid", Conditional.EQUALS, userid, Operator.NULL)})
                .forEach(r -> movies.add(Main.movieManager.getMovie(r.asInt("movieid"))));
        return movies;
    }
}
