package fr.iut_orsay.frinme.rest.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import fr.iut_orsay.frinme.model.ContactModel;
import fr.iut_orsay.frinme.view.Contact;

/**
 * Created by yyang5 on 10/04/2018.
 */

public class RechercheDynamique {

    @SerializedName("message")
    @Expose
    private List<ContactModel> message;

    public List<ContactModel> getMessage() {
        return message;
    }
}
