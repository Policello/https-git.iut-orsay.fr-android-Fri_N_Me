package fr.iut_orsay.frinme.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import fr.iut_orsay.frinme.model.ContactModel;

/**
 * Modèle contact de la BD locale
 */
@Dao
public interface ContactDao {

    @Query("SELECT * FROM contact")
    List<ContactModel> getAll();

    @Query("SELECT * FROM contact where pseudo LIKE :name")
    ContactModel findByName(String name);

    @Query("SELECT COUNT(*) from contact")
    int countContacts();

    @Query("DELETE FROM contact")
    void deleteAll();

    @Insert
    void insertAll(List<ContactModel> contact);

    @Delete
    void delete(ContactModel contact);
}
