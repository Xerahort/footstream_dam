package com.dam.footstream;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.dam.adapters.RadioImageAdapter;

public class RadiosActivity extends AppCompatActivity {

    public static final String RADIO_LOGO_EXTRA = "radio";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radios);

        final GridView gridview = (GridView) findViewById(R.id.gridview_radios);
        gridview.setAdapter(new RadioImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //Toast.makeText(RadiosActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(RadiosActivity.this, RadioActivity.class);
                i.putExtra(RADIO_LOGO_EXTRA, position);
                startActivity(i);

            }
        });
    }
}
