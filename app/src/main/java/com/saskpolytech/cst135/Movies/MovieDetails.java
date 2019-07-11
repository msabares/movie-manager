package com.saskpolytech.cst135.Movies;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.saskpolytech.cst135.Movie;
import com.saskpolytech.cst135.R;
import com.saskpolytech.cst135.Untils.MovieDBHelper;
import com.saskpolytech.cst135.Untils.NotificationHelper;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * File Name:	MovieDetails.java
 * Author:		Michael Sabares CST135
 * Date:		05/30/2019
 * Purpose:	    This class display a lot of the information about the movie, but as well, enables users
 *              to add the movie into the database as either Watch Later, Have already watched, or both.
 */

public class MovieDetails extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    //Attributes
    private final String BASE_URL_IMAGE = "https://image.tmdb.org/t/p/w"; //url path that allows us to obtain the image.
    private int imageSize = 500; //Values can be 200 - 500 maybe? //attribute that allows us to modify the size if.
    private Movie movie;
    private Button btnWatchList, btnWatched, btnSetReminder;

    //UI elements.
    private TextView txtMovieDetailsName, txtMovieDetailsAbout, txtMovieDetailsAvgScore;
    private ImageView ivMovieDetailsPictures;
    private ScrollView scrollView;
    private Calendar calReminder;

    //Attributes need for geolocation.
    private double geoLat, geoLong;
    LocationManager locationManager;
    LocationListener locationListener;

    //Class that allows us to use our Movie Database.
    private MovieDBHelper dbWatchList;

    /**
     * Method is called once the fragment is lunched. This method sets of the elements based on the
     *  Movie object and enables user functionality like adding movies to certain lists, reminders, and
     *  geolocatig.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.activity_movie_details, container, false);
        setRetainInstance(true);

        movie = (Movie) getArguments().getSerializable("movie"); //Used to obtain the ArrayList from the previous fragment.


        scrollView = myView.findViewById(R.id.scrollView); //Allows the view to be scrollable.

        calReminder = Calendar.getInstance();

        // Reference to the location changes
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        //Listens for any changes in the geolocation to update the attributes of this class.
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                geoLat = location.getLatitude();
                geoLong = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }

            @Override
            public void onProviderEnabled(String provider) { }

            @Override
            public void onProviderDisabled(String provider) { }
        };

        //Requests geolocation data.
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);
        } catch (SecurityException e) {

        }

        //Sets up UI elements.
        txtMovieDetailsName = myView.findViewById(R.id.txtMovieDetailsName);
        ivMovieDetailsPictures = myView.findViewById(R.id.ivMovieDetailsPicutres);
        txtMovieDetailsAbout = myView.findViewById(R.id.txtMovieDetailsAbout);
        txtMovieDetailsAvgScore = myView.findViewById(R.id.txtMovieDetailsAvgScore);
        btnWatchList = myView.findViewById(R.id.btnMovieDetailsWatchList);
        btnWatchList.setOnClickListener(this);
        btnWatched = myView.findViewById(R.id.btnMovieDetailsWatched);
        btnWatched.setOnClickListener(this);
        btnSetReminder = myView.findViewById(R.id.btnMovieDetailsSetReminder);
        btnSetReminder.setOnClickListener(this);

        dbWatchList = new MovieDBHelper(getContext());

        //Display movie information based on the Movie object.
        txtMovieDetailsName.setText(movie.getTitle());
        txtMovieDetailsAvgScore.setText((movie.getVoteAverage() * 10) + "%");
        txtMovieDetailsAbout.setText(movie.getOverview());

        //This API allows us to obtain images from a URL, as the movieDB.org API only provides us with
        //a URL for the image.
        Picasso.get()
                .load(BASE_URL_IMAGE + imageSize + movie.getPosterPath())
                .into(ivMovieDetailsPictures);

        //This method updates the button labels depending on the Movie object's attributes.
        updateButtonLabel();

        return myView;
    }


    /**
     * onClick adds functionality to the Add/Remove from Watchlist and WatchHistory, set reminders,
     * and obtain geolocation information, while updating, adding, and removing items from the
     * database appropriately
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            /**
             * THis Button relates to the Watch List. It will read and set the movie objects
             * inWatchList boolean to determine the function of the button, which are:
             *      Setting the boolean to true or false
             *      Populating the 'Set Reminder' button if true.
             *      Adding the movie to the database if already doesn't exist.
             */
            case R.id.btnMovieDetailsWatchList:
                dbWatchList.open();
                if(dbWatchList.checkifMovieExist(movie)) {

                    if (dbWatchList.checkIfMovieExistInWatchList(movie)) {
                        movie.setInWatchList(false);
                        btnSetReminder.setVisibility(View.GONE);
                        dbWatchList.updateMovie(movie);
                    } else {
                        movie.setInWatchList(true);
                        btnSetReminder.setVisibility(View.VISIBLE);
                        scrollView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        }, 20);
                        dbWatchList.updateMovie(movie);
                    }
                } else {
                    movie.setInWatchList(true);
                    dbWatchList.addMovieToWatchList(movie);
                    btnSetReminder.setVisibility(View.VISIBLE);
                    scrollView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    }, 20);
                }
                dbWatchList.close();
                updateButtonLabel();
                removeIfMovieBelongsInNoList(); //Removes the movie object if it has inWatchList and inWatchHistory both set to false.
                break;

            /**
             * THis Button relates to the Watch History. It will read and set the movie objects
             * inWatchHistory boolean to determine the function of the button, which are:
             *      Setting the boolean to true or false
             *      Updating the movie objects savedLocation appropriately
             *      Adding the movie to the database if already doesn't exist.
             *      Displays the name of the location base on the given Longitude and Latitude
             */
            case R.id.btnMovieDetailsWatched:
                dbWatchList.open();

                if(dbWatchList.checkifMovieExist(movie)) {
                    if (dbWatchList.checkIfMovieExistInWatchHistory(movie)) {
                        movie.setInWatchHistory(false);
                        dbWatchList.updateMovie(movie);
                    } else {
                        movie.setInWatchHistory(true);
                        movie.setSavedLocation(geoLong + " " + geoLat);
                        dbWatchList.updateMovie(movie);
                        try {
                            getAddress();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                } else {
                    movie.setSavedLocation(geoLong + " " + geoLat);
                    movie.setInWatchHistory(true);
                    dbWatchList.addMovieToWatchList(movie);
                    try {
                        getAddress();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                dbWatchList.close();
                updateButtonLabel();
                removeIfMovieBelongsInNoList(); //Removes the movie object if it has inWatchList and inWatchHistory both set to false.
                break;
            /**
             * Allows a user to set a reminder based on the input of the TimePickerDialog object.
             */
            case R.id.btnMovieDetailsSetReminder:
                new TimePickerDialog(getContext(), MovieDetails.this, calReminder
                        .get(Calendar.HOUR_OF_DAY), calReminder.get(Calendar.MINUTE),
                        false).show();
                break;
        }
    }

    /**
     * A method that checks the objects attributes to appropriately display the buttons and its labels.
     */
    private void updateButtonLabel() {
        dbWatchList.open();
        if(dbWatchList.checkifMovieExist(movie)) {
            if (dbWatchList.checkIfMovieExistInWatchList(movie)) {
                btnWatchList.setText("Remove from\nWatch List");
                btnSetReminder.setVisibility(View.VISIBLE);
            } else {
                btnWatchList.setText("Add to\nWatch List");
                btnSetReminder.setVisibility(View.GONE);
            }

            if (dbWatchList.checkIfMovieExistInWatchHistory(movie)) {
                btnWatched.setText("Remove from\nWatch History");

            } else {
                btnWatched.setText("Add to\nWatch History");
            }
        } else {
            btnWatchList.setText("Add to\nWatch List");
            btnSetReminder.setVisibility(View.GONE);
            btnWatched.setText("Add to\nWatch History");
        }

        dbWatchList.close();
    }

    /**
     * Checks the object if it doesn't any of its boolean values set to true. if it doesn't, it is removed from the database.
     */
    private void removeIfMovieBelongsInNoList() {
        dbWatchList.open();

        if(!dbWatchList.checkIfMovieExistInWatchList(movie) && !dbWatchList.checkIfMovieExistInWatchHistory(movie) && dbWatchList.checkifMovieExist(movie)) {
            dbWatchList.removeMovieFromWatchList(movie);
        }
        dbWatchList.close();
    }

    /**
     * A method that gets the name of the phones location using the Geocoder class and the long and lat from
     * a Location object.
     * @throws IOException
     */
    public void getAddress() throws IOException {

        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(geoLong, geoLat, 1);
            String currentLocation = addresses.get(0).getAddressLine(0);
            Toast.makeText(getContext(), "You watched this movie at: " + currentLocation, Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(getContext(), "Invalid coordinates. Unable to display the name of the location", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * used in the set reminder button, this method creates a pending Intent to be displayed based on the time
     * the user has set it and uses the NotificationHelper class to build and receive the notification.
     */
    public void createNotification() {
        // Intent to launch the service
        Intent intent = new Intent(getActivity(), NotificationHelper.class);
        PendingIntent pendingIntent = PendingIntent.getService(getContext(), 1000, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Schedule the service to run in 5 seconds using the alarm manager
        AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ( calReminder.getTimeInMillis() - System.currentTimeMillis() ), pendingIntent);

        Toast.makeText(getContext(), "You set a reminder for " + TimeUnit.MILLISECONDS.toMinutes(calReminder.getTimeInMillis() - System.currentTimeMillis()) + " about minutes", Toast.LENGTH_SHORT).show();

    }

    /**
     * Called when the user finishes using the TimePickerDialog, it updates our Calendar to represent the desired time, and
     * of when the user wants to be notified and calls createNotification to proceed to set up the notification.
     * @param view
     * @param hourOfDay
     * @param minute
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calReminder.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calReminder.set(Calendar.MINUTE, minute);

        createNotification();
    }

}


