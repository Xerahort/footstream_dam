package com.dam.footstream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dam.data.Match;
import com.dam.data.Team;
import com.dam.network.DownloadTeamInfo;

import java.util.concurrent.ExecutionException;

/**
 * Created by Felix on 17.11.2015.
 */
public class EquipoActivity extends Activity {

    public static final String TEAM_ID = "team_id";
    private String team_id; // nombre del equipo seleccionado
    private TextView team_textview;
    private TextView marketvalue_textview;
    private ListView matches_listview;
    private ProgressBar loading_progressbar;
    private BaseAdapter match_adapter;
    private Team team = new Team();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipo);

        Intent i = getIntent();
        team_id = i.getStringExtra(TEAM_ID);

        team_textview = (TextView) findViewById(R.id.teamname_textview);
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
        team_textview.setText(team.getName());
        marketvalue_textview.setText(team.getMarket_value());
        loading_progressbar.setVisibility(View.GONE);
        team_textview.setVisibility(View.VISIBLE);
        marketvalue_textview.setVisibility(View.VISIBLE);
        matches_listview.setVisibility(View.VISIBLE);
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

                if(m.isFinished()) listitem.score.setBackgroundResource(R.color.match_red);
                else listitem.score.setBackgroundResource(R.color.match_orange);

                return convertView;
            }
        };
        return match_adapter;
    }

    static class MatchListitem {
        TextView date, homeTeam, awayTeam, score;
    }

}
