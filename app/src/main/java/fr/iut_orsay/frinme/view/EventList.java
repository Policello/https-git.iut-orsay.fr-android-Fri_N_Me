package fr.iut_orsay.frinme.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import fr.iut_orsay.frinme.MainActivity;
import fr.iut_orsay.frinme.R;
import fr.iut_orsay.frinme.model.EventComparator;
import fr.iut_orsay.frinme.model.EventModel;
import fr.iut_orsay.frinme.model.Location;


public class EventList extends Fragment {

    List<EventModel> testEvent;
    private static final String[] TABLE_HEADERS = { "Nom", "Type", "Date", "Coordonnées" };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testEvent = new ArrayList<>();
        EventModel e1 = new EventModel("test", "cat", new Date(), new Location(2.36,5.69));
        EventModel e2 = new EventModel("wew", "chat", new Date(), new Location(8.36,5.6777));
        EventModel e3 = new EventModel("aaaaa", "zzzzzzzz", new Date(), new Location(2.36,5.69));
        testEvent.add(e1);
        testEvent.add(e2);
        testEvent.add(e3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        SortableTableView tableView = (SortableTableView ) view.findViewById(R.id.tableView);
        tableView.setColumnCount(4);
        tableView.setDataAdapter(new EventTableAdaptater (getActivity(), testEvent));
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(getActivity(), TABLE_HEADERS));
        tableView.addDataClickListener(new EventClickListener());
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
            getActivity().getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new Event())
                    .addToBackStack(null)
                    .commit();
        }
    }

}
