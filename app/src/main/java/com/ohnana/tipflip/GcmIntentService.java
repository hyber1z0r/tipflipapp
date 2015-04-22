package com.ohnana.tipflip;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by jakobgaardandersen on 21/04/15.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    public static final String TAG = "GCM Intent";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras(); // Get the 'data'-property as a Bundle obj from the intent
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            switch (messageType) {
                case GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR:
                    sendNotification("Send error: " + extras.toString());
                    break;
                case GoogleCloudMessaging.MESSAGE_TYPE_DELETED:
                    sendNotification("Deleted messages on server: " + extras.toString());
                    // If it's a regular GCM message, do some work.
                    break;
                case GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE:
                    Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                    // Post notification of received message.
                    sendNotification("Received: " + extras.get("string"));
                    Log.i(TAG, "Received: " + extras.get("string"));
                    break;
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver. So that the cpu can go back to sleep if it wants
        // Cause we have now assured that we have handled the message received from GCM
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    private void sendNotification(String msg) {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        // A PendingIntent assures that who ever we give it to, can run the code with our permissions!
        // If it was a normal intent, it could only run it with it's own permissions.
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher) /* MUST! */
                .setContentTitle("GCM Notification") /* MUST! */
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg); /* MUST! */

        mBuilder.setVibrate(new long[]{1000, 2000, 500, 500});
        mBuilder.setLights(Color.GREEN, 3000, 3000);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        mBuilder.setSound(sound);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}