package com.dam.footstream;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dam.data.Match;
import com.dam.data.Team;
import com.dam.network.DownloadNotifications;
import com.dam.network.TwitterTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LinearLayout cardviewLayout;
    //contains the teamnames of teams whose tweets haven't been already added
    public static ArrayList<String> notAddedTweets = new ArrayList<>();
    //contains the teamnames of teams which have been removed from the favorites and whose tweets appear in MainActivity
    public static ArrayList<String> notRemovedTweets = new ArrayList<>();
    private SimpleDateFormat format = new SimpleDateFormat("HH:mm E dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cardviewLayout = (LinearLayout) findViewById(R.id.cardviewLayout);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        handleIntent(getIntent());

        new DownloadNotifications(this).execute();

        for (String team : SplashActivity.favoriteTeams.keySet())
            new TwitterTask(this, team).execute(team);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //show tweets of recently added favorite teams
        for (String team : notAddedTweets)
            new TwitterTask(this, team).execute(team);
        notAddedTweets.clear();
        for (String team : notRemovedTweets)
            removeTeamTweets(team);
        notRemovedTweets.clear();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            Intent busqueda_intent = new Intent(MainActivity.this, BusquedaActivity.class);
            busqueda_intent.putExtra(SearchManager.QUERY, query);
            startActivity(busqueda_intent);

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_clasificacion) {
            startActivity(new Intent(MainActivity.this, ClasificacionActivity.class));
        } else if (id == R.id.nav_noticias) {
            startActivity(new Intent(MainActivity.this, NoticiasActivity.class));
        } else if (id == R.id.nav_radios) {
            startActivity(new Intent(MainActivity.this, RadiosActivity.class));
        } else if (id == R.id.nav_alarma_sleep) {
            startActivity(new Intent(MainActivity.this, AlarmaSleepActivity.class));
        } else if (id == R.id.nav_favoritos) {
            startActivity(new Intent(MainActivity.this, FavoritosActivity.class));
        } else if (id == R.id.nav_calendario) {
            startActivity(new Intent(MainActivity.this, CalendarioActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    /**
     * Adds new Tweets as cardviews to the MainActivity.
     *
     * @param tweets
     * @param team
     */
    public void twitterDataLoaded(ArrayList<twitter4j.Status> tweets, String team) {
        for (final twitter4j.Status status : tweets) {
            final CardView cardView = (CardView) getLayoutInflater().inflate(R.layout.twitter_cardview, null);
            TextView textView = (TextView) cardView.findViewById(R.id.twitter_textview);
            TextView dateTextView = (TextView) cardView.findViewById(R.id.twitter_date);
            TextView teamName = (TextView) cardView.findViewById(R.id.twitter_teamname);
            LinearLayout layout = (LinearLayout) cardView.findViewById(R.id.cardview_wrapper_layout);

            textView.setText(Html.fromHtml("<font color=\"#00aced\">@" + status.getUser().getScreenName() + "</font>: " + status.getText() + ""));
            dateTextView.setText(format.format(status.getCreatedAt()));
            teamName.setText(team);
            addTweetAtRightPosition(cardView, status.getCreatedAt());
            //cardviewLayout.addView(cardView);
            cardviewLayout.invalidate();

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //open tweet in browser or Twitter app (if installed)
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + status.getUser().getScreenName() + "+/status/" + status.getId()));
                    startActivity(intent);
                }
            });

        }
    }

    /**
     * Adds the given cardView at the right position in the CardView-Layout according to date (ascending).
     *
     * @param cardView the cardview that should be added
     */
    private void addTweetAtRightPosition(CardView cardView, Date tweetDate) {
        for (int i = 0; i < cardviewLayout.getChildCount(); i++) {
            View v = cardviewLayout.getChildAt(i);
            if (v.findViewById(R.id.twitter_date) != null) {
                try {
                    Date d = format.parse(((TextView) v.findViewById(R.id.twitter_date)).getText().toString());
                    if (d.before(tweetDate)) {
                        cardviewLayout.addView(cardView, i);
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        cardviewLayout.addView(cardView);
    }

    /**
     * Removes all tweets of the given team from the CardView-Layout.
     *
     * @param team team to delete
     */
    private void removeTeamTweets(String team) {
        for (int i = 0; i < cardviewLayout.getChildCount(); i++) {
            View v = cardviewLayout.getChildAt(i);
            if (v.findViewById(R.id.twitter_teamname) != null) {
                if (((TextView) v.findViewById(R.id.twitter_teamname)).getText().toString().equals(team)) {
                    cardviewLayout.removeView(v);
                    cardviewLayout.invalidate();
                    i--;
                }
            }
        }
    }

    public void dataLoaded(List<Match> list) {
        boolean encontrado = false;
        Match lm = null, nm = null;
        int i = 1;
        while (!encontrado && i<list.size()) {
            if (list.get(i).getStatus().equals("TIMED")) {
                nm = list.get(i);
                lm = list.get(i-1);
                encontrado = true;
            }
            i++;
        }

        TextView tvNot1Away = (TextView)findViewById(R.id.not_match_awayteam_textview);
        TextView tvNot1Date = (TextView)findViewById(R.id.not_match_date_textview);
        TextView tvNot1Home = (TextView)findViewById(R.id.not_match_hometeam_textview);
        TextView tvNot1Scor = (TextView)findViewById(R.id.not_match_score_textview);

        tvNot1Away.setText(lm.getAwayTeam().getName());
        tvNot1Date.setText(lm.getDateText());
        tvNot1Home.setText(lm.getHomeTeam().getName());
        tvNot1Scor.setText(lm.getGoalsHomeTeam() + " - " + lm.getGoalsAwayTeam());

        TextView tvNot2Away = (TextView)findViewById(R.id.not2_match_awayteam_textview);
        TextView tvNot2Date = (TextView)findViewById(R.id.not2_match_date_textview);
        TextView tvNot2Home = (TextView)findViewById(R.id.not2_match_hometeam_textview);

        tvNot2Away.setText(nm.getAwayTeam().getName());
        tvNot2Date.setText(nm.getDateText());
        tvNot2Home.setText(nm.getHomeTeam().getName());

    }
}
