package com.app.raghavbabu.collabeditor.client.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.raghavbabu.collabeditor.client.Objects.DocumentObjects;
import com.app.raghavbabu.collabeditor.client.Objects.Model;
import com.app.raghavbabu.collabeditor.client.Objects.Operation;
import com.app.raghavbabu.collabeditor.client.Objects.ShadowDocument;
import com.app.raghavbabu.collabeditor.client.R;
import com.app.raghavbabu.collabeditor.client.network.AsyncResponse;
import com.app.raghavbabu.collabeditor.client.network.Document;
import com.app.raghavbabu.collabeditor.client.network.LoginInputObject;
import com.app.raghavbabu.collabeditor.client.network.RequestObject;
import com.app.raghavbabu.collabeditor.client.network.TCPClient;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class NewFileActivity<T> extends AppCompatActivity implements AsyncResponse<T> {

    private List<Model> modelList = new LinkedList<Model>();
    private static Set<String> selectedCollaborators = new HashSet<>();
    public static Map<String, DocumentObjects> fileDocumentObjectsMap = new ConcurrentHashMap<>();
    private static String newFileName;
    private ArrayAdapter<Model> adapter;
    private Toolbar toolbar;

    public NewFileActivity() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_file);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView listView = (ListView)findViewById(R.id.android_list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            //selecting the collaborators.
            @Override
            public void onItemClick( AdapterView<?> parent, View item,
                                     int position, long id) {
                Model model = adapter.getItem( position );
                model.toggleChecked();
                ModelViewHolder viewHolder = (ModelViewHolder) item.getTag();
                viewHolder.getCheckBox().setChecked(model.isSelected());

            }
        });
        Log.v("ONcreate","coming_in");
        //server sends this list on login and this method populates the list view..
        Iterator<String> it = LoginActivity.loginObject.getUsers().iterator();

        while(it.hasNext()) {
            String user = (String)it.next();
            Model model = new Model(user);
            modelList.add(model);
        }

        adapter = new DynamicListAdapter(this,modelList);
        listView.setAdapter(adapter);

    }


    /** Holds one row.
     * */
    private static class ModelViewHolder {

        private CheckBox checkBox ;
        private TextView textView ;

        public ModelViewHolder() {}

        public ModelViewHolder( TextView textView, CheckBox checkBox ) {
            this.checkBox = checkBox ;
            this.textView = textView ;
        }
        public CheckBox getCheckBox() {
            return checkBox;
        }
        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }
        public TextView getTextView() {
            return textView;
        }
        public void setTextView(TextView textView) {
            this.textView = textView;
        }
    }


    private static class DynamicListAdapter extends ArrayAdapter<Model> {

        private LayoutInflater inflater;

        public DynamicListAdapter(Activity context, List<Model> list) {
            super(context, R.layout.simplerow,R.id.rowTextView,list);
            inflater = LayoutInflater.from(context) ;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Model model = this.getItem( position );

            CheckBox checkBox ;
            TextView textView ;

            // Create a new row view
            if ( convertView == null ) {
                convertView = inflater.inflate(R.layout.simplerow, null);

                // Find the child views.
                textView = (TextView) convertView.findViewById( R.id.rowTextView );
                checkBox = (CheckBox) convertView.findViewById( R.id.CheckBox01 );

                // Optimization: Tag the row with it's child views, so we don't have to
                // call findViewById() later when we reuse the row.
                convertView.setTag(new ModelViewHolder(textView,checkBox) );

                // If CheckBox is toggled, update the planet it is tagged with.
                checkBox.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;

                        Model model = (Model) cb.getTag();
                        model.setSelected(cb.isChecked() );
                        Log.v("Name", model.getName());
                        selectedCollaborators.add(model.getName());
                    }
                });
            }

            // Reuse existing row view
            else {
                // Because we use a ViewHolder, we avoid having to call findViewById().
                ModelViewHolder viewHolder = (ModelViewHolder) convertView.getTag();
                checkBox = viewHolder.getCheckBox() ;
                textView = viewHolder.getTextView() ;
            }

            // Tag the CheckBox with the Planet it is displaying, so that we can
            // access the planet in onClick() when the CheckBox is toggled.
            checkBox.setTag(model);

            checkBox.setChecked( model.isSelected() );
            textView.setText( model.getName() );


            return convertView;
        }

    }

    public void shareMsgToServerAndEdit(View view){

        //validating file Name.
        EditText editFileName = (EditText) findViewById(R.id.edit_filename);

        //store the file name.
        newFileName = editFileName.getText().toString();


            //check for empty string.
            if (newFileName.isEmpty()) {
                Toast.makeText(getBaseContext(), "Enter a File Name ", Toast.LENGTH_LONG).show();
                return;
            }

        Log.v("Selected collabs ",selectedCollaborators+"");

        //since new file both m and n will be 0;
        Document document = new Document(selectedCollaborators, newFileName, 0, 0);

        Log.v("User name",LoginActivity.userName);
        DocumentObjects documentObjects = new DocumentObjects(LoginActivity.userName, document);
        documentObjects.setDocument(document);

        fileDocumentObjectsMap.put(newFileName, documentObjects);

        //sending document object to server.
        TCPClient<Document,Boolean> client = new TCPClient(document, Operation.NEW_FILE_REQ, this);
        client.execute();

    }

    @Override
    public void processFinish(T responseObject, Operation operationType) {

        //New file response, so document will be empty

        if(responseObject instanceof Boolean) {
            if ((Boolean)responseObject) {
                Log.d("New File Activity : ", "File name and collaborators registered to server.");

                ShadowDocument shadowCopyObject = new ShadowDocument(newFileName, "", 0, 0);

                //get the old object from map and update it with new shadow copy.
                DocumentObjects objs = fileDocumentObjectsMap.get(newFileName);
                objs.setShadowDocument(shadowCopyObject);

                fileDocumentObjectsMap.put(newFileName, objs);

                //proceed to editClassActivity.
                Intent intent = new Intent(this, EditTextActivity.class);
                intent.putExtra("fileName", newFileName);
                startActivity(intent);

            } else {
                Toast.makeText(getBaseContext(), "FileName already used, Change File Name", Toast.LENGTH_LONG).show();
            }
        }

        else if(responseObject instanceof LoginInputObject){
            LoginActivity.loginObject = (LoginInputObject) responseObject;

        }
    }
/*
    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected  void onRestart(){
        super.onRestart();
    }


    @Override
    protected void onResume(){
       super.onResume();
        Log.v("Order","Starting again");

        RequestObject getUpdatedFileList = new RequestObject(LoginActivity.userName);

        TCPClient<RequestObject,LoginInputObject> client = new TCPClient(getUpdatedFileList, Operation.UPDATED_FILE_REQ, this);
        client.execute();
        Iterator<String> it = LoginActivity.loginObject.getUsers().iterator();
        modelList.clear();
        while(it.hasNext()) {
            String user = (String)it.next();
            Model model = new Model(user);
            modelList.add(model);
        }
        ListView listView = (ListView)findViewById(R.id.android_list);
        adapter = new DynamicListAdapter(this,modelList);
        listView.setAdapter(adapter);


    }

    @Override
    protected void onStart(){
        super.onStart();

    }*/



}


