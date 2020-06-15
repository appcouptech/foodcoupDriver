package foodcoup.driver.demand.FCDFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import foodcoup.driver.demand.FCDActivity.FCDDashboardActivity.FCD_DashboardActivity;
import foodcoup.driver.demand.FCDPojo.FCDLiveOrders.LiveOrdersObject;
import foodcoup.driver.demand.FCDUtils.Loader.LoaderTextView;
import foodcoup.driver.demand.FCDViews.AC_Textview;
import foodcoup.driver.demand.FCDViews.FCD_Common;
import foodcoup.driver.demand.FCDViews.FCD_SharedPrefManager;
import foodcoup.driver.demand.FCDViews.FCD_URL;
import foodcoup.driver.demand.FCDViews.FCD_User;
import foodcoup.driver.demand.FCDViews.Utils;
import foodcoup.driver.demand.R;

import static android.content.Context.VIBRATOR_SERVICE;
public class FCD_LiveOrders extends Fragment {
    private NewOrdersAdapter newOrdersAdapter;
    private ArrayList<LiveOrdersObject> liveOrdersObjects;
    private AlertDialog alertDialog;
    private Snackbar bar;
    private Handler handler;
    private LinearLayout ll_main;
    private Context context;
    private RecyclerView rv_newOrders;
    private AC_Textview txt_emptyview;
    private DutyStartedLiveOrder dutyStartedLiveOrder;
    private int  check =0;
    private int  check_new ;
    private int  order_check ;
    private Vibrator vibrator;
    public FCD_LiveOrders() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fcd__live_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FCD_Common.currentTask = 1;
        FCD_Common.orderConfirm = 0;



        context=getActivity();
        FCD_User user = FCD_SharedPrefManager.getInstance(context).getUser();
        FCD_Common.id = String.valueOf(user.getid());
        FCD_Common.token_type = String.valueOf(user.gettoken_type());
        FCD_Common.access_token = String.valueOf(user.getaccess_token());

        FindViewById(view);

        if (FCD_Common.dutyStarted == 1) {
            liveOrdersListDetails();
        } else {

            alertdialouge();
        }

        if (getActivity() instanceof DutyStartedLiveOrder)
            dutyStartedLiveOrder = (DutyStartedLiveOrder) getActivity();

        // FindStatus ();

