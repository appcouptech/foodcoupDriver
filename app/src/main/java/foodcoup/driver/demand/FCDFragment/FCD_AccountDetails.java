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
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import foodcoup.driver.demand.FCDActivity.FCDDashboardActivity.FCD_DashboardActivity;
import foodcoup.driver.demand.FCDUtils.Loader.LoaderTextView;
import foodcoup.driver.demand.FCDViews.AC_Textview;
import foodcoup.driver.demand.FCDViews.CircleImageView;
import foodcoup.driver.demand.FCDViews.FCD_Common;
import foodcoup.driver.demand.FCDViews.FCD_SharedPrefManager;
import foodcoup.driver.demand.FCDViews.FCD_URL;
import foodcoup.driver.demand.FCDViews.FCD_User;
import foodcoup.driver.demand.FCDViews.Utils;
import foodcoup.driver.demand.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FCD_AccountDetails extends AppCompatActivity {
    private Context context;
    private Snackbar bar;
    private AC_Textview txt_toolbarOrderNo;
    private CircleImageView lc_circleDriver;
    private ImageView img_back,img_currentTask,img_notification;
    private LinearLayout ll_main;
    private  LoaderTextView lt_driverName,lt_vehicleInsurance,lt_vehicleNumber,lt_vehicleModel,lt_vehicleName,lt_Email,lt_phone,lt_address,lt_accountName,lt_accountNumber,lt_bankName,lt_IFSC,lt_panCard;
    public FCD_AccountDetails() {
        // Required empty public constructor
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fcd__account_details);
        context = FCD_AccountDetails.this;
        FCD_User user = FCD_SharedPrefManager.getInstance(context).getUser();
        FCD_Common.id = String.valueOf(user.getid());
        FCD_Common.token_type = String.valueOf(user.gettoken_type());
        FCD_Common.access_token = String.valueOf(user.getaccess_token());

        FindViewById();
        AccountStatus();

        img_currentTask.setOnClickListener(v -> {
            Intent intent =new Intent(FCD_AccountDetails.this, FCD_DashboardActivity.class);
            startActivity(intent);
        });
        img_back.setOnClickListener(v -> onBackPressed());
    }

    private void FindViewById() {

        lc_circleDriver = findViewById(R.id.lc_circleDriver);
        lt_Email = findViewById(R.id.lt_Email);
        lt_phone = findViewById(R.id.lt_phone);
        lt_address = findViewById(R.id.lt_address);
        lt_driverName = findViewById(R.id.lt_driverName);
        lt_accountName = findViewById(R.id.lt_accountName);
        lt_accountNumber = findViewById(R.id.lt_accountNumber);
        lt_bankName = findViewById(R.id.lt_bankName);
        lt_IFSC = findViewById(R.id.lt_IFSC);
        lt_panCard = findViewById(R.id.lt_panCard);
        lt_vehicleName = findViewById(R.id.lt_vehicleName);
        lt_vehicleModel = findViewById(R.id.lt_vehicleModel);
        lt_vehicleNumber = findViewById(R.id.lt_vehicleNumber);
        lt_vehicleInsurance = findViewById(R.id.lt_vehicleInsurance);
        ll_main = findViewById(R.id.ll_main);

        txt_toolbarOrderNo = findViewById(R.id.txt_toolbarOrderNo);
        img_currentTask = findViewById(R.id.img_currentTask);
        img_notification = findViewById(R.id.img_notification);
        img_back = findViewById(R.id.img_back);
        img_back.setVisibility(View.VISIBLE);
        img_notification.setVisibility(View.GONE);
        img_currentTask.setVisibility(View.VISIBLE);
        txt_toolbarOrderNo.setVisibility(View.VISIBLE);
        txt_toolbarOrderNo.setText(getResources().getString(R.string.my_account));

        FCD_DashboardActivity.txt_toolbarOrderNo.setVisibility(View.GONE);
    }


    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void AccountStatus() {
        Utils.playProgressBar(FCD_AccountDetails.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FCD_URL.URL_DETAILUSER,
                ServerResponse -> {

                    Log.d("ServerResponse", "Account12" + FCD_URL.URL_DETAILUSER);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        FCD_Common.status = jsonResponse1.getString("status");

                        if (FCD_Common.status.equalsIgnoreCase("approved")){

                            Utils.stopProgressBar();
                            FCD_Common.AccountDriverName = jsonResponse1.getString("name");
                            FCD_Common.AccountEmail = jsonResponse1.getString("email");
                            FCD_Common.Accountphone = jsonResponse1.getString("mobile");
                            FCD_Common.Accountaddress= jsonResponse1.getString("location_address");
//                            FCD_Common.AccountNumber= jsonResponse1.getString("");
//                            FCD_Common.AccountIFSC= jsonResponse1.getString("");
//                            FCD_Common.AccountpanCard= jsonResponse1.getString("");
                            FCD_Common.AccountvehicleName= jsonResponse1.getString("vehicle_type");
                            FCD_Common.AccountvehicleModel= jsonResponse1.getString("vehicle_description");
                            FCD_Common.AccountvehicleNumber= jsonResponse1.getString("vehicle_plate");
                            FCD_Common.AccountvehicleInsurance= jsonResponse1.getString("insurance_number");
                            FCD_Common.AccountPhoto= jsonResponse1.getString("avatar");

                            Picasso.get().load(FCD_Common.AccountPhoto)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .into(lc_circleDriver);
                            lt_Email.setText(FCD_Common.AccountEmail);
                            lt_phone.setText(FCD_Common.Accountphone);
                            lt_address.setText(FCD_Common.Accountaddress);
                            lt_driverName.setText(FCD_Common.AccountDriverName);
                            lt_accountName.setText(FCD_Common.AccountName);
                            lt_accountNumber.setText(FCD_Common.AccountNumber);
                            lt_bankName.setText(FCD_Common.AccountbankName);
                            lt_IFSC.setText(FCD_Common.AccountIFSC);
                            lt_panCard.setText(FCD_Common.AccountpanCard);
                            lt_vehicleName.setText(FCD_Common.AccountvehicleName);
                            lt_vehicleModel.setText(FCD_Common.AccountvehicleModel);
                            lt_vehicleNumber.setText(FCD_Common.AccountvehicleNumber);
                            lt_vehicleInsurance.setText(FCD_Common.AccountvehicleInsurance);
                        }
                        else {
                            Utils.stopProgressBar();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("xcgsdgsdgsd", "dfhdf" + e);
                        Utils.stopProgressBar();
                    }
                }, volleyError -> {
            String value = volleyError.toString();
            snackBar(value);
            Log.d("fghfghfg","fghfdg"+FCD_URL.URL_DETAILUSER);
            Utils.stopProgressBar();
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
