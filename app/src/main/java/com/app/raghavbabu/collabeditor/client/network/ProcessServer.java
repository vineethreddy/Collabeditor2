package com.app.raghavbabu.collabeditor.client.network;

import android.os.AsyncTask;
import android.util.Log;

import com.app.raghavbabu.collabeditor.client.Objects.BasicVariables;
import com.app.raghavbabu.collabeditor.client.Objects.DocumentObjects;
import com.app.raghavbabu.collabeditor.client.Objects.PatchAndDiff;
import com.app.raghavbabu.collabeditor.client.activity.NewFileActivity;

import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class ProcessServer
 * Each Process receives event from other process and updates its vector clock.
 * @author Raghav Babu
 * Date : 03/22/2016
 */
public class ProcessServer extends AsyncTask<Void,Void,Boolean> {

	private InetSocketAddress boundPort = null;
	private int port;
	private ServerSocket serverSocket;
	Process process;

	public ProcessServer() {
		this.port = BasicVariables.clientServerPort;
	}

	@Override
	protected Boolean doInBackground(Void... params) {

		try {

			initServerSocket();

			while(true) {

				Socket connectionSocket;


				connectionSocket = serverSocket.accept();
				Log.v("Client connected : ", connectionSocket.getInetAddress().getHostName());
				ObjectInputStream din = new ObjectInputStream(connectionSocket.getInputStream());


				//Will receive the edit class object.

				EditClass editClass = (EditClass)din.readObject();
				Log.v("Edtcls Frm Server : ", editClass+"");

				//from the map i ll get document objects. update the document.
				//from the map document and shadow object can be retrieved.
				DocumentObjects docObjects = NewFileActivity.fileDocumentObjectsMap.get(editClass.getFileName());

				//call patchIt(shadowCopy,original,EditClass received,false) method from PatchAndDiffClass
				//discuss about boolean.
				PatchAndDiff.patchIt(editClass, docObjects.getDocument(), docObjects.getShadowDocument(),false);

				//clear all edits. In EditClassUtil, call static method clearEdits. get from map and empty the queue.
				//EditClassUtil.ClearEdits(docObjects.getEditClass());

				connectionSocket.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;

	}

	/**
	 * method which initialized and bounds a server socket to a port.
	 * @return void.
	 */
	private void initServerSocket()
	{
		boundPort = new InetSocketAddress(port);
		try
		{
			serverSocket = new ServerSocket(port);

			if (serverSocket.isBound())
			{
				System.out.println("Server bound to data port " + serverSocket.getLocalPort() + " and is ready...");
			}
		}
		catch (Exception e)
		{
			System.out.println("Unable to initiate socket.");
		}

	}

	}


