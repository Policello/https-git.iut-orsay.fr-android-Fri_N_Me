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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Représente un événement des Jo et tous ses attributs
 */

@Entity(tableName = "eventJo")
public class EventJoModel implements Parcelable, AbstractEvent {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("NumEvenement")
    @Expose
    private int id;

    @ColumnInfo(name = "nom")
    @SerializedName("NomEvenement")
    @Expose
    private String nom;

    @ColumnInfo(name = "type")
    @SerializedName("NomTypeEvenemen")
    @Expose
    private String type;

    @ColumnInfo(name = "date")
    @TypeConverters(Converters.DateConverter.class)
    @SerializedName("DateEvenement")
    @Expose
    private Date date;

    private String desc;

    @ColumnInfo(name = "localisation")
    @TypeConverters(Converters.LocationConverter.class)
    @SerializedName("Localisation")
    @Expose
    private Location coordonnées;

    @Ignore
    private List<ContactModel> participants;

    public EventJoModel(int id) {
        this.id = id;
        //TODO: Récupérer les infos manquantes depuis le serveur
    }

    public EventJoModel(String nom, String type, Date date, String desc, Location coord) {
        this.nom = nom;
        this.type = type;
        this.date = date;
        this.desc = desc;
        this.coordonnées = coord;
        this.participants = new ArrayList<>();
    }

    public EventJoModel(String nom, String type, Date date, String desc, Location coord, ArrayList<ContactModel> participants) {
        this.nom = nom;
        this.type = type;
        this.date = date;
        this.desc = desc;
        this.coordonnées = coord;
        this.participants = new ArrayList<>(participants);
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    public String getDesc() {
        return desc;
    }

    public Location getCoordonnées() {
        return coordonnées;
    }

    public List<ContactModel> getParticipants() {
        return participants;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setCoordonnées(Location coordonnées) {
        this.coordonnées = coordonnées;
    }

    public boolean addParticipant(ContactModel c) {
        return this.participants.add(c);
    }

    public boolean delParticipant(ContactModel c) {
        return this.participants.remove(c);
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
        out.writeInt(id);
        out.writeString(nom);
        out.writeString(type);
        out.writeLong(date != null ? date.getTime() : -1);
        out.writeString(desc);
        out.writeDouble(coordonnées.getLatitude());
        out.writeDouble(coordonnées.getLongitude());
        out.writeTypedList(participants);
    }

    public static final Parcelable.Creator<EventJoModel> CREATOR
            = new Parcelable.Creator<EventJoModel>() {
        public EventJoModel createFromParcel(Parcel in) {
            return new EventJoModel(in);
        }

        public EventJoModel[] newArray(int size) {
            return new EventJoModel[size];
        }
    };

    private EventJoModel(Parcel in) {
        id = in.readInt();
        nom = in.readString();
        type = in.readString();
        long tmpDate = in.readLong();
        date = tmpDate == -1 ? null : new Date(tmpDate);
        double latitude = in.readDouble();
        double longitude = in.readDouble();
        desc = in.readString();
        coordonnées = new Location(latitude, longitude);
        participants = new ArrayList<>();
        in.readTypedList(participants, ContactModel.CREATOR);
    }
}
