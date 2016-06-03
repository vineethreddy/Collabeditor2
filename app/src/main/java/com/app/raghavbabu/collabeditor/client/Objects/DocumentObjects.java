package com.app.raghavbabu.collabeditor.client.Objects;

import com.app.raghavbabu.collabeditor.client.network.Document;
import com.app.raghavbabu.collabeditor.client.network.EditClass;

/**
 * Created by raghavbabu on 4/28/16.
 */
public class DocumentObjects {

    String userName;
    Document document;
    ShadowDocument shadowDocument;
    EditClass editClass;

    public DocumentObjects(String userName, Document document) {
        this.document = document;
        this.userName = userName;
        this.editClass = new EditClass(userName, document.getFileName());
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public ShadowDocument getShadowDocument() {
        return shadowDocument;
    }

    public void setShadowDocument(ShadowDocument shadowDocument) {
        this.shadowDocument = shadowDocument;
    }

    public EditClass getEditClass() {
        return editClass;
    }

    public void setEditClass(EditClass editClass) {
        this.editClass = editClass;
    }


}
