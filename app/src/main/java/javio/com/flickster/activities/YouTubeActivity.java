package javio.com.flickster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import javio.com.flickster.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.loopj.android.http.AsyncHttpClient.log;

public class YouTubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    public static final String API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed";

    private static final String MOVIE_VIDEO_URL_PREFIX = "https://api.themoviedb.org/3/movie/%s/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US";

    long videoId;

    @BindView(R.id.youtube_player)
    YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_you_tube_base);

        ButterKnife.bind(this);


        Intent intent = getIntent();
        videoId = intent.getLongExtra("movieId", 0);

        youTubePlayerView.initialize(API_KEY, this);

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {


        final YouTubePlayer utPlayer = player;

        /** Start buffering **/
        if (!wasRestored) {
            OkHttpClient client = new OkHttpClient();
            final String url = String.format(MOVIE_VIDEO_URL_PREFIX, videoId);

            Request request = new Request.Builder().url(url).build();


            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("ERROR", "get image fail, url =" + url, e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }
                    final String responseData = response.body().string();
                    JSONObject jsonObject;

                    String result = "";
                    try {
                        jsonObject = new JSONObject(responseData);
                        JSONArray array = jsonObject.getJSONArray("results");
                        for (int i = 0; i < array.length(); i++) {
                            try {
                                JSONObject movie = array.getJSONObject(i);

                                if ("YouTube".equals(movie.getString("site"))) {
                                    result = movie.getString("key");
                                    break;
                                }

                            } catch (JSONException e) {
                                log.d("ERROR", "parse error!", e);
                            }
                        }
                    } catch (JSONException e) {
                        Log.d("ERROR", "parse responseData failed!" + responseData, e);
                        throw new IOException(e);
                    }
                    utPlayer.cueVideo(result);
                }
            });
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider
                                                provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
