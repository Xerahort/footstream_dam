package com.dam.network;

import android.support.annotation.Nullable;
import android.util.Log;

import com.dam.data.Match;
import com.dam.data.Team;
import com.dam.footstream.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by JRB on 10/01/2016.
 */
public class DownloadNotifications extends NetworkTask<String, Void, List<Match>> {

    private String team_id;
    private MainActivity activity;

    public DownloadNotifications(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected List<Match> doInBackground(String... params) {
        // enviar cuestión
        String response = "";
        try {
            // leer respuesta
            response = readResponse(sendRequest(new URL(HTTP + HOST + PATH + "soccerseasons/399/fixtures")));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return getMatchlist(response);
    }

    @Override
    protected void onPostExecute(List<Match> list) {
        super.onPostExecute(list);
        if(activity != null)
            activity.dataLoaded(list);
    }

    /**
     * Returns a list of Match-objects parsed out of the response.
     *
     * @param response the response JSON-String that contains the matches
     * @return an ArrayList containing the matches
     */
    private ArrayList<Match> getMatchlist(String response) {
        ArrayList<Match> matches = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONObject(response).getJSONArray("fixtures");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            format.setTimeZone(TimeZone.getTimeZone("MEZ"));

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Match match = new Match();


                try {
                    match.setDate(format.parse(object.getString("date")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                match.setStatus(object.getString("status"));
                match.setMatchday(object.getString("matchday"));
                match.setHomeTeam(new Team(getTeamIDofURL(object.getJSONObject("_links").getJSONObject("homeTeam").getString("href")), object.getString("homeTeamName")));
                match.setAwayTeam(new Team(getTeamIDofURL(object.getJSONObject("_links").getJSONObject("awayTeam").getString("href")), object.getString("awayTeamName")));
                match.setGoalsHomeTeam(object.getJSONObject("result").getString("goalsHomeTeam"));
                match.setGoalsAwayTeam(object.getJSONObject("result").getString("goalsAwayTeam"));

                Log.v("DownloadTeamInfo", "Match added: " + match);

                matches.add(match);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return matches;
    }

    /**
     * Gets the team-id out of the URL (the last number that appears in the URL)
     *
     * @param url the url
     * @return the id of the team
     */
    private String getTeamIDofURL(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

}

