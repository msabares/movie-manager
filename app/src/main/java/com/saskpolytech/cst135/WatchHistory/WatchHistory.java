package com.saskpolytech.cst135.WatchHistory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.saskpolytech.cst135.Movie;
import com.saskpolytech.cst135.Movies.MovieDetails;
import com.saskpolytech.cst135.Movies.MovieListAdapter;
import com.saskpolytech.cst135.R;
import com.saskpolytech.cst135.Untils.MovieDBHelper;

import java.util.ArrayList;

/**
 * File Name:	WatchHistory.java
 * Author:		Michael Sabares CST135
 * Date:		05/30/2019
 * Purpose:	    This class is exactly the same as WatchList and almost the same to MovieSearchResults. the difference is how it
 *              obtains its arraylist to display the data.
 */

public class WatchHistory  extends Fragment implements AdapterView.OnItemClickListener {

    private ListView lvWatchHistory;
    private ArrayList<Movie> historyList;

    private MovieListAdapter adapter;
    private MovieDBHelper movieDB;


    /**
     * When the fragment is created, it will initialize the elements in the UI and uses the MovieListAdapter
     * to properly display movies. It also access the database and performs a query search for movies with
     * IN_WATCH_HISTORY = 1 (true).
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.activity_watch_history, container, false);
        setRetainInstance(true);

        lvWatchHistory = myView.findViewById(R.id.lvWatchHistory);
        movieDB = new MovieDBHelper(getContext());

        movieDB.open();
        historyList = movieDB.getMovieList("watch_history");
        movieDB.close();

        adapter = new MovieListAdapter (getContext(), historyList);
        lvWatchHistory.setAdapter(adapter);
        lvWatchHistory.setOnItemClickListener(this);

        return myView;
    }

    /**
     * A listener that will take whatever Movie object was selected and call the next fragment and
     * passing that movie object as well.
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Movie movie = adapter.getItem(position);

        Fragment movieDetailsFragment = new MovieDetails();

        Bundle bundle = new Bundle();
        bundle.putSerializable("movie", movie);
        movieDetailsFragment.setArguments(bundle);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, movieDetailsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
