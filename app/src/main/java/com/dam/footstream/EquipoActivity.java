package com.dam.footstream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Felix on 17.11.2015.
 */
public class EquipoActivity extends Activity {

    private static final String TEAM_NAME = "team_name";
    private String team_name; // nombre del equipo seleccionado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipo);

        Intent i = getIntent();
        team_name = i.getStringExtra(TEAM_NAME);

        // TODO load team information, display information in activity

    }



}
