package fr.iut_orsay.frinme.model;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Différents convertisseurs de types
 */
public class Converters {

    public static class DateConverter {

        @TypeConverter
        public static Date toDate(Long timestamp) {
            return timestamp == null ? null : new Date(timestamp);
        }

        @TypeConverter
        public static Long toTimestamp(Date date) {
            return date == null ? null : date.getTime();
        }
    }

    public static class LocationConverter {

        @TypeConverter
        public static Location toLocation(String loc) {
            return new Location(Double.parseDouble(loc.split(" ")[0]),
                    Double.parseDouble(loc.split(" ")[1]));
        }

        @TypeConverter
        public static String toString(Location loc) {
            return loc.getLatitude() + " " + loc.getLongitude();
        }
    }
}
