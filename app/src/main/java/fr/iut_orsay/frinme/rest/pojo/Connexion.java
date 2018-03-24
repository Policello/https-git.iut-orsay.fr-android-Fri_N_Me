package fr.iut_orsay.frinme.rest.pojo;

import com.google.gson.annotations.SerializedName;

public class Connexion {

    @SerializedName("id")
    public int id;

    @SerializedName("message")
    public final String message;

    @SerializedName("success")
    private final boolean success;

    public Connexion(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    public boolean isSuccess() {
        return success;
    }
}