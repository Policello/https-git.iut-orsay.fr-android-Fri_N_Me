package fr.iut_orsay.frinme.rest.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AfficherUser {

    @SerializedName("id")
    @Expose
    private List<StringBD> event;

    @SerializedName("message")
    @Expose
    private String message;

    public List<StringBD> getEvent() {
        return event;
    }

    public void setEvent(List<StringBD> event) {
        this.event = event;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @SerializedName("success")
    @Expose
    private boolean success;

}
