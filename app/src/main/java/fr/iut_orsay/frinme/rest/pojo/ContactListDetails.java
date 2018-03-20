package fr.iut_orsay.frinme.rest.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import fr.iut_orsay.frinme.model.ContactModel;



public class ContactListDetails {


    @SerializedName("ContactUser")
    @Expose
    private List<ContactModel> Contacts;

    @SerializedName("message")
    @Expose
    private String message;

    private int id;

    public List<ContactModel> getContacts() {
        return Contacts;
    }

    public String getMessage() {
        return message;
    }

    public int getid() {
        return id;
    }
}
