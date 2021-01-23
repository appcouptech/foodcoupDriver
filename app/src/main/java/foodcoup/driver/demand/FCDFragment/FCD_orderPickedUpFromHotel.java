package foodcoup.driver.demand.FCDFragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import foodcoup.driver.demand.FCDActivity.FCDDashboardActivity.FCD_DashboardActivity;
import foodcoup.driver.demand.FCDUtils.Loader.LoaderTextView;
import foodcoup.driver.demand.FCDViews.AC_Edittext;
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
public class FCD_orderPickedUpFromHotel extends Fragment {
    private LoaderTextView lt_restaurantName,lt_orderNo;
    private LoaderTextView lt_restaurantAddress;
    private LoaderTextView lt_item;
    private LoaderTextView lt_customerName;
    private LoaderTextView lt_customerAddress;
    private LoaderTextView lt_currency;
    private AC_Textview txt_submit,txt_orderPickUp,txt_restaurantRating;
    private Context context;
    private AC_Edittext edt_commentres;
    private RatingBar ratingBar_restaurant;
    private BottomSheetDialog ratingdialog;
    public FCD_orderPickedUpFromHotel() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fcd_order_picked_up_from_hotel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context=getActivity();
        FCD_User user = FCD_SharedPrefManager.getInstance(context).getUser();
        FCD_Common.id = String.valueOf(user.getid());
        FCD_Common.token_type = String.valueOf(user.gettoken_type());
        FCD_Common.access_token = String.valueOf(user.getaccess_token());
        FindViewById(view);

