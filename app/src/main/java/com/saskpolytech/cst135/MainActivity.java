package com.saskpolytech.cst135;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.saskpolytech.cst135.Movies.MovieSearch;
import com.saskpolytech.cst135.WatchHistory.WatchHistory;
import com.saskpolytech.cst135.WatchList.WatchList;

/**
 * File Name:	MainActivity.java
 * Author:		Michael Sabares CST135
 * Date:		05/30/2019
 * Purpose:	    This class is the only activity in the application. It is responsible for displaying
 *              all the fragments based on what is selected on the bottom nav bar.
 */


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    //Attributes
    private BottomNavigationView navBar;


    /**
     * When the application first runs, it will first run everything in this method, which
     * initializes elements in the Activity and sets the default fragment to the Search function.
     * @param savedInstanceState Honestly, have no idea what this really means to the class.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navBar = findViewById(R.id.bottomNavBar);
        navBar.setOnNavigationItemSelectedListener(this);

        //When the app launches, it will default to the Fragment with the search function.
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                    new MovieSearch()).commit();
        }

    }

    /**
     * Allows the user to switch to different fragments based on what is selected on the bottom
     * nav bar.
     * @param menuItem
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment selectedFragment = null;
        switch (menuItem.getItemId()) {
            case R.id.navSearch:
                selectedFragment = new MovieSearch();
                break;
            case R.id.navWatchList:
                selectedFragment = new WatchList();
                break;
            case R.id.navWatchHistory:
                selectedFragment = new WatchHistory();
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                selectedFragment).commit();
        return true;
    }
}
