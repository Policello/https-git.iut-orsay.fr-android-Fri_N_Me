package fr.iut_orsay.frinme.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import fr.iut_orsay.frinme.model.EventJoModel;

@Dao
public interface EventJoDao {

    @Query("SELECT * FROM eventJo")
    List<EventJoModel> getAll();

    @Query("SELECT * FROM eventJo where nom LIKE  :nomEvenement")
    EventJoModel findByName(String nomEvenement);

    @Query("SELECT COUNT(*) from eventJo")
    int countEvents();

    @Query("DELETE FROM eventJo")
    void deleteAll();

    @Insert
    void insertAll(List<EventJoModel> events);

    @Delete
    void delete(EventJoModel event);
}
