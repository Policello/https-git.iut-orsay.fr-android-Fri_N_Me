package fr.iut_orsay.frinme.rest;

import fr.iut_orsay.frinme.rest.pojo.EventDetails;
import fr.iut_orsay.frinme.rest.pojo.EventListDetails;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RestService {

    @POST("event/listEventsSort/")
    Call<EventListDetails> getEventDetailedList();

    @FormUrlEncoded
    @POST("event/listEventsMapDetails/")
    Call<EventDetails> getEventDetails(@Field("nomEvent") String nomEvent);

}