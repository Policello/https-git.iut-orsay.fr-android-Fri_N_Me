package fr.iut_orsay.frinme.rest.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by yyang5 on 23/03/2018.
 */

public class EstAmi {


    @SerializedName("message")
    @Expose
    //
    private boolean message;

    public boolean getMessage() {
        return message;
    }
}
