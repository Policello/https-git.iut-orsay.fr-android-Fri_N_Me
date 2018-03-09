package fr.iut_orsay.frinme.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import fr.iut_orsay.frinme.R;

/**
 * Vue des détails de contacts
 * utilisateur quelconque
 */
public class Contact extends Fragment {

    private ListView mListView;

    private String[] events = new String[]{
            "course","natation","test","test","test","test","test","test","test","test","test","test","test","test"
    };

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

        PrenomContact.setText("BOB");
        nomContact.setText("léléfan");
        NumContactDetails.setText("0611210544");
        LocalisationContactsDetails.setText("Uganda");
        LastEvenementsContactsDetails.setText("Course");
        NotesContactsDetails.setText("Il é bo");

        ImageView img = (ImageView) view.findViewById(R.id.ImageProfil);
        img.setImageResource(R.drawable.ic_menu_camera);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, events);
        mListView.setAdapter(adapter);

    }

}
