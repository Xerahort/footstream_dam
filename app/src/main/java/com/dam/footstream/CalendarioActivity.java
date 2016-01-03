package com.dam.footstream;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dam.data.Match;
import com.dam.network.CalendarDataTask;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Felix on 15.12.2015.
 *
 * https://github.com/roomorama/Caldroid
 */
public class CalendarioActivity extends AppCompatActivity {

    public CaldroidFragment calendar;
    public HashMap<Date,ArrayList<Match>> calendarData = new HashMap<>();
    private ListView match_listview;
    private BaseAdapter listAdapter;
    private ProgressBar progressBar;
    private ArrayList<Match> shownMatches = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        progressBar = (ProgressBar) findViewById(R.id.calendar_progressbar);
        match_listview = (ListView) findViewById(R.id.calendar_matchlist);

        calendar = (CaldroidFragment) getSupportFragmentManager().findFragmentById(R.id.calendar_fragment);

        CalendarDataTask task = new CalendarDataTask(this,new ArrayList<String>(SplashActivity.favoriteTeams.values()));
        task.execute();

        calendar.setCaldroidListener(new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                shownMatches = calendarData.get(date);
                listAdapter.notifyDataSetChanged();
            }
        });

        listAdapter = createListAdapter();
        match_listview.setAdapter(listAdapter);
    }

    private BaseAdapter createListAdapter() {
        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return shownMatches.size();
            }

            @Override
            public Object getItem(int position) {
                return shownMatches.get(position);
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

                Match m = shownMatches.get(position);

                listitem.date.setText(m.getDateText());
                listitem.homeTeam.setText(m.getHomeTeam().getName());
                listitem.awayTeam.setText(m.getAwayTeam().getName());
                listitem.score.setText(m.getGoalsHomeTeam() + " - " + m.getGoalsAwayTeam());

                if(m.isFinished()) listitem.score.setBackgroundResource(R.drawable.match_finished_background);
                else listitem.score.setBackgroundResource(R.drawable.match_not_finished_background);

                return convertView;
            }
        };
        return adapter;
    }


    public void dataLoaded() {
        progressBar.setVisibility(View.GONE);
        calendar.refreshView();
        match_listview.setVisibility(View.VISIBLE);
    }

    static class MatchListitem {
        TextView date, homeTeam, awayTeam, score;
    }

}
