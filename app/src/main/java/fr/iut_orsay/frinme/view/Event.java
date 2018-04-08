package fr.iut_orsay.frinme.view;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import fr.iut_orsay.frinme.MainActivity;
import fr.iut_orsay.frinme.R;
import fr.iut_orsay.frinme.model.ContactModel;
import fr.iut_orsay.frinme.model.EventModel;
import fr.iut_orsay.frinme.rest.pojo.EventDetails;
import fr.iut_orsay.frinme.rest.RestUser;
import fr.iut_orsay.frinme.view.dialog.JoinFrag;
import fr.iut_orsay.frinme.view.dialog.QuitFrag;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);
        if (getArguments() != null) {
            currentEvent = ((EventModel) getArguments().getParcelable("event"));
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, 200, 0, "JoinEvent").setIcon(R.drawable.ic_add_black_24dp)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendRequest(View v) {
        Call<EventDetails> call = RestUser.get().getEventDetails(currentEvent.getNom());
        call.enqueue(new Callback<EventDetails>() {
            @Override
            public void onResponse(Call<EventDetails> call, Response<EventDetails> response) {
                final EventDetails r = response.body();
                if (r != null && response.isSuccessful()) {
                    TextView tvDesc = (TextView) v.findViewById(R.id.desc);
                    tvDesc.setText(r.getDesc());
                    if (r.getParticipants().size() != 0) {
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
