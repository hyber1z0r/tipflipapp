package com.ohnana.tipflip.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.ohnana.tipflip.MainActivity;
import com.ohnana.tipflip.R;

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
                    sendNotification("Tilbud på " + extras.get("category"));
                    Log.i(TAG, "Received: " + extras.get("category"));
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
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("com.ohnana.tipflip.notifyId", NOTIFICATION_ID);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, i, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_tf) /* MUST! */
                .setContentTitle("TipFlip") /* MUST! */
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg); /* MUST! */

        mBuilder.setVibrate(new long[]{0, 1500, 100, 250, 100, 250});
        mBuilder.setLights(Color.GREEN, 200, 200);
        //sets the notification sound to kaching sound
        Uri sound = Uri.parse("android.resource://com.ohnana.tipflip/" + R.raw.cashregister1);
        mBuilder.setSound(sound);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}