package com.app.raghavbabu.collabeditor.client.network;

import java.io.Serializable;

/**
 * Created by raghavbabu on 4/27/16.
 */
public class DocumentRequest implements Serializable {

    private static final long serialVersionUID = 2174270665230140083L;
    public String userName;
    public String fileName;

    public DocumentRequest(String userName, String fileName) {
        this.userName = userName;
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "DocumentRequest [userName=" + userName + ", fileName="
                + fileName + "]";
    }


}
