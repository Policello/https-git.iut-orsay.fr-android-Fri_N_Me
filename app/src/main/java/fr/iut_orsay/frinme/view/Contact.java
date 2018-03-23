package fr.iut_orsay.frinme.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.iut_orsay.frinme.R;
import fr.iut_orsay.frinme.model.ContactModel;
import fr.iut_orsay.frinme.model.EventModel;
import fr.iut_orsay.frinme.rest.RestUser;
import fr.iut_orsay.frinme.rest.pojo.ContactListDetails;
import fr.iut_orsay.frinme.rest.pojo.EstAmi;
import retrofit2.Call;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Vue des détails de contacts
 * utilisateur quelconque
 */
public class Contact extends Fragment {

    private ListView mListView;
    List<EventModel> testEvent;
    List<String> listEvenements = new ArrayList<>();
    private ContactModel contactRecu;
    private boolean defaultValues = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        if (getArguments() != null) {
            contactRecu = ((ContactModel) getArguments().getParcelable("Contact"));
        } else {
            defaultValues = true;
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mListView = (ListView) view.findViewById(R.id.ListeEvenementsCommunDetails);

        final TextView PrenomContact = (TextView) view.findViewById(R.id.PseudoContact);
        final TextView LocalisationContactsDetails = (TextView) view.findViewById(R.id.LocalisationContactsDetails);
        final TextView LastEvenementsContactsDetails = (TextView) view.findViewById(R.id.LastEvenementsContactsDetails);
        final TextView NotesContactsDetails = (TextView) view.findViewById(R.id.NotesContactsDetails);

        if (!defaultValues) {
            PrenomContact.setText(contactRecu.getPseudo());
            LocalisationContactsDetails.setText(contactRecu.getCoordonnées().toString());
            LastEvenementsContactsDetails.setText(contactRecu.getLastEvent());
            NotesContactsDetails.setText(contactRecu.getNotes());
        }

        ImageView img = (ImageView) view.findViewById(R.id.ImageProfil);
        img.setImageResource(R.drawable.ic_menu_camera);

        // final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,listEvenements );
        // mListView.setAdapter(adapter);
        mListView.setOnItemClickListener((a, v, position, id) -> getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new Event())
                .addToBackStack(null)
                .commit());

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.topbar, menu);
        Call<EstAmi> call = RestUser.get().getEstAmi(23,23);


        boolean ami = false;
        if (ami == true) {
            menu.add(0, 200, 0, "Supprimer un ami").setIcon(R.drawable.ic_close_black_24dp)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        } else if (ami == false) {
            menu.add(0, 200, 0, "Ajouter un ami").setIcon(R.drawable.ic_person_add_black_24dp)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }
}
