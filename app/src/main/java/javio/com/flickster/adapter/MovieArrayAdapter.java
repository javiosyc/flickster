package javio.com.flickster.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import javio.com.flickster.R;
import javio.com.flickster.models.Movie;
import javio.com.flickster.models.MovieUtils;


/**
 * Created by javiosyc on 2017/2/14.
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie> {


    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        int type = 0;
        Movie movie = getItem(position);

        if (movie.getVoteAverage() > 6) {
            type = 1;
        }

        return type;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);

        Movie movie = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());

            if (type == 1) {
                convertView = inflater.inflate(R.layout.item_movie_image, parent, false);
            } else {
                convertView = inflater.inflate(R.layout.item_movie, parent, false);
            }

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //clear out image from convertView


        if (type == 1) {
            viewHolder.fullImageView.setImageResource(0);
            MovieUtils.setImageByUrlWithRoundedConer(
                    getContext(),
                    movie.getBackDropPath(),
                    viewHolder.fullImageView);
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

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
