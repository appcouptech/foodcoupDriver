package foodcoup.driver.demand.FCDFragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.Objects;

import foodcoup.driver.demand.FCDViews.AC_Textview;
import foodcoup.driver.demand.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FCD_Help extends Fragment {


    public FCD_Help() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fcd__help, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.help_alert, null);
        dialogBuilder.setView(dialogView);

        AC_Textview txt_cancel = dialogView.findViewById(R.id.txt_cancel);
        AC_Textview txt_call = dialogView.findViewById(R.id.txt_call);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(false);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });

        txt_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
