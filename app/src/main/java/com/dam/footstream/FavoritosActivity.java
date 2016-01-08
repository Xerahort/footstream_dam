package com.dam.footstream;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class FavoritosActivity extends AppCompatActivity {

    public static ArrayList<String> datos;
    private ArrayAdapter<String> adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        ListView favoriteTeams = (ListView) findViewById(R.id.list_favs);

        datos = new ArrayList<>(SplashActivity.favoriteTeams.keySet());

        adaptador =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        datos);

        favoriteTeams.setAdapter(adaptador);
        favoriteTeams.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idteam = SplashActivity.favorites.getString(datos.get(position), "");
                if (!idteam.equals("")) {
                    Intent i = new Intent(FavoritosActivity.this, EquipoActivity.class);
                    i.putExtra(EquipoActivity.TEAM_ID, idteam);
                    startActivity(i);
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adaptador != null) adaptador.notifyDataSetChanged();
    }
}
