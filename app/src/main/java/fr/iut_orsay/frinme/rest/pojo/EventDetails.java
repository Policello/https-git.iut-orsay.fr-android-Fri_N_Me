package fr.iut_orsay.frinme.rest.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import fr.iut_orsay.frinme.model.ContactModel;

public class EventDetails extends Message{

    @SerializedName("Description")
    @Expose
    private String desc;

    @SerializedName("Participants")
    @Expose
    private List<ContactModel> participants;

    @SerializedName("success")
    @Expose
    private boolean success;

    public String getDesc() {
        return desc;
    }

    public List<ContactModel> getParticipants() {
        return participants;
    }

    public boolean isSuccess() {
        return success;
    }
}
