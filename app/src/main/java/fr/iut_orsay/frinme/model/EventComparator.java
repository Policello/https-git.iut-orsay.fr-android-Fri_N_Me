package fr.iut_orsay.frinme.model;

import java.util.Comparator;

/**
 * Classe permettant de comparer deux événement
 */
public class EventComparator {

    public static Comparator<AbstractEvent> getEventNameComparator() {
        return new EventNameComparator();
    }

    public static Comparator<AbstractEvent> getEventTypeComparator() {
        return new EventTypeComparator();
    }

    public static Comparator<AbstractEvent> getEventDateComparator() {
        return new EventDateComparator();
    }

    public static Comparator<AbstractEvent> getEventLocComparator() {
        return new EventLocComparator();
    }

    private static class EventNameComparator implements Comparator<AbstractEvent> {

        @Override
        public int compare(final AbstractEvent event1, final AbstractEvent event2) {
            return event1.getNom().compareTo(event2.getNom());
        }
    }

    private static class EventTypeComparator implements Comparator<AbstractEvent> {

        @Override
        public int compare(final AbstractEvent event1, final AbstractEvent event2) {
            return event1.getType().compareTo(event2.getType());
        }
    }

    private static class EventDateComparator implements Comparator<AbstractEvent> {

        @Override
        public int compare(final AbstractEvent event1, final AbstractEvent event2) {
            return event1.getDate().compareTo(event2.getDate());
        }
    }

    private static class EventLocComparator implements Comparator<AbstractEvent> {

        @Override
        public int compare(final AbstractEvent event1, final AbstractEvent event2) {
            return event1.getCoordonnées().compareTo(event2.getCoordonnées());
        }
    }
}
