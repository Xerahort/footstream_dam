package com.dam.network;

import android.os.AsyncTask;
import android.util.Log;

import com.dam.data.Match;
import com.dam.data.Team;
import com.dam.footstream.CalendarioActivity;
import com.dam.footstream.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Felix on 17.12.2015.
 */
public class CalendarDataTask extends AsyncTask<Void, Void, Void> {

    private CalendarioActivity activity;
    private ArrayList<String> ids;
    private ArrayList<DownloadTeamInfo> tasks = new ArrayList<>();

    public CalendarDataTask(CalendarioActivity activity, ArrayList<String> ids) {
        this.activity = activity;
        this.ids = ids;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        for (final String id : ids) {
            Log.v("CalendarDataTask", "Getting information for team " + id + "...");
            DownloadTeamInfo task = new DownloadTeamInfo(null);
            //downloading info for team with given id...
            tasks.add(task);
            task.execute(id);
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        ArrayList<Team> teams = new ArrayList<>();

        for(DownloadTeamInfo task: tasks)
            try {
                Team t = task.get();
                Log.v("CalendarDataTask","Terminated!");
                teams.add(t);
                for (Match m : t.getMatches()) {
                    activity.calendar.setBackgroundResourceForDate(R.drawable.matchday_hightlight_calendar, m.getDate());
                    if (activity.calendarData.containsKey(m.getDateWithoutTime()))
                        activity.calendarData.get(m.getDateWithoutTime()).add(m);
                    else {
                        ArrayList<Match> a = new ArrayList<Match>();
                        a.add(m);
                        activity.calendarData.put(m.getDateWithoutTime(), a);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        activity.dataLoaded();
    }
}
