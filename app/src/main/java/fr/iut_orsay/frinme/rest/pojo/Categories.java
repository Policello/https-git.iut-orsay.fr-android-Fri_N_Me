package fr.iut_orsay.frinme.rest.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Categories {

    @SerializedName("message")
    public List<String> message;

    public List<String> getMessage() {
        return message;
    }
}
