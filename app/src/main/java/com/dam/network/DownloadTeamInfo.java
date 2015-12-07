package com.dam.network;

import android.util.Log;

import com.dam.data.Match;
import com.dam.data.Team;
import com.dam.footstream.EquipoActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Downloads informacion about the team from the API football-data.org
 * The ID of the team is passed as parameter (String).
 * A Team-object is returned, containing all the informacion about the team.
 * <p/>
 * Created by Felix on 17.11.2015.
 */
public class DownloadTeamInfo extends NetworkTask<String, Void, Team> {

    private String team_id;
    private EquipoActivity activity;

    public DownloadTeamInfo(EquipoActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Team doInBackground(String... params) {
        team_id = params[0];
        Team t = new Team();
        // enviar cuestión
        try {
            // leer respuesta
            String response = readResponse(sendRequest(new URL(HTTP + HOST + PATH + "teams/" + team_id)));
            t = parseTeam(response);

            response = readResponse(sendRequest(new URL(HTTP + HOST + PATH + "teams/" + team_id + "/fixtures")));
            t.setMatches(getMatchlist(response));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return t;
    }

    @Override
    protected void onPostExecute(Team team) {
        super.onPostExecute(team);
        activity.dataLoaded(team);
    }


    /**
     * Parses the Team with it's information out of the JSON-String in the response
     *
     * @param response JSON-String response
     * @return the Team-object containing the new information
     */
    private Team parseTeam(String response) {
        Team t = new Team();
        try {
            JSONObject object = new JSONObject(response);
            t.setName(object.getString(Team.NAME));
            t.setShort_name(object.getString(Team.SHORT_NAME));
            t.setMarket_value(object.getString(Team.MARKET_VALUE));
            t.setEmblem_url(object.getString(Team.EMBLEM_URL));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return t;
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
