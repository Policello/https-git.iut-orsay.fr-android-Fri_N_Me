package fr.iut_orsay.frinme.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import fr.iut_orsay.frinme.R;

/**
 * Created by cyan on 27/03/2018.
 */

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter{
    private Context context;
    private Object o;
    TextView dialog_msg, dialog_title, dialog_ok;

    public CustomInfoWindowGoogleMap(Context ctx, Object o){
             context =ctx;
            this.o = o;


    }
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        /*switch (getObject().getClass().getSimpleName()) {
            case

        }*/
       View view=((Activity)context).getLayoutInflater().inflate(R.layout.item_dialog, null);
        dialog_msg = (TextView)view.findViewById(R.id.dialog_msg);
        dialog_title = (TextView)view.findViewById(R.id.dialog_title);
        dialog_ok = (TextView) view.findViewById(R.id.dialog_ok);
        dialog_title.setText("information");
        //dialog_msg.setText();
        dialog_ok.setText("voir les details");


        return view;
    }

    public Object getObject() {
        return (this.o);
    }





}
