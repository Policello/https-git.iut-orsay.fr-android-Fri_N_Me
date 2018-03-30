package fr.iut_orsay.frinme.model;

import java.util.Date;
import java.util.List;

public interface  AbstractEvent {

    int getId();

    String getNom();

    String getType();

    Date getDate();

    String getDesc();

    Location getCoordonnées();

    List<ContactModel> getParticipants();

    void setNom(String nom);

    void setType(String type);

    void setDate(Date date);

    void setDesc(String desc);

    void setCoordonnées(Location coordonnées);

    boolean addParticipant(ContactModel c);

    boolean delParticipant(ContactModel c);

}
