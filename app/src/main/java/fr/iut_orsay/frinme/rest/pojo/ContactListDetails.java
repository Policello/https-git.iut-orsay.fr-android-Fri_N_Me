package fr.iut_orsay.frinme.rest.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import fr.iut_orsay.frinme.model.ContactModel;



public class ContactListDetails {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("tab")
    @Expose
    private List<ContactModel> Contacts  ;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("success")
    @Expose
    private Boolean test;

    public List<ContactModel> getContacts() {
        return Contacts;
    }

    public String getMessage() {
        return message;
    }

    public int getid() {
        return id;
    }

    public Boolean getSuccess() { return test; }

}
