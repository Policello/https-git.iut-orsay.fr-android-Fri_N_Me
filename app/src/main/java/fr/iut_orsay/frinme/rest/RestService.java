package fr.iut_orsay.frinme.rest;

import fr.iut_orsay.frinme.rest.pojo.AddContact;
import fr.iut_orsay.frinme.rest.pojo.Connexion;
import fr.iut_orsay.frinme.rest.pojo.DeleteContact;
import fr.iut_orsay.frinme.rest.pojo.EstAmi;
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
    @POST("contact/deleteContact.php")
    Call<DeleteContact> getDeleteFriend(@Field("idUser") int idUser, @Field("idUserDeleted") int idUserDeleted);

    @FormUrlEncoded
    @POST("contact/addContact.php")
    Call<AddContact> getAddFriend(@Field("idUser") int idUser, @Field("idUserAdded") int idUserAdded);

    @FormUrlEncoded
    @POST("user/estAmi.php")
    Call<EstAmi> getEstAmi(@Field("idUser") int idUser, @Field("idFriend") int idFriend);

    @FormUrlEncoded
    @POST("user/login/")
    Call<Connexion> checkLogin(@Field("mail") String mail, @Field("mdpasse") String mdpasse);

    @FormUrlEncoded
    @POST("user/inscription.php")
    Call<Connexion> addUser(@Field("mail") String mail, @Field("mdpasse") String mdpasse,
                            @Field("pseudo") String pseudo, @Field("commentaire") String commentaire,
                            @Field("latitude") Long latitude, @Field("longitude") Long longitude);


}