package com.app.raghavbabu.collabeditor.client.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.app.raghavbabu.collabeditor.client.R;

public class SyncActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //shouldnt edit..juz view and synchronize it!!!
        EditText textContent = (EditText) findViewById(R.id.viewText);
        textContent.setEnabled(false);
    }

}
