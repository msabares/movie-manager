package com.saskpolytech.cst135.WatchList;

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
 * File Name:	WatchList.java
 * Author:		Michael Sabares CST135
 * Date:		05/30/2019
 * Purpose:	    This class is exactly the same as WatchHistory and almost the same to MovieSearchResults. the difference is how it
 *              obtains its arraylist to display the data.
 */

public class WatchList extends Fragment implements AdapterView.OnItemClickListener {

    private ListView lvWatchList;
    private ArrayList<Movie> watchList;

    private MovieListAdapter adapter;
    private MovieDBHelper movieDB;


    /**
     * When the fragment is created, it will initialize the elements in the UI and uses the MovieListAdapter
     * to properly display movies. It also access the database and performs a query search for movies with
     * IN_WATCH_LIST = 1 (true).
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.activity_watch_list, container, false);
        setRetainInstance(true);

        lvWatchList = myView.findViewById(R.id.lvWatchHistory);
        movieDB = new MovieDBHelper(getContext());

        movieDB.open();
        watchList = movieDB.getMovieList("watch_list");
        movieDB.close();

        adapter = new MovieListAdapter (getContext(), watchList);
        lvWatchList.setAdapter(adapter);
        lvWatchList.setOnItemClickListener(this);

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
