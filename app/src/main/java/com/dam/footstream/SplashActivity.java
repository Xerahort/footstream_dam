package com.dam.footstream;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    protected static final String PREFERENCES = "FootstreamPreferences";
    protected static final String FAVORITES = "FootstreamFavorites";

    //protected static final String LEAGUE = "league";

    protected static HashMap<String, String> favoriteTeams;

    protected static SharedPreferences prefs, favorites;
    protected static SharedPreferences.Editor prefs_editor, favorites_editor;

    private Handler splashHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        TextView name = (TextView) findViewById(R.id.splashname);
        ImageView logo = (ImageView) findViewById(R.id.splashlogo);

        // load the animation
        Animation animt = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_move);
        Animation animi = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_move);
        animi.setStartOffset(500);
        name.startAnimation(animt);
        logo.startAnimation(animi);

        Runnable r = new Runnable() {
            public void run() {
                Intent brain = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(brain);
                finish();
            }
        };

        if (isNetworkAvailable()) {
            splashHandler.postDelayed(r, 2000);

            SplashActivity.prefs = getSharedPreferences(SplashActivity.PREFERENCES, Context.MODE_PRIVATE);
            SplashActivity.favorites = getSharedPreferences(SplashActivity.FAVORITES, Context.MODE_PRIVATE);
            SplashActivity.prefs_editor = SplashActivity.prefs.edit();
            SplashActivity.favorites_editor = SplashActivity.favorites.edit();
            SplashActivity.favoriteTeams = new HashMap<>();

            //SplashActivity.favorites_editor.clear().commit();

            //String league = prefs.getString(SplashActivity.LEAGUE, "PD");
            Map<String, ?> allEntries = SplashActivity.favorites.getAll();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                this.favoriteTeams.put(entry.getKey(), entry.getValue().toString());
                Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
                Log.v("SplashActivity", "Adding favorite team entry: " + entry.getKey() + ": " + entry.getValue().toString());
            }

        } else {
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
