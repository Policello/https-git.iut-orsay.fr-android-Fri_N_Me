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

    @SerializedName("NumUtilisateur")
    @Expose
    private int id;

    @SerializedName("Pseudo")
    @Expose
    private String nom;

    private String prenom;
    private String numeroTel;
    private Location lastLocalisation;
    private String lastEvent;
    private String notes;
    private List<EventModel> listEvenement;

    public ContactModel(int id) {
        this.id = id;
        //TODO: Récupérer les infos manquantes depuis le serveur
    }

    public String getNumeroTel() {
        return numeroTel;
    }

    public void setNumeroTel(String numeroTel) {
        this.numeroTel = numeroTel;
    }

    public ContactModel(int id, String nom, String prenom, String numeroTel, Location lastLocalisation, String lastEvent, String notes, List<EventModel> listEvenement) {
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
        this.numeroTel = numeroTel;
        this.lastLocalisation = lastLocalisation;
        this.lastEvent = lastEvent;
        this.notes = notes;
        this.listEvenement = new ArrayList<>();
        this.listEvenement.addAll(listEvenement);
        //TODO: Récupérer les infos manquantes depuis le serveur
    }

    public ContactModel(int id, String nom, String prenom, String numeroTel, Location lastLocalisation, String lastEvent, String notes) {
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
        this.numeroTel = numeroTel;
        this.lastLocalisation = lastLocalisation;
        this.lastEvent = lastEvent;
        this.notes = notes;
    }

    // Pour tester la liste
    public ContactModel(String nom, String prenom) {
        this.prenom = prenom;
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
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
        return nom;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(this.nom);
        out.writeString(this.prenom);
        out.writeString(this.numeroTel);
        out.writeDouble(lastLocalisation.getLatitude());
        out.writeDouble(lastLocalisation.getLongitude());
        out.writeString(this.lastEvent);
        out.writeString(this.notes);
        out.writeTypedList(this.listEvenement);
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
        this.nom = in.readString();
        this.prenom = in.readString();
        this.numeroTel = in.readString();
        this.lastLocalisation = in.readParcelable(Location.class.getClassLoader());
        this.lastEvent = in.readString();
        this.notes = in.readString();
        double latitude = in.readDouble();
        double longitude = in.readDouble();
        lastLocalisation = new Location(latitude, longitude);
        listEvenement = new ArrayList<>();
        in.readTypedList(listEvenement, EventModel.CREATOR);
    }

}
