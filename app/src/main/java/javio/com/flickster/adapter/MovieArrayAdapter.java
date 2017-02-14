package javio.com.flickster.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import javio.com.flickster.R;
import javio.com.flickster.models.Movie;


/**
 * Created by javiosyc on 2017/2/14.
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    private static class ViewHolder {
        ImageView imageView;
        TextView tvTitle;
        TextView tvOverview;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie movie = getItem(position);


        ViewHolder viewHolder ;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(R.layout.item_movie, parent, false);

            viewHolder.imageView =  (ImageView) convertView.findViewById(R.id.ivMovieImage);
            viewHolder.tvTitle  = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvOverview = (TextView) convertView.findViewById(R.id.tvOverView);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        setImageByOrientation(viewHolder.imageView, movie);

        viewHolder.tvTitle.setText(movie.getOriginalTitle());
        viewHolder.tvOverview.setText(movie.getOverview());

        return convertView;
    }

    private ImageView setImageByOrientation(ImageView imageView, Movie movie) {

        //clear out image from convertView
        imageView.setImageResource(0);

        int orientation = imageView.getResources().getConfiguration().orientation;

        Picasso.with(getContext()).load(movie.getPathByOrientation(orientation)).
                placeholder(R.drawable.ic_file_download_black_24dp)
                .error(R.drawable.ic_error_black_24dp).
                into(imageView);

        return imageView;
    }
}
