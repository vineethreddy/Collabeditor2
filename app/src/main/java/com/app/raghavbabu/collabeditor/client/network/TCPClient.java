package com.app.raghavbabu.collabeditor.client.network;

import android.os.AsyncTask;
import android.util.Log;

import com.app.raghavbabu.collabeditor.client.Objects.BasicVariables;
import com.app.raghavbabu.collabeditor.client.Objects.Operation;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * class TCPClient to communicate with Server.
 * Created by raghavbabu on 4/24/16.
 * T- object sent to server.
 * R- result object.
 */

public class TCPClient<T,R> extends AsyncTask<Void,Void,R> {

    T transferObj;
    String ipAddress;
    int port;
    public AsyncResponse delegate = null;
    Operation operationType;

    public TCPClient(T transferObj, Operation operationType,AsyncResponse delegate){
        this.ipAddress = BasicVariables.serverIPAddress;
        this.port = BasicVariables.serverPort;
        this.transferObj = transferObj;
        this.operationType = operationType;
        this.delegate = delegate;
    }

    @Override
    protected R doInBackground(Void... params) {

        Socket socket = null;
        Object responseObj = null;

        try {
            socket = new Socket(ipAddress, port);
            ObjectInputStream din = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream dout = new ObjectOutputStream(socket.getOutputStream());


            try {
                Thread.sleep(1000);
            }catch(Exception e){
                e.printStackTrace();
            }

            dout.writeObject(transferObj);

            Log.v("Message sent to : ", ipAddress);
            //        Log.v("Object : ", din.readObject().getClass().toString()+"");

            responseObj = din.readObject();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.v("details ",responseObj+"");

        return (R)responseObj;
    }


    @Override
    protected void onPostExecute(R responseObject) {
        delegate.processFinish(responseObject,operationType);
    }

}
