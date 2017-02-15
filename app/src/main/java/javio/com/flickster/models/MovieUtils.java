package javio.com.flickster.models;

import android.content.Context;
import android.content.res.Configuration;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import javio.com.flickster.R;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by javiosyc on 2017/2/14.
 */

public class MovieUtils {

    private static final String MOVIE_API_URL_PREFIX = "https://image.tmdb.org/t/p/";

    private static final String POSTER_SIZE = "w342";
    private static final String BACKDROP_SIZE = "w780";

    private static final String POSTER_URL_FORMAT = MOVIE_API_URL_PREFIX + POSTER_SIZE + "%s";
    private static final String BACK_DROP_URL_FORMAT = MOVIE_API_URL_PREFIX + BACKDROP_SIZE + "%s";

    private MovieUtils() {}
    static String getPosterUrl (String path) {
        return String.format(POSTER_URL_FORMAT,path);
    }

    static  String getBackDropUrl (String path) {
        return String.format(BACK_DROP_URL_FORMAT, path);
    }


    public static String getPathByOrientation(int orientation,Movie movie) {
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

    public static void setImageByUrl (Context context,String url,ImageView imageView) {
        Picasso.with(context).load(url).fit()
                .placeholder(R.drawable.ic_file_download_black_24dp)
                .error(R.drawable.ic_error_black_24dp)
                .into(imageView);
    }

    public static void setImageByUrlWithRoundedConer(Context context,String url,ImageView imageView) {
        Picasso.with(context).load(url)
                .placeholder(R.drawable.ic_file_download_black_24dp)
                .error(R.drawable.ic_error_black_24dp)
                .transform(new RoundedCornersTransformation(30, 30))
                .into(imageView);

    }
}
