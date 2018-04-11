package fr.iut_orsay.frinme.rest.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Permet de recuperer un String d'une liste
 * donnée en json
 */
public class StringBD {
    @SerializedName("NomEvenement")
    @Expose
    private String event;

    public String getNomEvent() {
        return event;
    }

    public void setCancer(String cancer) {
        this.event = cancer;
    }

    @Override
    public String toString() {
        return event;
    }
}
