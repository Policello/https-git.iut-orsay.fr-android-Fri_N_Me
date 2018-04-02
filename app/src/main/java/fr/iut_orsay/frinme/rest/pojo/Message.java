package fr.iut_orsay.frinme.rest.pojo;

import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("message")
    public String message;

    public String getMessage() {
        return message;
    }
}
