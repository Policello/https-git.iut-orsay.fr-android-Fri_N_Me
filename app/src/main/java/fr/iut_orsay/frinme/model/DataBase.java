package fr.iut_orsay.frinme.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import fr.iut_orsay.frinme.dao.ContactDao;
import fr.iut_orsay.frinme.dao.EventDao;
import fr.iut_orsay.frinme.rest.RestUser;
import fr.iut_orsay.frinme.rest.pojo.ContactListDetails;
import fr.iut_orsay.frinme.rest.pojo.EventListDetails;
import retrofit2.Call;
import retrofit2.Response;

@Database(entities = {EventModel.class, ContactModel.class} , version = 1, exportSchema = false)
public abstract class DataBase extends RoomDatabase {

    private static DataBase INSTANCE;

    public abstract EventDao eventDao();
    public abstract ContactDao contactDao();

    public static DataBase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), DataBase.class, "database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public static void fetchEvents(Context c) {
        Call<EventListDetails> call = RestUser.get().getEventDetailedList();
        call.enqueue(new retrofit2.Callback<EventListDetails>() {
            @Override
            public void onResponse(Call<EventListDetails> call, Response<EventListDetails> response) {
                final EventListDetails r = response.body();
                if (r != null && response.isSuccessful()) {
                    if (r.getEvents().size() != DataBase.getAppDatabase(c).eventDao().countEvents()) {
                        DataBase.getAppDatabase(c).eventDao().deleteAll();
                        DataBase.getAppDatabase(c).eventDao().insertAll(r.getEvents());
                        DataBase.getAppDatabase(c).eventDao().insertAll(r.getEventsJo());
                    }
                } else {
                    Log.e("REST CALL", "sendRequest not successful");
                }
            }

            @Override
            public void onFailure(Call<EventListDetails> call, Throwable t) {
                Log.e("REST CALL", t.getMessage());
            }
        });
    }

    public static void fetchContacts(Context c, int userId) {
        Call<ContactListDetails> call = RestUser.get().getContactDetailedList(userId);
        call.enqueue(new retrofit2.Callback<ContactListDetails>() {
            @Override
            public void onResponse(Call<ContactListDetails> call, Response<ContactListDetails> response) {
                final ContactListDetails r = response.body();
                if (r != null && response.isSuccessful()) {
                    if (r.getContacts().size() != DataBase.getAppDatabase(c).contactDao().countContacts()){
                        DataBase.getAppDatabase(c).contactDao().deleteAll();
                        DataBase.getAppDatabase(c).contactDao().insertAll(r.getContacts());
                    }
                } else {
                    Log.e("REST CALL", "sendRequest not successful");
                }
            }

            @Override
            public void onFailure(Call<ContactListDetails> call, Throwable t) {
                Log.e("REST CALL", t.getMessage());
            }
        });
    }
}