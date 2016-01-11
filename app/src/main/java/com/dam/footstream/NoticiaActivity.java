package com.dam.footstream;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dam.rss.RssItem;
import com.dam.rss.RssListListener;
import com.dam.rss.RssReader;

import java.util.List;

public class NoticiaActivity extends AppCompatActivity {

    private NoticiaActivity local;
    private int position;
    private static final String NEWSPAPERS[] = {"http://estaticos.marca.com/rss/portada.xml", "http://cadenaser.com/rss/ser/portada.xml", "http://as.com/rss/tags/ultimas_noticias.xml", "http://www.mundodeportivo.com/feed/rss/futbol", "http://www.sport.es/es/rss/last_news/rss.xml"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia);
        // Set reference to this activity
        local = this;
        position = getIntent().getIntExtra(NoticiasActivity.NOTICIA_LOGO_EXTRA, 0);

        RssDataTask task = new RssDataTask();

        // Start download RSS task
        task.execute(NEWSPAPERS[position]);


        // Debug the thread name
        Log.d("ITCRssReader", Thread.currentThread().getName());
    }

    public class RssDataTask extends AsyncTask<String, Void, List<RssItem>> {
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
            ArrayAdapter<RssItem> adapter = new ArrayAdapter<>(local, android.R.layout.simple_list_item_1, result);
            // Set list adapter for the ListView
            itcItems.setAdapter(adapter);

            // Set list view item click listener
            itcItems.setOnItemClickListener(new RssListListener(result, local));
        }
    }

}

