package fr.iut_orsay.frinme.model;

import java.util.Date;

/**
 * Représente un événement et tous ses attributs
 */
public class EventModel {

    private int id;
    private String nom;
    private String type;
    private Date date;
    private Location coordonnées;

    public EventModel(int id){
        this.id = id;
        //TODO: Récupérer les infos manquantes depuis le serveur
    }

    public EventModel(String nom, String type, Date date, Location coord){
        this.nom = nom;
        this.type = type;
        this.date = date;
        this.coordonnées = coord;
        //TODO: Ajouter les informations au serveur
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

    public Location getCoordonnées() {
        return coordonnées;
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

    public void setCoordonnées(Location coordonnées) {
        this.coordonnées = coordonnées;
    }

    @Override
    public String toString(){
        return nom;
    }

}
