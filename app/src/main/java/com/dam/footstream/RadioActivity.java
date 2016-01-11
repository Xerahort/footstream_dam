package com.dam.footstream;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.dam.adapters.RadioImageAdapter;

import java.io.IOException;

public class RadioActivity extends AppCompatActivity implements OnClickListener {

    private ImageButton buttonPlay;
    private ImageButton buttonStopPlay;
    private MediaPlayer player;
    private ImageView logo;
    private int position;
    private static final String RADIO_URLS[] = {"http://usa8-vn.mixstream.net:8138", "http://live1.interoutemediaservices.com/?i...13-3e8344c6a675", "http://cope.stream.flumotion.com/cope/copefm.mp3.m3u "};


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);
        position = getIntent().getIntExtra(RadiosActivity.RADIO_LOGO_EXTRA, 0);

        initializeUIElements();

        initializeMediaPlayer();
    }

    private void initializeUIElements() {

        logo = (ImageView) findViewById(R.id.image_radio);
        logo.setImageResource(RadioImageAdapter.mThumbIds[getIntent().getIntExtra(RadiosActivity.RADIO_LOGO_EXTRA, 0)]);

        buttonPlay = (ImageButton) findViewById(R.id.button_play);
        buttonPlay.setOnClickListener(this);

        buttonStopPlay = (ImageButton) findViewById(R.id.button_stop);
        buttonStopPlay.setEnabled(false);
        buttonStopPlay.setVisibility(View.GONE);
        buttonStopPlay.setOnClickListener(this);

    }

    public void onClick(View v) {
        if (v == buttonPlay) {
            startPlaying();
        } else if (v == buttonStopPlay) {
            stopPlaying();
        }
    }

    private void startPlaying() {
        buttonStopPlay.setVisibility(View.VISIBLE);
        buttonStopPlay.setEnabled(true);
        buttonPlay.setEnabled(false);
        buttonPlay.setVisibility(View.GONE);

        player.prepareAsync();

        player.setOnPreparedListener(new OnPreparedListener() {

            public void onPrepared(MediaPlayer mp) {
                player.start();
            }
        });

    }

    private void stopPlaying() {
        if (player.isPlaying()) {
            player.stop();
            player.release();
            initializeMediaPlayer();
        }

        buttonPlay.setVisibility(View.VISIBLE);
        buttonPlay.setEnabled(true);
        buttonStopPlay.setEnabled(false);
        buttonStopPlay.setVisibility(View.GONE);

    }

    private void initializeMediaPlayer() {
        player = new MediaPlayer();
        try {
            player.setDataSource(getApplicationContext(), Uri.parse(RADIO_URLS[position]));
        } catch (IllegalArgumentException | IOException | IllegalStateException e) {
            e.printStackTrace();
        }

        player.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Log.i("Buffering", "" + percent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player.isPlaying()) {
            player.stop();
        }
    }
}
