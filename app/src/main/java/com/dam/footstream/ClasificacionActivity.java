package com.dam.footstream;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dam.data.ClassificationPosition;
import com.dam.network.DownloadClassification;

import java.util.List;

public class ClasificacionActivity extends AppCompatActivity {

    public static final String LEAGUE_ID = "399";

    public static ClassificationPosition[] data;
    private ListView lst;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clasificacion);

        this.progressbar = (ProgressBar) findViewById(R.id.classification_progressbar);
        this.lst = (ListView)findViewById(R.id.classification_list);
        View header = getLayoutInflater().inflate(R.layout.cabecera_classification, null);
        this.lst.addHeaderView(header);

        DownloadClassification downloadTask = new DownloadClassification(this);
        downloadTask.execute(LEAGUE_ID);

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Intent i = new Intent(ClasificacionActivity.this, EquipoActivity.class);
                    i.putExtra(EquipoActivity.TEAM_ID, data[position - 1].getId());

                    startActivity(i);
                }
            }
        });

    }

    public void dataLoaded(List<ClassificationPosition> classification) {
        data = new ClassificationPosition[classification.size()];
        classification.toArray(data);
        lst.setAdapter(new ClassificationAdapter(this, data));
        progressbar.setVisibility(View.GONE);
        lst.setVisibility(View.VISIBLE);
    }

    private class ClassificationAdapter extends ArrayAdapter<ClassificationPosition> {

        private ClassificationPosition[] datos;

        public ClassificationAdapter(Context context, ClassificationPosition[] data) {
            super(context, R.layout.position_classification, data);
            datos = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View item = convertView;
            ViewHolder holder;

            if (item == null) {
                LayoutInflater inflater = getLayoutInflater();
                item = inflater.inflate(R.layout.position_classification, null);

                holder = new ViewHolder();
                holder.position = (TextView) item.findViewById(R.id.classification_position);
                holder.team= (TextView) item.findViewById(R.id.classification_name);
                holder.points = (TextView) item.findViewById(R.id.classification_points);
                holder.goals = (TextView) item.findViewById(R.id.classification_goals);

                item.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.position.setText(String.valueOf(datos[position].getPosition()));
            holder.team.setText(datos[position].getName());
            holder.points.setText(String.valueOf(datos[position].getPoints()));
            holder.goals.setText(String.valueOf(datos[position].getGoals()));

            return item;
        }

    }

    private static class ViewHolder {
        private TextView position, team, points, goals;
    }
}
