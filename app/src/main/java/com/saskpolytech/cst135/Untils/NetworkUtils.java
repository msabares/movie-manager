package com.saskpolytech.cst135.Untils;

import android.os.AsyncTask;

import com.saskpolytech.cst135.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * File Name:	NetworkUtils.java
 * Author:		Michael Sabares CST135
 * Date:		05/30/2019
 * Purpose:	    When called, this call will perform an http request that will
 *              retrieve, which will than be parsed and be returned to the MovieSearch Class.
 *              This whole concept of connecting of making http request is new to me so for some of this
 *              code written, i don't understand how it really works.
 */

public class NetworkUtils extends AsyncTask<String, Void, ArrayList<Movie>> {

    ArrayList<Movie> movieList = new ArrayList<>();

    public AsyncResponse delegate = null;

    public NetworkUtils(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    /**
     * Called automatically, this method does all the leg work and does it in a thread.
     * @param url The query string that is used to communicate with the movieDB.org api
     * @return
     */
    @Override
    protected ArrayList<Movie> doInBackground(String... url) {
        try {

            //Creates a connection using the query string
            URL requestQuery = new URL(url[0]);
            HttpURLConnection connection = (HttpURLConnection) requestQuery.openConnection();
            connection.connect();

            InputStream obIn = connection.getInputStream();

            //Reads in the JSON and puts it in a String builder, which is then added in to a String
            //For some reason? There's probably a reason, id like to think.
            BufferedReader reader = new BufferedReader(new InputStreamReader(obIn));
            StringBuilder results = new StringBuilder();
            results.append(reader.readLine());

            String data = results.toString();

            //The movie is parsed and added into the movieList.
            parseJSON(data, movieList);
            obIn.close();
            return movieList;

        } catch (IOException e) {

        }
        return null;
    }


    protected void onPostExecute(ArrayList<Movie> movieResultList) {
        delegate.processFinish(movieResultList);
    }

    /**
     * Method that takes the string, turns it into a JSON object, which allows us to look through
     * the JSON, create movie objects and add them to the ArrayList.
     * @param data
     * @param movieList
     */
    public void parseJSON(String data, ArrayList<Movie> movieList) {
        try {

            JSONObject obJSON = new JSONObject(data);

            JSONArray JSONarray = obJSON.getJSONArray("results");
            for(int i = 0; i < JSONarray.length(); i++) {
                JSONObject jsonMovie = JSONarray.getJSONObject(i);

                Movie movie = new Movie();

                movie.setId(jsonMovie.getInt("id"))
                        .setTitle(jsonMovie.getString("title"))
                        .setVoteAverage(jsonMovie.getDouble("vote_average"))
                        .setOverview(jsonMovie.getString("overview"))
                        .setReleaseDate(jsonMovie.getString("release_date"))
                        .setPosterPath(jsonMovie.getString("poster_path"));
                movieList.add(movie);
            }

        } catch (JSONException e) {

        }

    }


}
