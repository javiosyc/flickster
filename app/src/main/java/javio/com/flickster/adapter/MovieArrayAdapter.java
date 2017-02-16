package javio.com.flickster.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import javio.com.flickster.R;
import javio.com.flickster.activities.YouTubeActivity;
import javio.com.flickster.models.Movie;
import javio.com.flickster.models.MovieUtils;


/**
 * Created by javiosyc on 2017/2/14.
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie> {
    private final static int POPULAR_AVERAGE_SCORE = 6;

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @Override
    public int getViewTypeCount() {
        return MovieType.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        Movie movie = getItem(position);
        return MovieType.getTypeBy(movie).getValue();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieType type = MovieType.getMoiveTypeByValue(getItemViewType(position));

        final Movie movie = getItem(position);

        final ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());

            if (type == MovieType.POPULAR) {
                convertView = inflater.inflate(R.layout.item_movie_image, parent, false);
            } else {
                convertView = inflater.inflate(R.layout.item_movie, parent, false);
            }

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (type == MovieType.POPULAR) {
            //clear out image from convertView
            viewHolder.fullImageView.setImageResource(0);

            viewHolder.playButton.setVisibility(View.INVISIBLE);

            MovieUtils.setPopularImageByUrl(
                    getContext(),
                    movie.getBackDropPath(),
                    viewHolder.fullImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            viewHolder.playButton.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {
                            viewHolder.playButton.setVisibility(View.INVISIBLE);
                        }
                    });

            viewHolder.playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), YouTubeActivity.class);
                    intent.putExtra("movieId", movie.getId());
                    ((AppCompatActivity) getContext()).startActivityForResult(intent, 200);
                }
            });
        } else {
            viewHolder.imageView.setImageResource(0);
            setImageByOrientation(viewHolder.imageView, movie);
            viewHolder.tvTitle.setText(movie.getOriginalTitle());
            viewHolder.tvOverview.setText(movie.getOverview());
        }
        return convertView;
    }

    private ImageView setImageByOrientation(ImageView imageView, Movie movie) {

        int orientation = imageView.getResources().getConfiguration().orientation;

        MovieUtils.setImageByUrl(
                getContext(),
                MovieUtils.getPathByOrientation(orientation, movie),
                imageView);

        return imageView;
    }

    private enum MovieType {
        POPULAR(1), NON_POPULAR(0);
        private final int value;

        private MovieType(int value) {
            this.value = value;
        }

        public static MovieType getTypeBy(Movie movie) {
            return movie.getVoteAverage() > POPULAR_AVERAGE_SCORE ? POPULAR : NON_POPULAR;
        }

        public static MovieType getMoiveTypeByValue(int value) {
            return value == 1 ? POPULAR : NON_POPULAR;
        }

        public int getValue() {
            return value;
        }
    }

    static class ViewHolder {

        @Nullable
        @BindView(R.id.ivMovieFullImage)
        ImageView fullImageView;
        @Nullable
        @BindView(R.id.ivMovieImage)
        ImageView imageView;
        @Nullable
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @Nullable
        @BindView(R.id.tvOverView)
        TextView tvOverview;

        @Nullable
        @BindView(R.id.playButton)
        ImageButton playButton;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
