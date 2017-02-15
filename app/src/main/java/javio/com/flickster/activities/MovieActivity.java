package javio.com.flickster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

        //MovieUtils.getMoviesDataUsingAsyncHttpClient(url,movies,movieArrayAdapter);

        MovieUtils.getMoviesDataUsingOkHttpClient(this,url,movies,movieArrayAdapter);

    }

    @OnItemClick(R.id.lvMovies)
    public void showEditActivity(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MovieActivity.this, MovieDetailActivity.class);
        intent.putExtra("movie", movies.get(position));
        startActivityForResult(intent, REQUEST_CODE);
    }
}
