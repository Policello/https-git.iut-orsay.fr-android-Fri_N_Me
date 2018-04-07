package fr.iut_orsay.frinme.view;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import fr.iut_orsay.frinme.R;
import fr.iut_orsay.frinme.rest.RestUser;
import fr.iut_orsay.frinme.rest.pojo.Categories;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventAdd extends Fragment {

    private EditText date_picker;
    private EditText time_picker;
    private int mYear, mMonth, mDay;
    private int mHour, mMin;
    private List<String> categories;


    public EventAdd() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_event_add, container, false);

        categories = new ArrayList<>();

        getCategories();

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

        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        mHour = ca.get(Calendar.HOUR);
        mMin = ca.get(Calendar.MINUTE);

        return v;
    }

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
}
