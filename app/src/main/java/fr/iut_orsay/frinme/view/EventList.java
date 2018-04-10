package fr.iut_orsay.frinme.view;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import fr.iut_orsay.frinme.R;
import fr.iut_orsay.frinme.model.DataBase;
import fr.iut_orsay.frinme.model.EventComparator;
import fr.iut_orsay.frinme.model.EventModel;


public class EventList extends Fragment {

    // Liste d'événements
    List<EventModel> events;
    // Nom les colonnes du tableau
    private static final String[] TABLE_HEADERS = {"Nom", "Type", "Date", "Coordonnées"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialisation de la liste d'événements
        events = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (isHidden()) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .show(EventList.this)
                    .commit();
        }
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        events.clear();
        events.addAll(DataBase.getAppDatabase(getActivity()).eventDao().getAll());
        SortableTableView tableView = (SortableTableView) view.findViewById(R.id.tableView);
        // Tableau de 4 colonnes
        tableView.setColumnCount(4);
        // Adaptateur pour l'affichage du contenu des cases
        tableView.setDataAdapter(new EventTableAdaptater(getActivity(), events));
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
            args.putParcelable("event", events.get(rowIndex));
            Event EventFrag = new Event();
            EventFrag.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out,
                            android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.fragment_container, EventFrag)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
