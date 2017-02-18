package javio.com.flickster.utils;

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
import javio.com.flickster.models.Movie;
import javio.com.flickster.network.OKHttpClientUtils;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * Created by javiosyc on 2017/2/14.
 */

public class MovieUtils {
    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    private static final String MOVIE_API_URL_PREFIX = "https://image.tmdb.org/t/p/w";

    private static final int POSTER_SIZE = 342;
    private static final int BACKDROP_SIZE = 780;

    private static final String POSTER_URL_FORMAT = MOVIE_API_URL_PREFIX + POSTER_SIZE + "%s";
    private static final String BACK_DROP_URL_FORMAT = MOVIE_API_URL_PREFIX + BACKDROP_SIZE + "%s";

    private MovieUtils() {
    }

    public static String getPosterUrl(String path) {
        return String.format(POSTER_URL_FORMAT, path);
    }

    public static String getBackDropUrl(String path) {
        return String.format(BACK_DROP_URL_FORMAT, path);
    }


    public static void setDetailImageByUrl(Context context, String url, ImageView imageView) {
        Picasso.with(context).load(url).resize(BACKDROP_SIZE, 0)
                .placeholder(R.drawable.ic_file_download_342)
                .error(R.drawable.ic_error_black_320dp)
                .into(imageView);
    }


    public static void setPopularImageByUrl(Context context, String url, ImageView imageView, Callback callback) {
        Picasso.with(context).load(url)
                .placeholder(R.drawable.ic_file_download_342)
                .error(R.drawable.ic_error_black_320dp)
                .transform(new RoundedCornersTransformation(30, 30))
                .into(imageView, callback);
    }


    public static void getMoviesDataUsingAsyncHttpClient(final ArrayList<Movie> movies, final MovieArrayAdapter movieArrayAdapter) {
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
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
            return fromJSONArray(moviesResult);
        } catch (JSONException e) {
            Log.d("DEBUG", response.toString(), e);
            return new ArrayList<>();
        }
    }

    public static void getMoviesDataUsingOkHttpClient(final MovieActivity movieActivity, final ArrayList<Movie> movies, final MovieArrayAdapter movieArrayAdapter) {
        OKHttpClientUtils client = OKHttpClientUtils.getInstance();

        client.asyncCall(MovieUtils.NOW_PLAYING_URL, client.new OKHttpClientCallBack() {
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

    public static void setImageByOrientation(Context context, ImageView imageView, Movie movie, int ori) {

        Orientation orientation = Orientation.getFrom(ori);

        Picasso.with(context).load(orientation.getPathFromMovie(movie))
                .resize(orientation.imageSize, 0)
                .placeholder(orientation.placeholderResource)
                .error(R.drawable.ic_error_black_320dp)
                .transform(new RoundedCornersTransformation(10, 10))
                .into(imageView);
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

    private enum Orientation {
        LANDSCAPE(BACKDROP_SIZE, R.drawable.ic_file_download_342) {
            @Override
            protected String getPathFromMovie(Movie movie) {
                return movie.getBackDropPath();
            }
        }, PORTRAIT(POSTER_SIZE, R.drawable.ic_file_download_342) {
            @Override
            protected String getPathFromMovie(Movie movie) {
                return movie.getPosterPath();
            }
        };
        private final int imageSize;
        private final int placeholderResource;

        Orientation(int imageSize, int placeholderResource) {
            this.imageSize = imageSize;
            this.placeholderResource = placeholderResource;
        }

        private static Orientation getFrom(int orientation) {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                return LANDSCAPE;
            } else {
                return PORTRAIT;
            }
        }

        protected abstract String getPathFromMovie(Movie movie);
    }
}
