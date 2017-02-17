package javio.com.flickster.models;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import javio.com.flickster.utils.MovieUtils;

/**
 * Created by javiosyc on 2017/2/14.
 */

public class Movie implements Serializable {

    private final Long id;
    private final String posterPath;
    private final String originalTitle;
    private final String overview;
    private final String backDropPath;
    private final String releaseDate;
    private final Double voteAverage;

    public Movie(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getLong("id");
        this.posterPath = jsonObject.getString("poster_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.backDropPath = jsonObject.getString("backdrop_path");
        this.voteAverage = jsonObject.getDouble("vote_average");
        this.releaseDate = jsonObject.getString("release_date");
    }

    protected Movie(Parcel in) {
        this.id = in.readLong();
        this.posterPath = in.readString();
        this.originalTitle = in.readString();
        this.overview = in.readString();
        this.backDropPath = in.readString();
        this.releaseDate = in.readString();
        this.voteAverage = in.readDouble();
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

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", posterPath='" + posterPath + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", overview='" + overview + '\'' +
                ", backDropPath='" + backDropPath + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", voteAverage=" + voteAverage +
                '}';
    }
}
