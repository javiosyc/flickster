package javio.com.flickster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import cz.msebera.android.httpclient.Header;
import javio.com.flickster.R;
import javio.com.flickster.adapter.MovieArrayAdapter;
import javio.com.flickster.models.Movie;


public class MovieActivity extends AppCompatActivity {
    private final int REQUEST_CODE = 200;
    ArrayList<Movie> movies;
    MovieArrayAdapter movieArrayAdapter;

    @BindView(R.id.lvMovies)
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        movies = new ArrayList<>();
        movieArrayAdapter = new MovieArrayAdapter(this, movies);

        ButterKnife.bind(this);

        lvItems.setAdapter(movieArrayAdapter);

        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray moviesResult;

                try {
                    moviesResult = response.getJSONArray("results");
                    movies.addAll(Movie.fromJSONArray(moviesResult));
                    movieArrayAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("DEBUG", movies.toString(), e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    @OnItemClick(R.id.lvMovies)
    public void showEditActivity(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MovieActivity.this, MovieDetailActivity.class);
        intent.putExtra("movie", movies.get(position));
        startActivityForResult(intent, REQUEST_CODE);
    }
}
