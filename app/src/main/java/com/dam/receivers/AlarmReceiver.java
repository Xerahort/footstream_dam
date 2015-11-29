package com.dam.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.dam.footstream.R;
import com.dam.footstream.SplashActivity;

/**
 * Configures a Broadcast Receiver that activates the alarm notification
 * given the alarm intent on user selected time.
 * <p/>
 * Created by JMRB on 24.11.2015.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm", Toast.LENGTH_LONG).show();
        showNotification(context);
    }

    private void showNotification(Context context) {
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, SplashActivity.class), 0);

        // define sound URI, the sound to be played when there's a notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        // this is it, we'll build the notification!
        NotificationCompat.Builder mB = new NotificationCompat.Builder(context)
                .setContentTitle("Footstream")
                .setContentText("Alarm ")
                .setSmallIcon(R.drawable.ic_launcher_footstream)
                .setContentIntent(contentIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setAutoCancel(true)
                .setSound(soundUri);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, mB.build());
    }


}