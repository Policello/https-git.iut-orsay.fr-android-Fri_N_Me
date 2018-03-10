package fr.iut_orsay.frinme.model;

import android.support.annotation.NonNull;

/**
 * Représente la localisation d'un lieu
 * sous la forme d'un couple de coordonnées
 */
public class Location implements Comparable {

    private final static double RAYON_TERRE = 6367445;
    private double latitude, longitude;

    /**
     * Construit un point dont les coordonnées sont nulles
     */
    public Location(){
        latitude = longitude = 0;
    }

    /**
     * Construit un point dont les coordonnées sont passées en paramètres
     *
     * @param latitude latitude du point
     * @param longitude longitude du point
     */
    public Location(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     *
     * @return la longitude du point
     */
    public double getLongitude(){
        return longitude;
    }

    /**
     *
     * @return la latitude du point
     */
    public double getLatitude(){
        return latitude;
    }

    /**
     * Calcule la distance entre deux coordonnées géographiques
     * R×arccos(sin(a)sin(b)+cos(a)cos(b)cos(c−d))
     *
     * @param l Coordonnée géographique du second point
     * @return la distance entre deux coordonnées géographiques
     */
    public double distanceTo(Location l){
        return RAYON_TERRE * Math.acos(
                Math.sin(Math.toRadians(latitude)) * Math.sin(Math.toRadians(l.getLatitude()))
                + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(l.getLatitude()))
                * Math.cos(Math.toRadians(longitude) - Math.toRadians(l.getLongitude()))
        );
    }

    @Override
    public int compareTo(@NonNull Object o) {
        //TODO: implémenter la distance par rapport à la distance actuelle
        //TODO: pour trier les évenements en fonction de leur proximité
        return 0;
    }

    @Override
    public String toString(){
        return latitude+" ; "+longitude;
    }
}
