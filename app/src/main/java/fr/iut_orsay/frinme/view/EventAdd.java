package fr.iut_orsay.frinme.view;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import fr.iut_orsay.frinme.R;
import fr.iut_orsay.frinme.model.DataBase;
import fr.iut_orsay.frinme.model.SessionManagerPreferences;
import fr.iut_orsay.frinme.rest.RestUser;
import fr.iut_orsay.frinme.rest.pojo.Categories;
import fr.iut_orsay.frinme.rest.pojo.Message;
import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Fragment permettant d'ajouter un événement
 */
public class EventAdd extends Fragment {

    private EditText date_picker;
    private EditText time_picker;
    private EditText location_picker;
    private Button validEvent;
    private int mYear, mMonth, mDay;
    private int mHour, mMin;
    private LatLng location;
    private List<String> categories;


    public EventAdd() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_event_add, container, false);

        categories = new ArrayList<>();

        getCategories();

        List<Integer> nbChoices = new ArrayList<>();
        for (int i = 1; i < 100; ++i) {
            nbChoices.add(i);
        }

        Spinner nbPersPicker = v.findViewById(R.id.nbPersPicker);
        final ArrayAdapter<Integer> aa = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, nbChoices);
        nbPersPicker.setAdapter(aa);
        nbPersPicker.setSelection(0); // éviter null pointer

        validEvent = v.findViewById(R.id.validEvent);
        validEvent.setEnabled(false);
        validEvent.setOnClickListener(view -> addEvent());

        date_picker = v.findViewById(R.id.datePicker);
        date_picker.setOnClickListener(view -> {
            DatePickerDialog datePicker = new DatePickerDialog(getActivity(), mdateListener, mYear, mMonth, mDay);
            datePicker.show();
        });

        time_picker = v.findViewById(R.id.timePicker);
        time_picker.setOnClickListener(view -> {
            TimePickerDialog timePicker = new TimePickerDialog(getActivity(), mtimeListener, mHour, mMin, true);
            timePicker.show();
        });

        location_picker = v.findViewById(R.id.locationPicker);
        location_picker.setOnClickListener(view -> {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            try {
                startActivityForResult(builder.build(getActivity()), 2);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        });

        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        mHour = ca.get(Calendar.HOUR);
        mMin = ca.get(Calendar.MINUTE);

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Place selectedPlace = PlacePicker.getPlace(data, getActivity());
                location_picker.setText(selectedPlace.getAddress());
                location = selectedPlace.getLatLng();
            }
        }
    }

    /**
     * Affiche le résultat des dialogs de date
     */
    public void display() {
        date_picker.setText(new StringBuffer().append(mMonth + 1).append("-").append(mDay).append("-").append(mYear).append(" "));
        time_picker.setText(new StringBuffer().append(mHour).append(":").append(mMin));
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            display();
        }
    };

    private TimePickerDialog.OnTimeSetListener mtimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            mHour = hour;
            mMin = minute;
            display();
        }
    };

    /**
     * Récupère les catégories depuis le serveur
     */
    private void getCategories() {
        Call<Categories> call = RestUser.get().getTypeActivities();
        call.enqueue(new retrofit2.Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                final Categories r = response.body();
                if (r != null && response.isSuccessful()) {
                    categories.addAll(r.getMessage());
                    Spinner catPicker = (Objects.requireNonNull(getView()).findViewById(R.id.catPicker));
                    final ArrayAdapter<String> aa = new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_list_item_1, categories);
                    catPicker.setAdapter(aa);
                    catPicker.setSelection(0); // éviter null pointer
                    validEvent.setEnabled(true);
                } else {
                    Log.e("REST CALL", "sendRequest not successful");
                }
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                Log.e("REST CALL", t.getMessage());
            }
        });
    }

    /**
     * Ajout un événement dans la base de données
     */
    private void addEvent() {
        String nom = ((EditText) Objects.requireNonNull(getView()).findViewById(R.id.eventName)).getText().toString();
        String category = ((Spinner) getView().findViewById(R.id.catPicker)).getSelectedItem().toString();
        int nbPers = (Integer) ((Spinner) getView().findViewById(R.id.nbPersPicker)).getSelectedItem();
        String commentaire = ((EditText) getView().findViewById(R.id.des)).getText().toString();

        if (nom.matches("\\s*") || category.matches("\\s*") || location == null) {
            Toasty.warning(getActivity(), "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show();
        } else {
            Call<Message> call = RestUser.get().addEvent(nbPers,
                    DateFormat.getTimeInstance().format(Calendar.getInstance().getTime()),
                    DateFormat.getDateInstance().format(Calendar.getInstance().getTime()),
                    SessionManagerPreferences.getSettings(getActivity()).getUsrId(),
                    location.latitude, location.longitude, commentaire, category, nom);
            call.enqueue(new retrofit2.Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    final Message r = response.body();
                    if (r != null && response.isSuccessful()) {
                        Toasty.success(getActivity(), r.getMessage(), Toast.LENGTH_LONG).show();
                        DataBase.fetchEvents(getActivity());
                        getFragmentManager().popBackStack();
                    } else {
                        Log.e("REST CALL", "sendRequest not successful");
                    }
                }

                @Override
                public void onFailure(Call<Message> call, Throwable t) {
                    Log.e("REST CALL", t.getMessage());
                }
            });
        }
    }
}
