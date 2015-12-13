package com.dam.footstream;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dam.rss.RssItem;
import com.dam.rss.RssListListener;
import com.dam.rss.RssReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NoticiaActivity extends AppCompatActivity {

    private NoticiaActivity local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia);
// Set reference to this activity
        local = this;

        RssDataTask task = new RssDataTask();

        // Start download RSS task
        task.execute("http://estaticos.marca.com/rss/portada.xml");

        // Debug the thread name
        Log.d("ITCRssReader", Thread.currentThread().getName());
    }

    public class RssDataTask extends AsyncTask<String, Void, List<RssItem> > {
        @Override
        protected List<RssItem> doInBackground(String... urls) {

            // Debug the task thread name
            Log.d("ITCRssReader", Thread.currentThread().getName());

            try {
                // Create RSS reader
                RssReader rssReader = new RssReader(urls[0]);

                // Parse RSS, get items
                return rssReader.getItems();

            } catch (Exception e) {
                Log.e("ITCRssReader", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<RssItem> result) {

            // Get a ListView from main view
            ListView itcItems = (ListView) findViewById(R.id.list_rss);

            // Create a list adapter
            ArrayAdapter<RssItem> adapter = new ArrayAdapter<>(local,android.R.layout.simple_list_item_1, result);
            // Set list adapter for the ListView
            itcItems.setAdapter(adapter);

            // Set list view item click listener
            itcItems.setOnItemClickListener(new RssListListener(result, local));
        }
    }
}

