package fr.iut_orsay.frinme.rest.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StringBD {
    @SerializedName("NomEvenement")
    @Expose
    private String cancer;

    public String getCancer() {
        return cancer;
    }

    public void setCancer(String cancer) {
        this.cancer = cancer;
    }

    @Override
    public String toString() {
        return cancer;
    }
}
