package foodcoup.driver.demand.FCDFragment.FCDLoginDuration;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import foodcoup.driver.demand.FCDActivity.FCDDashboardActivity.FCD_DashboardActivity;
import foodcoup.driver.demand.FCDPojo.FCDLoginDuration.LoginDurationObject;
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
public class FCD_LoginDuration extends Fragment {

    private RecyclerView rv_pastLoginInformation ;
    private Snackbar bar;
    private Context context;
    private  AC_Textview txt_emptyview;
    private LoaderTextView lt_totalhrs;
    private LinearLayout  ll_main;
    private PastLoginInfoAdpater pastLoginInfoAdpater ;
    private ArrayList<LoginDurationObject> loginDurationObjects = new ArrayList<>();

    public FCD_LoginDuration() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fcd__login_duration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        FCD_User user = FCD_SharedPrefManager.getInstance(context).getUser();
        FCD_Common.id = String.valueOf(user.getid());
        FCD_Common.token_type = String.valueOf(user.gettoken_type());
        FCD_Common.access_token = String.valueOf(user.getaccess_token());
        FindViewById(view);

        pastLoginInfoAdpater = new PastLoginInfoAdpater(loginDurationObjects);
        rv_pastLoginInformation.setAdapter(pastLoginInfoAdpater);
        rv_pastLoginInformation.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        LoginList();
    }

    private void FindViewById(View view) {
        rv_pastLoginInformation = view.findViewById(R.id.rv_pastLoginInformation);
        txt_emptyview = view.findViewById(R.id.txt_emptyview);
        lt_totalhrs = view.findViewById(R.id.lt_totalhrs);
        ll_main = view.findViewById(R.id.ll_main);

        LoginDurationObject durationObject = new LoginDurationObject();
        durationObject.setD_image("");
        durationObject.setD_name("");
        loginDurationObjects.add(durationObject);
        durationObject.setD_image("");
        durationObject.setD_name("");
        loginDurationObjects.add(durationObject);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @SuppressLint("SetTextI18n")
    private void  LoginList() {
        Utils.playProgressBar(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FCD_URL.URL_LOGINLIST,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        Log.d("fdghfdgfdgd","fdgfd"+obj);
                        if (obj.optString("success").equals("1")) {
                            JSONArray dataArray = obj.getJSONArray("data");
                            Log.d("fghfghfg","fhfghfg"+dataArray);


                            Utils.stopProgressBar();
                            loginDurationObjects.clear();
                            rv_pastLoginInformation.setVisibility(View.VISIBLE);
                            txt_emptyview.setVisibility(View.GONE);
                            FCD_Common.Loginnet_time=obj.getString("net_time");
                            lt_totalhrs.setText(FCD_Common.Loginnet_time);
                            for (int i = 0; i < dataArray.length(); i++) {
                                LoginDurationObject playerModel = new LoginDurationObject();
                                JSONObject product = dataArray.getJSONObject(i);
                                try {

                                    playerModel.setDate(product.getString("date"));
                                    playerModel.setDuty_count(product.getString("duty_count"));
                                    playerModel.setTotal_duration(product.getString("total_duration"));



                                    loginDurationObjects.add(playerModel);
                                    if (loginDurationObjects != null) {
                                        pastLoginInfoAdpater.visibleContentLayout();
                                        Log.d("fdghfdgfdgd","654hgfnjfgfghfghfghfg");
                                    }
                                    else {
                                        rv_pastLoginInformation.setVisibility(View.GONE);
                                        txt_emptyview.setVisibility(View.VISIBLE);
                                        Log.d("fdghfdgfdgd","243df4545");
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    Log.d("fdghfdgfdgd","65fghfgh"+ex);
                                }}
                        } else {
                            rv_pastLoginInformation.setVisibility(View.GONE);
                            txt_emptyview.setVisibility(View.VISIBLE);
                            Utils.stopProgressBar();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar("Hotseller" + e);
                        Utils.stopProgressBar();
                    }
                }, error -> {
            String error_value = String.valueOf(error);
            snackBar("Hotsellernew" + error_value);
            Utils.stopProgressBar();
        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", FCD_Common.token_type + " " + FCD_Common.access_token);
                Log.d("fghfghfghfg","fdhfgg"+FCD_Common.token_type + " " + FCD_Common.access_token);
                return params;
            }
        };
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(context).getApplicationContext());
            requestQueue.add(stringRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private class PastLoginInfoAdpater extends RecyclerView.Adapter<PastLoginInfoAdpater.ViewHolder>{
        private final ArrayList<LoginDurationObject> loginDurationObjects;

        boolean visible;
        PastLoginInfoAdpater(ArrayList<LoginDurationObject> loginDurationObjects) {

            this.loginDurationObjects = loginDurationObjects ;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_past_login_info_item, parent, false);
            return new ViewHolder(listItem);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            if (visible){
                @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date oneWayTripDate = input.parse(loginDurationObjects.get(position).getDate());
                    assert oneWayTripDate != null;
                    FCD_Common.todayDate= output.format(oneWayTripDate);
                    holder.lt_date.setText(FCD_Common.todayDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                holder.lt_totalTime.setText(loginDurationObjects.get(position).getTotal_duration());
                holder.lt_logincount.setText(getResources().getString(R.string.logincount)+" "+loginDurationObjects.get(position).getDuty_count());
            holder.ll_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putString("duty_date", loginDurationObjects.get(position).getDate());

                    FCD_particularLoginDuration particularLoginDuration = new FCD_particularLoginDuration();
                    particularLoginDuration.setArguments(bundle);
                    FragmentTransaction fragmentTransactionparticularLoginDuration = FCD_DashboardActivity.fragmentManager.beginTransaction();
                    fragmentTransactionparticularLoginDuration.add(R.id.content_frame, particularLoginDuration , "particularLoginDuration");
                    fragmentTransactionparticularLoginDuration.addToBackStack("loginDuration");
                    fragmentTransactionparticularLoginDuration.commit();
                    FCD_DashboardActivity.txt_toolbar.setText(R.string.login_duration);
                }
            });
            }

        }

        @Override
        public int getItemCount() {
            return loginDurationObjects.size();
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }
        void visibleContentLayout() {
            visible = true;
            notifyDataSetChanged();
        }
        class ViewHolder extends RecyclerView.ViewHolder {
            LoaderTextView lt_date,lt_totalTime,lt_logincount,lt_status;
            LinearLayout ll_login;
            ViewHolder(@NonNull View itemView)
            {
                super(itemView);
                ll_login = itemView.findViewById(R.id.ll_login);
                lt_date = itemView.findViewById(R.id.lt_date);
                lt_totalTime = itemView.findViewById(R.id.lt_totalTime);
                lt_logincount = itemView.findViewById(R.id.lt_logincount);

            }
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