        FCD_Common.currentTask =  6;
        txt_orderPickUp.setOnClickListener(view1 -> {

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.confirmation_alert, null);
            dialogBuilder.setView(dialogView);

            AC_Textview txt_alertDesc = dialogView.findViewById(R.id.txt_alertDesc);
            AC_Textview txt_cancel = dialogView.findViewById(R.id.txt_cancel);
            AC_Textview txt_confirm = dialogView.findViewById(R.id.txt_confirm);

            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setCancelable(false);
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


            txt_alertDesc.setText(R.string.sure_picked_up_the_order);
            txt_cancel.setOnClickListener(view11 -> alertDialog.dismiss());

            txt_confirm.setOnClickListener(view112 -> {
                FCD_Common.currentTask =  7;
                ConfirmOrder();
                alertDialog.dismiss();
            });
            alertDialog.show();

        });

        txt_restaurantRating.setOnClickListener(v -> {
            @SuppressLint("InflateParams")
            View view1 = getLayoutInflater().inflate(R.layout.layout_rating, null);
            FindViewByIdBottomDialogRating(view1);

            ratingdialog = new BottomSheetDialog(context);
            ratingdialog.setContentView(view1);
            ratingdialog.show();
        });

        OrderDetail();

        lt_restaurantName.setOnClickListener(v -> {
            FCD_Common.phone=FCD_Common.confirmrestaurant_phone;
            Intent inSOS = new Intent(Intent.ACTION_DIAL);
            inSOS.setData(Uri.parse("tel:" + FCD_Common.phone));
            startActivity(inSOS);
          /*  if (Build.VERSION.SDK_INT < 23) {
                phoneCall();
            }else {
                if (ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    phoneCall();
                }else {
                    final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                    //Asking request Permissions
                    ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE, 9);
                }
            }*/
        });

        lt_customerName.setOnClickListener(v -> {
            FCD_Common.phone=FCD_Common.confirmmobile;
            Intent inSOS = new Intent(Intent.ACTION_DIAL);
            inSOS.setData(Uri.parse("tel:" + FCD_Common.phone));
            startActivity(inSOS);
          /*  if (Build.VERSION.SDK_INT < 23) {
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
            }*/
        });
    }

    @SuppressLint("SetTextI18n")
    private void FindViewById(View view) {
        txt_orderPickUp = view.findViewById(R.id.txt_orderPickUp);
        txt_restaurantRating = view.findViewById(R.id.txt_restaurantRating);
        lt_restaurantName = view.findViewById(R.id.lt_restaurantName);
        lt_orderNo = view.findViewById(R.id.lt_orderNo);
        lt_restaurantAddress = view.findViewById(R.id.lt_restaurantAddress);
        lt_item = view.findViewById(R.id.lt_item);
        lt_currency = view.findViewById(R.id.lt_currency);
        lt_customerAddress = view.findViewById(R.id.lt_customerAddress);
        lt_customerName = view.findViewById(R.id.lt_customerName);
        FCD_DashboardActivity.txt_toolbarOrderNo.setVisibility(View.GONE);
        FCD_DashboardActivity.txt_toolbarOrderNo.setText("" + 123456789);
    }

    @SuppressLint("SetTextI18n")
    private void OrderDetail() {
        Utils.playProgressBar(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FCD_URL.URL_ORDERDETAIL,
                response -> {
                    Log.d("", ">>" + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        FCD_Common.success = obj.getString("success");

                        Log.d("ghfghfghf", "fhfgdhfd" + obj);
                        if (FCD_Common.success.equalsIgnoreCase("1")) {
                            Utils.stopProgressBar();
                            FCD_Common.confirmrestaurant_id = obj.getString("id");
                            FCD_Common.confirmrestaurant_name = obj.getString("restaurant_name");
                            FCD_Common.confirmorderstatus = obj.getString("status");
                            FCD_Common.confirmdriver_id = obj.getString("driver_id");
                            FCD_Common.confirmrestaurant_phone = obj.getString("restaurant_phone");
                            FCD_Common.confirmrestaurant_address = obj.getString("restaurant_address");
                            FCD_Common.confirmrestaurant_latitude = obj.getString("restaurant_latitude");
                            FCD_Common.confirmrestaurant_longitude = obj.getString("restaurant_longitude");
                            FCD_Common.confirmname = obj.getString("name");
                            FCD_Common.confirmmobile = obj.getString("mobile");
                            FCD_Common.confirmmap_address = obj.getString("map_address");
                            FCD_Common.confirmlatitude = obj.getString("latitude");
                            FCD_Common.confirmlongitude = obj.getString("longitude");
                            FCD_Common.confirmpayable = obj.getString("payable");
                            FCD_Common.confirmitem = obj.getString("item");
                            FCD_Common.confirmcurrency = obj.getString("currency");
                            FCD_Common.confirmbooking_id = obj.getString("booking_id");
                            lt_orderNo.setText(FCD_Common.confirmbooking_id );
                            lt_restaurantName.setText(FCD_Common.confirmrestaurant_name );
                            lt_restaurantAddress.setText(FCD_Common.confirmrestaurant_address );
                            lt_item.setText(FCD_Common.confirmitem );
                            lt_customerName.setText(FCD_Common.confirmname );
                            lt_customerAddress.setText(FCD_Common.confirmmap_address);
                            lt_currency.setText(FCD_Common.confirmcurrency+" "+FCD_Common.confirmpayable);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        // snackBar(String.valueOf(e));
                        Utils.stopProgressBar();
                        Log.d("dfghdghfgfdb", "fdhfdh" + e);
                        // Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);

                    }
                },
                error -> {
                    //displaying the error in toast if occurrs
                    //snackBar(String.valueOf(error));
                    Utils.stopProgressBar();
                    Log.d("dfhfdghfgh", "hfdhdf" + error);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("order_id", FCD_Common.orderid);
                params.put("current_task", String.valueOf(FCD_Common.currentTask));
                params.put("latitude", String.valueOf(FCD_Common.latitude));
                params.put("longitude", String.valueOf(FCD_Common.longitude));
                Log.d("getParams: ", "" + params);
                return params;
            }
            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String> params = new HashMap<>();
                Log.d("getParams: ", "" + params);
                params.put("Authorization", FCD_Common.token_type+" "+FCD_Common.access_token);
                return params;
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void ConfirmOrder() {
        Utils.playProgressBar(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FCD_URL.URL_UPDATESTATUS,
                ServerResponse -> {

                    Log.d("ServerResponse", "OrderPicked" + ServerResponse);
                    Log.d("ServerResponse", "OrderPickednew" + FCD_URL.URL_UPDATESTATUS);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        FCD_Common.status = jsonResponse1.getString("success");
                        if (FCD_Common.status.equalsIgnoreCase("1")) {
                            FCD_Common.message = jsonResponse1.getString("message");
                            Utils.stopProgressBar();
                            //snackBar(FCD_Common.message);
                            FCD_Common.currentTask =  7;
                            FCD_ConfirmOrderFromHotel ConfirmOrderFromHotel = new FCD_ConfirmOrderFromHotel();
                            FragmentTransaction fragmentTransactionConfirmOrderFromHotel = FCD_DashboardActivity.fragmentManager.beginTransaction();
                            fragmentTransactionConfirmOrderFromHotel.replace(R.id.content_frame, ConfirmOrderFromHotel);
                            fragmentTransactionConfirmOrderFromHotel.commit();
                            FCD_DashboardActivity.txt_toolbar.setText(R.string.confirm_order);

                        }
                        else {
                            Utils.toast(getActivity(),getResources().getString(R.string.enable_location));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("xcgsdgsdgsd", "dfhdf" + e);
                    }
                }, volleyError -> {
            // snackBar(value);
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("order_id", FCD_Common.orderid);
                params.put("order_status", "PICKEDUP");
                params.put("current_task", String.valueOf(FCD_Common.currentTask));
                params.put("latitude", String.valueOf(FCD_Common.latitude));
                params.put("longitude", String.valueOf(FCD_Common.longitude));
                Log.d("fgjfghfg","fghfgh"+params);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();

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
   /* private void phoneCall() {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" +  FCD_Common.phone));
            getActivity().startActivity(callIntent);
        } else {
            Toast.makeText(getActivity(), "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        boolean permissionGranted = false;
        if (requestCode == 9) {
            permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
        if(permissionGranted){
            //phoneCall();
        }else {
            Toast.makeText(getActivity(), "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }

    private void FindViewByIdBottomDialogRating(View view) {


        LoaderTextView lt_restaurantnameRating = view.findViewById(R.id.lt_restaurantnameRating);
        txt_submit=view.findViewById(R.id.txt_submit);
        edt_commentres=view.findViewById(R.id.edt_commentres);
        ratingBar_restaurant=view.findViewById(R.id.ratingBar_restaurant);
        lt_restaurantnameRating.setText(FCD_Common.confirmrestaurant_name);
        txt_submit.setOnClickListener(v -> {

            Log.d("hvcnbvcnbvc","cgcc"+FCD_Common.restaurant_rating);

            if (FCD_Common.restaurant_rating.equalsIgnoreCase(""))
            {
                Log.d("hvcnbvcnbvc","dg4wcgcc"+FCD_Common.restaurant_rating);
                //snackBar(getResources().getString(R.string.rate_res));
            }

            if (FCD_Common.restaurant_rating.equalsIgnoreCase(".0")||
                    FCD_Common.restaurant_rating.equalsIgnoreCase("0.0")){
                Log.d("fghfghfghfg","fail");
            }
            else {
                txt_submit.setEnabled(false);
                FCD_Common.restaurant_comments=edt_commentres.getText().toString().trim();
                FCD_Common.restaurant_rating=String.valueOf(ratingBar_restaurant.getRating());

                Updaterating();
            }
        });
    }



    private void Updaterating() {

        Utils.playProgressBar(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FCD_URL.URL_RATE,
                response -> {
                    Log.d("cvnvncvb", ">>" + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        FCD_Common.success = obj.getString("success");
                        FCD_Common.message = obj.getString("message");
                        Log.d("ghfghfghf", "fhfgdhfd" + obj);
                        if (FCD_Common.success.equalsIgnoreCase("1")) {
                            Utils.stopProgressBar();
                            txt_submit.setEnabled(true);
                            Utils.toast(getContext(),FCD_Common.message);
                            //snackBar(FCD_Common.message);
                            ratingdialog.dismiss();
                        } else {
                            Utils.stopProgressBar();
                            txt_submit.setEnabled(true);
                            Utils.toast(getContext(),FCD_Common.message);
                            //snackBar(FCD_Common.message);
                            ratingdialog.dismiss();
                        }

                    } catch (JSONException e) {
                        txt_submit.setEnabled(true);
                        e.printStackTrace();
                        Utils.stopProgressBar();
                        Log.d("dfghdghfgfdb", "fdhfdh" + e);
                        // Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);
                    }
                },
                error -> {
                    txt_submit.setEnabled(true);
                    //displaying the error in toast if occurrs

                    Log.d("dfhfdghfgh", "hfdhdf" + error);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("order_id", FCD_Common.orderid);
                params.put("restaurant_rating", FCD_Common.restaurant_rating);
                params.put("restaurant_comment", FCD_Common.restaurant_comments);
                //params.put("rating", String.valueOf(FC_Common.restaurant_rating_int));
                //  params.put("order_id", "+");

                Log.d("getParams: ", "" + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                Log.d("getParams: ", "" + params);
                params.put("Authorization", FCD_Common.token_type + " " + FCD_Common.access_token);
                return params;
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);

    }

}
