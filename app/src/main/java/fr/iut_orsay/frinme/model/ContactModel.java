package fr.iut_orsay.frinme.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un contact , les évenements auquels il participe et ses attributs.
 */
public class ContactModel {

    private int id;
    private String nom;
    private String prenom;
    private String numeroTel;
    private Location lastLocalisation;
    private String lastEvent;
    private String notes;
    private ArrayList<EventModel> listEvenement= new ArrayList<>();

    public ContactModel(int id){
        this.id = id;
        //TODO: Récupérer les infos manquantes depuis le serveur
    }

    public String getNumeroTel() {
        return numeroTel;
    }

    public void setNumeroTel(String numeroTel) {
        this.numeroTel = numeroTel;
    }

// Pour tester la liste
    public ContactModel(String nom,String prenom){
        this.prenom=prenom;
        this.nom=nom;
    }

    public ContactModel(int id, String nom, String prenom, String numeroTel, Location lastLocalisation, String lastEvent, String notes, List<EventModel> listEvenement){
        this.id = id;
        this.prenom=prenom;
        this.nom=nom;
        this.numeroTel=numeroTel;
        this.lastLocalisation=lastLocalisation;
        this.lastEvent=lastEvent;
        this.notes=notes;

        for (int i = 0; i < listEvenement.size(); i++) {
            this.listEvenement.add(listEvenement.get(i));
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
}
