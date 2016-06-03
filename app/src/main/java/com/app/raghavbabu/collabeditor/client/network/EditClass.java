package com.app.raghavbabu.collabeditor.client.network;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

/*
 removeEditsFrom(int toRemoveFrom) //remove all edits from that number not including toRemoveFrom
 removeEditsTill(int toRemoveTill) //removes all edits till not including toRemoveFrom

*/
public class EditClass implements Serializable {

    private static final long serialVersionUID = 4077789149947712504L;
    private long clientN;
    private long serverM;
    private String userName;
    private String fileName;
    private Queue<DiffObject> diffQueue;


    /* EditClass object initialized using a userName of the client performing the edits and
    the fileName on which the edits are supposed to be performed*/
    public EditClass(String userName, String fileName)
    {
        this.userName = userName;
        this.fileName = fileName;
        this.diffQueue = new LinkedList<DiffObject>();
    }

    public long getClientN() {
        return clientN;
    }

    public void setClientN(long clientN) {
        this.clientN = clientN;
    }

    public long getServerM() {
        return serverM;
    }

    public void setServerM(long serverM) {
        this.serverM = serverM;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Queue<DiffObject> getDiffQueue() {
        return diffQueue;
    }

    public void setDiffQueue(Queue<DiffObject> diffQueue) {
        this.diffQueue = diffQueue;
    }

    @Override
    public String toString() {
        return "EditClass [clientN=" + clientN + ", serverM=" + serverM
                + ", userName=" + userName + ", fileName=" + fileName
                + ", diffs=" + diffQueue + "]";
    }

}
