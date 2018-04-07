package fr.iut_orsay.frinme.rest.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Connexion extends Message{

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("success")
    @Expose
    private boolean success;

    public int getId() {
        return id;
    }

    public boolean isSuccess() {
        return success;
    }
}