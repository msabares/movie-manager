package com.saskpolytech.cst135.Untils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.saskpolytech.cst135.Movie;

import java.util.ArrayList;

/**
 * File Name:	MovieDBHelper.java
 * Author:		Michael Sabares CST135
 * Date:		05/30/2019
 * Purpose:	    This class helps us create and manage the Movie database.
 */

public class MovieDBHelper extends SQLiteOpenHelper {

    // The Attributes that will make up the Watch Later Database.
    public static final String DB_NAME = "movie_list.db",
            TABLE_NAME = "Watch_Later",
            ID = "_id",
            TITLE = "title",
            OVERVIEW = "overview",
            RELEASE_DATE = "releaseDate",
            POSTER_PATH = "posterPath",
            VOTE_AVERAGE = "vote_average",
            SAVED_LOCATION = "saved_location",
            IN_WATCH_LIST = "in_watch_list",
            IN_WATCH_HISTORY = "in_watch_history";

    public static final int DB_VERSION = 1;

    public SQLiteDatabase sqlDB; // Reference to the SQLite database on the file system

    /* Constructor */
    /**
     * The name of the database and the version number is already initialized for the purpose to
     * create a Watch later Database.
     * @param context Requires the context of the activity you are in.
     */
    public MovieDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /* Methods */

    /**
     * Automatically is called if the
     * database file does not already exists and creates the Watch List Table
     * @param db Used to call .execSQL to create the Watch List Table.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE " +
                TABLE_NAME + "(" +
                ID + " integer primary key, " +
                TITLE + " text not null," +
                OVERVIEW + " text not null," +
                RELEASE_DATE + " text not null," +
                POSTER_PATH + " text not null," +
                VOTE_AVERAGE + " double not null," +
                SAVED_LOCATION + " text not null," +
                IN_WATCH_LIST + " boolean not null," +
                IN_WATCH_HISTORY + " boolean not null);";
        db.execSQL(create);
    }

    /**
     * Automatically called with the version # increases. onUpgrade will drop the existing table
     * and call onCreate() to create a new table, removing all the data.
     * @param db Used to call .execSQL drop the Course Table.
     * @param oldVersion old Version Number
     * @param newVersion new Version Number
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drops existing table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Recreates it
        onCreate(db);
    }

    /**
     * When working with the database, you'll need to call this method so the database is writable?
     * @throws SQLException
     */
    public void open() throws SQLException {
        sqlDB = this.getWritableDatabase();
    }

    /**
     * When you are no longer working with the database, calling this method will prevent the
     * database to be writable?
     */
    public void close() {
        sqlDB.close();
    }

    /**
     * Takes in an Movie object so a ContentValues object can put the attributes of that object
     * into the database using .insert() method from SQLiteDatabase. This method also sets and returns
     * the auto increment value into the database.
     * @param movie The Movie object you wish to add into the Watch Later Database.
     */
    public void addMovieToWatchList(Movie movie) {
        // A Container to store each column and value
        ContentValues cvs = createCVSofMovie(movie);

        sqlDB.insert(TABLE_NAME, null, cvs);

    }

    /**
     * Takes in a movie object and called .update() to update the existing data using the movie.id
     * @param movie
     * @return
     */
    public boolean updateMovie(Movie movie) {
        // A Container to store each column and value
        ContentValues cvs = createCVSofMovie(movie);

        return sqlDB.update(TABLE_NAME, cvs, ID + " = " + movie.getId() , null) > 0;
    }

    /**
     * This method is used for making the Watch List and Watch History results. Rather than having two
     * separate methods for each type of list, this method is used to help make a generic method that
     * will display the appropriate results, which is used in getMovieList() method.
     * @param movie
     * @return
     */
    private ContentValues createCVSofMovie(Movie movie) {
        // A Container to store each column and value
        ContentValues cvs = new ContentValues();

        // Add an item for each column and value
        cvs.put(ID, movie.getId());
        cvs.put(TITLE, movie.getTitle());
        cvs.put(OVERVIEW, movie.getOverview());
        cvs.put(RELEASE_DATE, movie.getReleaseDate());
        cvs.put(POSTER_PATH, movie.getPosterPath());
        cvs.put(VOTE_AVERAGE, movie.getVoteAverage());
        cvs.put(SAVED_LOCATION, movie.getSavedLocation());
        cvs.put(IN_WATCH_LIST, movie.isInWatchList());
        cvs.put(IN_WATCH_HISTORY, movie.isInWatchHistory());

        return cvs;
    }

