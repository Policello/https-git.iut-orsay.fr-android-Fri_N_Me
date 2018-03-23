package fr.iut_orsay.frinme.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un contact , les évenements auquels il participe et ses attributs.
 */
public class ContactModel implements Parcelable {

    @SerializedName("User")
    @Expose
    private int id;

    @SerializedName("UserFriend")
    @Expose
    private int userFriend;

    @SerializedName("Pseudo")
    @Expose
    private String pseudo;

    @SerializedName("Localisation")
    @Expose
    private Location lastLocalisation;

    private String lastEvent;
    private String notes;

    public ContactModel(int id) {
        this.id = id;
        //TODO: Récupérer les infos manquantes depuis le serveur
    }


    public ContactModel(int id, String pseudo, Location lastLocalisation, String lastEvent, String notes) {
        this.id = id;
        this.pseudo  = pseudo;
        this.lastLocalisation = lastLocalisation;
        this.lastEvent = lastEvent;
        this.notes = notes;
    }

    // Pour tester la liste
    public ContactModel(String pseudo) {
        this.pseudo=pseudo;
    }

    public int getId() {
        return id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public Location getCoordonnées() {
        return lastLocalisation;
    }

    public String getLastEvent() {
        return lastEvent;
    }

    public void setLastEvent(String lastEvent) {
        this.lastEvent = lastEvent;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return pseudo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(this.pseudo);
        out.writeDouble(lastLocalisation.getLatitude());
        out.writeDouble(lastLocalisation.getLongitude());
        out.writeString(this.lastEvent);
        out.writeString(this.notes);
    }

    public static final Parcelable.Creator<ContactModel> CREATOR = new Parcelable.Creator<ContactModel>() {
        @Override
        public ContactModel createFromParcel(Parcel source) {
            return new ContactModel(source);
        }

        @Override
        public ContactModel[] newArray(int size) {
            return new ContactModel[size];
        }
    };

    private ContactModel(Parcel in) {
        this.id = in.readInt();
        this.pseudo = in.readString();
        this.lastLocalisation = in.readParcelable(Location.class.getClassLoader());
        this.lastEvent = in.readString();
        this.notes = in.readString();
        double latitude = in.readDouble();
        double longitude = in.readDouble();
        lastLocalisation = new Location(latitude, longitude);
    }

}
