package com.saskpolytech.cst135.Untils;

import com.saskpolytech.cst135.Movie;

import java.util.ArrayList;

/**
 * File Name:	AsyncResponse.java
 * Author:		Michael Sabares CST135
 * Date:		05/30/2019
 * Purpose:	    I had to make this MovieSearch can obtain the processed data from NetworkUtils.. maybe?
 */

public interface AsyncResponse {
    void processFinish(ArrayList<Movie> movieResultList);
}
