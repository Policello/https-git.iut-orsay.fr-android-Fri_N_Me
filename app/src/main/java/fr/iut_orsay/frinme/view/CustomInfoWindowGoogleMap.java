package fr.iut_orsay.frinme.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;

import android.util.Log;
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
 * Fenêtre personnalisée lors du click sur un marker
 */
public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter, View.OnClickListener{
    private Context context;
    private Object o;
    private EventModel event;
    private ContactModel contact;
    private InfoWindowData info;
    private String TAG = getClass().getSimpleName();

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
        view.setOnClickListener(this);
        dialog_msg = (TextView) view.findViewById(R.id.dialog_msg);
        dialog_title = (TextView) view.findViewById(R.id.dialog_title);
        dialog_ok = (TextView) view.findViewById(R.id.dialog_ok);
        dialog_date = (TextView) view.findViewById(R.id.dialog_date);
        dialog_cap = (TextView) view.findViewById(R.id.dialog_cap);


        dialog_title.setText(marker.getTitle());
        dialog_msg.setText(marker.getSnippet());
        dialog_ok.setText("Cliquer pour plus de détails");

        info = (InfoWindowData) marker.getTag();


        switch (info.getObject().getClass().getSimpleName()) {
            case "EventModel":
                this.event = (EventModel) info.getObject();
                //Log.d("DATE", event.toString());
                //DateFormat df = new SimpleDateFormat("dd/MM/YY HH:mm");
                DateFormat df = DateFormat.getDateInstance();
                String date = df.format(event.getDate());
                String s = "" + date;
                dialog_date.setText(s);
                dialog_cap.setText("");
                //dialog_date.setText("TEST");
                break;
            case "ContactModel":
                this.contact = (ContactModel) o;
                dialog_date.setText("");
                dialog_date.setText("");
                dialog_cap.setText("");
                break;
            default:
                dialog_ok.setClickable(false);
        }

       return view;


    }

    public Object getObject() {
        return (this.o);
    }

    @Override
    public void onClick(View v) {
        }
    }
