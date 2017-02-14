package javio.com.flickster.models;

import android.content.res.Configuration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * Created by javiosyc on 2017/2/14.
 */

public class Movie {

    private final String MOVIE_API_URL_PREFIX = "https://image.tmdb.org/t/p/";

    private final String POSTER_SIZE = "w342";
    private final String BACKDROP_SIZE = "w780";

    private final String POSTER_URL_FORMAT = MOVIE_API_URL_PREFIX + POSTER_SIZE + "%s";
    private final String BACK_DROP_URL_FORMAT = MOVIE_API_URL_PREFIX + BACKDROP_SIZE + "%s";

    private final String posterPath;
    private final String originalTitle;
    private final String overview;
    private final String backDropPath;

    public String getPosterPath() {

        return String.format(POSTER_URL_FORMAT,posterPath);
    }

    public String getBackDropPath() {
        return String.format(BACK_DROP_URL_FORMAT, backDropPath);
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public Movie(JSONObject jsonObject) throws JSONException {
        this.posterPath = jsonObject.getString("poster_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.backDropPath = jsonObject.getString("backdrop_path");
    }


    public static ArrayList<Movie> fromJSONArray(JSONArray array) {
        ArrayList<Movie> results = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject movie = array.getJSONObject(i);
                results.add(new Movie(movie));
            } catch (JSONException e) {
                log.d("ERROR", "parse error!", e);
            }
        }
        return results;
    }

    public String getPathByOrientation(int orientation) {
        String path;
        switch (orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                path = this.getBackDropPath();
                break;
            case Configuration.ORIENTATION_PORTRAIT:
            default:
                path = this.getPosterPath();
                break;
        }
        return path;
    }
}
