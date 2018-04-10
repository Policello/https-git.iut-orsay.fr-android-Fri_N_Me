package fr.iut_orsay.frinme.view;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.iut_orsay.frinme.R;
import fr.iut_orsay.frinme.model.ContactModel;
import fr.iut_orsay.frinme.model.DataBase;
import fr.iut_orsay.frinme.rest.RestUser;
import fr.iut_orsay.frinme.rest.pojo.AfficherUser;
import fr.iut_orsay.frinme.rest.pojo.EstAmi;
import fr.iut_orsay.frinme.rest.pojo.Message;
import fr.iut_orsay.frinme.rest.pojo.StringBD;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Vue des détails de contacts
 * utilisateur quelconque
 */
public class Contact extends Fragment implements AdapterView.OnItemClickListener {

    private static final String TAG = "tag";
    private ListView mListView;
    private static List<StringBD> eventNomListe;
    private List<String> eventNomListeAdapte;
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
            //On recupere le contact recu depuis un autre ecran (e.g : Event ou listeContacts)
            contactRecu = ((ContactModel) getArguments().getParcelable("Contact"));
            eventNomListeAdapte = new ArrayList<>();
            eventNomListe = new ArrayList<>();
            Log.e(TAG, "onCreateView: " + contactRecu.getId());
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
            LatLng myLoc = new LatLng(contactRecu.getCoordonnées().getLatitude(), contactRecu.getCoordonnées().getLongitude());
            LocalisationContactsDetails.setText(getInfoFromLatLng(myLoc));

        }

        ImageView img = (ImageView) view.findViewById(R.id.ImageProfil);
        img.setImageResource(R.drawable.ic_menu_camera);
        RecupererContact();
    }

    /**
     *  Recupere les coordonnées(longitude,latitude) et
     *  renvoie les informations du lieu le plus proche(Ville par exemple)
     *  Si rien ne se trouve à proximité il renverra "Pas d'info de lieu"
     * @param l
     * @return lieu
     */
    public String getInfoFromLatLng(LatLng l) {
        Geocoder gcd = new Geocoder(this.getActivity(), Locale.getDefault());

        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(l.latitude, l.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "pas de résultats");
        }

        String lieu;

        try {
            Log.d(TAG, addresses.get(0).toString());
            if (addresses.get(0).getLocality() != null) {
                lieu = addresses.get(0).getLocality();
            } else if (addresses.get(0).getCountryName() != null) {
                lieu = addresses.get(0).getCountryName();
            } else {
                lieu = "Pas d'infos de lieu";
            }
        } catch (IndexOutOfBoundsException e) {
            lieu = "Pas d'infos de lieu";
        }

        return lieu;
    }

    /**
     * Recupere la position d'un bouton(ici les boutons ont pour nom divers event)
     * dans la ListView et renplacera le fragment present par le fragment de l'event
     * selectionné par l'utilisateur
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle args = new Bundle();
        args.putParcelable("event", DataBase.getAppDatabase(getActivity()).eventDao().findByName(parent.getItemAtPosition(position).toString()));
        // args.putParcelable("event", eventListe.get(position));
        Event EventFrag = new Event();
        EventFrag.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out,
                        android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.fragment_container, EventFrag)
                .addToBackStack(null)
                .commit();

    }

    /**
     * Permet de récuperer la liste d'event ou participe le contact selectionnée
     */
    private void RecupererContact() {
        Call<fr.iut_orsay.frinme.rest.pojo.AfficherUser> call = RestUser.get().getInfoEvenementsUtilisateurs(contactRecu.getId());
        call.enqueue(new Callback<fr.iut_orsay.frinme.rest.pojo.AfficherUser>() {
            @Override
            public void onResponse(Call<fr.iut_orsay.frinme.rest.pojo.AfficherUser> call, Response<fr.iut_orsay.frinme.rest.pojo.AfficherUser> response) {
                if (response.isSuccessful()) {
                    final AfficherUser r = response.body();
                    eventNomListe.addAll(r.getEvent());
                    Log.e(TAG, "onResponse: " + eventNomListe.size());
                    Log.e(TAG, "onResponse: " + eventNomListe.toString());
                    if (eventNomListe.size() != 0) {
                        for (StringBD event : eventNomListe) {
                            eventNomListeAdapte.add(event.getNomEvent());
                            Log.e(TAG, "Creation array " + event.getNomEvent());
                        }
                        final ArrayAdapter<String> adapter;
                        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, eventNomListeAdapte);
                        mListView.setAdapter(adapter);
                    }

                } else {
                    Log.e("REST CALL", "sendRequest not successful listeContact");
                }
            }

            @Override
            public void onFailure(Call<fr.iut_orsay.frinme.rest.pojo.AfficherUser> call, Throwable t) {
                Log.e("REST CALL", t.getMessage());
            }
        });
        mListView.setOnItemClickListener(this);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Call<EstAmi> call = RestUser.get().getEstAmi(fr.iut_orsay.frinme.model.SessionManagerPreferences.getSettings(getActivity()).getUsrId()
                , contactRecu.getId());
        call.enqueue(new Callback<EstAmi>() {
            @Override
            public void onResponse(Call<EstAmi> call, Response<EstAmi> response) {
                final EstAmi r = response.body();
                if (r != null && response.isSuccessful()) {
                    //Toast.makeText(getActivity(), r.getMessage() + "", Toast.LENGTH_LONG).show();
                    boolean ami = r.getMessage();
                    if (ami) {
                        menu.add(0, 100, 0, "Supprimer un ami").setIcon(R.drawable.ic_close_black_24dp)
                                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                    } else {
                        menu.add(0, 200, 0, "Ajouter un ami").setIcon(R.drawable.ic_person_add_black_24dp)
                                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                    }
                } else {
                    Log.e("REST CALL", "sendRequest not successful");
                }
            }

            @Override
            public void onFailure(Call<EstAmi> call, Throwable t) {
                Log.e("REST CALL", t.getMessage());
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 100:
                Call<Message> callDel = RestUser.get().getDeleteFriend(fr.iut_orsay.frinme.model.SessionManagerPreferences.getSettings(getActivity()).getUsrId()
                        , contactRecu.getId());
                callDel.enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        final Message r = response.body();
                        if (r != null && response.isSuccessful()) {
                            getActivity().invalidateOptionsMenu();
                            Toast.makeText(getActivity(), r.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Log.e("REST CALL", "sendRequest not successful");
                        }
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        Log.e("REST CALL", t.getMessage());
                    }
                });
                return true;
            case 200:
                Call<Message> callAdd = RestUser.get().getAddFriend(fr.iut_orsay.frinme.model.SessionManagerPreferences.getSettings(getActivity()).getUsrId()
                        , contactRecu.getId());
                callAdd.enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        final Message r = response.body();
                        if (r != null && response.isSuccessful()) {
                            getActivity().invalidateOptionsMenu();
                            Toast.makeText(getActivity(), r.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Log.e("REST CALL", "sendRequest not successful");
                        }
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        Log.e("REST CALL", t.getMessage());
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
