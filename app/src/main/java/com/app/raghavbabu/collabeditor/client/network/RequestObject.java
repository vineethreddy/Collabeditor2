package com.app.raghavbabu.collabeditor.client.network;
import java.io.Serializable;


public class RequestObject implements Serializable{

    private static final long serialVersionUID = -4281321601748367630L;
    String userName;
    boolean register;

    public RequestObject(String userName){
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isRegister() {
        return register;
    }

    public void setRegister(boolean register) {
        this.register = register;
    }
}