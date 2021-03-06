package foodcoup.driver.demand.FCDFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import foodcoup.driver.demand.FCDFragment.FCDWeeklyEarnings.FCD_WeeklyEarnings;
import foodcoup.driver.demand.FCDPojo.FCDTodaysEarnings.TodaysEarningsObject;
import foodcoup.driver.demand.FCDUtils.Loader.LoaderTextView;
import foodcoup.driver.demand.FCDViews.AC_Textview;
import foodcoup.driver.demand.FCDViews.FCD_Common;
import foodcoup.driver.demand.FCDViews.FCD_SharedPrefManager;
import foodcoup.driver.demand.FCDViews.FCD_URL;
import foodcoup.driver.demand.FCDViews.FCD_User;
import foodcoup.driver.demand.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FCD_TodaysEarnings extends AppCompatActivity {

    private RecyclerView rv_earningList ;
    private EarningsAdapter earningsAdapter ;
    private ArrayList<TodaysEarningsObject> todaysEarningsObjects = new ArrayList<>();
    private AC_Textview txt_toolbarOrderNo,txt_earnings ,txt_incentives,txt_emptyview;
    private LinearLayout ll_main;
    private LoaderTextView lt_mainTotal;
    private LinearLayout ll_incentiveLayout ;
    private Snackbar bar;
    private ImageView img_back,img_currentTask,img_notification;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fcd__todays_earnings);
        context = FCD_TodaysEarnings.this;

        FCD_User user = FCD_SharedPrefManager.getInstance(context).getUser();
        FCD_Common.id = String.valueOf(user.getid());
        FCD_Common.token_type = String.valueOf(user.gettoken_type());
        FCD_Common.access_token = String.valueOf(user.getaccess_token());


        FindViewById();

        earningsAdapter = new EarningsAdapter(todaysEarningsObjects);
        rv_earningList.setAdapter(earningsAdapter);
        rv_earningList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        dailyearnings();
        txt_earnings.setOnClickListener(view1 -> {

            rv_earningList.setVisibility(View.VISIBLE);
            //earningsAdapter.visibleContentLayout();
            txt_earnings.setTextColor(Objects.requireNonNull(context).getResources().getColor(R.color.white));
            txt_earnings.setBackground(Objects.requireNonNull(context).getResources().getDrawable(R.drawable.rec_bg_green_layout));
            txt_incentives.setTextColor(context.getResources().getColor(R.color.colordarkgrey));
            txt_incentives.setBackground(Objects.requireNonNull(context).getResources().getDrawable(R.drawable.rec_bg_stroke_green_layout));
            ll_incentiveLayout.setVisibility(View.GONE);
            dailyearnings();
        });


        txt_incentives.setOnClickListener(view12 -> {

            rv_earningList.setVisibility(View.GONE);
            txt_incentives.setTextColor(Objects.requireNonNull(context).getResources().getColor(R.color.white));
            txt_incentives.setBackground(Objects.requireNonNull(context).getResources().getDrawable(R.drawable.rec_bg_green_layout));
            txt_earnings.setTextColor(context.getResources().getColor(R.color.colordarkgrey));
            txt_earnings.setBackground(Objects.requireNonNull(context).getResources().getDrawable(R.drawable.rec_bg_stroke_green_layout));
            ll_incentiveLayout.setVisibility(View.VISIBLE);
        });

        img_currentTask.setOnClickListener(v -> {
            Intent intent =new Intent(FCD_TodaysEarnings.this, FCD_DashboardActivity.class);
            startActivity(intent);
        });
        img_back.setOnClickListener(v -> onBackPressed());
    }

    private void FindViewById() {

        ll_main = findViewById(R.id.ll_main);
        rv_earningList = findViewById(R.id.rv_earningList);
        txt_earnings = findViewById(R.id.txt_earnings);
        txt_incentives = findViewById(R.id.txt_incentives);

        ll_incentiveLayout = findViewById(R.id.ll_incentiveLayout);
        lt_mainTotal = findViewById(R.id.lt_mainTotal);
        txt_emptyview = findViewById(R.id.txt_emptyview);

        txt_toolbarOrderNo = findViewById(R.id.txt_toolbarOrderNo);
        img_currentTask = findViewById(R.id.img_currentTask);
        img_notification = findViewById(R.id.img_notification);
        img_back = findViewById(R.id.img_back);
        img_back.setVisibility(View.VISIBLE);
        img_notification.setVisibility(View.GONE);
        img_currentTask.setVisibility(View.VISIBLE);
        txt_toolbarOrderNo.setVisibility(View.VISIBLE);
        txt_toolbarOrderNo.setText(getResources().getString(R.string.today_earning));

        TodaysEarningsObject earningsObject = new TodaysEarningsObject();
        earningsObject.setD_image("");
        earningsObject.setD_name("");
        todaysEarningsObjects.add(earningsObject);
        earningsObject.setD_image("");
        earningsObject.setD_name("");
        todaysEarningsObjects.add(earningsObject);
        earningsObject.setD_image("");
        earningsObject.setD_name("");
        todaysEarningsObjects.add(earningsObject);

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
    private void  dailyearnings() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FCD_URL.URL_TODAYEARNING,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        Log.d("fdghfdgfdgd","fdgfd"+obj);
                        if (obj.optString("success").equals("1")) {
                            JSONArray dataArray = obj.getJSONArray("data");
                            Log.d("fghfghfg","fhfghfg"+dataArray);

                            FCD_Common.todayTotal=obj.getString("total");
                            FCD_Common.todaycurrency=obj.getString("currency");
                            todaysEarningsObjects.clear();
                            rv_earningList.setVisibility(View.VISIBLE);
                            txt_emptyview.setVisibility(View.GONE);
                            lt_mainTotal.setText(FCD_Common.todaycurrency+" "+FCD_Common.todayTotal);
                            for (int i = 0; i < dataArray.length(); i++) {
                                TodaysEarningsObject playerModel = new TodaysEarningsObject();
                                JSONObject product = dataArray.getJSONObject(i);
                                try {

                                    playerModel.setId(product.getString("id"));
                                    playerModel.setRestaurant_name(product.getString("restaurant_name"));
                                    playerModel.setName(product.getString("name"));
                                    playerModel.setDelivery_charge(product.getString("delivery_charge"));
                                    playerModel.setStatus(product.getString("status"));
                                    playerModel.setCreated_at(product.getString("created_at"));


                                    todaysEarningsObjects.add(playerModel);
                                    if (todaysEarningsObjects != null) {
                                        earningsAdapter.visibleContentLayout();
                                        Log.d("fdghfdgfdgd","654hgfnjfgfghfghfghfg");
                                    }
                                    else {
                                        rv_earningList.setVisibility(View.GONE);
                                        txt_emptyview.setVisibility(View.VISIBLE);
                                        Log.d("fdghfdgfdgd","243df4545");
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    Log.d("fdghfdgfdgd","65fghfgh"+ex);
                                }}
                        } else {
                            rv_earningList.setVisibility(View.GONE);
                            txt_emptyview.setVisibility(View.VISIBLE);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar("Hotseller" + e);
                    }
                }, error -> {
            String error_value = String.valueOf(error);
            snackBar("Hotsellernew" + error_value);
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
    private static class EarningsAdapter extends RecyclerView.Adapter<EarningsAdapter.ViewHolder>{
        private final ArrayList<TodaysEarningsObject> todaysEarningsObjects;
        boolean visible;
        EarningsAdapter(ArrayList<TodaysEarningsObject> todaysEarningsObjects) {

            this.todaysEarningsObjects = todaysEarningsObjects ;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_todays_earings_item, parent, false);
            return new ViewHolder(listItem);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            if (visible) {

                Log.d("fdghfdgfdgd","fdh546ydfg");

                holder.viewLine.setVisibility(View.INVISIBLE);
                holder.lt_restaurantName.setText(todaysEarningsObjects.get(position).getRestaurant_name());
                holder.lt_dateTime.setText(todaysEarningsObjects.get(position).getCreated_at());
                holder.lt_status.setText(todaysEarningsObjects.get(position).getStatus());

                @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                try {
                    Date oneWayTripDate = input.parse(todaysEarningsObjects.get(position).getCreated_at());
                    assert oneWayTripDate != null;
                    FCD_Common.todayDate= output.format(oneWayTripDate);
                    holder.lt_dateTime.setText(FCD_Common.todayDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                holder.lt_currency.setText(FCD_Common.todaycurrency+" "+todaysEarningsObjects.get(position).getDelivery_charge());
            }
            else {

                Log.d("fdghfdgfdgd","fghfghfghfg");
            }
        }

        @Override
        public int getItemCount() {
            return todaysEarningsObjects.size();
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

        static class ViewHolder extends RecyclerView.ViewHolder {
            View viewLine ;
            LoaderTextView lt_restaurantName,lt_dateTime,lt_currency,lt_status;
            ViewHolder(@NonNull View itemView) {
                super(itemView);

                viewLine = itemView.findViewById(R.id.viewLine);
                lt_restaurantName = itemView.findViewById(R.id.lt_restaurantName);
                lt_dateTime = itemView.findViewById(R.id.lt_dateTime);
                lt_currency = itemView.findViewById(R.id.lt_currency);
                lt_status = itemView.findViewById(R.id.lt_status);
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
