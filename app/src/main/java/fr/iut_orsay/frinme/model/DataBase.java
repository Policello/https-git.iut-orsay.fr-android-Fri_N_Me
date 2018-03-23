package fr.iut_orsay.frinme.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin on 23/03/2018.
 */

public class DataBase {



    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("tab")
    @Expose
    private List<ContactModel> tabContacts;

    @SerializedName("tabEventJO")
    @Expose
    private List<EventModel> tabEventJO;

    @SerializedName("tabEventUser")
    @Expose
    private List<EventModel> tabEventUser;;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("success")
    @Expose
    private Boolean test;

    public List<EventModel> getTabEventUser() {

        return tabEventUser;
    }

    public List<EventModel> getTabEventJO() {

        return tabEventJO;
    }

    public List<ContactModel> getTabContact() {

        return tabContacts;
    }
}
