package foodcoup.driver.demand.FCDFragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import foodcoup.driver.demand.FCDActivity.FCDDashboardActivity.FCD_DashboardActivity;
import foodcoup.driver.demand.FCDFragment.FCDLoginDuration.FCD_LoginDuration;
import foodcoup.driver.demand.FCDUtils.Loader.LoaderTextView;
import foodcoup.driver.demand.FCDViews.AC_Textview;
import foodcoup.driver.demand.FCDViews.FCD_Common;
import foodcoup.driver.demand.FCDViews.FCD_SharedPrefManager;
import foodcoup.driver.demand.FCDViews.FCD_URL;
import foodcoup.driver.demand.FCDViews.FCD_User;
import foodcoup.driver.demand.FCDViews.Utils;
import foodcoup.driver.demand.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FCD_FloatingCash extends AppCompatActivity {

    private LoaderTextView lt_floatCash,lt_limitCash;
    private LinearLayout ll_main;
    private Snackbar bar;
    private Context context;
    private AC_Textview txt_toolbarOrderNo;
    private ImageView img_back,img_currentTask,img_notification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fcd__floating_cash);
        context = FCD_FloatingCash.this;
        FCD_User user = FCD_SharedPrefManager.getInstance(context).getUser();
        FCD_Common.id = String.valueOf(user.getid());
        FCD_Common.token_type = String.valueOf(user.gettoken_type());
        FCD_Common.access_token = String.valueOf(user.getaccess_token());

        FindViewById();
        ConfirmOrder();
        img_currentTask.setOnClickListener(v -> {
            Intent intent =new Intent(FCD_FloatingCash.this, FCD_DashboardActivity.class);
            startActivity(intent);
        });
        img_back.setOnClickListener(v -> onBackPressed());
    }

    private void FindViewById() {

        lt_floatCash = findViewById(R.id.lt_floatCash);
        lt_limitCash = findViewById(R.id.lt_limitCash);
        ll_main = findViewById(R.id.ll_main);

        txt_toolbarOrderNo = findViewById(R.id.txt_toolbarOrderNo);
        img_currentTask = findViewById(R.id.img_currentTask);
        img_notification = findViewById(R.id.img_notification);
        img_back = findViewById(R.id.img_back);
        img_back.setVisibility(View.VISIBLE);
        img_notification.setVisibility(View.GONE);
        img_currentTask.setVisibility(View.VISIBLE);
        txt_toolbarOrderNo.setVisibility(View.VISIBLE);
        txt_toolbarOrderNo.setText(getResources().getString(R.string.floating_cash));
        //snackBar("dfsdfsdf");
    }
    @SuppressLint("SetTextI18n")
    private void ConfirmOrder() {
        Utils.playProgressBar(FCD_FloatingCash.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FCD_URL.URL_FLOATCASH,
                ServerResponse -> {

                    Log.d("ServerResponse", "floatcash" + ServerResponse);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        FCD_Common.status = jsonResponse1.getString("success");
                        if (FCD_Common.status.equalsIgnoreCase("1")) {
                            Utils.stopProgressBar();
                            FCD_Common.float_cash=jsonResponse1.getString("float_cash");
                            FCD_Common.float_currency=jsonResponse1.getString("currency");
                            FCD_Common.float_limit=jsonResponse1.getString("float_limit");
                            lt_floatCash.setText(FCD_Common.float_currency+" "+FCD_Common.float_cash);
                            lt_limitCash.setText(getResources().getString(R.string.floatlimit)+FCD_Common.float_currency+" "+FCD_Common.float_limit);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("xcgsdgsdgsd", "dfhdf" + e);
                    }
                }, volleyError -> {
            String value = volleyError.toString();
            snackBar(value);
        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                Log.d("fdgdfgfdg", "sdfgsdgs" + FCD_Common.token_type + " " + FCD_Common.access_token);
                params.put("Authorization", FCD_Common.token_type + " " + FCD_Common.access_token);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(context));
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void snackBar(final String value) {
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(50);
                    bar = Snackbar.make(ll_main, value, Snackbar.LENGTH_LONG)
                            .setAction("Dismiss", v -> bar.dismiss());
                    bar.setActionTextColor(Color.RED);
                    TextView tv = bar.getView().findViewById(R.id.snackbar_text);
                    tv.setTextColor(Color.CYAN);
                    bar.show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } }};
        timerThread.start();
    }

}
