package com.app.raghavbabu.collabeditor.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.app.raghavbabu.collabeditor.client.Objects.DocumentObjects;
import com.app.raghavbabu.collabeditor.client.Objects.EditClassUtil;
import com.app.raghavbabu.collabeditor.client.Objects.Operation;
import com.app.raghavbabu.collabeditor.client.Objects.PatchAndDiff;
import com.app.raghavbabu.collabeditor.client.Objects.ShadowDocument;
import com.app.raghavbabu.collabeditor.client.R;
import com.app.raghavbabu.collabeditor.client.network.AsyncResponse;
import com.app.raghavbabu.collabeditor.client.network.Document;
import com.app.raghavbabu.collabeditor.client.network.EditClass;
import com.app.raghavbabu.collabeditor.client.network.TCPClient;

public class SharedFileEditTextActivity extends AppCompatActivity implements AsyncResponse<EditClass> {

    private Toolbar toolbar;
    String fileName;
    Document document;
    String currentCopy;
    EditText textContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_file_edit_text);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = this.getIntent().getExtras();

        if (extras != null) {
            document = (Document)extras.getSerializable("receivedDocument");
            //Log.v("Received Edit Document ", document +"");
            fileName = document.getFileName();
        }

        //from the map document and shadow object can be retrieved.
        final DocumentObjects docObjects = NewFileActivity.fileDocumentObjectsMap.get(fileName);

        textContent = (EditText) findViewById(R.id.sharedEditText);
        textContent.setText(document.getContent());
        currentCopy = textContent.getText().toString();

        //update document.
        docObjects.setDocument(document);

        //update shadow document.
        ShadowDocument shadowDocument = docObjects.getShadowDocument();
        shadowDocument.setContent(document.getContent());
        shadowDocument.setFileName(document.getFileName());
        shadowDocument.setServerM(document.getServerM());
        shadowDocument.setClientN(document.getClientN());

        docObjects.setShadowDocument(shadowDocument);
        NewFileActivity.fileDocumentObjectsMap.put(fileName, docObjects);

        textContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentCopy = textContent.getText().toString();
                Log.v("Current Copy on change",currentCopy);

                docObjects.getDocument().setContent(currentCopy);
            }
        });


        sendEditClassToServer(docObjects, currentCopy);

    }

    public void showVersionList(View view){

        Intent intent = new Intent(this, VersionActivity.class);
        startActivity(intent);
    }

    public void sendEditClassToServer(DocumentObjects docObjects, String currentCopy){

        //to find diff,
        //document.updateString(currentCopy). then create EditClass Object and call addEdit(EditClassObject,currentCopy,shadowCopy,false)
        // in EditClassUtil. it will return updated Editclass.


        Document originalCopy = docObjects.getDocument();
        ShadowDocument shadowCopy = docObjects.getShadowDocument();
        originalCopy.setContent(currentCopy);

        EditClass editClass = docObjects.getEditClass();

        //Log.v("Edit class : ",editClass+"");
        Log.v("---Original Copy----",originalCopy+" "+shadowCopy);

        EditClassUtil.addEdit(editClass, originalCopy, shadowCopy, false);

        Log.v("Sending editClass", editClass + "");
        //Send it to the server.

        TCPClient<EditClass,Boolean> client = new TCPClient(editClass, Operation.SEND_SHARED_EDIT_CLASS, this);
        client.execute();


    }

    //update the client side edit activity for every 1 second and keep on sending edit classobject.
    @Override
    public void processFinish(EditClass responseObject, Operation operationType) {


       /* try {
            Thread.sleep(500);
        }catch(Exception e){
            e.printStackTrace();
        }
*/
        Log.v("-------------",responseObject.getUserName() +"-------------");
        Log.v("Receiving editClass", responseObject+"");
        Log.v("-------------","-------------");

        //from the map document and shadow object can be retrieved.
        DocumentObjects docObjects = NewFileActivity.fileDocumentObjectsMap.get(fileName);

        Log.v("Old Doc content :", docObjects.getDocument().getContent());
        //call patchIt(shadowCopy,original,EditClass received,false) method from PatchAndDiffClass
        //discuss about boolean.
        PatchAndDiff.patchIt(responseObject, docObjects.getDocument(), docObjects.getShadowDocument(), false);

        Log.v(" New Doc content :",docObjects.getDocument().getContent());


        //setting the docuemt after patching.
        textContent = (EditText) findViewById(R.id.sharedEditText);
        textContent.setText(docObjects.getDocument().getContent());
        currentCopy = textContent.getText().toString();

        //clear all edits. In EditClassUtil, call static method clearEdits. get from map and empty the queue.
        EditClassUtil.ClearEdits(docObjects.getEditClass());

        //Send it to the server.
        sendEditClassToServer(docObjects, currentCopy);

    }

    /*@Override
    protected*/

}
