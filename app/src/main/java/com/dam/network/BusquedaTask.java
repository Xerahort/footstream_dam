package com.dam.network;

import android.util.Log;

import com.dam.data.Team;
import com.dam.footstream.BusquedaActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Felix on 23.11.2015.
 */
public class BusquedaTask extends NetworkTask<String, Void, ArrayList<Team>> {

    private BusquedaActivity activity;

    public BusquedaTask(BusquedaActivity activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<Team> doInBackground(String... params) {
        String search_string = params[0];
        ArrayList<Team> teams = new ArrayList<>();

        try {
            String response = readResponse(sendRequest(new URL(HTTP + HOST + PATH + "soccerseasons/399/teams")));
                Log.v("BusquedaTask", "response League: " + response);
                teams.addAll(getTeamsOfLeague(response));

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.v("BusquedaTask", "Number of Teams: " + Integer.toString(teams.size()));
        ArrayList<Team> foundTeams = search(teams, search_string);

        return foundTeams;
    }

    @Override
    protected void onPostExecute(ArrayList<Team> teams) {
        super.onPostExecute(teams);
        activity.dataLoaded(teams);
    }


    /**
     * Gets all the Team-names and Team-ids out of the JSON-String containing a league.
     *
     * @param response the JSON-String resulting from the league-request.
     * @return a ArrayList of teams, which are initialized with their name and id.
     */
    private ArrayList<Team> getTeamsOfLeague(String response) {
        ArrayList<Team> teams = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONObject(response).getJSONArray("teams");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String urlString = object.getJSONObject("_links").getJSONObject("self").getString("href");
                String id = urlString.substring(urlString.lastIndexOf('/') + 1);
                String name = object.getString(Team.NAME);
                teams.add(new Team(id, name));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return teams;
    }

    /**
     * Searches the given teamname in the list of teams.
     *
     * @param teams the ArrayList of teams
     * @param name  the search-string
     * @return an ArrayList with matching Teams
     */
    private ArrayList<Team> search(ArrayList<Team> teams, String name) {
        Log.v("BusquedaTask", "Searching for " + name + " in teams");
        ArrayList<Team> foundTeams = new ArrayList<>();
        // searching for teamnames that contain the given search-String
        for (Team t : teams) {
            // comparing if the teamname contains the searched string (ignoring case)
            if (t.getName().toLowerCase().contains(name.toLowerCase())) {
                Log.v("BusquedaTask", "Team in List: " + t.getName().toLowerCase() + "\tTeam searched: " + name.toLowerCase());
                foundTeams.add(t);
            }
        }
        return foundTeams;
    }

}
