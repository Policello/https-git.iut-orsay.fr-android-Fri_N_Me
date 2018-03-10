package fr.iut_orsay.frinme.model;

import java.util.Comparator;

/**
 * Classe permettant de comparer deux événement
 */
public class EventComparator {

    public static Comparator<EventModel> getEventNameComparator() {
        return new EventNameComparator();
    }

    public static Comparator<EventModel> getEventTypeComparator() {
        return new EventTypeComparator();
    }

    public static Comparator<EventModel> getEventDateComparator() {
        return new EventDateComparator();
    }

    public static Comparator<EventModel> getEventLocComparator() {
        return new EventLocComparator();
    }

    private static class EventNameComparator implements Comparator<EventModel> {

        @Override
        public int compare(final EventModel event1, final EventModel event2) {
            return event1.getNom().compareTo(event2.getNom());
        }
    }

    private static class EventTypeComparator implements Comparator<EventModel> {

        @Override
        public int compare(final EventModel event1, final EventModel event2) {
            return event1.getType().compareTo(event2.getType());
        }
    }

    private static class EventDateComparator implements Comparator<EventModel> {

        @Override
        public int compare(final EventModel event1, final EventModel event2) {
            return event1.getDate().compareTo(event2.getDate());
        }
    }

    private static class EventLocComparator implements Comparator<EventModel> {

        @Override
        public int compare(final EventModel event1, final EventModel event2) {
            return event1.getCoordonnées().compareTo(event2.getCoordonnées());
        }
    }
}
