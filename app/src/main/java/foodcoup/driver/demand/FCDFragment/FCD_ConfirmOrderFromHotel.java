package foodcoup.driver.demand.FCDFragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
public class FCD_ConfirmOrderFromHotel extends Fragment {

    private Context context;
    private LinearLayout ll_deliverOrder ;
    private AC_Textview txt_cashCard ;
    private LoaderTextView lt_orderNo,lt_customerPhone,lt_restaurantName,lt_restaurantAddress,lt_customerName,lt_customerAddress,lt_currency,lt_restaurantPhone;
    public FCD_ConfirmOrderFromHotel() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fcd__confirm_order_from_hotel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context=getActivity();

        context=getActivity();
        FCD_User user = FCD_SharedPrefManager.getInstance(context).getUser();
        FCD_Common.id = String.valueOf(user.getid());
        FCD_Common.token_type = String.valueOf(user.gettoken_type());
        FCD_Common.access_token = String.valueOf(user.getaccess_token());

        Log.d("fdgfdgfdg","dfgfdg"+FCD_Common.Totaldistance);
        FindViewById(view);
        FCD_Common.currentTask =  7;

        ll_deliverOrder.setOnClickListener(view1 -> {

            FCD_Common.currentTask =  8;
            FCD_PickUpTaskToDeliver PickedUpOrderFragment = new FCD_PickUpTaskToDeliver();
            FragmentTransaction fragmentTransactionPickedUpOrder = FCD_DashboardActivity.fragmentManager.beginTransaction();
            fragmentTransactionPickedUpOrder.replace(R.id.content_frame, PickedUpOrderFragment);
            fragmentTransactionPickedUpOrder.commit();
            FCD_DashboardActivity.txt_toolbar.setText(R.string.picked_up_order);



        });

        OrderDetail();

        lt_restaurantName.setOnClickListener(v -> {
            FCD_Common.phone=FCD_Common.confirmrestaurant_phone;
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

        lt_customerName.setOnClickListener(v -> {
            FCD_Common.phone=FCD_Common.confirmmobile;
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

    }

    private void FindViewById(View view) {

        FCD_DashboardActivity.txt_toolbarOrderNo.setVisibility(View.GONE);
        txt_cashCard = view.findViewById(R.id.txt_cashCard);
        ll_deliverOrder = view.findViewById(R.id.ll_deliverOrder);
        lt_orderNo = view.findViewById(R.id.lt_orderNo);
        lt_restaurantName = view.findViewById(R.id.lt_restaurantName);
        lt_restaurantAddress = view.findViewById(R.id.lt_restaurantAddress);
       // lt_item = view.findViewById(R.id.lt_item);
        lt_currency = view.findViewById(R.id.lt_currency);
        lt_customerAddress = view.findViewById(R.id.lt_customerAddress);
        lt_customerName = view.findViewById(R.id.lt_customerName);
        lt_restaurantPhone = view.findViewById(R.id.lt_restaurantPhone);
        lt_customerPhone = view.findViewById(R.id.lt_customerPhone);
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
                            FCD_Common.confirmpayment_mode = obj.getString("payment_mode");
                           // lt_restaurantPhone.setText(getResources().getString(R.string.pick_up)+""+FCD_Common.confirmrestaurant_phone);
                            lt_customerPhone.setText(getResources().getString(R.string.deliver_to)+""+FCD_Common.confirmmobile);
                            lt_restaurantName.setText(FCD_Common.confirmrestaurant_name );
                            lt_restaurantAddress.setText(FCD_Common.confirmrestaurant_address );
                            lt_orderNo.setText(FCD_Common.confirmbooking_id );
                            lt_restaurantPhone.setText(getResources().getString(R.string.pick_up)+" "+FCD_Common.confirmrestaurant_phone);
                            txt_cashCard.setText(getResources().getString(R.string.pay)+" "+FCD_Common.confirmpayment_mode);
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
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(context).getApplicationContext());
        requestQueue.add(stringRequest);

    }

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
