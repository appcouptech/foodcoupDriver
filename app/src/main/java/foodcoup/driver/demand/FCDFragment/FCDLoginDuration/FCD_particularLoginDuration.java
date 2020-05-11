package foodcoup.driver.demand.FCDFragment.FCDLoginDuration;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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
import foodcoup.driver.demand.FCDPojo.FCDLoginDuration.ParticularLoginDurationObject;
import foodcoup.driver.demand.FCDUtils.Loader.LoaderTextView;
import foodcoup.driver.demand.FCDViews.FCD_Common;
import foodcoup.driver.demand.FCDViews.FCD_SharedPrefManager;
import foodcoup.driver.demand.FCDViews.FCD_URL;
import foodcoup.driver.demand.FCDViews.FCD_User;
import foodcoup.driver.demand.FCDViews.Utils;
import foodcoup.driver.demand.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FCD_particularLoginDuration extends Fragment {

    private Snackbar bar;
    private RecyclerView rv_loginDuration ;
   private LinearLayout ll_main;
   private LoaderTextView lt_totalhrs;
   private Context context;
    private  LoginDuartionAdapter loginDuartionAdapter ;
    private ArrayList<ParticularLoginDurationObject> particularLoginDurationObjects = new ArrayList<>();

    public FCD_particularLoginDuration() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fcd_particular_login_duration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        context = getActivity();
        FCD_User user = FCD_SharedPrefManager.getInstance(context).getUser();
        FCD_Common.id = String.valueOf(user.getid());
        FCD_Common.token_type = String.valueOf(user.gettoken_type());
        FCD_Common.access_token = String.valueOf(user.getaccess_token());

        Bundle object = getArguments();

        if (object != null) {
            FCD_Common.duty_date = object.getString("duty_date");
            Log.d("dfhdfhfdg","dfgfd"+FCD_Common.duty_date);
        }
        findViewById(view);

        loginDuartionAdapter = new LoginDuartionAdapter(particularLoginDurationObjects);
        rv_loginDuration.setAdapter(loginDuartionAdapter);
        rv_loginDuration.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        LoginList();
    }

    private void findViewById(View view) {
        rv_loginDuration = view.findViewById(R.id.rv_loginDuration);
        lt_totalhrs = view.findViewById(R.id.lt_totalhrs);
        ll_main = view.findViewById(R.id.ll_main);

        ParticularLoginDurationObject durationObject = new ParticularLoginDurationObject();
        durationObject.setD_image("");
        durationObject.setD_name("");
        particularLoginDurationObjects.add(durationObject);
        durationObject.setD_image("");
        durationObject.setD_name("");
        particularLoginDurationObjects.add(durationObject);



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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FCD_URL.URL_LOGINLISTDETAIL,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        Log.d("fdghfdgfdgd","fdgfd"+obj);
                        if (obj.optString("success").equals("1")) {
                            JSONArray dataArray = obj.getJSONArray("data");
                            Log.d("fghfghfg","fhfghfg"+dataArray);
                            Utils.stopProgressBar();
                            particularLoginDurationObjects.clear();

                            rv_loginDuration.setVisibility(View.VISIBLE);
                            FCD_Common.LoginHRS=obj.getString("total_time");

                           // txt_emptyview.setVisibility(View.GONE);
                             lt_totalhrs.setText(FCD_Common.LoginHRS);
                            for (int i = 0; i < dataArray.length(); i++) {
                                ParticularLoginDurationObject playerModel = new ParticularLoginDurationObject();
                                JSONObject product = dataArray.getJSONObject(i);
                                try {

                                    playerModel.setTotal_order(product.getString("total_order"));
                                    playerModel.setStart_time(product.getString("start_time"));
                                    playerModel.setEnd_time(product.getString("end_time"));



                                    particularLoginDurationObjects.add(playerModel);
                                    if (particularLoginDurationObjects != null) {
                                        loginDuartionAdapter.visibleContentLayout();
                                        Log.d("fdghfdgfdgd","654hgfnjfgfghfghfghfg");
                                    }
                                    else {
                                        rv_loginDuration.setVisibility(View.GONE);
                                       // txt_emptyview.setVisibility(View.VISIBLE);
                                        Log.d("fdghfdgfdgd","243df4545");
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    Log.d("fdghfdgfdgd","65fghfgh"+ex);
                                }}
                        } else {
                            rv_loginDuration.setVisibility(View.GONE);
                           // txt_emptyview.setVisibility(View.VISIBLE);
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("duty_date", FCD_Common.duty_date);
                return params;
            }

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

    private class LoginDuartionAdapter extends RecyclerView.Adapter<LoginDuartionAdapter.ViewHolder>{
        private final ArrayList<ParticularLoginDurationObject> particularLoginDurationObjects;

        boolean visible;
        LoginDuartionAdapter(ArrayList<ParticularLoginDurationObject> particularLoginDurationObjects) {

            this.particularLoginDurationObjects = particularLoginDurationObjects ;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_login_duration_item, parent, false);
            return new ViewHolder(listItem);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            if (visible){
                holder.lt_totalOrder.setText(particularLoginDurationObjects.get(position).getTotal_order());
                holder.lt_startTime.setText(particularLoginDurationObjects.get(position).getStart_time());
                holder.lt_endTime.setText(particularLoginDurationObjects.get(position).getEnd_time());


            }

        }

        @Override
        public int getItemCount() {
            return particularLoginDurationObjects.size();
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
            LoaderTextView lt_totalOrder,lt_startTime,lt_endTime;
            LinearLayout ll_login;
            ViewHolder(@NonNull View itemView)
            {
                super(itemView);
                lt_totalOrder = itemView.findViewById(R.id.lt_totalOrder);
                lt_startTime = itemView.findViewById(R.id.lt_startTime);
                lt_endTime = itemView.findViewById(R.id.lt_endTime);
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
