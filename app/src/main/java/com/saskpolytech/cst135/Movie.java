package com.saskpolytech.cst135;

import java.io.Serializable;

/**
 * File Name:	Movie.java
 * Author:		Michael Sabares CST135
 * Date:		05/30/2019
 * Purpose:	    This class represents a movie that the user will be able to search through the movieDB.org API.
 *              Not all of the data we are able to get from the API is represented. This object is Serializable because
 *              we are passing the object through different fragments.
 */

public class Movie implements Serializable {

    //Movie Attributes
    private int id;
    private String title, overview, releaseDate, posterPath, savedLocation;
    private double voteAverage;
    private boolean inWatchList, inWatchHistory;

    public Movie() {
        this.savedLocation = ""; //Used to save the Long / Lat from Location object.

        //Booleans to determine what list it is in, if any.
        this.inWatchList = false;
        this.inWatchHistory = false;
    }

    public int getId() {
        return id;
    }

    public Movie setId(int id) {
        this.id = id;
        return this;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public Movie setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Movie setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getOverview() {
        return overview;
    }

    public Movie setOverview(String overview) {
        this.overview = overview;
        return this;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Movie setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public Movie setPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    public String getSavedLocation() {
        return savedLocation;
    }

    public Movie setSavedLocation(String savedLocation) {
        this.savedLocation = savedLocation;
        return this;
    }

    public boolean isInWatchList() {
        return inWatchList;
    }

    public Movie setInWatchList(boolean inWatchList) {
        this.inWatchList = inWatchList;
        return this;
    }

    public boolean isInWatchHistory() {
        return inWatchHistory;

    }

    public Movie setInWatchHistory(boolean inWatchHistory) {
        this.inWatchHistory = inWatchHistory;
        return this;
    }
}
