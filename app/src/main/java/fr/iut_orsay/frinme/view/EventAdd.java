package fr.iut_orsay.frinme.view;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

import fr.iut_orsay.frinme.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventAdd extends Fragment {

    private EditText date_picker;
    private EditText time_picker;
    private int mYear, mMonth, mDay;
    private int mHour, mMin;


    public EventAdd() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_event_add, container, false);

        date_picker = v.findViewById(R.id.datePicker);
        date_picker.setOnClickListener(view -> {
            DatePickerDialog datePicker = new  DatePickerDialog(getActivity(), mdateListener, mYear, mMonth, mDay);
            datePicker.show();
        });

        time_picker = v.findViewById(R.id.timePicker);
        time_picker.setOnClickListener(view -> {
            TimePickerDialog timePicker = new  TimePickerDialog(getActivity(), mtimeListener, mHour, mMin, true);
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
}
