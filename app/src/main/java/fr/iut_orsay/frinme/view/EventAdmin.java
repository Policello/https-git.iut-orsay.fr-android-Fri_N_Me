package fr.iut_orsay.frinme.view;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import fr.iut_orsay.frinme.MainActivity;
import fr.iut_orsay.frinme.R;
import fr.iut_orsay.frinme.view.dialog.JoinFrag;
import fr.iut_orsay.frinme.view.dialog.QuitFrag;

/**
 * Vue des détails de l'événement
 * créateur de l'événement
 */
public class EventAdmin extends Fragment {

    private ListView mListView;

    private String[] prenoms = new String[]{
            "Antoine", "Benoit", "Cyril", "David", "Eloise", "Florent",
            "Gerard", "Hugo", "Ingrid", "Jonathan", "Kevin", "Logan",
            "Mathieu", "Noemie", "Olivia", "Philippe", "Quentin", "Romain",
            "Sophie", "Tristan", "Ulric", "Vincent", "Willy", "Xavier",
            "Yann", "Zoé"
    };


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_admin, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mListView = (ListView) view.findViewById(R.id.listView);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, prenoms);
        mListView.setAdapter(adapter);

        Spinner spinner = (Spinner) view.findViewById(R.id.cat);
        ArrayAdapter<CharSequence> adapterS = ArrayAdapter.createFromResource(getActivity(),
                R.array.cat, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterS);

        ImageView img = (ImageView) view.findViewById(R.id.imageView);
        img.setOnClickListener(diag -> {
            DialogFragment dialogFragment;
            if (MainActivity.userStatus == MainActivity.Status.EXTERNE) {
                dialogFragment = new JoinFrag();
            } else {
                dialogFragment = new QuitFrag();
            }
            dialogFragment.show(getFragmentManager(), "Popup");
        });
    }
}
