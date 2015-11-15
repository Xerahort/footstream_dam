package com.dam.footstream;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    private Handler splashHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        Runnable r = new Runnable(){
            public void run(){
                Intent brain = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(brain);
                finish();
            }
        };

        if(isNetworkAvailable())
            splashHandler.postDelayed(r, 2000);
        else {
            //Notify user they aren't connected
            Toast.makeText(getApplicationContext(), "You aren't connected to the internet.", Toast.LENGTH_SHORT).show();
            //close the app
            finish();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
