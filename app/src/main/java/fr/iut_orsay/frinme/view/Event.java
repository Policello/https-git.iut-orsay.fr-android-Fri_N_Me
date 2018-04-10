package fr.iut_orsay.frinme.view;

import android.content.Context;
import android.support.annotation.NonNull;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import fr.iut_orsay.frinme.MainActivity;
import fr.iut_orsay.frinme.R;
import fr.iut_orsay.frinme.model.ContactModel;
import fr.iut_orsay.frinme.model.DataBase;
import fr.iut_orsay.frinme.model.EventModel;
import fr.iut_orsay.frinme.model.Location;
import fr.iut_orsay.frinme.rest.pojo.AfficherUser;
import fr.iut_orsay.frinme.rest.pojo.ContactListDetails;
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
public class Event extends Fragment implements AdapterView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;
    private ListView mListView;
    private EventModel currentEvent;
    private boolean defaultValues = false;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String nomEvent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);
        if (getArguments() != null) {
            currentEvent = ((EventModel) getArguments().getParcelable("event"));
        } else {
            defaultValues = true;
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mListView = (ListView) view.findViewById(R.id.listView);

        // Remplissage de la liste d'amis
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, new String[]{"Aucun"});
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);

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
        switch (currentEvent.getType()) {
            case "Athlétisme":
                img.setImageResource(R.drawable.athletisme);
                break;
            case "Autre":
                img.setImageResource(R.drawable.autre);
                break;
            case "Badminton":
                img.setImageResource(R.drawable.badminton);
                break;
            case "BasketBall":
                img.setImageResource(R.drawable.basketball);
                break;
            case "Boxe":
                img.setImageResource(R.drawable.boxe);
                break;
            case "Criterium":
                img.setImageResource(R.drawable.criterium);
                break;
            case "Cyclisme sur piste":
                img.setImageResource(R.drawable.cyclismesurpiste);
                break;
            case "Escrime":
                img.setImageResource(R.drawable.escrime);
                break;
            case "Football":
                img.setImageResource(R.drawable.football);
                break;
            case "Gymnastique":
                img.setImageResource(R.drawable.gymnastique);
                break;
            case "HandBall":
                img.setImageResource(R.drawable.handball);
                break;
            case "Hockey":
                img.setImageResource(R.drawable.hockey);
                break;
            case "Judo":
                img.setImageResource(R.drawable.judo);
                break;
            case "Remise de médailles":
                img.setImageResource(R.drawable.medaille);
                break;
            case "Natation":
                img.setImageResource(R.drawable.natation);
                break;
            case "Ouverture":
                img.setImageResource(R.drawable.ouverture);
                break;
            case "Fermeture et remise des coupes":
                img.setImageResource(R.drawable.remisecoupes);
                break;
            case "Rugby":
                img.setImageResource(R.drawable.rugby);
                break;
            case "Tennis":
                img.setImageResource(R.drawable.tennis);
                break;
            case "Tennis de table":
                img.setImageResource(R.drawable.tennisdetable);
                break;
            case "VolleyBall":
                img.setImageResource(R.drawable.volleyball);
                break;
            case "Water-polo":
                img.setImageResource(R.drawable.waterpolo);
                break;
            default:
                img.setImageResource(R.drawable.bike);
                break;
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (MainActivity.userStatus == MainActivity.Status.EXTERNE) {
            menu.add(0, 200, 0, "JoinEvent").setIcon(R.drawable.ic_add_black_24dp)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        } else {
            menu.add(0, 300, 0, "QuitEvent").setIcon(R.drawable.ic_close_black_24dp)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 200:
                // Dialogs permettant de s'inscrire aux evts
                mListener.onFragmentInteraction(currentEvent.getNom());
                DialogFragment joinFragment;
                joinFragment = new JoinFrag();
                joinFragment.show(getFragmentManager(), "PopupJoin");
                return true;
            case 300:
                // Dialogs permettant de se de-inscrire aux evts
                mListener.onFragmentInteraction(currentEvent.getNom());
                DialogFragment quitFrag;
                quitFrag = new QuitFrag();
                quitFrag.show(getFragmentManager(), "PopupQuit");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        mListener = null;
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Call<AfficherUser> call = RestUser.get().getInfoEvenementsUtilisateurs(1);
        call.enqueue(new Callback<AfficherUser>() {
            @Override
            public void onResponse(Call<AfficherUser> call, Response<AfficherUser> response) {
                final AfficherUser r = response.body();
                if (r != null && response.isSuccessful()) {
                    Bundle args = new Bundle();
                    args.putParcelable("Contact", new ContactModel(1,"test",new Location(0,0)));
                    Contact ContactFrag = new Contact();
                    ContactFrag.setArguments(args);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out,
                                    android.R.animator.fade_in, android.R.animator.fade_out)
                            .replace(R.id.fragment_container, ContactFrag)
                            .addToBackStack(null)
                            .commit();
                } else {
                    Log.e("REST CALL", "sendRequest not successful");
                }
            }

            @Override
            public void onFailure(Call<AfficherUser> call, Throwable t) {
                Log.e("REST CALL", t.getMessage());
            }
        });
    }

}
