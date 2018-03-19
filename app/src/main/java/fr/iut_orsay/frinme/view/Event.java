package fr.iut_orsay.frinme.view;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import fr.iut_orsay.frinme.model.ContactModel;
import fr.iut_orsay.frinme.model.EventModel;
import fr.iut_orsay.frinme.rest.EventDetails;
import fr.iut_orsay.frinme.rest.RestUser;
import fr.iut_orsay.frinme.view.dialog.JoinFrag;
import fr.iut_orsay.frinme.MainActivity;
import fr.iut_orsay.frinme.view.dialog.QuitFrag;
import fr.iut_orsay.frinme.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Vue des détails de l'événement
 * utilisateur quelconque
 */
public class Event extends Fragment {

    private ListView mListView;
    private EventModel currentEvent;
    private boolean defaultValues = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);
        if (getArguments() != null) {
            currentEvent = ((EventModel)getArguments().getParcelable("event"));
        } else {
            defaultValues = true;
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mListView = (ListView) view.findViewById(R.id.listView);

        // Remplissage de la liste d'amis
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, new String[]{"Aucun"});
        mListView.setAdapter(adapter);

        // Affichage de l'événement selectionné
        if (!defaultValues) {
            sendRequest(view);

            TextView tvNomc = (TextView) view.findViewById(R.id.nom);
            tvNomc.setText(currentEvent.getNom());

            TextView tvDate = (TextView) view.findViewById(R.id.date);
            tvDate.setText(currentEvent.getDate().toString());

            TextView tvCat = (TextView) view.findViewById(R.id.cat);
            tvCat.setText(currentEvent.getType());

            TextView tvLocation = (TextView) view.findViewById(R.id.location);
            tvLocation.setText(currentEvent.getCoordonnées().toString());
        }

        // Affiche l'aimge associée à l'événement
        // TODO: Récupérer l'image depuis le serveur
        ImageView img = (ImageView) view.findViewById(R.id.imageView);

        // Dialogs permettant de se (de)inscrire aux evts
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

    private void sendRequest(View v) {
        Call<EventDetails> call = RestUser.get().getEventDetails(currentEvent.getId());
        call.enqueue(new Callback<EventDetails>() {
            @Override
            public void onResponse(Call<EventDetails> call, Response<EventDetails> response) {
                if (response.isSuccessful()) {
                    final EventDetails r = response.body();
                    TextView tvDesc = (TextView) v.findViewById(R.id.desc);
                    tvDesc.setText(r.getDesc());
                    if (r.getParticipants().size() != 0){
                        final ArrayAdapter<ContactModel> adapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_list_item_1, r.getParticipants());
                        mListView.setAdapter(adapter);
                    }
                } else {
                    Log.e("REST CALL", "sendRequest not successful");
                }
            }

            @Override
            public void onFailure(Call<EventDetails> call, Throwable t) {
                Log.e("REST CALL", t.getMessage());
            }
        });
    }

}
