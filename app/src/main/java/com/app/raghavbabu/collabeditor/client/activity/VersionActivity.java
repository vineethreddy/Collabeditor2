package com.app.raghavbabu.collabeditor.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.app.raghavbabu.collabeditor.client.R;

import java.util.ArrayList;
import java.util.List;

public class VersionActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //fill in the version list for that particular file.
        listView = (ListView) findViewById(R.id.android_versionlist);

        List<String> list = new ArrayList<>();
        list.add("Version 1");
        list.add("Version 2");
        list.add("Version 3");
        list.add("Version 4");


        adapter = new ArrayAdapter<String>(this, R.layout.filerow, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {

                Intent intent = new Intent(VersionActivity.this, SyncActivity.class);
                startActivity(intent);
            }

        });
    }

}
