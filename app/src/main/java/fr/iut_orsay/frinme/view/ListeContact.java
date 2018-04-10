package fr.iut_orsay.frinme.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
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
import fr.iut_orsay.frinme.model.SessionManagerPreferences;
import fr.iut_orsay.frinme.rest.RestUser;
import fr.iut_orsay.frinme.rest.pojo.ContactListDetails;
import fr.iut_orsay.frinme.rest.pojo.RechercheDynamique;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yyang5 on 13/03/2018.
 */

public class ListeContact extends Fragment {

    List<ContactModel> testContact, tempTestContact;
    private static final String[] TABLE_HEADERS = {"Pseudo"};
    SearchView sv;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testContact = new ArrayList<>();
        tempTestContact = new ArrayList<>();
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

    /**
     * Creer une table adapté qui sera triable
     */
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
        sv = (SearchView) view.findViewById(R.id.SearchListeContact);
        sv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sv.setIconified(false);
            }
        });
        SortableTableView tableView = (SortableTableView) view.findViewById(R.id.ListeContact);
        tableView.setDataAdapter(new ListeContact.ContactTableAdaptater(getActivity(), testContact));
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(getActivity(), TABLE_HEADERS));
        tableView.addDataClickListener(new ListeContact.EventClickListener());
        tableView.setColumnComparator(0, ContactComparator.getContactPseudoComparator());

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //Inutile

            @Override
            public boolean onQueryTextSubmit(String s) {
                //Toast.makeText(getActivity(), "OnQueryTextSubmit", Toast.LENGTH_LONG).show();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //Toast.makeText(getActivity(), "OnQueryTextChange", Toast.LENGTH_LONG).show();
                if (!TextUtils.isEmpty(s)) {
                    Call<RechercheDynamique> call = RestUser.get().getRechercheDynamique(s);
                    call.enqueue(new Callback<RechercheDynamique>() {
                        @Override
                        public void onResponse(Call<RechercheDynamique> call, Response<RechercheDynamique> response) {
                            if (response.isSuccessful()) {
                                final RechercheDynamique r = response.body();
                                if (tempTestContact.size() == 0)
                                    tempTestContact.addAll(testContact);
                                testContact.clear();
                                testContact.addAll(r.getMessage());
                                SortableTableView tableView = (SortableTableView) view.findViewById(R.id.ListeContact);
                                tableView.setDataAdapter(new ListeContact.ContactTableAdaptater(getActivity(), testContact));
                                Log.e("REST CALL", testContact.toString());
                            } else {
                                //
                                Log.e("REST CALL", "sendRequest not successful listeContact");
                            }

                        }

                        @Override
                        public void onFailure(Call<RechercheDynamique> call, Throwable t) {
                            Log.e("REST CALL", t.getMessage());
                        }

                    });
                } else {
                    testContact.clear();
                    testContact.addAll(DataBase.getAppDatabase(getActivity()).contactDao().getAll());
                }
                return false;
            }
        });
    }

    /**
     * Recupére un contact que l'on à cliqué et le redirigera sur
     * sa fiche detaillé donc la classe Contact
     */
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

    /**
     * Recupére la liste contact de l'utilsateur et crée une liste
     * triable avec le nom de ces différents contact
     *
     * @param v
     */
    private void sendRequest(View v) {
        Call<ContactListDetails> call = RestUser.get().getContactDetailedList(SessionManagerPreferences.getSettings(getActivity()).getUsrId());
        call.enqueue(new Callback<ContactListDetails>() {
            @Override
            public void onResponse(Call<ContactListDetails> call, Response<ContactListDetails> response) {
                if (response.isSuccessful()) {
                    final ContactListDetails r = response.body();
                    //Toast.makeText(getActivity(), r.getMessage(), Toast.LENGTH_LONG).show();
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
        Call<ContactListDetails> call = RestUser.get().getContactDetailedList(SessionManagerPreferences.getSettings(getActivity()).getUsrId());
        call.enqueue(new Callback<ContactListDetails>() {
            @Override
            public void onResponse(Call<ContactListDetails> call, Response<ContactListDetails> response) {
                if (response.isSuccessful()) {
                    final ContactListDetails r = response.body();
                    //Toast.makeText(getActivity(), r.getMessage(), Toast.LENGTH_LONG).show();
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
