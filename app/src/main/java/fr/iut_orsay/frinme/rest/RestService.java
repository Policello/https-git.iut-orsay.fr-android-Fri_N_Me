package fr.iut_orsay.frinme.rest;

import retrofit2.Call;
import retrofit2.http.POST;

public interface RestService {

    @POST("listEventsSort/")
    Call<EventListDetails> getEventDetailedList();
}