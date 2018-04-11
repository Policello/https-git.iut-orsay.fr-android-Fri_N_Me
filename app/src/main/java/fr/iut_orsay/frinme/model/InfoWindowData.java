package fr.iut_orsay.frinme.model;

/**
 * Modèle de la fenêtre d'informations GoogleMaps
 */
public class InfoWindowData {

    private Object o;

    public InfoWindowData(Object o) {
        this.o = o;
    }

    public Object getObject () {
        return this.o;
    }
}
