package fr.iut_orsay.frinme.model;

import java.util.ArrayList;

/**
 * Représente un contact , les évenements auquels il participe et ses attributs.
 */
public class ContactModel {

    private int id;
    private String nom;
    private String prenom;
    private String lastLocalisation;
    private EventModel lastEvent;
    private String notes;
    private ArrayList<EventModel> listEvenement;

    public ContactModel(int id){
        this.id = id;
        //TODO: Récupérer les infos manquantes depuis le serveur
    }
    public ContactModel(int id,String nom,String prenom,String lastLocalisation,EventModel lastEvent,String notes,ArrayList<EventModel> listEvenement){
        this.id = id;
        this.prenom=prenom;
        this.nom=nom;
        this.lastLocalisation=lastLocalisation;
        this.lastEvent=lastEvent;
        this.notes=notes;
        for (int i = 0; i < listEvenement.size(); i++) {
            this.listEvenement.add(i, listEvenement.get(i));
        }

        //TODO: Récupérer les infos manquantes depuis le serveur
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

    public String getLastLocalisation() {
        return lastLocalisation;
    }

    public void setLastLocalisation(String lastLocalisation) {
        this.lastLocalisation = lastLocalisation;
    }

    public EventModel getLastEvent() {
        return lastEvent;
    }

    public void setLastEvent(EventModel lastEvent) {
        this.lastEvent = lastEvent;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
