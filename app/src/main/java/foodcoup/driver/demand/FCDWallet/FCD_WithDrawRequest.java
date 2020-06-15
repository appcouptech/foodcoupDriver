package foodcoup.driver.demand.FCDWallet;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import foodcoup.driver.demand.FCDPojo.FCDWallet.WalletObject;
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
public class FCD_WithDrawRequest extends AppCompatActivity {

    private EarningsAdapter earningsAdapter ;
    private RecyclerView rv_weekEarnings ;
    private ArrayList<WalletObject> WalletObjects = new ArrayList<>();
    private Snackbar bar;
    private int check_date1,check_date2;
    private int dialogleft = 1;
    private int dialogright = 1;
    private int mYear, mMonth, mDay;
    private Context context;
    private ImageView img_right,img_left;
    private AC_Textview txt_toDate,txt_fromDate,txt_emptyview,txtchgdate1;
    private static String compareFromDate;
    private static String compareToDate;
    private LinearLayout ll_mainlayout,lin_dately,lin_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fcd__weekly_earnings);
        context = FCD_WithDrawRequest.this;
        FCD_User user = FCD_SharedPrefManager.getInstance(context).getUser();
        FCD_Common.id = String.valueOf(user.getid());
        FCD_Common.token_type = String.valueOf(user.gettoken_type());
        FCD_Common.access_token = String.valueOf(user.getaccess_token());

        FindViewById();
        earningsAdapter = new EarningsAdapter( WalletObjects);
        rv_weekEarnings.setAdapter(earningsAdapter);
        rv_weekEarnings.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        datemethod1();
        walletAmount();
        //dialogcounter = dialogcounter + 1;

        img_left.setOnClickListener(v -> {

            dialogleft = dialogleft + 1;
            check_date1=Integer.parseInt("-"+dialogleft);
            Log.d("sdfgsdfsd","sfsasdfsd"+check_date1);
            datemethod1();
        });
        img_right.setOnClickListener(v -> {

            dialogright = dialogright + 1;
           // datemethod2();
            datemethod1();
            check_date1=Integer.parseInt("+"+dialogright);
            Log.d("sdfgsdfsd","sdfsd"+check_date2);
        });
    }

    private void FindViewById() {

        txtchgdate1 = findViewById(R.id.txtchgdate1);
        img_left = findViewById(R.id.img_left);
        img_right = findViewById(R.id.img_right);
        lin_date = findViewById(R.id.lin_date);
        lin_dately = findViewById(R.id.lin_dately);
        ll_mainlayout = findViewById(R.id.ll_mainlayout);
        LinearLayout ll_thisWeek = findViewById(R.id.ll_thisWeek);
        ll_thisWeek.setVisibility(View.GONE);
        lin_date.setVisibility(View.GONE);
        lin_dately.setVisibility(View.VISIBLE);
        txt_toDate = findViewById(R.id.txt_toDate);
        txt_fromDate = findViewById(R.id.txt_fromDate);
        rv_weekEarnings = findViewById(R.id.rv_weekEarnings);
        rv_weekEarnings.setBackground(getResources().getDrawable(R.drawable.rec_bg_stroke_green_layout));
        txt_emptyview = findViewById(R.id.txt_emptyview);
       // lt_mainTotal = findViewById(R.id.lt_mainTotal);

        WalletObject earningsObject = new WalletObject();
        earningsObject.setD_image("");
        earningsObject.setD_name("");
        WalletObjects.add(earningsObject);
        earningsObject.setD_image("");
        earningsObject.setD_name("");
        WalletObjects.add(earningsObject);
        earningsObject.setD_image("");
        earningsObject.setD_name("");
        WalletObjects.add(earningsObject);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    private void  walletAmount() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST , FCD_URL.URL_WALLETHISTORY,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        Log.d("fdghfdgfdgd","fdgfd"+obj);
                        if (obj.optString("success").equals("1")) {
                            JSONArray dataArray = obj.getJSONArray("data");
                            Log.d("fghfghfg","fhfghfg"+dataArray);

                            FCD_Common.WalletCurrency=obj.getString("currency");
                            WalletObjects.clear();
                            rv_weekEarnings.setVisibility(View.VISIBLE);
                            txt_emptyview.setVisibility(View.GONE);
                            //lt_mainTotal.setText(FCD_Common.todaycurrency+" "+FCD_Common.todayTotal);
                            for (int i = 0; i < dataArray.length(); i++) {
                                WalletObject playerModel = new WalletObject();
                                JSONObject product = dataArray.getJSONObject(i);
                                try {

                                    playerModel.setId(product.getString("id"));
                                    playerModel.setAmount(product.getString("amount"));
                                    playerModel.setTrans_mode(product.getString("trans_mode"));
                                    playerModel.setTrans_id(product.getString("trans_id"));
                                    playerModel.setStatus(product.getString("status"));
                                    playerModel.setCreated_at(product.getString("created_at"));
                                    playerModel.setNote(product.getString("note"));


                                    WalletObjects.add(playerModel);
                                    if (WalletObjects != null) {
                                        earningsAdapter.visibleContentLayout();
                                        Log.d("fdghfdgfdgd","654hgfnjfgfghfghfghfg");
                                    }
                                    else {
                                        txt_emptyview.setVisibility(View.GONE);
                                        txt_emptyview.setVisibility(View.VISIBLE);
                                        Log.d("fdghfdgfdgd","243df4545");
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    Log.d("fdghfdgfdgd","65fghfgh"+ex);
                                }}
                        } else {
                            txt_emptyview.setVisibility(View.GONE);
                            txt_emptyview.setVisibility(View.VISIBLE);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //Utils.snackBar(context,);
                    }
                }, error -> {
            //   String error_value = String.valueOf(error);
            // snackBar("Hotsellernew" + error_value);
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("start_date", FCD_Common.DateWithdrawToItem);
                params.put("end_date", FCD_Common.DateWithdrawToItem);
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
    private class EarningsAdapter extends RecyclerView.Adapter<EarningsAdapter.ViewHolder>{
        private final ArrayList<WalletObject> WalletObjects;
        boolean visible;
        EarningsAdapter(ArrayList<WalletObject> WalletObjects) {

            this.WalletObjects = WalletObjects ;
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
                // holder.lt_restaurantName.setText(WalletObjects.get(position).getRestaurant_name());
                holder.lt_dateTime.setText(WalletObjects.get(position).getCreated_at());
                holder.lt_status.setText(WalletObjects.get(position).getStatus());
                holder.lt_restaurantName.setText(WalletObjects.get(position).getTrans_mode());
                if (WalletObjects.get(position).getTrans_mode().equalsIgnoreCase("RECHARGE"))
                {
                    holder.ll_main.setBackgroundColor(getResources().getColor(R.color.pale_green));
                    holder.lt_restaurantName.setTextColor(getResources().getColor(R.color.black));
                    holder.lt_currency.setTextColor(getResources().getColor(R.color.black));
                }
                else if(WalletObjects.get(position).getTrans_mode().equalsIgnoreCase("DEBITED"))
                {
                    holder.ll_main.setBackgroundColor(getResources().getColor(R.color.pale_red));
                    holder.lt_restaurantName.setTextColor(getResources().getColor(R.color.black));
                    holder.lt_currency.setTextColor(getResources().getColor(R.color.black));
                }
                else if(WalletObjects.get(position).getTrans_mode().equalsIgnoreCase("CREDITED"))
                {
                    holder.ll_main.setBackgroundColor(getResources().getColor(R.color.pale_blue));
                    holder.lt_restaurantName.setTextColor(getResources().getColor(R.color.black));
                    holder.lt_currency.setTextColor(getResources().getColor(R.color.black));
                }
                else {
                    holder.ll_main.setBackgroundColor(getResources().getColor(R.color.pale_blue));
                    holder.lt_restaurantName.setTextColor(getResources().getColor(R.color.black));
                    holder.lt_currency.setTextColor(getResources().getColor(R.color.black));
                }

                @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                try {
                    Date oneWayTripDate = input.parse(WalletObjects.get(position).getCreated_at());
                    assert oneWayTripDate != null;
                    FCD_Common.todayDate= output.format(oneWayTripDate);
                    holder.lt_dateTime.setText(FCD_Common.todayDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // holder.lt_currency.setText(FCD_Common.todaycurrency+" "+WalletObjects.get(position).getDelivery_charge());
                holder.lt_currency.setText(FCD_Common.WalletCurrency+" "+WalletObjects.get(position).getAmount());
            }
            else {

                Log.d("fdghfdgfdgd","fghfghfghfg");
            }
        }

        @Override
        public int getItemCount() {
            return WalletObjects.size();
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
            View viewLine ;
            LinearLayout ll_main;
            LoaderTextView lt_restaurantName,lt_dateTime,lt_currency,lt_status;
            ViewHolder(@NonNull View itemView) {
                super(itemView);

                viewLine = itemView.findViewById(R.id.viewLine);
                ll_main = itemView.findViewById(R.id.ll_main);
                lt_restaurantName = itemView.findViewById(R.id.lt_restaurantName);
                lt_dateTime = itemView.findViewById(R.id.lt_dateTime);
                lt_currency = itemView.findViewById(R.id.lt_currency);
                lt_status = itemView.findViewById(R.id.lt_status);
            }
        }
    }

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
                DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(FCD_WithDrawRequest.this),
                        (view, year12, monthOfYear, dayOfMonth) -> {
                            compareFromDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year12;
                            FCD_Common.DateWithdrawFromItem= year12 +"-"+ (monthOfYear + 1)+"-"+dayOfMonth;
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(FCD_WithDrawRequest.this),
                        (view, year1, monthOfYear, dayOfMonth) -> {
                            compareToDate=dayOfMonth+"-"+(monthOfYear + 1)+"-"+ year1;
                            FCD_Common.DateWithdrawToItem= year1 +"-"+ (monthOfYear + 1)+"-"+dayOfMonth;
                            compare_date();
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            });
            String date_from = txt_fromDate.getText().toString();
            String date1 = txt_toDate.getText().toString();
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
                FCD_Common.DateWithdrawFromItem = output.format(oneWayTripDate);
                Date oneWayTripDate1 = input1.parse(date1);
                assert oneWayTripDate1 != null;
                FCD_Common.DateWithdrawToItem = output1.format(oneWayTripDate1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    private void datemethod1() {
        try {
            @SuppressLint("SimpleDateFormat")
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = new Date();
            String mtydate = dateFormat.format(date);
            txtchgdate1.setText(dateFormat.format(date));
            compareToDate=dateFormat.format(date);
            Date myDate = dateFormat.parse(mtydate);
            Calendar calendar = Calendar.getInstance();
            assert myDate != null;
            calendar.setTime(myDate);
            calendar.add(Calendar.DAY_OF_YEAR, check_date1);
            Date newDate = calendar.getTime();
            String datee = dateFormat.format(newDate);
            txtchgdate1.setText(datee);
            compareFromDate=dateFormat.format(newDate);
            final String day =datee.substring(0,2);
            final int n1= Integer.parseInt(day);
            String month =datee.substring(3,5);
            final int nn2= Integer.parseInt((month));
            final int newmonth=nn2-1;
            String year =datee.substring(6,10);
            final int n3= Integer.parseInt(year);

              /*  final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(FCD_WithDrawRequest.this),
                        (view, year12, monthOfYear, dayOfMonth) -> {
                            compareFromDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year12;
                            FCD_Common.DateWithdrawFromItem= year12 +"-"+ (monthOfYear + 1)+"-"+dayOfMonth;
                            FCD_Common.DateWithdrawToItem= year12 +"-"+ (monthOfYear + 1)+"-"+dayOfMonth;
                            //progressbar.setVisibility(View.VISIBLE);
                            compare_date();
                        }, n3, newmonth, n1);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();*/




            String date_from = txtchgdate1.getText().toString();
            String date1 = txtchgdate1.getText().toString();
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
                FCD_Common.DateWithdrawFromItem = output.format(oneWayTripDate);
                Date oneWayTripDate1 = input1.parse(date1);
                assert oneWayTripDate1 != null;
                FCD_Common.DateWithdrawToItem = output1.format(oneWayTripDate1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    private void datemethod2() {
        try {
            @SuppressLint("SimpleDateFormat")
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = new Date();
            String mtydate = dateFormat.format(date);
            txtchgdate1.setText(dateFormat.format(date));
            compareToDate=dateFormat.format(date);
            Date myDate = dateFormat.parse(mtydate);
            Calendar calendar = Calendar.getInstance();
            assert myDate != null;
            calendar.setTime(myDate);
            calendar.add(Calendar.DAY_OF_YEAR, check_date2);
            Date newDate = calendar.getTime();
            String datee = dateFormat.format(newDate);
            txtchgdate1.setText(datee);
            compareFromDate=dateFormat.format(newDate);
            final String day =datee.substring(0,2);
            final int n1= Integer.parseInt(day);
            String month =datee.substring(3,5);
            final int nn2= Integer.parseInt((month));
            final int newmonth=nn2-1;
            String year =datee.substring(6,10);
            final int n3= Integer.parseInt(year);

              /*  final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(FCD_WithDrawRequest.this),
                        (view, year12, monthOfYear, dayOfMonth) -> {
                            compareFromDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year12;
                            FCD_Common.DateWithdrawFromItem= year12 +"-"+ (monthOfYear + 1)+"-"+dayOfMonth;
                            FCD_Common.DateWithdrawToItem= year12 +"-"+ (monthOfYear + 1)+"-"+dayOfMonth;
                            //progressbar.setVisibility(View.VISIBLE);
                            compare_date();
                        }, n3, newmonth, n1);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();*/




            String date_from = txtchgdate1.getText().toString();
            String date1 = txtchgdate1.getText().toString();
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
                FCD_Common.DateWithdrawFromItem = output.format(oneWayTripDate);
                Date oneWayTripDate1 = input1.parse(date1);
                assert oneWayTripDate1 != null;
                FCD_Common.DateWithdrawToItem = output1.format(oneWayTripDate1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }



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
                Log.d("fghfghfgh","DateWithdrawFromItem"+FCD_Common.DateWithdrawFromItem);
                Log.d("fghfghfgh","DateWithdrawToItem"+FCD_Common.DateWithdrawToItem);
                walletAmount();
                //progressbar.setVisibility(View.VISIBLE);
            }
            if(date1.equals(date2)){
                txt_fromDate.setText(compareFromDate);
                txt_toDate.setText(compareToDate);
                Log.d("fghfghfgh","DateWithdrawFromItem"+FCD_Common.DateWithdrawFromItem);
                Log.d("fghfghfgh","DateWithdrawToItem"+FCD_Common.DateWithdrawToItem);
                walletAmount();
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
                    bar = Snackbar.make(ll_mainlayout, value, Snackbar.LENGTH_LONG)
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
