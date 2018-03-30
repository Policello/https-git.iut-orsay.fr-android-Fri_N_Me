package fr.iut_orsay.frinme.view;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import fr.iut_orsay.frinme.R;
import fr.iut_orsay.frinme.model.ContactComparator;
import fr.iut_orsay.frinme.model.ContactModel;
import fr.iut_orsay.frinme.model.Location;
import fr.iut_orsay.frinme.model.SessionManagerPreferences;
import fr.iut_orsay.frinme.rest.RestUser;
import fr.iut_orsay.frinme.rest.pojo.ContactListDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yyang5 on 13/03/2018.
 */

public class ListeContact extends Fragment {

    List<ContactModel> testContact;
    private static final String[] TABLE_HEADERS = {"Pseudo"};
    SearchView sv;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testContact = new ArrayList<>();
        //ContactModel c1 = new ContactModel("nom1","prenom1");
       // ContactModel c2 = new ContactModel(1, "Salut", new Location(14.7, 15.7), "Test", "Notes test");
        //ContactModel c3 = new ContactModel("mon3","erpnom3");
        //testContact.add(c1);
       // testContact.add(c2);
        //testContact.add(c3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (isHidden()) {
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
        sendRequest(view);
        sv=(SearchView) view.findViewById(R.id.SearchListeContact);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            //Inutile
            @Override
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(getActivity(), "OnQueryTextSubmit", Toast.LENGTH_LONG).show();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Toast.makeText(getActivity(), "OnQueryTextChange", Toast.LENGTH_LONG).show();

                return false;
            }
        });
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
            getActivity().getFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out,
                            android.R.animator.fade_in, android.R.animator.fade_out)
                    .add(R.id.fragment_container, EventContact)
                    .hide(ListeContact.this)
                    .addToBackStack(null)
                    .commit();
        }
    }
//
    private void sendRequest(View v) {
        Call<ContactListDetails> call = RestUser.get().getContactDetailedList(23);
        call.enqueue(new Callback<ContactListDetails>() {
            @Override
            public void onResponse(Call<ContactListDetails> call, Response<ContactListDetails> response) {
                if (response.isSuccessful()) {
                    final ContactListDetails r = response.body();
                    Toast.makeText(getActivity(), r.getMessage(), Toast.LENGTH_LONG).show();
                    testContact.addAll(r.getContacts());
                    SortableTableView tableView = (SortableTableView) v.findViewById(R.id.ListeContact);
                    tableView.setDataAdapter(new ListeContact.ContactTableAdaptater(getActivity(), testContact));
                    Log.e("REST CALL", testContact.toString());
                } else {
                    Log.e("REST CALL", "sendRequest not successful listeContact");
                }
            }

            @Override
            public void onFailure(Call<ContactListDetails> call, Throwable t) {
                Log.e("REST CALL", t.getMessage());
            }
        });
    }
    private void sendRequestDyna(View v) {
        Call<ContactListDetails> call = RestUser.get().getContactDetailedList(23);
        call.enqueue(new Callback<ContactListDetails>() {
            @Override
            public void onResponse(Call<ContactListDetails> call, Response<ContactListDetails> response) {
                if (response.isSuccessful()) {
                    final ContactListDetails r = response.body();
                    Toast.makeText(getActivity(), r.getMessage(), Toast.LENGTH_LONG).show();
                    testContact.addAll(r.getContacts());
                    SortableTableView tableView = (SortableTableView) v.findViewById(R.id.ListeContact);
                    tableView.setDataAdapter(new ListeContact.ContactTableAdaptater(getActivity(), testContact));
                    Log.e("REST CALL", testContact.toString());
                } else {
                    Log.e("REST CALL", "sendRequest not successful listeContact");
                }
            }

            @Override
            public void onFailure(Call<ContactListDetails> call, Throwable t) {
                Log.e("REST CALL", t.getMessage());
            }
        });
    }
}
