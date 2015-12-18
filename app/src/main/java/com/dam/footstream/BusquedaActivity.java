package com.dam.footstream;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.data.Team;
import com.dam.network.BusquedaTask;

import java.util.ArrayList;

/**
 * Created by Felix on 23.11.2015.
 */
public class BusquedaActivity extends AppCompatActivity {

    private ProgressBar busqueda_progressbar;
    private ListView busqueda_listview;
    private ArrayList<Team> foundTeams = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);

        Intent intent = getIntent();

        String search_text = intent.getStringExtra(SearchManager.QUERY);
        Toast.makeText(this, "Texto buscado: " + search_text, Toast.LENGTH_LONG).show();

        busqueda_progressbar = (ProgressBar) findViewById(R.id.busqueda_progressbar);
        busqueda_listview = (ListView) findViewById(R.id.busqueda_listview);
        busqueda_listview.setAdapter(createListViewAdapter());

        busqueda_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(BusquedaActivity.this, EquipoActivity.class);
                i.putExtra(EquipoActivity.TEAM_ID, foundTeams.get(position).getId());

                startActivity(i);
            }
        });

        BusquedaTask busquedaTask = new BusquedaTask(this);
        busquedaTask.execute(search_text);

    }

    private ListAdapter createListViewAdapter() {
        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return foundTeams.size();
            }

            @Override
            public Object getItem(int position) {
                return foundTeams.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                BusquedaListitem listitem;

                if (convertView == null) {
                    LayoutInflater inflater = getLayoutInflater();
                    convertView = inflater.inflate(R.layout.busqueda_listitem, parent, false);

                    listitem = new BusquedaListitem();
                    listitem.foundText = (TextView) convertView.findViewById(R.id.busqueda_found_text);

                    convertView.setTag(listitem);
                } else listitem = (BusquedaListitem) convertView.getTag();

                Team t = foundTeams.get(position);
                listitem.foundText.setText(t.getName());

                return convertView;
            }
        };
        return adapter;
    }


    /**
     * Refreshes the adapter of the ListView with the new data.
     * Lets the progressbar dissapear and shows the ListView with the matching teams.
     *
     * @param found the ArrayList which contains the matching teams.
     */
    public void dataLoaded(ArrayList<Team> found) {
        foundTeams.addAll(found);
        ((BaseAdapter) busqueda_listview.getAdapter()).notifyDataSetChanged();
        busqueda_progressbar.setVisibility(View.GONE);
        busqueda_listview.setVisibility(View.VISIBLE);
    }

    static class BusquedaListitem {
        TextView foundText;
    }

}
