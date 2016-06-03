package com.app.raghavbabu.collabeditor.client.network;

import java.io.Serializable;
import java.util.Set;


public class Document implements Serializable
{

    private static final long serialVersionUID = -3304455971439242921L;
    String content;
    long clientN;
    long serverM;
    String fileName;
    Set<String> collaboratorsList;

    // boolean value to restrict user's  typing access
    volatile boolean ConnectionOn;


    // First Constructor of the Document class with the list of collabarotors and String fileName,m,n as required arguments
    public Document(Set<String> collaboratorsList, String fileName, long serverM, long clientN)
    {
        this.collaboratorsList = collaboratorsList;
        this.fileName = fileName;
        this.clientN = clientN;
        this.serverM = serverM;
        this.content = "";

        this.ConnectionOn=false;
    }

    //Constructor with String fileName, content received from server ,m,n as required arguments
    //for already shared file
    public Document(String fileName,String content, long serverM, long clientN)
    {
        this.fileName = fileName;
        this.clientN = clientN;
        this.serverM = serverM;
        this.content = content;

        this.ConnectionOn = false;
    }

    //constructor to accept and create a new document in server on receiving new document created in client.
    public Document(Document clientDocument)
    {
        this.fileName = clientDocument.getFileName();
        this.content = clientDocument.getContent();

        this.ConnectionOn = false;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    // Method to updating the M(server version) value of a document object
    public void updateM()
    {
        this.serverM += 1;
    }

    public void updateM(long newM)
    {
        this.serverM = newM;
        updateM();
    }


    // Method to updating the N(client version) value of a document object
    public void updateN()
    {
        this.clientN += 1;
    }

    public void updateN(long newN)
    {
        this.clientN = newN;
        updateN();
    }

    public Set<String> getCollaboratorsList() {
        return collaboratorsList;
    }

    public void setCollaboratorsList(Set<String> collaboratorsList) {
        this.collaboratorsList = collaboratorsList;
    }

    public boolean isConnectionOn() {
        return ConnectionOn;
    }

    public void setConnectionOn(boolean connectionOn) {
        ConnectionOn = connectionOn;
    }

    @Override
    public String toString() {
        return "Document [content=" + content + ", clientN=" + clientN
                + ", serverM=" + serverM + ", fileName=" + fileName
                + ", collaboratorsList=" + collaboratorsList
                + ", ConnectionOn=" + ConnectionOn + "]";
    }
}