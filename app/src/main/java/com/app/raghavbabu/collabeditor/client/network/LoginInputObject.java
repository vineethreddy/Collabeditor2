package com.app.raghavbabu.collabeditor.client.network;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by raghavbabu on 4/26/16.
 */
public class LoginInputObject implements Serializable {

    private static final long serialVersionUID = -8896547300079517662L;

    boolean connEstablished = false;
    Set<String> files;
    Set<String> users;

    public Set<String> getFiles() {
        return files;
    }

    public void setFiles(Set<String> files) {
        this.files = files;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }

    public boolean getConnEstablished() {
        return connEstablished;
    }

    public void setConnEstablished(boolean connEstablished) {
        this.connEstablished = connEstablished;
    }

    @Override
    public String toString() {
        return "LoginInputObject{" +
                "connEstablished=" + connEstablished +
                ", files=" + files +
                ", users=" + users +
                '}';
    }

}
