package com.saskpolytech.cst135.Movies;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.saskpolytech.cst135.Movie;
import com.saskpolytech.cst135.R;
import com.saskpolytech.cst135.Untils.AsyncResponse;
import com.saskpolytech.cst135.Untils.NetworkUtils;

import java.util.ArrayList;

/**
 * File Name:	MovieSearch.java
 * Author:		Michael Sabares CST135
 * Date:		05/30/2019
 * Purpose:	    This class allows the user to search for a movie, connects to the movieDB.org API
 *              to retrieve a list of result and moves on to the next fragment that will display
 *              the results.
 */
public class MovieSearch extends Fragment implements View.OnClickListener, AsyncResponse {

    //Attributes

    //UI Elements
    private EditText etSearchMovies;
    private Button btnSearch;

    private final String API_KEY = "74d7ab0b265101d23e8e4377077069ae"; //My API Key
    //sendRequest is the string we use to query the API and the movieName is what we are searching for.
    private String sendRequest, movieName;


    /**
     * This method runs as soon as we move into this fragment. This initializes our UI elements an on
     * click listener that will query the API.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.activity_movie_search, container, false);
        setRetainInstance(true);
        etSearchMovies = myView.findViewById(R.id.etSeachMovies);
        btnSearch = myView.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);

        return myView;
    }


    /**
     * onClick will query by appending the taking whatever the user typed and using th API_KEY and calling
     * NetworkUtils, which will run a thread to query the search.
     * @param v Search button
     */
    @Override
    public void onClick(View v) {

        this.movieName = this.etSearchMovies.getText().toString();

        if(this.movieName.equals("")) {
            Toast.makeText(getContext(), "Please enter a name of a movie", Toast.LENGTH_SHORT).show();
        } else {
            this.sendRequest = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&language=en-US&query=" + movieName +"&page=1&include_adult=false";
            new NetworkUtils(this).execute(this.sendRequest);
            Toast.makeText(getContext(), "Searching movie...", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method is automatically called once the query is finished processing. It will obtain an
     * ArrayList of movies from the NetworkUtils class and proceed to start the next fragment and giving it the ArrayList
     * @param movieResultList Will take an ArrayList of movies that was given by NetworkUtils
     */
    @Override
    public void processFinish(ArrayList<Movie> movieResultList) {
        Fragment movieResultFragment = new MovieSearchResults();

        //This processes allows us to pass data to the next Fragment.
        Bundle bundle = new Bundle();
        bundle.putSerializable("movieResults", movieResultList);
        movieResultFragment.setArguments(bundle);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, movieResultFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
