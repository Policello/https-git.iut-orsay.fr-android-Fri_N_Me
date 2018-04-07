package fr.iut_orsay.frinme.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.text.DateFormat;

import fr.iut_orsay.frinme.R;
import fr.iut_orsay.frinme.model.ContactModel;
import fr.iut_orsay.frinme.model.EventModel;
import fr.iut_orsay.frinme.model.InfoWindowData;

/**
 * Created by cyan on 27/03/2018.
 */

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {
    private Context context;
    private Object o;
    private EventModel event;
    private ContactModel contact;

    TextView dialog_msg, dialog_title, dialog_ok, dialog_date, dialog_cap;

    public CustomInfoWindowGoogleMap(Context ctx) {
        context = ctx;
        //this.o = o;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        /*switch (getObject().getClass().getSimpleName()) {
            case "EventModel":
                this.event = (EventModel) o;
                break;
            case "ContactModel":
                this.contact = (ContactModel) o;
                break;
        }*/

        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.item_dialog, null);
        dialog_msg = (TextView) view.findViewById(R.id.dialog_msg);
        dialog_title = (TextView) view.findViewById(R.id.dialog_title);
        dialog_ok = (TextView) view.findViewById(R.id.dialog_ok);
        dialog_date = (TextView) view.findViewById(R.id.dialog_date);
        dialog_cap = (TextView) view.findViewById(R.id.dialog_cap);


        dialog_title.setText(marker.getTitle());
        dialog_msg.setText(marker.getSnippet());
        dialog_ok.setText("voir les details");

        InfoWindowData info = (InfoWindowData) marker.getTag();


        switch (info.getObject().getClass().getSimpleName()) {
            case "EventModel":
                this.event = (EventModel) info.getObject();
                //Log.d("DATE", event.toString());
                //DateFormat df = new SimpleDateFormat("dd/MM/YY HH:mm");
                DateFormat df = DateFormat.getDateInstance();
                String date = df.format(event.getDate());
                String s = "" + date;
                dialog_date.setText(s);
                //dialog_date.setText("TEST");
                break;
            case "ContactModel":
                this.contact = (ContactModel) o;
                break;
            default:
                dialog_ok.setClickable(false);
        }


        return view;
    }

    public Object getObject() {
        return (this.o);
    }

}
