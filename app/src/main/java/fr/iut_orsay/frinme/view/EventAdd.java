package fr.iut_orsay.frinme.view;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.iut_orsay.frinme.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventAdd extends Fragment {


    public EventAdd() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_add, container, false);
    }

}
