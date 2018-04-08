package fr.iut_orsay.frinme.view;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import fr.iut_orsay.frinme.R;
import fr.iut_orsay.frinme.model.ContactComparator;
import fr.iut_orsay.frinme.model.ContactModel;
import fr.iut_orsay.frinme.model.DataBase;

/**
 * Created by yyang5 on 13/03/2018.
 */

public class ListeContact extends Fragment {

    List<ContactModel> testContact;
    private static final String[] TABLE_HEADERS = {"Pseudo"};


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testContact = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (isHidden()) {
            getActivity().getSupportFragmentManager().beginTransaction()
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
                    tvName.setText(contact.getPseudo());
                    tvName.setGravity(Gravity.CENTER);
                    renderedView = tvName;
                    break;
            }
            return renderedView;
        }

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        testContact.clear();
        testContact.addAll(DataBase.getAppDatabase(getActivity()).contactDao().getAll());
        SortableTableView tableView = (SortableTableView) view.findViewById(R.id.ListeContact);
        tableView.setDataAdapter(new ListeContact.ContactTableAdaptater(getActivity(), testContact));
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(getActivity(), TABLE_HEADERS));
        tableView.addDataClickListener(new ListeContact.EventClickListener());
        tableView.setColumnComparator(0, ContactComparator.getContactPseudoComparator());
    }

    private class EventClickListener implements TableDataClickListener<ContactModel> {
        @Override
        public void onDataClicked(int rowIndex, ContactModel clickedData) {
            Bundle args = new Bundle();
            args.putParcelable("Contact", testContact.get(rowIndex));
            Contact EventContact = new Contact();
            EventContact.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out,
                            android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.fragment_container, EventContact)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
