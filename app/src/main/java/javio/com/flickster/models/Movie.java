package javio.com.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * Created by javiosyc on 2017/2/14.
 */

//TODO implements Parcelable
public class Movie implements Serializable{
    private final String posterPath;
    private final String originalTitle;
    private final String overview;
    private final String backDropPath;

    private final String releaseDate;
    private final double voteAverage;

    public Movie(JSONObject jsonObject) throws JSONException {
        this.posterPath = jsonObject.getString("poster_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.backDropPath = jsonObject.getString("backdrop_path");
        this.voteAverage = jsonObject.getDouble("vote_average");
        this.releaseDate = jsonObject.getString("release_date");
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

    public String getPosterPath() {
        return MovieUtils.getPosterUrl(posterPath);
    }

    public String getBackDropPath() {
        return MovieUtils.getBackDropUrl(posterPath);
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "posterPath='" + posterPath + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", overview='" + overview + '\'' +
                ", backDropPath='" + backDropPath + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", voteAverage=" + voteAverage +
                '}';
    }
}
