package foodcoup.driver.demand.FCDFragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Objects;

import foodcoup.driver.demand.FCDActivity.FCDDashboardActivity.FCD_DashboardActivity;
import foodcoup.driver.demand.FCDFragment.FCDLoginDuration.FCD_LoginDuration;
import foodcoup.driver.demand.FCDFragment.FCDWeeklyEarnings.FCD_WeeklyEarnings;
import foodcoup.driver.demand.FCDUtils.BottomSheet.BottomSheetHomeStartDuty.StartDutyDialog;
import foodcoup.driver.demand.FCDUtils.Loader.LoaderTextView;
import foodcoup.driver.demand.FCDViews.AC_Textview;
import foodcoup.driver.demand.FCDViews.FCD_Common;
import foodcoup.driver.demand.FCDViews.FCD_SharedPrefManager;
import foodcoup.driver.demand.FCDViews.FCD_URL;
import foodcoup.driver.demand.FCDViews.FCD_User;
import foodcoup.driver.demand.R;

import static foodcoup.driver.demand.FCDActivity.FCDDashboardActivity.FCD_DashboardActivity.fragmentManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class FCD_HomeFragment extends Fragment implements View.OnClickListener {


    private Snackbar bar;
    private LoaderTextView lt_todayEarning;
    private LoaderTextView lt_weekEarning;
    private LoaderTextView lt_loginDuration;
    private LoaderTextView lt_floatCash;
    private LoaderTextView lt_amt,lt_currency;
    private LinearLayout ll_main;
    private LinearLayout ll_dudyStarted;
    private LinearLayout ll_searchOrders;

    public FCD_HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fcd__home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = getActivity();
        FCD_User user = FCD_SharedPrefManager.getInstance(context).getUser();
        FCD_Common.id = String.valueOf(user.getid());
        FCD_Common.token_type = String.valueOf(user.gettoken_type());
        FCD_Common.access_token = String.valueOf(user.getaccess_token());
        FindViewById(view);

        FCD_Common.currentTask = 2;

        if ( FCD_Common.dutyStarted == 1){
            ll_searchOrders.setVisibility(View.VISIBLE);
            ll_dudyStarted.setVisibility(View.GONE);
        }
        else {
            ll_dudyStarted.setVisibility(View.VISIBLE);
            ll_searchOrders.setVisibility(View.GONE);
        }

        homeStatus();
    }

    private void FindViewById(View view) {

        FCD_DashboardActivity.txt_toolbarOrderNo.setVisibility(View.GONE);

        lt_amt = view.findViewById(R.id.lt_amt);
        lt_currency = view.findViewById(R.id.lt_currency);
        ll_main = view.findViewById(R.id.ll_main);
        lt_todayEarning = view.findViewById(R.id.lt_todayEarning);
        Spannable colorSpan = new SpannableString(FCD_Common.HometodayEarning);
        colorSpan.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        lt_todayEarning.setText(colorSpan);

        lt_weekEarning = view.findViewById(R.id.lt_weekEarning);
        colorSpan = new SpannableString(FCD_Common.HomeweekEarning);
        colorSpan.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        lt_weekEarning.setText(colorSpan);

        lt_loginDuration = view.findViewById(R.id.lt_loginDuration);
        colorSpan = new SpannableString(FCD_Common.HomeloginDuration);
        colorSpan.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        lt_loginDuration.setText(colorSpan);

        lt_floatCash = view.findViewById(R.id.lt_floatCash);
        colorSpan = new SpannableString(FCD_Common.HomefloatCash);
        colorSpan.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        lt_floatCash.setText(colorSpan);

        AC_Textview txt_startDuty = view.findViewById(R.id.txt_startDuty);
        ll_dudyStarted = view.findViewById(R.id.ll_dudyStarted);
        ll_searchOrders = view.findViewById(R.id.ll_searchOrders);
        AC_Textview txt_searchOrders = view.findViewById(R.id.txt_searchOrders);


        LinearLayout ll_loginDuration = view.findViewById(R.id.ll_loginDuration);
        LinearLayout ll_todaysEarnings = view.findViewById(R.id.ll_todaysEarnings);
        LinearLayout ll_weeksEarnings = view.findViewById(R.id.ll_weeksEarnings);
        LinearLayout ll_floatingCash = view.findViewById(R.id.ll_floatingCash);

        txt_startDuty.setOnClickListener(this);
        txt_searchOrders.setOnClickListener(this);


        ll_loginDuration.setOnClickListener(this);
        ll_todaysEarnings.setOnClickListener(this);
        ll_weeksEarnings.setOnClickListener(this);
        ll_floatingCash.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void homeStatus() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FCD_URL.URL_EARNING,
                ServerResponse -> {

                    Log.d("ServerResponse", "dashboard" + ServerResponse);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        FCD_Common.status = jsonResponse1.getString("success");

                        if (FCD_Common.status.equalsIgnoreCase("1")){
                            FCD_Common.HometodayEarning = jsonResponse1.getString("today_earning");
                            FCD_Common.HometodayCurrency = jsonResponse1.getString("currency");
                            FCD_Common.HomeweekEarning = jsonResponse1.getString("weekly_earning");
                            FCD_Common.HomeloginDuration = jsonResponse1.getString("total_time");
                            FCD_Common.HomefloatCash = jsonResponse1.getString("float_cash");
                            lt_todayEarning.setText(FCD_Common.HometodayCurrency+" "+FCD_Common.HometodayEarning);
                            lt_weekEarning.setText(FCD_Common.HometodayCurrency+" "+FCD_Common.HomeweekEarning);
                            lt_loginDuration.setText(FCD_Common.HomeloginDuration);
                            lt_floatCash.setText(FCD_Common.HomefloatCash);
                            lt_currency.setText(FCD_Common.HometodayCurrency);
                            lt_amt.setText(FCD_Common.HometodayEarning);
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
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.txt_startDuty:

                FCD_Common.currentTask = 3;

                StartDutyDialog startDutyDialog =
                        StartDutyDialog.newInstance();
                startDutyDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),
                        "startDutyDialog");

                break;

            case R.id.txt_searchOrders:

                if (FCD_Common.longitude > 0 || FCD_Common.latitude > 0 || FCD_Common.dutyStarted == 1){

                    FCD_LiveOrders liveOrderFragment = new FCD_LiveOrders();
                    FragmentTransaction fragmentTransactionLiveOrder = fragmentManager.beginTransaction();
                    fragmentTransactionLiveOrder.replace(R.id.content_frame, liveOrderFragment);
                    fragmentTransactionLiveOrder.commit();
                    FCD_DashboardActivity.txt_toolbar.setText(R.string.live_order);

                    FCD_Common.currentTask = 1;
                }else {
                    Toast.makeText(getActivity(),"Current Location Not Recieved",Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.ll_todaysEarnings :

                FCD_TodaysEarnings todaysEarnings = new FCD_TodaysEarnings();
                FragmentTransaction fragmentTransactiontodaysEarnings = fragmentManager.beginTransaction();
                fragmentTransactiontodaysEarnings.add(R.id.content_frame, todaysEarnings);
                fragmentTransactiontodaysEarnings.addToBackStack("homeFragment");
                fragmentTransactiontodaysEarnings.commit();
                FCD_DashboardActivity.txt_toolbar.setText(R.string.todays_earnings);

                break;

            case R.id.ll_weeksEarnings:

                FCD_WeeklyEarnings weeklyEarnings = new FCD_WeeklyEarnings();
                FragmentTransaction fragmentTransactionweeklyEarnings = fragmentManager.beginTransaction();
                fragmentTransactionweeklyEarnings.add(R.id.content_frame, weeklyEarnings , "weeklyEarnings");
                fragmentTransactionweeklyEarnings.addToBackStack("homeFragment");
                fragmentTransactionweeklyEarnings.commit();
                FCD_DashboardActivity.txt_toolbar.setText(R.string.week_earning);
                break;

            case R.id.ll_loginDuration:

                FCD_LoginDuration loginDuration = new FCD_LoginDuration();
                FragmentTransaction fragmentTransactionloginDuration = fragmentManager.beginTransaction();
                fragmentTransactionloginDuration.add(R.id.content_frame, loginDuration , "loginDuration");
                fragmentTransactionloginDuration.addToBackStack("homeFragment");
                fragmentTransactionloginDuration.commit();
                FCD_DashboardActivity.txt_toolbar.setText(R.string.login_duration);
                break;

            case R.id.ll_floatingCash:

                FCD_FloatingCash floatingCash = new FCD_FloatingCash();
                FragmentTransaction fragmentTransactionfloatingCash = fragmentManager.beginTransaction();
                fragmentTransactionfloatingCash.add(R.id.content_frame, floatingCash , "floatingCash");
                fragmentTransactionfloatingCash.addToBackStack("homeFragment");
                fragmentTransactionfloatingCash.commit();
                FCD_DashboardActivity.txt_toolbar.setText(R.string.floating_cash);
                break;

        }
    }


    public void  DutyStartedlayoutChange() {

        Log.d("dutyStarted " , "dutyStarted : " + FCD_Common.dutyStarted);
        if ( FCD_Common.dutyStarted == 1){
            Log.d("dutyStarted if" , "dutyStarted : " + FCD_Common.dutyStarted);
            ll_searchOrders.setVisibility(View.VISIBLE);
            ll_dudyStarted.setVisibility(View.GONE);
        }
        else {
            Log.d("dutyStarted else " , "dutyStarted : " + FCD_Common.dutyStarted);
            ll_dudyStarted.setVisibility(View.VISIBLE);
            ll_searchOrders.setVisibility(View.GONE);
        }

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
