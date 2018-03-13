package fr.iut_orsay.frinme.view;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import fr.iut_orsay.frinme.R;
import fr.iut_orsay.frinme.model.ContactModel;
import fr.iut_orsay.frinme.model.EventComparator;
import fr.iut_orsay.frinme.model.EventModel;


/**
 * Created by yyang5 on 13/03/2018.
 */

public class ListeContact extends Fragment {

    List<ContactModel> testContact;
    private static final String[] TABLE_HEADERS = { "Nom", "prenom" };

    private String[] prenoms = new String[]{
            "Antoine", "Benoit", "Cyril", "David", "Eloise", "Florent",
            "Gerard", "Hugo", "Ingrid", "Jonathan", "Kevin", "Logan",
            "Mathieu", "Noemie", "Olivia", "Philippe", "Quentin", "Romain",
            "Sophie", "Tristan", "Ulric", "Vincent", "Willy", "Xavier",
            "Yann", "Zoé"
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testContact = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.liste_contact, container, false);

        return view;
    }


    private class EventTableAdaptater extends TableDataAdapter<ContactModel> {

        EventTableAdaptater(Context context, List<ContactModel> data) {
            super(context, data);
        }

        @Override
        public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
            ContactModel evt = getRowData(rowIndex);
            View renderedView = null;

            return renderedView;
        }

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        SortableTableView tableView = (SortableTableView ) view.findViewById(R.id.ListeContact);
        tableView.setDataAdapter(new ListeContact.EventTableAdaptater(getActivity(), testContact));
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(getActivity(), TABLE_HEADERS));
        tableView.addDataClickListener(new ListeContact.EventClickListener());
        tableView.setColumnComparator(0, EventComparator.getEventNameComparator());
        tableView.setColumnComparator(1, EventComparator.getEventTypeComparator());
    }

    private class EventClickListener implements TableDataClickListener<EventModel> {
        @Override
        public void onDataClicked(int rowIndex, EventModel event) {
            getActivity().getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new Contact())
                    .addToBackStack(null)
                    .commit();
        }
    }
}
