package com.saskpolytech.cst135.Movies;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.saskpolytech.cst135.Movie;
import com.saskpolytech.cst135.R;

import java.util.ArrayList;

/**
 * File Name:	MovieSearchResults.java
 * Author:		Michael Sabares CST135
 * Date:		05/30/2019
 * Purpose:	    A fragment that displays all of the movie results in a list, using a custom adapter
 *              to properly display each individual element in the list.
 */

public class MovieSearchResults extends Fragment implements AdapterView.OnItemClickListener {

    //Attributes
    private ListView lvResults;
    private ArrayList<Movie> resultList;

    private MovieListAdapter adapter;

    /**
     * When the fragment is created, it will initialize the elements in the UI and uses the MovieListAdapter
     * to properly display movies.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.activity_movie_search_results, container, false);
        setRetainInstance(true);

        lvResults = myView.findViewById(R.id.lvMovieSearchResults);
        resultList = (ArrayList<Movie>) getArguments().getSerializable("movieResults"); //Used to obtain the ArrayList from the previous fragment.

        if(resultList.size() == 0) {
            Toast.makeText(getContext(), "No results.", Toast.LENGTH_SHORT).show();
        } else {
            adapter = new MovieListAdapter (getContext(), resultList);
            lvResults.setAdapter(adapter);
            lvResults.setOnItemClickListener(this);
        }

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

        //This processes allows us to pass data to the next Fragment.
        Bundle bundle = new Bundle();
        bundle.putSerializable("movie", movie);
        movieDetailsFragment.setArguments(bundle);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, movieDetailsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
