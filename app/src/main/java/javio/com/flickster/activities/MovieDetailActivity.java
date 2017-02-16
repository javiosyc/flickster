package javio.com.flickster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import javio.com.flickster.R;
import javio.com.flickster.models.Movie;
import javio.com.flickster.models.MovieUtils;

/**
 * Created by javiosyc on 2017/2/15.
 */
public class MovieDetailActivity extends AppCompatActivity {
    @BindView(R.id.ivDetailImage)
    ImageView imageView;
    @BindView(R.id.tvDetailTitle)
    TextView tvTitle;
    @BindView(R.id.tvDetailOverView)
    TextView tvOverview;
    @BindView(R.id.tvDetailReleaseDate)
    TextView tvDetailReleaseDate;
    @BindView(R.id.rbVoteAverage)
    RatingBar rbVoteAverage;
    private Intent intent;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = getIntent();

        movie = (Movie) intent.getSerializableExtra("movie");

        initView(movie);
    }

    public void initView(Movie movie) {
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);

        ViewGroup view = (ViewGroup) getWindow().getDecorView();

        MovieUtils.setDetailImageByUrl(view.getContext(), movie.getBackDropPath(), imageView);

        tvTitle.setText(movie.getOriginalTitle());
        tvOverview.setText(movie.getOverview());
        tvDetailReleaseDate.setText(movie.getReleaseDate());

        rbVoteAverage.setNumStars(10);
        rbVoteAverage.setMax(10);
        rbVoteAverage.setStepSize(0.1f);
        rbVoteAverage.setRating((float) movie.getVoteAverage());
    }
}
