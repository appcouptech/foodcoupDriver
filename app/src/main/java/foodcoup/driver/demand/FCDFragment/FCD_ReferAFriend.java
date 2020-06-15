package foodcoup.driver.demand.FCDFragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import foodcoup.driver.demand.FCDActivity.FCDDashboardActivity.FCD_DashboardActivity;
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
public class FCD_ReferAFriend extends AppCompatActivity {

    private Context context;
    private Snackbar bar;
    private LoaderTextView lt_referralCode;
    private LinearLayout ll_main;
    private ImageView img_back,img_currentTask,img_notification;
    private AC_Textview txt_toolbarOrderNo,txt_inviteFrnds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fcd__refer_afriend);
        context = FCD_ReferAFriend.this;

        FCD_User user = FCD_SharedPrefManager.getInstance(context).getUser();
        FCD_Common.id = String.valueOf(user.getid());
        FCD_Common.token_type = String.valueOf(user.gettoken_type());
        FCD_Common.access_token = String.valueOf(user.getaccess_token());
        FindViewById();
        GetUserDetails();

        txt_inviteFrnds.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String text = getResources().getString(R.string.refcode)+" - "+FCD_Common.referealcode;
            // change with required  application package

            intent.setPackage("com.whatsapp");
            if (intent != null) {
                intent.putExtra(Intent.EXTRA_TEXT, text);//
                startActivity(Intent.createChooser(intent, text));
            } else {

                Toast.makeText(context, "App not found", Toast.LENGTH_SHORT).show();
            }
        });

        img_currentTask.setOnClickListener(v -> {
            Intent intent =new Intent(FCD_ReferAFriend.this, FCD_DashboardActivity.class);
            startActivity(intent);
        });
        img_back.setOnClickListener(v -> onBackPressed());
    }


    private void FindViewById() {
        lt_referralCode = findViewById(R.id.lt_referralCode);
        ll_main = findViewById(R.id.ll_main);
        txt_inviteFrnds = findViewById(R.id.txt_inviteFrnds);

        txt_toolbarOrderNo = findViewById(R.id.txt_toolbarOrderNo);
        img_currentTask = findViewById(R.id.img_currentTask);
        img_notification = findViewById(R.id.img_notification);
        img_back = findViewById(R.id.img_back);
        img_back.setVisibility(View.VISIBLE);
        img_notification.setVisibility(View.GONE);
        img_currentTask.setVisibility(View.VISIBLE);
        txt_toolbarOrderNo.setVisibility(View.VISIBLE);
        txt_toolbarOrderNo.setText(getResources().getString(R.string.refer_a_friend));
    }
    private void GetUserDetails() {
        Utils.playProgressBar(FCD_ReferAFriend.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FCD_URL.URL_DETAILUSER,
                ServerResponse -> {

                    Log.d("fgjfghjfghfg", "" + ServerResponse);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        //FC_Common.status = jsonResponse1.getString("success");
                        FCD_Common.referealcode = jsonResponse1.getString("refer_code");

                        Utils.stopProgressBar();
                        lt_referralCode.setText(FCD_Common.referealcode);
                        Log.d("fdhfdgfd","dfghfdgfdgf"+FCD_Common.referealcode);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utils.stopProgressBar();
                        Log.d("fdhbdfghdf", "dfhdf" + e);
                        snackBar(String.valueOf(e));
                    }
                }, volleyError -> {
            String value = volleyError.toString();
            Utils.stopProgressBar();
            snackBar(value);
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                Log.d("fdgdfgfdg","sdfgsdgs"+FCD_Common.token_type+" "+FCD_Common.access_token);
                params.put("Authorization", FCD_Common.token_type+" "+FCD_Common.access_token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
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
