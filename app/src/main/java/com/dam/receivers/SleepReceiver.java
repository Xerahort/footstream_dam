package com.dam.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.dam.footstream.SplashActivity;


/**
 * Configures a Broadcast Receiver that activates the sleep notification
 * given the alarm intent on user selected time.
 * <p/>
 * Created by JMRB on 24.11.2015.
 */
public class SleepReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Go to Sleep", Toast.LENGTH_LONG).show();
        Intent i = new Intent(context,SplashActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}