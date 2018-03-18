package fr.iut_orsay.frinme.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import fr.iut_orsay.frinme.R;
import fr.iut_orsay.frinme.model.EventComparator;
import fr.iut_orsay.frinme.model.EventModel;
import fr.iut_orsay.frinme.model.Location;
import fr.iut_orsay.frinme.rest.EventListDetails;
import fr.iut_orsay.frinme.rest.RestUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EventList extends Fragment {

    // Liste d'événements
    List<EventModel> testEvent;
    // Nom les colonnes du tableau
    private static final String[] TABLE_HEADERS = { "Nom", "Type", "Date", "Coordonnées" };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Jeu de test d'événements
        testEvent = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(isHidden()){
            getActivity().getFragmentManager().beginTransaction()
                    .show(EventList.this)
                    .commit();
        }
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        sendRequest(view);
        SortableTableView tableView = (SortableTableView ) view.findViewById(R.id.tableView);
        // Tableau de 4 colonnes
        tableView.setColumnCount(4);
        // Adaptateur pour l'affichage du contenu des cases
        tableView.setDataAdapter(new EventTableAdaptater (getActivity(), testEvent));
        // Header simple
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(getActivity(), TABLE_HEADERS));
        // Réaction au click
        tableView.addDataClickListener(new EventClickListener());
        // Comparateurs permettant de trier les colonnes
        tableView.setColumnComparator(0, EventComparator.getEventNameComparator());
        tableView.setColumnComparator(1, EventComparator.getEventTypeComparator());
        tableView.setColumnComparator(2, EventComparator.getEventDateComparator());
        tableView.setColumnComparator(3, EventComparator.getEventLocComparator());
    }

    private class EventTableAdaptater extends TableDataAdapter<EventModel> {

        EventTableAdaptater(Context context, List<EventModel> data) {
            super(context, data);
        }

        @Override
        public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
            EventModel evt = getRowData(rowIndex);
            View renderedView = null;

            switch (columnIndex) {
                case 0:
                    TextView tvName = new TextView(getContext());
                    tvName.setText(evt.getNom());
                    tvName.setGravity(Gravity.CENTER);
                    renderedView = tvName;
                    break;
                case 1:
                    TextView tvType = new TextView(getContext());
                    tvType.setText(evt.getType());
                    tvType.setGravity(Gravity.CENTER);
                    renderedView = tvType;
                    break;
                case 2:
                    TextView tvDate = new TextView(getContext());
                    tvDate.setText(DateFormat.getDateTimeInstance().format(evt.getDate()));
                    tvDate.setGravity(Gravity.CENTER);
                    renderedView = tvDate;
                    break;
                case 3:
                    TextView tvCoord = new TextView(getContext());
                    tvCoord.setText(evt.getCoordonnées().toString());
                    tvCoord.setGravity(Gravity.CENTER);
                    renderedView = tvCoord;
                    break;
            }

            return renderedView;
        }

    }

    private class EventClickListener implements TableDataClickListener<EventModel> {
        @Override
        public void onDataClicked(int rowIndex, EventModel event) {
            Bundle args = new Bundle();
            args.putParcelable("event", testEvent.get(rowIndex));
            Event EventFrag = new Event();
            EventFrag.setArguments(args);
            getActivity().getFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out,
                            android.R.animator.fade_in, android.R.animator.fade_out)
                    .add(R.id.fragment_container, EventFrag)
                    .hide(EventList.this)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void sendRequest(View v) {
        Call<EventListDetails> call = RestUser.get().getEventDetailedList();
        call.enqueue(new Callback<EventListDetails>() {
            @Override
            public void onResponse(Call<EventListDetails> call, Response<EventListDetails> response) {
                if (response.isSuccessful()) {
                    final EventListDetails r = response.body();
                    Toast.makeText(getActivity(), r.getMessage(), Toast.LENGTH_LONG).show();
                    testEvent.addAll(r.getEvents());
                    SortableTableView tableView = (SortableTableView ) v.findViewById(R.id.tableView);
                    tableView.setDataAdapter(new EventTableAdaptater (getActivity(), testEvent));
                    Log.e("REST CALL", testEvent.toString());
                } else {
                    Log.e("REST CALL", "sendRequest not successful");
                }
            }

            @Override
            public void onFailure(Call<EventListDetails> call, Throwable t) {
                Log.e("REST CALL", t.getMessage());
            }
        });
    }

}
