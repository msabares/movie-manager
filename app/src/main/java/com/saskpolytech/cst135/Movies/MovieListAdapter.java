package com.saskpolytech.cst135.Movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.saskpolytech.cst135.Movie;
import com.saskpolytech.cst135.R;

import java.util.List;

/**
 * File Name:	MovieListAdapter.java
 * Author:		Michael Sabares CST135
 * Date:		05/30/2019
 * Purpose:	    A class that extends ArrayAdapter to display a layout that will invidually display
 *              all movies in a ListView. The class uses single_movie_result Layout, obtain the appropriated
 *              movie object, and initializes the layout with its information.
 */

public class MovieListAdapter extends ArrayAdapter<Movie> {

    public MovieListAdapter(Context context, List<Movie> movies) {
        super(context, R.layout.single_movie_result, movies);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //Get the person at the current position
        Movie movie = this.getItem(position);

        View movieItemView = convertView; //Used the passed in view if already inflated
        //Get Android to inflate the view (boilerplate code) if it hasn't already been done
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            movieItemView = inflater.inflate(R.layout.single_movie_result, parent, false);
        }

        TextView title = movieItemView.findViewById(R.id.txtMovieName);
        TextView releaseDate = movieItemView.findViewById(R.id.txtReleaseDate);

        title.setText(movie.getTitle());
        releaseDate.setText(movie.getReleaseDate());

        return movieItemView;

    }
}