    /**
     * This method allows the user to remove a move form the database by passing in a movie object
     * they wish to remove.
     * @param movie
     * @return
     */
    public boolean removeMovieFromWatchList(Movie movie) {
        return sqlDB.delete(TABLE_NAME, ID + " = " + movie.getId(), null) > 0;
    }

    /**
     * this method takes in a string that identifies what kind of arraylist will it produce. Used in
     * WatchHistory and WatchList class.
     * @param listType
     * @return
     */
    public ArrayList<Movie> getMovieList(String listType) {
        ArrayList<Movie> tempArray = new ArrayList<>();
        String[] columns = new String[] {ID, TITLE, OVERVIEW, RELEASE_DATE, POSTER_PATH, VOTE_AVERAGE, SAVED_LOCATION ,IN_WATCH_LIST, IN_WATCH_HISTORY};
        Cursor cursor = null;

        switch (listType) {
            case "watch_list":
                cursor = sqlDB.query(TABLE_NAME, columns, IN_WATCH_LIST + " = " + 1, null, null, null, null, null);
                break;
            case "watch_history":
                cursor = sqlDB.query(TABLE_NAME, columns, IN_WATCH_HISTORY + " = " + 1, null, null, null, null, null);
                break;

                default:
                    cursor = sqlDB.query(TABLE_NAME, columns, null, null, null, null, null, null);
                    break;
        }

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            tempArray.add(new Movie()
                    .setId(cursor.getInt(0))
                    .setTitle(cursor.getString(1))
                    .setOverview(cursor.getString(2))
                    .setReleaseDate(cursor.getString(3))
                    .setPosterPath(cursor.getString(4))
                    .setVoteAverage(cursor.getDouble(5))
                    .setSavedLocation(cursor.getString(6))
                    .setInWatchList(cursor.getInt(7) > 0)
                    .setInWatchHistory(cursor.getInt(8) > 0));
        }

        return tempArray;
    }


    /**
     * Checks if a movie with the inWatchList attribute is set to true.
     * @param movie
     * @return
     */
    public boolean checkIfMovieExistInWatchList(Movie movie) {
        String[] column = new String[] {ID, TITLE, OVERVIEW, RELEASE_DATE, POSTER_PATH, VOTE_AVERAGE, SAVED_LOCATION ,IN_WATCH_LIST, IN_WATCH_HISTORY};
        Cursor fpCursor = sqlDB.query(TABLE_NAME, column, ID + " = " + movie.getId() + " AND " + IN_WATCH_LIST + " = " + 1, null, null, null, null, null);

        return fpCursor.getCount() > 0;
    }

    /**
     * Checks if a movie with the inWatchHistory attribute is set to true.
     * @param movie
     * @return
     */
    public boolean checkIfMovieExistInWatchHistory(Movie movie) {
        String[] column = new String[] {ID, TITLE, OVERVIEW, RELEASE_DATE, POSTER_PATH, VOTE_AVERAGE, SAVED_LOCATION ,IN_WATCH_LIST, IN_WATCH_HISTORY};
        Cursor fpCursor = sqlDB.query(TABLE_NAME, column, ID + " = " + movie.getId() + " AND " + IN_WATCH_HISTORY + " = " + 1, null, null, null, null, null);

        return fpCursor.getCount() > 0;
    }

    /**
     * Checks if a movie object exists in the database.
     * @param movie
     * @return
     */
    public boolean checkifMovieExist(Movie movie) {
        String[] column = new String[] {ID, TITLE, OVERVIEW, RELEASE_DATE, POSTER_PATH, VOTE_AVERAGE, SAVED_LOCATION ,IN_WATCH_LIST, IN_WATCH_HISTORY};
        Cursor fpCursor = sqlDB.query(TABLE_NAME, column, ID + " = " + movie.getId(), null, null, null, null, null);

        return fpCursor.getCount() > 0;
    }
}
