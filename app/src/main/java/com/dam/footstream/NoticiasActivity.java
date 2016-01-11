package com.dam.footstream;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.dam.adapters.ImageAdapter;

public class NoticiasActivity extends AppCompatActivity {

    public static final String NOTICIA_LOGO_EXTRA = "noticia";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticias);

        final GridView gridview = (GridView) findViewById(R.id.gridview_noticias);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //Toast.makeText(NoticiasActivity.this, "" + position,Toast.LENGTH_SHORT).show();
                Intent i = new Intent(NoticiasActivity.this, NoticiaActivity.class);
                i.putExtra(NOTICIA_LOGO_EXTRA, position);
                startActivity(i);

            }
        });
    }
}
