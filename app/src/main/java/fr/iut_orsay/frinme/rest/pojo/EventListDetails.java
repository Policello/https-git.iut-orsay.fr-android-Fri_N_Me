package fr.iut_orsay.frinme.rest.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import fr.iut_orsay.frinme.model.EventModel;

public class EventListDetails {

    @SerializedName("tabEventJO")
    @Expose
    private List<EventModel> eventsJo;

    @SerializedName("tabEventUser")
    @Expose
    private List<EventModel> events;

    @SerializedName("message")
    @Expose
    private String message;

    public List<EventModel> getEventsJo() {
        return eventsJo;
    }

    public List<EventModel> getEvents() {
        return events;
    }

    public String getMessage() {
        return message;
    }
}