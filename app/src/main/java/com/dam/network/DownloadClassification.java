package com.dam.network;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;

import com.dam.data.ClassificationPosition;
import com.dam.footstream.ClasificacionActivity;
import com.dam.footstream.R;
import com.dam.receivers.WidgetProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Downloads informacion about the classification from the API football-data.org
 * The classification list-object is returned, containing all the informacion about the team.
 * <p/>
 * Created by JMRB on 01/12/2015.
 */
public class DownloadClassification extends NetworkTask<String, Void, List<ClassificationPosition>> {

    private String league_id;
    private ClasificacionActivity activity;
    private ListProvider prov;
    private AppWidgetManager widgetManager;
    private Context context;

    public DownloadClassification( ClasificacionActivity activity) {
        this.activity = activity;
    }

    public DownloadClassification(AppWidgetManager appWidgetManager, ListProvider listProvider, Context context) {
        this.prov = listProvider;
        this.widgetManager = appWidgetManager;
        this.context = context;}

    @Override
    protected List<ClassificationPosition> doInBackground(String... params) {
        league_id = params[0];
        List<ClassificationPosition> t = new ArrayList<>();
        // enviar cuestión
        try {
            // leer respuesta
            String response = readResponse(sendRequest(new URL(HTTP + HOST + PATH + "soccerseasons/" + league_id + "/leagueTable")));
            t = parseTeam(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return t;
    }

    @Override
    protected void onPostExecute(List<ClassificationPosition> classification) {
        super.onPostExecute(classification);
        if (this.activity != null) activity.dataLoaded(classification);
        else if (this.prov != null) {
            prov.dataLoaded(classification);
            widgetManager.notifyAppWidgetViewDataChanged(widgetManager.getAppWidgetIds(
                    new ComponentName(context, WidgetProvider.class)), R.id.listViewWidget);
        }
    }


    /**
     * Parses the Classification with it's information out of the JSON-String in the response
     *
     * @param response JSON-String response
     * @return the List-object containing the new information
     */
    private List<ClassificationPosition> parseTeam(String response) {
        ArrayList<ClassificationPosition> t = new ArrayList<>();
        try {
            JSONArray items = new JSONObject(response).getJSONArray("standing");

            //TODO print all information

            for (int i = 0; i<items.length(); i++) {
                JSONObject object =  items.getJSONObject(i);
                ClassificationPosition pos =
                        new ClassificationPosition(
                                object.getInt("position"),
                                object.getString("teamName"),
                                object.getInt("points"),
                                object.getInt("goals"));
                t.add(pos);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return t;
    }

}
