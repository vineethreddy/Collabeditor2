package com.app.raghavbabu.collabeditor.client.Objects;

/**
 * Created by raghavbabu on 4/28/16.
 */
public class ShadowDocument {

    String content;
    long clientN;
    long serverM;
    String fileName;

    // Constructor of the ShadowDocument class with the LinkedList of LstCollabarotors and String fileName as required arguments
    public ShadowDocument(String fileName,String content, long serverM, long clientN)
    {
        this.fileName = fileName;
        this.content = content;
        this.clientN = clientN;
        this.serverM = serverM;
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

    public void updateString(String content)
    {
        this.content = content;
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

    @Override
    public String toString() {
        return "ShadowDocument [content=" + content + ", clientN=" + clientN
                + ", serverM=" + serverM + ", fileName=" + fileName + "]";
    }

}
