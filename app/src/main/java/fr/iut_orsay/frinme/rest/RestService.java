package fr.iut_orsay.frinme.rest;

import fr.iut_orsay.frinme.rest.pojo.Connexion;
import fr.iut_orsay.frinme.rest.pojo.EventDetails;
import fr.iut_orsay.frinme.rest.pojo.EventListDetails;
import fr.iut_orsay.frinme.rest.pojo.ContactListDetails;
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

    @FormUrlEncoded
    @POST("contact/listContact.php")
    Call<ContactListDetails> getContactDetailedList(@Field("idUser") int idUser);

    @FormUrlEncoded
    @POST("user/login/")
    Call<Connexion> checkLogin(@Field("mail") String mail, @Field("mdpasse") String mdpasse);


}