package com.repogithub.restapicall.model;

import com.google.gson.annotations.SerializedName;

public class ModelUsername {


    @SerializedName("login")
    private String login;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

}