        handler = new Handler();
        int delay = 35000; //milliseconds
        //int delay = 15000; //milliseconds
        handler.postDelayed(new Runnable() {
            public void run() {
                //do something

                if (FCD_Common.dutyStarted == 1) {
                    liveOrdersListDetails();
                    Log.d("dfgfdgfdg","dfgfdgfdg");
                } else {

                   // alertdialouge();
                    Log.d("dfgfdgfdg","dfgfdgfdg");
                }
                // ItemViewList();
                handler.postDelayed(this, delay);
            }
        }, delay);

    }


    private void FindStatus() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FCD_URL.URL_DRIVER_STATUS_CHECK,
                ServerResponse -> {

                    Log.d("ServerResponse", "LiveOrders" + ServerResponse);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        FCD_Common.status = jsonResponse1.getString("success");
                        FCD_Common.dutyStarted = Integer.parseInt(jsonResponse1.getString("driver_status"));
                        FCD_Common.orderid = jsonResponse1.getString("order_id");
                        //    FCD_Common.currentTask = Integer.parseInt(jsonResponse1.getString("current_task"));

                        Log.d("dutyStarted response", " dutyStarted : " + FCD_Common.dutyStarted);

                        if (FCD_Common.dutyStarted == 1) {

                            if (dutyStartedLiveOrder != null)
                                dutyStartedLiveOrder.dutyStartedLiveOrder(FCD_Common.dutyStarted);

                            liveOrdersListDetails();
                        } else {
                            alertdialouge();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("xcgsdgsdgsd", "dfhdf" + e);
                    }
                }, volleyError -> {
            String value = volleyError.toString();
           // snackBar(value);
           // Utils.toast(getActivity(),value);
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

    private void liveOrdersListDetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FCD_URL.URL_LOCATION_BASED_LIST,
                ServerResponse -> {
                    try {
                    Log.d("ServerResponse", "Liveordersnew" + ServerResponse);
                    Log.d("ServerResponse", "check_new" + check_new);
                    Log.d("ServerResponse", "check" + check);
                    Log.d("ServerResponse","order_check"+order_check);
                    Log.d("ServerResponse","count"+FCD_Common.count);
                    JSONObject jsonResponse = new JSONObject(ServerResponse);
                    FCD_Common.order_id_check = jsonResponse.getInt("order_id");
                        Log.d("ServerResponse","count"+ FCD_Common.order_id_check);
                    if (FCD_Common.count==1) {
                        if (check < FCD_Common.order_id_check) {
                            vibrator = (Vibrator) Objects.requireNonNull(getActivity()).getSystemService(VIBRATOR_SERVICE);
                            MediaPlayer ring = MediaPlayer.create(context, R.raw.alert);
                            ring.start();
                            if (Build.VERSION.SDK_INT >= 26) {
                                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                vibrator.vibrate(200);
                            }
                            check=FCD_Common.order_id_check;
                        }
                        else {
                            Log.d("ServerResponse","xcbfxcbvxc"+ FCD_Common.order_id_check);
                        }
                    }


                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        FCD_Common.status = jsonResponse1.getString("success");
                        if (FCD_Common.status.equalsIgnoreCase("1")) {

                            rv_newOrders.setVisibility(View.VISIBLE);
                            txt_emptyview.setVisibility(View.GONE);
                            if (FCD_Common.dutyStarted == 1) {

                                if (dutyStartedLiveOrder != null)
                                    dutyStartedLiveOrder.dutyStartedLiveOrder(FCD_Common.dutyStarted);
                            }
                            JSONArray product = jsonResponse1.getJSONArray("data");
                            Log.d("ServerResponse","product"+product);
                            liveOrdersObjects.clear();
                            for (int i = 0; i < product.length(); i++) {
                                LiveOrdersObject object = new LiveOrdersObject();
                                JSONObject data = product.getJSONObject(i);
                                object.setId(data.getInt("id"));
                                object.setStatus(data.getString("status"));
                                object.setDriver_id(data.getInt("driver_id"));
                                object.setRestaurant_name(data.getString("restaurant_name"));
                                object.setRestaurant_phone(data.getString("restaurant_phone"));
                                object.setRestaurant_address(data.getString("restaurant_address"));
                                object.setRestaurant_latitude(data.getDouble("restaurant_latitude"));
                                object.setRestaurant_longitude(data.getDouble("restaurant_longitude"));
                                object.setName(data.getString("name"));
                                object.setMobile(data.getString("mobile"));
                                object.setMap_address(data.getString("map_address"));
                                object.setDistance(data.getInt("distance"));
                                object.setBooking_id(data.getString("booking_id"));
                                object.setPayment_mode(data.getString("payment_mode"));
                                liveOrdersObjects.add(object);
                            }

                            newOrdersAdapter.visibleContentLayout();
                        }
                        else {
                            txt_emptyview.setVisibility(View.VISIBLE);
                            rv_newOrders.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        txt_emptyview.setVisibility(View.VISIBLE);
                        rv_newOrders.setVisibility(View.GONE);
                        Log.d("xcgsdgsdgsd", "dfhdf" + e);
                    }
                }, volleyError -> {
            String value = volleyError.toString();
               // snackBar(value);
           // Utils.toast(getActivity(),value);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("latitude", String.valueOf(FCD_Common.latitude));
                params.put("longitude", String.valueOf(FCD_Common.longitude));
                params.put("current_task", String.valueOf(FCD_Common.currentTask));
                Log.d("params"," lat lng : " + FCD_Common.latitude + " , " + FCD_Common.longitude);
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
        try{
            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
            requestQueue.add(stringRequest);
            requestQueue.getCache().clear();
        }
        catch (RuntimeException ex){
            ex.printStackTrace();
        }

    }

    private void alertdialouge() {

        try {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.confirmation_alert, null);
        dialogBuilder.setView(dialogView);

        AC_Textview txt_alertDesc = dialogView.findViewById(R.id.txt_alertDesc);
        AC_Textview txt_alertDesc1 = dialogView.findViewById(R.id.txt_alertDesc1);
        txt_alertDesc1.setVisibility(View.VISIBLE);
        AC_Textview txt_cancel = dialogView.findViewById(R.id.txt_cancel);
        AC_Textview txt_confirm = dialogView.findViewById(R.id.txt_confirm);

        alertDialog = dialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(false);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Spannable colorSpan = new SpannableString(getActivity().getResources().getString(R.string.currently_offline));
        colorSpan.setSpan(new ForegroundColorSpan(getActivity().getResources().getColor(R.color.txt_site_red_color)), 17, 25, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt_alertDesc.setText(colorSpan);

        colorSpan = new SpannableString(getActivity().getResources().getString(R.string.go_online));
        colorSpan.setSpan(new ForegroundColorSpan(getActivity().getResources().getColor(R.color.txt_site_green_color)), 3, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt_alertDesc1.setText(colorSpan);

        txt_cancel.setOnClickListener(view -> alertDialog.dismiss());

        txt_confirm.setOnClickListener(view -> {

            FCD_HomeFragment homeFragment = new FCD_HomeFragment();
            FragmentTransaction fragmentTransactionHome = FCD_DashboardActivity.fragmentManager.beginTransaction();
            fragmentTransactionHome.add(R.id.content_frame, homeFragment, "homeFragment");
            fragmentTransactionHome.commit();
            FCD_DashboardActivity.txt_toolbar.setText(R.string.home);
            FCD_Common.currentTask = 2;

            alertDialog.dismiss();
        });
        alertDialog.show();
        }
        catch (RuntimeException ex)
        {
            ex.printStackTrace();
        }
    }

    private void FindViewById(View view) {

        FCD_DashboardActivity.txt_toolbarOrderNo.setVisibility(View.GONE);
        rv_newOrders = view.findViewById(R.id.rv_newOrders);
        ll_main = view.findViewById(R.id.ll_main);
        txt_emptyview = view.findViewById(R.id.txt_emptyview);

        liveOrdersObjects = new ArrayList<>();
        LiveOrdersObject ordersObject = new LiveOrdersObject();
        ordersObject.setD_image("");
        ordersObject.setD_name("");
        liveOrdersObjects.add(ordersObject);
        ordersObject.setD_image("");
        ordersObject.setD_name("");
        liveOrdersObjects.add(ordersObject);
        ordersObject.setD_image("");
        ordersObject.setD_name("");
        liveOrdersObjects.add(ordersObject);

        newOrdersAdapter = new NewOrdersAdapter(liveOrdersObjects);
        rv_newOrders.setAdapter(newOrdersAdapter);
        rv_newOrders.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private class NewOrdersAdapter extends RecyclerView.Adapter<NewOrdersAdapter.ViewHolder> {
        private final ArrayList<LiveOrdersObject> liveOrdersObjects;
        boolean visible;
        private Spannable colorSpan;

        NewOrdersAdapter(ArrayList<LiveOrdersObject> liveOrdersObjects) {
            this.liveOrdersObjects = liveOrdersObjects;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_new_orders_item, parent, false);
            return new ViewHolder(listItem);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            if (visible) {

                Log.d(" size : ", " liveOrdersObjects : " + liveOrdersObjects.size());
                if (FCD_Common.count==0){
                    //check=liveOrdersObjects.size();
                    check=FCD_Common.order_id_check;

                    FCD_Common.count++;
                }
                check_new=liveOrdersObjects.size();
               // order_check=liveOrdersObjects.get(position).getId();
                Log.d("dfhgdfgfd","dfgdfgfd"+order_check);
                Log.d("dfhgdfgfd","count"+FCD_Common.count);
                if (check<FCD_Common.order_id_check){
                    vibrator = (Vibrator) Objects.requireNonNull(getActivity()).getSystemService(VIBRATOR_SERVICE);
                    MediaPlayer ring=MediaPlayer.create(context,R.raw.alert);
                    ring.start();
                    if (Build.VERSION.SDK_INT >= 26) {
                        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(200);
                    }
                }
                Log.d("dfghdfgfd","dfgdfgfd"+check);
                holder.ll_newOrderRequestLoader.setVisibility(View.GONE);
                holder.ll_newOrderRequest.setVisibility(View.VISIBLE);

                Log.d("fghfghfg","fghfgh"+liveOrdersObjects.get(position).getMobile());
                holder.txt_cashCard.setText(getResources().getString(R.string.pay)+" "+liveOrdersObjects.get(position).getPayment_mode());
                holder.lt_orderNo.setText(" "+liveOrdersObjects.get(position).getBooking_id());
                holder.txt_restaurantPhone.setText(getResources().getString(R.string.pick_up)+""+liveOrdersObjects.get(position).getRestaurant_phone());
                holder.txt_customerPhone.setText(getResources().getString(R.string.deliver_to)+""+liveOrdersObjects.get(position).getMobile());
                holder.txt_restaurantName.setText(liveOrdersObjects.get(position).getRestaurant_name());
                holder.txt_restaurantAddress.setText(liveOrdersObjects.get(position).getRestaurant_address());
                holder.txt_customerName.setText(liveOrdersObjects.get(position).getName());
                holder.txt_customerAddress.setText(liveOrdersObjects.get(position).getMap_address());

                holder.txt_restaurantPhone.setOnClickListener(v -> {
                    Log.d("fghfghfgh","dfhgdfg");
                    FCD_Common.phone=liveOrdersObjects.get(position).getRestaurant_phone();
                    if (Build.VERSION.SDK_INT < 23) {
                        phoneCall();
                    }else {
                        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            phoneCall();
                        }else {
                            final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                            //Asking request Permissions
                            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE, 9);
                        }
                    }
                });
                holder.txt_customerPhone.setOnClickListener(v -> {
                    Log.d("fghfghfgh","dfhgdfg");
                    FCD_Common.phone=liveOrdersObjects.get(position).getMobile();
                    if (Build.VERSION.SDK_INT < 23) {
                        phoneCall();
                    }else {
                        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            phoneCall();
                        }else {
                            final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                            //Asking request Permissions
                            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE, 9);
                        }
                    }
                });
                holder.txt_confirm.setOnClickListener(view -> {

                    if (FCD_Common.dutyStarted == 1) {

                        FCD_Common.orderid = String.valueOf(liveOrdersObjects.get(position).getId());

                        FCD_Common.count=2;
                        Utils.toast(getActivity(),"Your Order Confirming Loading please Wait");
                        ConfirmOrder();

                    } else {

                        FCD_Common.currentTask = 1;
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                        LayoutInflater inflater = getLayoutInflater();
                        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.confirmation_alert, null);
                        dialogBuilder.setView(dialogView);

                        AC_Textview txt_alertDesc = dialogView.findViewById(R.id.txt_alertDesc);
                        AC_Textview txt_alertDesc1 = dialogView.findViewById(R.id.txt_alertDesc1);
                        txt_alertDesc1.setVisibility(View.VISIBLE);
                        AC_Textview txt_cancel = dialogView.findViewById(R.id.txt_cancel);
                        AC_Textview txt_confirm = dialogView.findViewById(R.id.txt_confirm);

                        final AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        alertDialog.setCancelable(false);
                        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        colorSpan = new SpannableString(getActivity().getResources().getString(R.string.currently_offline));
                        colorSpan.setSpan(new ForegroundColorSpan(getActivity().getResources().getColor(R.color.txt_site_red_color)), 17, 25, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        txt_alertDesc.setText(colorSpan);

                        colorSpan = new SpannableString(getActivity().getResources().getString(R.string.go_online));
                        colorSpan.setSpan(new ForegroundColorSpan(getActivity().getResources().getColor(R.color.txt_site_green_color)), 3, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        txt_alertDesc1.setText(colorSpan);

                        txt_cancel.setOnClickListener(view12 -> alertDialog.dismiss());

                        txt_confirm.setOnClickListener(view1 -> {
                            //snackBar("Loading please Wait");
                            Utils.toast(getActivity(),"Loading please Wait");
                            FCD_HomeFragment homeFragment = new FCD_HomeFragment();
                            FragmentTransaction fragmentTransactionHome = FCD_DashboardActivity.fragmentManager.beginTransaction();
                            fragmentTransactionHome.replace(R.id.content_frame, homeFragment, "homeFragment");
                            fragmentTransactionHome.commit();
                            FCD_DashboardActivity.txt_toolbar.setText(R.string.home);
                            FCD_Common.currentTask = 2;

                            alertDialog.dismiss();
                        });
                        alertDialog.show();

                    }

                });

                holder.txt_rejected.setOnClickListener(view -> {
                    if (FCD_Common.dutyStarted == 1) {

                        FCD_Common.orderid = String.valueOf(liveOrdersObjects.get(position).getId());

                        FCD_Common.count=2;
                        Utils.toast(getActivity(),"Your Order Rejecting Loading please Wait");
                        RejectOrder();

                    } else {

                        FCD_Common.currentTask = 1;
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                        LayoutInflater inflater = getLayoutInflater();
                        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.confirmation_alert, null);
                        dialogBuilder.setView(dialogView);

                        AC_Textview txt_alertDesc = dialogView.findViewById(R.id.txt_alertDesc);
                        AC_Textview txt_alertDesc1 = dialogView.findViewById(R.id.txt_alertDesc1);
                        txt_alertDesc1.setVisibility(View.VISIBLE);
                        AC_Textview txt_cancel = dialogView.findViewById(R.id.txt_cancel);
                        AC_Textview txt_confirm = dialogView.findViewById(R.id.txt_confirm);

                        final AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        alertDialog.setCancelable(false);
                        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        colorSpan = new SpannableString(getActivity().getResources().getString(R.string.currently_offline));
                        colorSpan.setSpan(new ForegroundColorSpan(getActivity().getResources().getColor(R.color.txt_site_red_color)), 17, 25, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        txt_alertDesc.setText(colorSpan);

                        colorSpan = new SpannableString(getActivity().getResources().getString(R.string.go_online));
                        colorSpan.setSpan(new ForegroundColorSpan(getActivity().getResources().getColor(R.color.txt_site_green_color)), 3, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        txt_alertDesc1.setText(colorSpan);

                        txt_cancel.setOnClickListener(view12 -> alertDialog.dismiss());

                        txt_confirm.setOnClickListener(view1 -> {
                            //snackBar("Loading please Wait");
                            Utils.toast(getActivity(),"Loading please Wait");
                            FCD_HomeFragment homeFragment = new FCD_HomeFragment();
                            FragmentTransaction fragmentTransactionHome = FCD_DashboardActivity.fragmentManager.beginTransaction();
                            fragmentTransactionHome.replace(R.id.content_frame, homeFragment, "homeFragment");
                            fragmentTransactionHome.commit();
                            FCD_DashboardActivity.txt_toolbar.setText(R.string.home);
                            FCD_Common.currentTask = 2;

                            alertDialog.dismiss();
                        });
                        alertDialog.show();

                    }
                });
            } else {
                Log.d("dfgsfdgfdg","dfgdfgfdg");
                holder.ll_newOrderRequestLoader.setVisibility(View.VISIBLE);
                holder.ll_newOrderRequest.setVisibility(View.GONE);
            }


        }

        private void ConfirmOrder() {
            Utils.playProgressBar(getActivity());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, FCD_URL.URL_CONFIRM_ORDER,
                    ServerResponse -> {

                        Log.d("ServerResponse", "Liveorderscheck" + ServerResponse);
                        try {

                            JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                            FCD_Common.status = jsonResponse1.getString("success");
                            if (FCD_Common.status.equalsIgnoreCase("1")) {

                              //  snackBar("Loading please Wait");
                                //Utils.toast(getActivity(),"Loading please Wait");
                                FCD_Common.currentTask = 4;
                                Utils.stopProgressBar();
                                FCD_ConfirmOrders confirmFragment = new FCD_ConfirmOrders();
                                FragmentTransaction fragmentTransactionConfirmOrder = FCD_DashboardActivity.fragmentManager.beginTransaction();
                                fragmentTransactionConfirmOrder.replace(R.id.content_frame, confirmFragment);
                                fragmentTransactionConfirmOrder.commit();
                                FCD_DashboardActivity.txt_toolbar.setText(R.string.confirm_order);
                            }
                            else {
                                Utils.stopProgressBar();
                                FCD_Common.message = jsonResponse1.getString("message");
                                Utils.toast(context,FCD_Common.message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("xcgsdgsdgsd", "dfhdf" + e);
                        }
                    }, volleyError -> {
                String value = volleyError.toString();
                Log.d("sdgsdfsdf","sdfds"+value);
                //Utils.toast(getActivity(),value);
                //snackBar(value);
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("order_id", String.valueOf(FCD_Common.orderid));
                    params.put("latitude", String.valueOf(FCD_Common.latitude));
                    params.put("longitude", String.valueOf(FCD_Common.longitude));
                    params.put("current_task", String.valueOf(FCD_Common.currentTask));
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
            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
            requestQueue.add(stringRequest);
            requestQueue.getCache().clear();

        }
        private void RejectOrder() {
            Utils.playProgressBar(getActivity());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, FCD_URL.URL_REJECT_ORDER,
                    ServerResponse -> {

                        Log.d("ServerResponse", "Liveorderscheck" + ServerResponse);
                        try {

                            JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                            FCD_Common.status = jsonResponse1.getString("success");
                            if (FCD_Common.status.equalsIgnoreCase("1")) {

                                Utils.stopProgressBar();
                                liveOrdersListDetails();
                            }
                            else {
                                Utils.stopProgressBar();
                                FCD_Common.message = jsonResponse1.getString("message");
                                Utils.toast(context,FCD_Common.message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("xcgsdgsdgsd", "dfhdf" + e);
                        }
                    }, volleyError -> {
                String value = volleyError.toString();
                Log.d("sdgsdfsdf","sdfds"+value);
                //Utils.toast(getActivity(),value);
                //snackBar(value);
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("order_id", String.valueOf(FCD_Common.orderid));
                    params.put("latitude", String.valueOf(FCD_Common.latitude));
                    params.put("longitude", String.valueOf(FCD_Common.longitude));
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
            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
            requestQueue.add(stringRequest);
            requestQueue.getCache().clear();

        }


        @Override
        public int getItemCount() {
            return liveOrdersObjects.size();
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
            LinearLayout ll_newOrderRequestLoader, ll_newOrderRequest;
            LoaderTextView lt_orderNo;
            AC_Textview txt_cashCard,txt_customerAddress,txt_customerName,txt_rejected, txt_confirm,txt_restaurantPhone,txt_restaurantName,txt_restaurantAddress,txt_customerPhone;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                txt_rejected = itemView.findViewById(R.id.txt_rejected);
                lt_orderNo = itemView.findViewById(R.id.lt_orderNo);
                txt_cashCard = itemView.findViewById(R.id.txt_cashCard);
                txt_confirm = itemView.findViewById(R.id.txt_confirm);
                txt_restaurantPhone = itemView.findViewById(R.id.txt_restaurantPhone);
                txt_restaurantName = itemView.findViewById(R.id.txt_restaurantName);
                txt_restaurantAddress = itemView.findViewById(R.id.txt_restaurantAddress);
                txt_customerPhone = itemView.findViewById(R.id.txt_customerPhone);
                txt_customerName = itemView.findViewById(R.id.txt_customerName);
                txt_customerAddress = itemView.findViewById(R.id.txt_customerAddress);

                ll_newOrderRequestLoader = itemView.findViewById(R.id.ll_newOrderRequestLoader);
                ll_newOrderRequest = itemView.findViewById(R.id.ll_newOrderRequest);
            }
        }
    }

    public interface DutyStartedLiveOrder {
        void dutyStartedLiveOrder(int value);
    }

    /*private void snackBar(final String value) {
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
    }*/

    private void phoneCall() {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" +  FCD_Common.phone));
            getActivity().startActivity(callIntent);
        } else {
            Toast.makeText(getActivity(), "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        boolean permissionGranted = false;
        if (requestCode == 9) {
            permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
        if(permissionGranted){
            phoneCall();
        }else {
            Toast.makeText(getActivity(), "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }
}
