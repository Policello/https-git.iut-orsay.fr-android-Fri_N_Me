package fr.iut_orsay.frinme.rest.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import fr.iut_orsay.frinme.model.ContactModel;

/**
 * Created by yyang5 on 10/04/2018.
 */

public class RechercheDynamique {

    @SerializedName("message")
    @Expose
    private List<String> message;

    private List<ContactModel> contacts;

    public void setContacts(List<String> message){
        contacts.clear();
        for (String i:message) {
                contacts.add(new ContactModel(i));
        }
    }

    public List<String> getMessage() {
        return message;
    }
    public List<ContactModel> getContacts() {
        return contacts;
    }
}
