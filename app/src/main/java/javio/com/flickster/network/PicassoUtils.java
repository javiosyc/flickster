package javio.com.flickster.network;

import android.content.Context;

import com.squareup.picasso.Picasso;

/**
 * Created by javiosyc on 2017/2/18.
 */

public class PicassoUtils {

    private PicassoUtils() {}

    public static void init (Context context){
        Picasso.Builder picassoBuilder = new Picasso.Builder(context);
        picassoBuilder.indicatorsEnabled(true);
        picassoBuilder.loggingEnabled(true);

        Picasso picasso = picassoBuilder.build();
        try {
            Picasso.setSingletonInstance(picasso);
        } catch (IllegalStateException illegalStateException) {

        }
    }
}
