package com.saskpolytech.cst135.Untils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.saskpolytech.cst135.MainActivity;
import com.saskpolytech.cst135.R;

/**
 * File Name:	NotificationHelper.java
 * Author:		Michael Sabares CST135
 * Date:		05/30/2019
 * Purpose:	    This class is used to help create a scheduled notification.
 */


public class NotificationHelper extends Service {

    /**
     * Not sure how to use this method. so i just left it alone.
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {

        // TODO: Return the communication channel to the service. I'm not sure how to use this.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * When this class is called, it creates and intent, a pending intent, and uses channels and builds a notification?
     * A lot of things is going on here that's above my pay grade and its 2am.
     */
    @Override
    public void onCreate() {

        // Get a reference to the manager
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        // A reference to a notification object
        Notification notification;

        // Create a pending intent to open this activity when the notification is clicked on
        Intent intent = new Intent(this, MainActivity.class); //DOES THIS ACTUALLY WORK?
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // API 26 and never use "channels" or notifications
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Declare channel information
            String channel = "1111";
            CharSequence name = "Movie Reminder";
            NotificationChannel notificationChannel = new NotificationChannel(channel, name, NotificationManager.IMPORTANCE_HIGH);

            // Add the channel to the manager
            notificationManager.createNotificationChannel(notificationChannel);

            // Use a builder to configure out notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setChannelId(channel)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentTitle("Movie Reminder!")
                    .setContentText("You gotta movie to watch!")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(false)
                    .setDefaults(Notification.DEFAULT_ALL);

            notification = builder.build();
        } else { // Old way without channels
            // Use a builder to configure out notification
            Notification.Builder builder = new Notification.Builder(getApplicationContext())
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setTicker("Ticker notify")
                    .setContentTitle("Movie Reminder!")
                    .setContentText("You gotta movie to watch!")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(false);

            notification = builder.build();
        }

        // The ID can be used if you need to identify between several different notification
        notificationManager.notify(1, notification);
    }

}


