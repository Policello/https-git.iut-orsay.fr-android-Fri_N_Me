package fr.iut_orsay.frinme.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import fr.iut_orsay.frinme.model.EventModel;

/**
 * Modèle événement de la BD locale
 */
@Dao
public interface EventDao {

    @Query("SELECT * FROM event")
    List<EventModel> getAll();

    @Query("SELECT * FROM event where nom LIKE :nomEvenement")
    EventModel findByName(String nomEvenement);

    @Query("SELECT COUNT(*) from event")
    int countEvents();

    @Query("DELETE FROM event")
    void deleteAll();

    @Insert
    void insertAll(List<EventModel> events);

    @Delete
    void delete(EventModel event);
}
