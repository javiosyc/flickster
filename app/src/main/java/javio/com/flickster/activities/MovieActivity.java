package javio.com.flickster.activities;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import javio.com.flickster.R;
import javio.com.flickster.adapter.MovieArrayAdapter;
import javio.com.flickster.models.Movie;
import javio.com.flickster.utils.MovieUtils;


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

        getMoviesFromMovieDatabase();
    }

    private void getMoviesFromMovieDatabase() {
        String clientType = getClientType();

        if ("AsyncHttpClient".equals(clientType)) {
            MovieUtils.getMoviesDataUsingAsyncHttpClient(movies, movieArrayAdapter);
        } else {
            MovieUtils.getMoviesDataUsingOkHttpClient(this, movies, movieArrayAdapter);
        }
    }

    private String getClientType() {
        String clientType;
        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = info.metaData;
            clientType = bundle.getString("http_client");
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("DEBUG", "Client Type is not found", e);
            clientType = "";
        }
        return clientType;
    }

    @OnItemClick(R.id.lvMovies)
    public void showEditActivity(int position) {
        Intent intent = new Intent(MovieActivity.this, MovieDetailActivity.class);
        intent.putExtra("movie", movies.get(position));
        startActivityForResult(intent, REQUEST_CODE);
    }
}
