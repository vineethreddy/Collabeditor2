package com.app.raghavbabu.collabeditor.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.app.raghavbabu.collabeditor.client.Objects.Operation;
import com.app.raghavbabu.collabeditor.client.R;
import com.app.raghavbabu.collabeditor.client.network.AsyncResponse;
import com.app.raghavbabu.collabeditor.client.network.LoginInputObject;
import com.app.raghavbabu.collabeditor.client.network.RequestObject;
import com.app.raghavbabu.collabeditor.client.network.TCPClient;

public class LoginActivity extends AppCompatActivity implements AsyncResponse<LoginInputObject> {


    boolean successfulLoginOrRegistration = false;
    public static LoginInputObject loginObject;
    public static String userName;

    @Override
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.app.raghavbabu.collabeditor.client.R.layout.activity_login);
    }

    public void performLoginOperation(View view){

        EditText editTextUserName = (EditText) findViewById(R.id.edit_username);
        String userName = editTextUserName.getText().toString();
        LoginActivity.userName = userName;

        //check for empty string.
        if(userName.isEmpty()){
            Toast.makeText(getBaseContext(),"Enter a valid Username", Toast.LENGTH_LONG).show();
        }

        else {
            Log.d("Log in : ", "User logging in ");

            //Request Object adding step.
            RequestObject reqObject = new RequestObject(userName);
            reqObject.setRegister(false);

            TCPClient<RequestObject,LoginInputObject> client = new TCPClient(reqObject,Operation.LOGIN,this);
            client.execute();

        }

    }

    public void registerUser(View view){

        EditText editTextUserName = (EditText) findViewById(R.id.edit_username);
        String userName = editTextUserName.getText().toString();
        LoginActivity.userName = userName;

        //check for empty string.
        if(userName.isEmpty()){
            Toast.makeText(getBaseContext(),"Enter a valid Username", Toast.LENGTH_LONG).show();
        }

        else {
            Log.d("RegisterActivity : ", "User registering in server ");

            //Request Object adding step.
            RequestObject reqObject = new RequestObject(userName);
            reqObject.setRegister(true);

            TCPClient<RequestObject,LoginInputObject> client = new TCPClient(reqObject, Operation.REGISTRATION,this);
            client.execute();

        }
    }

    @Override
    public void processFinish(LoginInputObject responseObject, Operation type){

        loginObject = new LoginInputObject();
        loginObject.setConnEstablished(responseObject.getConnEstablished() );
        loginObject.setFiles(responseObject.getFiles());
        loginObject.setUsers(responseObject.getUsers());

        String connStatus = String.valueOf(responseObject.getConnEstablished() );
        Log.v("Reply for " + type + " : ",connStatus );

        successfulLoginOrRegistration = responseObject.getConnEstablished();

        if(type == Operation.REGISTRATION) {

            if (successfulLoginOrRegistration) {
                Log.d("RegisterActivity : ", "Register successful ");

                Toast.makeText(getBaseContext(), type.toString() + " SUCCESSFUL", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, FileActivity.class);
                startActivity(intent);
            }

            else {
                Toast.makeText(getBaseContext(), "Server down, " +type.toString() + " : Failure ", Toast.LENGTH_LONG).show();
            }
        }

        else if(type == Operation.LOGIN) {

            if (successfulLoginOrRegistration) {
                Log.d("LoginActivity : ", "Valid User");

                Toast.makeText(getBaseContext(), type.toString() + " SUCCESSFUL", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, FileActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getBaseContext(), "Invalid user,Please Register", Toast.LENGTH_LONG).show();
            }
        }
    }

}
