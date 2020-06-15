package foodcoup.driver.demand.FCDFragment.FCDWeeklyEarnings;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import foodcoup.driver.demand.FCDActivity.FCDDashboardActivity.FCD_DashboardActivity;
import foodcoup.driver.demand.FCDPojo.FCDWeeklyEarnings.WeeklyEarningsObject;
import foodcoup.driver.demand.FCDUtils.Loader.LoaderTextView;
import foodcoup.driver.demand.FCDViews.AC_Textview;
import foodcoup.driver.demand.FCDViews.FCD_Common;
import foodcoup.driver.demand.FCDViews.FCD_SharedPrefManager;
import foodcoup.driver.demand.FCDViews.FCD_URL;
import foodcoup.driver.demand.FCDViews.FCD_User;
import foodcoup.driver.demand.FCDViews.Utils;
import foodcoup.driver.demand.FCDWallet.FCD_Wallet;
import foodcoup.driver.demand.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FCD_WeeklyEarnings extends AppCompatActivity {

    private WeeklyEarningAdapter weeklyEarningAdapter;
    private RecyclerView rv_weekEarnings ;
    private ArrayList<WeeklyEarningsObject> weeklyEarningsObjects = new ArrayList<>();
    private LinearLayout ll_thisWeek,ll_main;
    private int mYear, mMonth, mDay;
    private Snackbar bar;
    private LoaderTextView lt_mainTotal;
    private AC_Textview txt_toolbarOrderNo,txt_wkdate,txt_toDate,txt_fromDate,txt_emptyview;
    private static String compareFromDate;
    private ImageView img_back,img_currentTask,img_notification;
    private static String compareToDate;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fcd__weekly_earnings);
        context = FCD_WeeklyEarnings.this;

        FCD_User user = FCD_SharedPrefManager.getInstance(context).getUser();
        FCD_Common.id = String.valueOf(user.getid());
        FCD_Common.token_type = String.valueOf(user.gettoken_type());
        FCD_Common.access_token = String.valueOf(user.getaccess_token());


        FindViewById();

        weeklyEarningAdapter = new WeeklyEarningAdapter( weeklyEarningsObjects);
        rv_weekEarnings.setAdapter(weeklyEarningAdapter);
        rv_weekEarnings.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        ll_thisWeek.setOnClickListener(view1 -> {

            FCD_ParticularWeekEarnings particularWeekEarnings = new FCD_ParticularWeekEarnings();
            FragmentTransaction fragmentTransactionparticularWeekEarnings= FCD_DashboardActivity.fragmentManager.beginTransaction();
            fragmentTransactionparticularWeekEarnings.add(R.id.content_frame, particularWeekEarnings,"particularWeekEarnings");
            fragmentTransactionparticularWeekEarnings.commit();
            fragmentTransactionparticularWeekEarnings.addToBackStack("weeklyEarnings");
            FCD_DashboardActivity.txt_toolbar.setText(R.string.week_earning);
        });

        datemethod();
        dailyearnings();

        img_currentTask.setOnClickListener(v -> {
            Intent intent =new Intent(FCD_WeeklyEarnings.this, FCD_DashboardActivity.class);
            startActivity(intent);
        });
        img_back.setOnClickListener(v -> onBackPressed());
    }

    private void FindViewById() {

        ll_main = findViewById(R.id.ll_main);
        txt_wkdate = findViewById(R.id.txt_wkdate);
        txt_toDate = findViewById(R.id.txt_toDate);
        txt_fromDate = findViewById(R.id.txt_fromDate);
        rv_weekEarnings = findViewById(R.id.rv_weekEarnings);
        txt_emptyview = findViewById(R.id.txt_emptyview);
        lt_mainTotal = findViewById(R.id.lt_mainTotal);
        ll_thisWeek = findViewById(R.id.ll_thisWeek);

        txt_toolbarOrderNo = findViewById(R.id.txt_toolbarOrderNo);
        img_currentTask = findViewById(R.id.img_currentTask);
        img_notification = findViewById(R.id.img_notification);
        img_back = findViewById(R.id.img_back);
        img_back.setVisibility(View.VISIBLE);
        img_notification.setVisibility(View.GONE);
        img_currentTask.setVisibility(View.VISIBLE);
        txt_toolbarOrderNo.setVisibility(View.VISIBLE);
        txt_toolbarOrderNo.setText(getResources().getString(R.string.week_earning));

        WeeklyEarningsObject earningsObject = new WeeklyEarningsObject();
        earningsObject.setD_image("");
        earningsObject.setD_name("");
        weeklyEarningsObjects.add(earningsObject);
        earningsObject.setD_image("");
        earningsObject.setD_name("");
        weeklyEarningsObjects.add(earningsObject);
        earningsObject.setD_image("");
        earningsObject.setD_name("");
        weeklyEarningsObjects.add(earningsObject);

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
        Utils.playProgressBar(FCD_WeeklyEarnings.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FCD_URL.URL_WEEKEARNING,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        Log.d("fdghfdgfdgd","fdgfd"+obj);
                        if (obj.optString("success").equals("1")) {
                            JSONArray dataArray = obj.getJSONArray("data");
                            Log.d("fghfghfg","fhfghfg"+dataArray);

                            Utils.stopProgressBar();
                            FCD_Common.WeeklyTotal=obj.getString("total");
                            FCD_Common.Weeklycurrency=obj.getString("currency");
                            weeklyEarningsObjects.clear();
                            rv_weekEarnings.setVisibility(View.VISIBLE);
                            txt_emptyview.setVisibility(View.GONE);
                            lt_mainTotal.setText(FCD_Common.Weeklycurrency+" "+FCD_Common.WeeklyTotal);
                            for (int i = 0; i < dataArray.length(); i++) {
                                WeeklyEarningsObject playerModel = new WeeklyEarningsObject();
                                JSONObject product = dataArray.getJSONObject(i);
                                try {

                                    playerModel.setCreated(product.getString("date"));
                                    playerModel.setCount(product.getString("order_count"));
                                    playerModel.setTotal(product.getString("total_charge"));

                                    weeklyEarningsObjects.add(playerModel);
                                    if (weeklyEarningsObjects != null) {
                                        weeklyEarningAdapter.visibleContentLayout();
                                        Log.d("fdghfdgfdgd","654hgfnjfgfghfghfghfg");
                                    }
                                    else {
                                        Utils.stopProgressBar();
                                        rv_weekEarnings.setVisibility(View.GONE);
                                        txt_emptyview.setVisibility(View.VISIBLE);
                                        Log.d("fdghfdgfdgd","243df4545");
                                    }
                                } catch (Exception ex) {
                                    Utils.stopProgressBar();
                                    ex.printStackTrace();
                                    Log.d("fdghfdgfdgd","65fghfgh"+ex);
                                }}
                        } else {
                            Utils.stopProgressBar();
                            rv_weekEarnings.setVisibility(View.GONE);
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("from_date", FCD_Common.DateFromItem);
                params.put("to_date", FCD_Common.DateToItem);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                Log.d("fdgdfgfdg", "sdfgsdgs" + FCD_Common.token_type + " " + FCD_Common.access_token);
                params.put("Authorization", FCD_Common.token_type + " " + FCD_Common.access_token);
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

    private class WeeklyEarningAdapter extends RecyclerView.Adapter<WeeklyEarningAdapter.ViewHolder>{
        private final ArrayList<WeeklyEarningsObject> weeklyEarningsObjects;
       boolean visible;
        WeeklyEarningAdapter(ArrayList<WeeklyEarningsObject> weeklyEarningsObjects) {
            this.weeklyEarningsObjects = weeklyEarningsObjects ;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_weekly_earings_item, parent, false);
            return new ViewHolder(listItem);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
           // holder.lt_date.setText(weeklyEarningsObjects.get(position).getCreated());


            if (visible) {
                holder.lt_count.setText(getResources().getString(R.string.driverdelivered)+" "+weeklyEarningsObjects.get(position).getCount());
                holder.lt_currency.setText(FCD_Common.Weeklycurrency+" "+weeklyEarningsObjects.get(position).getTotal());
                Log.d("dfghfdgfd", "dfgdfg" + weeklyEarningsObjects.get(position).getCreated());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");
            try {

                Date oneWayTripDate = input.parse(weeklyEarningsObjects.get(position).getCreated());
                FCD_Common.todayDate_check= output.format(oneWayTripDate);
                holder.lt_date.setText(FCD_Common.todayDate_check);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            }
        }

        @Override
        public int getItemCount() {
            return weeklyEarningsObjects.size();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }
        void visibleContentLayout() {
            visible = true;
            notifyDataSetChanged();
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            LoaderTextView lt_date,lt_currency,lt_count;
            ViewHolder(@NonNull View itemView) {
                super(itemView);
                lt_date = itemView.findViewById(R.id.lt_date);
                lt_currency = itemView.findViewById(R.id.lt_currency);
                lt_count = itemView.findViewById(R.id.lt_count);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void datemethod() {
        try {
            @SuppressLint("SimpleDateFormat")
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = new Date();
            String mtydate = dateFormat.format(date);
            txt_toDate.setText(dateFormat.format(date));
            compareToDate=dateFormat.format(date);
            Date myDate = dateFormat.parse(mtydate);
            Calendar calendar = Calendar.getInstance();
            assert myDate != null;
            calendar.setTime(myDate);
            calendar.add(Calendar.DAY_OF_YEAR, -7);
            Date newDate = calendar.getTime();
            String datee = dateFormat.format(newDate);
            txt_fromDate.setText(datee);
            compareFromDate=dateFormat.format(newDate);
            final String day =datee.substring(0,2);
            final int n1= Integer.parseInt(day);
            String month =datee.substring(3,5);
            final int nn2= Integer.parseInt((month));
            final int newmonth=nn2-1;
            String year =datee.substring(6,10);
            final int n3= Integer.parseInt(year);
            txt_fromDate.setOnClickListener(v -> {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(context),
                        (view, year12, monthOfYear, dayOfMonth) -> {
                            compareFromDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year12;
                            FCD_Common.DateFromItem= year12 +"-"+ (monthOfYear + 1)+"-"+dayOfMonth;
                            //progressbar.setVisibility(View.VISIBLE);
                            compare_date();
                        }, n3, newmonth, n1);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            });
            txt_toDate.setOnClickListener(v -> {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(context),
                        (view, year1, monthOfYear, dayOfMonth) -> {
                            compareToDate=dayOfMonth+"-"+(monthOfYear + 1)+"-"+ year1;
                            FCD_Common.DateToItem= year1 +"-"+ (monthOfYear + 1)+"-"+dayOfMonth;
                            compare_date();
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            });
            String date_from = txt_fromDate.getText().toString();
            String date1 = txt_toDate.getText().toString();

            txt_wkdate.setText(date_from+" - "+date1);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat input = new SimpleDateFormat("dd-MM-yy");
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat input1 = new SimpleDateFormat("dd-MM-yy");
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat output1 = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date oneWayTripDate = input.parse(date_from);
                assert oneWayTripDate != null;
                FCD_Common.DateFromItem = output.format(oneWayTripDate);
                Date oneWayTripDate1 = input1.parse(date1);
                assert oneWayTripDate1 != null;
                FCD_Common.DateToItem = output1.format(oneWayTripDate1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private void compare_date() {
        try{
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date date1 = sdf.parse(compareFromDate);
            Date date2 = sdf.parse(compareToDate);
            String value;
            assert date1 != null;
            if(date1.after(date2)){
                value ="Date Mismatches";
                snackBar(value);
                //progressbar.setVisibility(View.GONE);
            }
            if(date1.before(date2)) {
                txt_fromDate.setText(compareFromDate);
                txt_toDate.setText(compareToDate);

                txt_wkdate.setText(compareFromDate+" - "+compareToDate);
                Log.d("fghfghfgh","DateFromItem"+FCD_Common.DateFromItem);
                Log.d("fghfghfgh","DateToItem"+FCD_Common.DateToItem);
                dailyearnings();
                //progressbar.setVisibility(View.VISIBLE);
            }
            if(date1.equals(date2)){
                txt_fromDate.setText(compareFromDate);
                txt_toDate.setText(compareToDate);
                txt_wkdate.setText(compareFromDate+" - "+compareToDate);
                Log.d("fghfghfgh","DateFromItem"+FCD_Common.DateFromItem);
                Log.d("fghfghfgh","DateToItem"+FCD_Common.DateToItem);
                dailyearnings();
            }





        }catch(ParseException ex){
            ex.printStackTrace();
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
