package fr.iut_orsay.frinme.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.codecrafters.tableview.listeners.TableDataClickListener;
import fr.iut_orsay.frinme.R;
import fr.iut_orsay.frinme.model.ContactModel;
import fr.iut_orsay.frinme.model.EventModel;
import fr.iut_orsay.frinme.model.Location;

/**
 * Vue des détails de contacts
 * utilisateur quelconque
 */
public class Contact extends Fragment {

    private ListView mListView;
    List<EventModel> testEvent;
    List<String> listEvenements= new ArrayList<>();
    private ContactModel contactRecu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Jeu de test d'événements
        testEvent = new ArrayList<>();
        EventModel e1 = new EventModel("test", "cat", new Date(), "", new Location(2.36,5.69));
        EventModel e2 = new EventModel("wew", "chat", new Date(), "description très longue" ,new Location(8.36,5.6777));
        EventModel e3 = new EventModel("aaaaa", "zzzzzzzz", new Date(), "", new Location(2.36,5.69));
        testEvent.add(e1);
        testEvent.add(e2);
        testEvent.add(e3);

        contactRecu = new ContactModel(1,"Carlos","Juan","0102060405",new Location(14.7,15.7),e1.getNom(),"Notes test",testEvent);

        for (int i = 0; i < testEvent.size(); i++) {
            listEvenements.add(testEvent.get(i).getNom());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mListView = (ListView) view.findViewById(R.id.ListeEvenementsCommunDetails);

        final TextView PrenomContact = (TextView) view.findViewById(R.id.PrenomContact);
        final TextView nomContact = (TextView) view.findViewById(R.id.NomContact);
        final TextView NumContactDetails = (TextView) view.findViewById(R.id.NumContactDetails);
        final TextView LocalisationContactsDetails = (TextView) view.findViewById(R.id.LocalisationContactsDetails);
        final TextView LastEvenementsContactsDetails = (TextView) view.findViewById(R.id.LastEvenementsContactsDetails);
        final TextView NotesContactsDetails = (TextView) view.findViewById(R.id.NotesContactsDetails);

        PrenomContact.setText(contactRecu.getPrenom());
        nomContact.setText(contactRecu.getNom());
        NumContactDetails.setText(contactRecu.getNumeroTel());
        LocalisationContactsDetails.setText(contactRecu.getCoordonnées().toString());
        LastEvenementsContactsDetails.setText(contactRecu.getLastEvent());
        NotesContactsDetails.setText(contactRecu.getNotes());

        ImageView img = (ImageView) view.findViewById(R.id.ImageProfil);
        img.setImageResource(R.drawable.ic_menu_camera);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,listEvenements );
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener((a, v, position, id) -> getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new Event())
                .addToBackStack(null)
                .commit());

    }
}
