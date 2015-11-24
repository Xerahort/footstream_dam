package com.dam.footstream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dam.data.Team;
import com.dam.network.DownloadTeamInfo;

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


        // TODO load team information, display information in activity

        DownloadTeamInfo downloadTask = new DownloadTeamInfo(this);
        downloadTask.execute(team_id);


    }


    /**
     * Manages the view of the activty after the data of the team has been loaded.
     * The progressbar gets hidden, and the loaded data is shown in the different views of the activity.
     *
     * @param team the Team-object which contains the loaded data
     */
    public void dataLoaded(Team team) {
        team_textview.setText(team.getName());
        marketvalue_textview.setText(team.getMarket_value());
        loading_progressbar.setVisibility(View.GONE);
        team_textview.setVisibility(View.VISIBLE);
        marketvalue_textview.setVisibility(View.VISIBLE);

    }

}
