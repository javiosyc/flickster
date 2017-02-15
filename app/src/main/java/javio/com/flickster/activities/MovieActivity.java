package javio.com.flickster.activities;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import javio.com.flickster.R;
import javio.com.flickster.adapter.MovieArrayAdapter;
import javio.com.flickster.models.Movie;
import javio.com.flickster.models.MovieUtils;


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

        String clientType = getClientType();

        if ("AsyncHttpClient".equals(clientType)) {
            MovieUtils.getMoviesDataUsingAsyncHttpClient(MovieUtils.NOW_PLAYING_URL, movies, movieArrayAdapter);
        } else {
            MovieUtils.getMoviesDataUsingOkHttpClient(this, MovieUtils.NOW_PLAYING_URL, movies, movieArrayAdapter);
        }
    }

    private String getClientType() {
        String clientType;
        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = info.metaData;
            clientType = bundle.getString("http_client");
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("DEBUG", "client Type not found", e);
            clientType = "";
        }
        return clientType;
    }

    @OnItemClick(R.id.lvMovies)
    public void showEditActivity(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MovieActivity.this, MovieDetailActivity.class);
        intent.putExtra("movie", movies.get(position));
        startActivityForResult(intent, REQUEST_CODE);
    }
}
