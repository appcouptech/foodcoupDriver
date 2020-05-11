package foodcoup.driver.demand.FCDUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import foodcoup.driver.demand.R;

public class UtilsStatic {

    static LayoutInflater inflater ;
    static int i = 0;

    public static void noInternetConnection(LinearLayout linearLayout, Context context){
        i= 1;
        inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View inflatedLayout= inflater.inflate(R.layout.no_internet_connection,null, false);
        linearLayout.addView(inflatedLayout);
    }
}
