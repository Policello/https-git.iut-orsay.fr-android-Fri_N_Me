package fr.iut_orsay.frinme.rest.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import fr.iut_orsay.frinme.model.ContactModel;

public class EventDetails extends Message {

    @SerializedName("NomEvenement")
    @Expose
    private String nomEvent;
    @SerializedName("Description")
    @Expose
    private String desc;

    @SerializedName("Participants")
    @Expose
    private List<ContactModel> participants;

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("NomTypeEvenement")
    @Expose
    private String type;

    @SerializedName("DateEvenement")
    @Expose
    private Date DateEvent;

    @SerializedName("NumEvenement")
    @Expose
    private int NumEvent;



    public String getDesc() {
        return desc;
    }

    public List<ContactModel> getParticipants() {
        return participants;
    }

    public String getNomEvent() {
        return nomEvent;
    }

    public void setNomEvent(String nomEvent) {
        this.nomEvent = nomEvent;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setParticipants(List<ContactModel> participants) {
        this.participants = participants;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDateEvent() {
        return DateEvent;
    }

    public void setDateEvent(Date dateEvent) {
        DateEvent = dateEvent;
    }

    public int getNumEvent() {
        return NumEvent;
    }

    public void setNumEvent(int numEvent) {
        NumEvent = numEvent;
    }

    public boolean isSuccess() {
        return success;
    }
}
