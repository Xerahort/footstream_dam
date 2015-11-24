package com.dam.network;

import com.dam.data.Team;
import com.dam.footstream.EquipoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

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
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO download team-fixtures and players!

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

}
