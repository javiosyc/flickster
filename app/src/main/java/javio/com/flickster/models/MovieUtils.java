package javio.com.flickster.models;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import javio.com.flickster.R;
import javio.com.flickster.activities.MovieActivity;
import javio.com.flickster.adapter.MovieArrayAdapter;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import network.OKHttpClientUtils;

/**
 * Created by javiosyc on 2017/2/14.
 */

public class MovieUtils {
    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    private static final String MOVIE_API_URL_PREFIX = "https://image.tmdb.org/t/p/";

    private static final String POSTER_SIZE = "w342";
    private static final String BACKDROP_SIZE = "w780";

    private static final String POSTER_URL_FORMAT = MOVIE_API_URL_PREFIX + POSTER_SIZE + "%s";
    private static final String BACK_DROP_URL_FORMAT = MOVIE_API_URL_PREFIX + BACKDROP_SIZE + "%s";

    private MovieUtils() {
    }

    static String getPosterUrl(String path) {
        return String.format(POSTER_URL_FORMAT, path);
    }

    static String getBackDropUrl(String path) {
        return String.format(BACK_DROP_URL_FORMAT, path);
    }


    public static String getPathByOrientation(int orientation, Movie movie) {
        String path;
        switch (orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                path = movie.getBackDropPath();
                break;
            case Configuration.ORIENTATION_PORTRAIT:
            default:
                path = movie.getPosterPath();
                break;
        }
        return path;
    }

    public static void setImageByUrl(Context context, String url, ImageView imageView) {
        Picasso.with(context).load(url).fit()
                .placeholder(R.drawable.ic_file_download_black_120dp)
                .error(R.drawable.ic_error_black_320dp)
                .into(imageView);
    }


    public static void setPopularImageByUrl(Context context, String url, ImageView imageView, Callback callback) {
        Picasso.with(context).load(url)
                .placeholder(R.drawable.ic_file_download_black_120dp)
                .error(R.drawable.ic_error_black_320dp)
                .transform(new RoundedCornersTransformation(30, 30))
                .into(imageView, callback);
    }


    public static void getMoviesDataUsingAsyncHttpClient(String url, final ArrayList<Movie> movies, final MovieArrayAdapter movieArrayAdapter) {
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode >= 200 && statusCode < 300) {
                    movies.addAll(getMoviesFromJSONObject(response));
                    movieArrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private static List<Movie> getMoviesFromJSONObject(JSONObject response) {
        JSONArray moviesResult;
        try {
            moviesResult = response.getJSONArray("results");
            return Movie.fromJSONArray(moviesResult);
        } catch (JSONException e) {
            Log.d("DEBUG", response.toString(), e);
            return new ArrayList<>();
        }
    }

    public static void getMoviesDataUsingOkHttpClient(final MovieActivity movieActivity, String url, final ArrayList<Movie> movies, final MovieArrayAdapter movieArrayAdapter) {
        OKHttpClientUtils client = OKHttpClientUtils.getOkHttpClient();

        client.asyncCall(url, client.new OKHttpClientCallBack() {
            @Override
            protected void processingJsonData(final JSONObject jsonObject) {
                movieActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        movies.addAll(getMoviesFromJSONObject(jsonObject));
                        movieArrayAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}
