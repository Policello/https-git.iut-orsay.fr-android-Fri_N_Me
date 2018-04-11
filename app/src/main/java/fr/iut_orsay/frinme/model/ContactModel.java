package fr.iut_orsay.frinme.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Représente un contact, les évenements auquels il participe et ses attributs.
 */
@Entity(tableName = "contact")
public class ContactModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @SerializedName("NumUtilisateur")
    @Expose
    private int id;

    @ColumnInfo(name = "pseudo")
    @SerializedName("Pseudo")
    @Expose
    private String pseudo;

    @ColumnInfo(name = "localisation")
    @TypeConverters(Converters.LocationConverter.class)
    @SerializedName("Localisation")
    @Expose
    private Location lastLocalisation;

    @Ignore
    private String lastEvent;

    @Ignore
    private String notes;

    @Ignore
    public ContactModel(int id) {
        this.id = id;
        //TODO: Récupérer les infos manquantes depuis le serveur
    }

    public ContactModel(int id, String pseudo, Location lastLocalisation) {
        this.id = id;
        this.pseudo = pseudo;
        this.lastLocalisation = lastLocalisation;
        this.lastEvent = lastEvent;
        this.notes = notes;
    }


    public ContactModel(int id, String pseudo, Location lastLocalisation, String lastEvent, String notes) {
        this.id = id;
        this.pseudo = pseudo;
        this.lastLocalisation = lastLocalisation;
        this.lastEvent = lastEvent;
        this.notes = notes;
    }

    // Pour tester la liste
    @Ignore
    public ContactModel(String pseudo) {
        this.pseudo = pseudo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Location getLastLocalisation() {
        return lastLocalisation;
    }

    public void setLastLocalisation(Location lastLocalisation) {
        this.lastLocalisation = lastLocalisation;
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
