package fr.iut_orsay.frinme.rest.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Martin on 20/03/2018.
 */

public class LocaliDetails {

    @SerializedName("Description")
    @Expose
    private String desc;

    /*@SerializedName("Participants")
    @Expose
    private List<ContactModel> participants;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("success")
    @Expose
    private boolean success;

    public String getDesc() {
        return desc;
    }

    public List<ContactModel> getParticipants() {
        return participants;
    }

    public String getMessage() {
        return message;
    }*/

    //public boolean isSuccess() {
        //return success;
    //}
}
