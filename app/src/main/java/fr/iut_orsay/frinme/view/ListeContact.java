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
import fr.iut_orsay.frinme.model.ContactComparator;
import fr.iut_orsay.frinme.model.ContactModel;
import fr.iut_orsay.frinme.model.EventComparator;
import fr.iut_orsay.frinme.model.EventModel;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import fr.iut_orsay.frinme.R;
import fr.iut_orsay.frinme.model.ContactModel;
import fr.iut_orsay.frinme.model.EventModel;
import fr.iut_orsay.frinme.model.Location;

/**
 * Created by yyang5 on 13/03/2018.
 */

public class ListeContact extends Fragment {

    List<ContactModel> testContact;
    private static final String[] TABLE_HEADERS = { "Nom", "prenom" };


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testContact = new ArrayList<>();
        //ContactModel c1 = new ContactModel("nom1","prenom1");
        ContactModel c2 = new ContactModel(1,"Carlos","Juan","0102060405",new Location(14.7,15.7),"Test","Notes test");
        //ContactModel c3 = new ContactModel("mon3","erpnom3");
        //testContact.add(c1);
        testContact.add(c2);
        //testContact.add(c3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(isHidden()){
            getActivity().getFragmentManager().beginTransaction()
                    .show(ListeContact.this)
                    .commit();
        }

        View view = inflater.inflate(R.layout.liste_contact, container, false);
        return view;
    }

    private class ContactTableAdaptater extends TableDataAdapter<ContactModel> {

        ContactTableAdaptater(Context context, List<ContactModel> data) {
            super(context, data);
        }

        @Override
        public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
            ContactModel contact = getRowData(rowIndex);
            View renderedView = null;

            switch (columnIndex) {
                case 0:
                    TextView tvName = new TextView(getContext());
                    tvName.setText(contact.getNom());
                    tvName.setGravity(Gravity.CENTER);
                    renderedView = tvName;
                    break;
                case 1:
                    TextView tvType = new TextView(getContext());
                    tvType.setText(contact.getPrenom());
                    tvType.setGravity(Gravity.CENTER);
                    renderedView = tvType;
                    break;
            }

            return renderedView;
        }

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        SortableTableView tableView = (SortableTableView ) view.findViewById(R.id.ListeContact);
        tableView.setDataAdapter(new ListeContact.ContactTableAdaptater(getActivity(), testContact));
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(getActivity(), TABLE_HEADERS));
        tableView.addDataClickListener(new ListeContact.EventClickListener());
        tableView.setColumnComparator(0, ContactComparator.getContactNomComparator());
        tableView.setColumnComparator(1, ContactComparator.getContactPrenomComparator());
    }

    private class EventClickListener implements TableDataClickListener<ContactModel> {
        @Override
        public void onDataClicked(int rowIndex, ContactModel clickedData) {
            Bundle args = new Bundle();
            args.putParcelable("Contact", testContact.get(rowIndex));
            Contact EventContact = new Contact();
            EventContact.setArguments(args);
            getActivity().getFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out,
                            android.R.animator.fade_in, android.R.animator.fade_out)
                    .add(R.id.fragment_container, EventContact)
                    .hide(ListeContact.this)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
