package com.dam.footstream;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dam.data.Match;
import com.dam.data.Team;
import com.dam.network.DownloadTeamInfo;

import java.util.concurrent.ExecutionException;

/**
 * Created by Felix on 17.11.2015.
 */
public class EquipoActivity extends AppCompatActivity {

    public static final String TEAM_ID = "team_id";
    private String team_id; // nombre del equipo seleccionado
    private TextView marketvalue_textview;
    private ListView matches_listview;
    private ProgressBar loading_progressbar;
    private ToggleButton fav;
    private BaseAdapter match_adapter;
    private Team team = new Team();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipo);

        Intent i = getIntent();
        team_id = i.getStringExtra(TEAM_ID);

        fav = (ToggleButton)findViewById(R.id.tb_favteam);
        matches_listview = (ListView) findViewById(R.id.matches_listview);
        loading_progressbar = (ProgressBar) findViewById(R.id.team_progresscircle);
        marketvalue_textview = (TextView) findViewById(R.id.marketvalue_textview);

        match_adapter = createMatchAdapter();
        matches_listview.setAdapter(match_adapter);

        DownloadTeamInfo downloadTask = new DownloadTeamInfo(this);
        downloadTask.execute(team_id);
        try {
            team = downloadTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }


    /**
     * Manages the view of the activty after the data of the team has been loaded.
     * The progressbar gets hidden, and the loaded data is shown in the different views of the activity.
     *
     * @param team the Team-object which contains the loaded data
     */
    public void dataLoaded(Team team) {
        match_adapter.notifyDataSetChanged();
        setTitle(team.getName());
        marketvalue_textview.setText(team.getMarket_value());
        loading_progressbar.setVisibility(View.GONE);
        marketvalue_textview.setVisibility(View.VISIBLE);
        matches_listview.setVisibility(View.VISIBLE);
        fav.setVisibility(View.VISIBLE);
        final String id = team.getName();
        String isFav = SplashActivity.favorites.getString(id,"");
        if (isFav.equals(team_id)) fav.setChecked(true);
        else fav.setChecked(false);
        fav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SplashActivity.favorites_editor.putString(id, team_id);
                    SplashActivity.favoriteTeams.put(id, team_id);
                    MainActivity.notAddedTweets.add(id);
                    MainActivity.notRemovedTweets.remove(id);
                }
                else {
                    SplashActivity.favorites_editor.remove(id);
                    SplashActivity.favoriteTeams.remove(id);
                    MainActivity.notAddedTweets.remove(id);
                    MainActivity.notRemovedTweets.add(id);
                }
                SplashActivity.favorites_editor.commit();
            }
        });
    }


    /**
     * Creates the adapter for the list of matches of the given Team
     *
     * @return
     */
    private BaseAdapter createMatchAdapter() {
        match_adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return team.getMatches().size();
            }

            @Override
            public Object getItem(int position) {
                return team.getMatches().get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                MatchListitem listitem;

                if (convertView == null) {
                    LayoutInflater inflater = getLayoutInflater();
                    convertView = inflater.inflate(R.layout.match_listitem, parent, false);

                    listitem = new MatchListitem();
                    listitem.date = (TextView) convertView.findViewById(R.id.match_date_textview);
                    listitem.homeTeam = (TextView) convertView.findViewById(R.id.match_hometeam_textview);
                    listitem.awayTeam = (TextView) convertView.findViewById(R.id.match_awayteam_textview);
                    listitem.score = (TextView) convertView.findViewById(R.id.match_score_textview);

                    convertView.setTag(listitem);
                } else listitem = (MatchListitem) convertView.getTag();

                Match m = team.getMatches().get(position);

                listitem.date.setText(m.getDateText());
                listitem.homeTeam.setText(m.getHomeTeam().getName());
                listitem.awayTeam.setText(m.getAwayTeam().getName());
                listitem.score.setText(m.getGoalsHomeTeam() + " - " + m.getGoalsAwayTeam());

                if(m.isFinished()) listitem.score.setBackgroundResource(R.drawable.match_finished_background);
                else listitem.score.setBackgroundResource(R.drawable.match_not_finished_background);

                return convertView;
            }
        };
        return match_adapter;
    }


    static class MatchListitem {
        TextView date, homeTeam, awayTeam, score;
    }
    
}
