package com.app.raghavbabu.collabeditor.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.app.raghavbabu.collabeditor.client.Objects.DocumentObjects;
import com.app.raghavbabu.collabeditor.client.Objects.Operation;
import com.app.raghavbabu.collabeditor.client.Objects.ShadowDocument;
import com.app.raghavbabu.collabeditor.client.R;
import com.app.raghavbabu.collabeditor.client.network.AsyncResponse;
import com.app.raghavbabu.collabeditor.client.network.Document;
import com.app.raghavbabu.collabeditor.client.network.DocumentRequest;
import com.app.raghavbabu.collabeditor.client.network.LoginInputObject;
import com.app.raghavbabu.collabeditor.client.network.RequestObject;
import com.app.raghavbabu.collabeditor.client.network.TCPClient;

import java.util.ArrayList;
import java.util.List;


public class FileActivity extends AppCompatActivity implements AsyncResponse<Document> {

    private Toolbar toolbar;
    private ListView listView;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Find the ListView resource.
        listView = (ListView) findViewById(R.id.android_list);

        List<String> list = new ArrayList<>();
        list.addAll(LoginActivity.loginObject.getFiles());

        adapter = new ArrayAdapter<String>(this, R.layout.filerow, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                String selectedFromList = (String) listView.getItemAtPosition(position);

                DocumentRequest reqDocument = new DocumentRequest(LoginActivity.userName, selectedFromList);

                TCPClient<DocumentRequest, Document> client = new TCPClient(reqDocument,
                        Operation.FILE_REQ, FileActivity.this);
                client.execute();
            }

        });
    }

    @Override
    public void processFinish(Document responseObject, Operation operationType) {

        //for requested Document Object.
        //server will return document object.

        DocumentObjects docObjects;

        // use old one.
        if (NewFileActivity.fileDocumentObjectsMap.containsKey(responseObject.getFileName())){

            docObjects = NewFileActivity.fileDocumentObjectsMap.get(responseObject.getFileName());

            //creating new document after receiving response object from server with version starting from beginning.
            docObjects.setDocument(new Document(responseObject.getFileName(),responseObject.getContent(), 0,0) );

            //creating new shadow object, so get and set new content.
            ShadowDocument newShadowCopy = new ShadowDocument(responseObject.getFileName(),responseObject.getContent(),
                    responseObject.getServerM(), responseObject.getClientN());

            docObjects.setShadowDocument(newShadowCopy);

        }
        //create new DocumentObjects in map if file entry not avaialble
        else{

            //creating new document after receiving response object from server with version starting from beginning.
            Document document = new Document(responseObject.getFileName(),responseObject.getContent(), 0, 0);
            docObjects = new DocumentObjects(LoginActivity.userName,document);

            //creating new shadow object, so get and set new content.
            ShadowDocument newShadowCopy = new ShadowDocument(responseObject.getFileName(),responseObject.getContent(),
                    responseObject.getServerM(), responseObject.getClientN());

            docObjects.setShadowDocument(newShadowCopy);

        }

        NewFileActivity.fileDocumentObjectsMap.put(responseObject.getFileName(), docObjects);

        Log.v("Received Document ", responseObject + "");
        Intent intent = new Intent(this, SharedFileEditTextActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("receivedDocument",responseObject);
        intent.putExtras(bundle);

        startActivity(intent);
    }


    public void createNewFile(View view) {
        Log.d("FileActivity : ", "Work on Document");
        Intent intent = new Intent(this, NewFileActivity.class);
        startActivity(intent);
   }
/*
    protected void onResume(){
        super.onResume();
        // Find the ListView resource.
        RequestObject getUpdatedFileList = new RequestObject(LoginActivity.userName);

        TCPClient<RequestObject,LoginInputObject> client = new TCPClient(getUpdatedFileList, Operation.UPDATED_FILE_REQ, this);
        client.execute();
        listView = (ListView) findViewById(R.id.android_list);

        List<String> list = new ArrayList<>();
        list.addAll(LoginActivity.loginObject.getFiles());

        adapter = new ArrayAdapter<String>(this, R.layout.filerow, list);
        listView.setAdapter(adapter);

    }*/

}
