package foodcoup.driver.demand.FCDActivity.FCDDashboardActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import foodcoup.driver.demand.FCDActivity.FCDSplashActivity.FCD_SplashActivity;
import foodcoup.driver.demand.FCDFragment.FCDIncentives.FCD_Incentives;
import foodcoup.driver.demand.FCDFragment.FCDLoginDuration.FCD_LoginDuration;
import foodcoup.driver.demand.FCDFragment.FCDWeeklyEarnings.FCD_WeeklyEarnings;
import foodcoup.driver.demand.FCDFragment.FCD_AccountDetails;
import foodcoup.driver.demand.FCDFragment.FCD_ConfirmOrderFromHotel;
import foodcoup.driver.demand.FCDFragment.FCD_ConfirmOrders;
import foodcoup.driver.demand.FCDFragment.FCD_FloatingCash;
import foodcoup.driver.demand.FCDFragment.FCD_HomeFragment;
import foodcoup.driver.demand.FCDFragment.FCD_LiveOrders;
import foodcoup.driver.demand.FCDFragment.FCD_PickUpTaskToDeliver;
import foodcoup.driver.demand.FCDFragment.FCD_PickedUpOrder;
import foodcoup.driver.demand.FCDFragment.FCD_ReferAFriend;
import foodcoup.driver.demand.FCDFragment.FCD_RideHistory;
import foodcoup.driver.demand.FCDFragment.FCD_TodaysEarnings;
import foodcoup.driver.demand.FCDFragment.FCD_WalletHistory;
import foodcoup.driver.demand.FCDFragment.FCD_orderPickedUpFromHotel;
import foodcoup.driver.demand.FCDUtils.BottomSheet.BottomSheetHomeStartDuty.StartDutyDialog;
import foodcoup.driver.demand.FCDUtils.FCD_Logout;
import foodcoup.driver.demand.FCDUtils.Loader.LoaderTextView;
import foodcoup.driver.demand.FCDUtils.NetworkChecking.NetworkChangeReceiver;
import foodcoup.driver.demand.FCDUtils.UtilsStatic;
import foodcoup.driver.demand.FCDViews.AC_BoldTextview;
import foodcoup.driver.demand.FCDViews.AC_Textview;
import foodcoup.driver.demand.FCDViews.CircleImageView;
import foodcoup.driver.demand.FCDViews.FCD_Common;
import foodcoup.driver.demand.FCDViews.FCD_SharedPrefManager;
import foodcoup.driver.demand.FCDViews.FCD_URL;
import foodcoup.driver.demand.FCDViews.FCD_User;
import foodcoup.driver.demand.FCDViews.Utils;
import foodcoup.driver.demand.FCDWallet.FCD_Wallet;
import foodcoup.driver.demand.R;

/**
 * Created by kamal and ravi 26-2-2020
 */
public class FCD_DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, StartDutyDialog.DutyStarted, NetworkChangeReceiver.ConnectivityReceiverListener, FCD_LiveOrders.DutyStartedLiveOrder {

    Toolbar toolbar;
    private androidx.appcompat.app.AlertDialog alertDialog;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    SwitchCompat sw_dutyStatus;
    AC_Textview txt_off, txt_on;
    LinearLayout ll_profile;
    private CircleImageView imageView;
    public static FragmentManager fragmentManager;
    @SuppressLint("StaticFieldLeak")
    public static Context dashContext;
    ImageView img_currentTask, img_notification;
    private LoaderTextView lt_driverName,lt_driveremail;
    View parentLayout;
    Snackbar bar;
    public static AC_Textview txt_toolbar, txt_toolbarOrderNo;

    NetworkChangeReceiver networkChangeReceiver;
    IntentFilter intentFilter;
    FrameLayout content_frame;
    LinearLayout ll_noInternetConnection;

    final static int REQUEST_LOCATION = 199;
    double latitude; // latitude
    double longitude;
    FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationRequest;
    List<Location> locationList = new ArrayList<>();

    int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        dashContext = FCD_DashboardActivity.this;
        fragmentManager = getSupportFragmentManager();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        networkChangeReceiver = new NetworkChangeReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        FCD_User user = FCD_SharedPrefManager.getInstance(dashContext).getUser();
        FCD_Common.id = String.valueOf(user.getid());
        FCD_Common.token_type = String.valueOf(user.gettoken_type());
        FCD_Common.access_token = String.valueOf(user.getaccess_token());

        checkPermission();

        FindViewById();

        // here we checking the duty is on or off then also checking the current task and redirecting  to it current task fragment :
        FindStatus();
        AccountStatus();
        Log.d("dfgfdgfdg","dfgfgdfg"+FCD_Common.currentTask);
        img_currentTask.setOnClickListener(view -> {

            Log.d("dfgfdgfdg","dfgfgdfg"+FCD_Common.currentTask);

            if (FCD_Common.currentTask == 1) {
                FCD_LiveOrders liveOrders = new FCD_LiveOrders();
                FragmentTransaction fragmentTransactionliveOrders = fragmentManager.beginTransaction();
                fragmentTransactionliveOrders.replace(R.id.content_frame, liveOrders);
                fragmentTransactionliveOrders.commit();
                txt_toolbar.setText(R.string.live_order);

            } else if (FCD_Common.currentTask == 2) {
                FCD_HomeFragment homeFragment = new FCD_HomeFragment();
                FragmentTransaction fragmentTransactionHome = FCD_DashboardActivity.fragmentManager.beginTransaction();
                fragmentTransactionHome.replace(R.id.content_frame, homeFragment, "homeFragment");
                fragmentTransactionHome.commit();
                FCD_DashboardActivity.txt_toolbar.setText(R.string.home);

            } else if (FCD_Common.currentTask == 3) {
                FCD_HomeFragment homeFragment = new FCD_HomeFragment();
                FragmentTransaction fragmentTransactionHome = FCD_DashboardActivity.fragmentManager.beginTransaction();
                fragmentTransactionHome.replace(R.id.content_frame, homeFragment, "homeFragment");
                fragmentTransactionHome.commit();
                FCD_DashboardActivity.txt_toolbar.setText(R.string.home);

            } else if (FCD_Common.currentTask == 4) {
                FCD_ConfirmOrders confirmFragment = new FCD_ConfirmOrders();
                FragmentTransaction fragmentTransactionConfirmOrder = FCD_DashboardActivity.fragmentManager.beginTransaction();
                fragmentTransactionConfirmOrder.replace(R.id.content_frame, confirmFragment);
                fragmentTransactionConfirmOrder.commit();
                FCD_DashboardActivity.txt_toolbar.setText(R.string.confirm_order);

            } else if (FCD_Common.currentTask == 5) {
                FCD_PickedUpOrder PickedUpOrderFragment = new FCD_PickedUpOrder();
                FragmentTransaction fragmentTransactionPickedUpOrder = FCD_DashboardActivity.fragmentManager.beginTransaction();
                fragmentTransactionPickedUpOrder.replace(R.id.content_frame, PickedUpOrderFragment);
                fragmentTransactionPickedUpOrder.commit();
                FCD_DashboardActivity.txt_toolbar.setText(R.string.picked_up_order);

            } else if (FCD_Common.currentTask == 6) {
                FCD_orderPickedUpFromHotel orderPickedUpFromHotel = new FCD_orderPickedUpFromHotel();
                FragmentTransaction fragmentTransactionorderPickedUpFromHotel = FCD_DashboardActivity.fragmentManager.beginTransaction();
                fragmentTransactionorderPickedUpFromHotel.replace(R.id.content_frame, orderPickedUpFromHotel);
                fragmentTransactionorderPickedUpFromHotel.commit();
                FCD_DashboardActivity.txt_toolbar.setText(R.string.picked_up_order);

            } else if (FCD_Common.currentTask == 7) {
                FCD_ConfirmOrderFromHotel ConfirmOrderFromHotel = new FCD_ConfirmOrderFromHotel();
                FragmentTransaction fragmentTransactionConfirmOrderFromHotel = FCD_DashboardActivity.fragmentManager.beginTransaction();
                fragmentTransactionConfirmOrderFromHotel.replace(R.id.content_frame, ConfirmOrderFromHotel);
                fragmentTransactionConfirmOrderFromHotel.commit();
                FCD_DashboardActivity.txt_toolbar.setText(R.string.confirm_order);

            } else if (FCD_Common.currentTask == 8) {
                FCD_PickUpTaskToDeliver PickedUpOrderFragment = new FCD_PickUpTaskToDeliver();
                FragmentTransaction fragmentTransactionPickedUpOrder = FCD_DashboardActivity.fragmentManager.beginTransaction();
                fragmentTransactionPickedUpOrder.replace(R.id.content_frame, PickedUpOrderFragment);
                fragmentTransactionPickedUpOrder.commit();
                FCD_DashboardActivity.txt_toolbar.setText(R.string.picked_up_order);
            }
        });

        img_notification.setOnClickListener(v -> {
            Intent intent =new Intent(FCD_DashboardActivity.this, FCD_WalletHistory.class);
            startActivity(intent);
        });
        sw_dutyStatus.setOnCheckedChangeListener((compoundButton, isChecked) -> {

            if (isChecked) {
                txt_on.setVisibility(View.VISIBLE);
                txt_off.setVisibility(View.GONE);
            } else {
                txt_on.setVisibility(View.GONE);
                txt_off.setVisibility(View.VISIBLE);
            }
        });
    }

    private void FindStatus() {
        Utils.playProgressBar(FCD_DashboardActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FCD_URL.URL_DRIVER_STATUS_CHECK,
                ServerResponse -> {

                    Log.d("ServerResponse", "dashboard" + ServerResponse);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        FCD_Common.status = jsonResponse1.getString("success");
                        FCD_Common.dutyStarted = Integer.parseInt(jsonResponse1.getString("driver_status"));
                        FCD_Common.orderid = jsonResponse1.getString("order_id");
                        FCD_Common.currentTask = Integer.parseInt(jsonResponse1.getString("current_task"));

                        Log.d("dutyStarted response", " dutyStarted : " + FCD_Common.dutyStarted);

                        if (FCD_Common.dutyStarted == 1) {
                            Utils.stopProgressBar();
                            sw_dutyStatus.setChecked(true);
                            check = 1;
                            fucedLocation();
                        } else {
                            Utils.stopProgressBar();
                            sw_dutyStatus.setChecked(false);
                            HomeFragment();
                        }


                    } catch (JSONException e) {
                        Utils.stopProgressBar();
                        e.printStackTrace();
                        Log.d("xcgsdgsdgsd", "dfhdf" + e);
                    }
                }, volleyError -> {
            Utils.stopProgressBar();
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
        RequestQueue requestQueue = Volley.newRequestQueue(FCD_DashboardActivity.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }


    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void AccountStatus() {
       // Utils.playProgressBar(FCD_DashboardActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FCD_URL.URL_DETAILUSER,
                ServerResponse -> {

                    Log.d("ServerResponse", "Account34fsd" + FCD_URL.URL_DETAILUSER);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        FCD_Common.status = jsonResponse1.getString("status");

                        if (FCD_Common.status.equalsIgnoreCase("approved")){

                            //Utils.stopProgressBar();
                            FCD_Common.DriverName = jsonResponse1.getString("name");
                            FCD_Common.DriverEmail = jsonResponse1.getString("email");
                            FCD_Common.Driverphone = jsonResponse1.getString("mobile");
                            FCD_Common.Driverhelp = jsonResponse1.getString("help_number");
                            FCD_Common.DriverPhoto= jsonResponse1.getString("avatar");

                            Picasso.get().load(FCD_Common.DriverPhoto)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .into(imageView);
                            lt_driverName.setText(FCD_Common.DriverName);
                            lt_driveremail.setText(FCD_Common.DriverEmail);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("xcgsdgsdgsd", "dfhdf" + e);
                      //  Utils.stopProgressBar();
                    }
                }, volleyError -> {
            String value = volleyError.toString();
            snackBar(value);
            Log.d("fghfghfg","fghfdg"+FCD_URL.URL_DETAILUSER);
           // Utils.stopProgressBar();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                Log.d("fdgdfgfdg", "sdfgsdgs" + FCD_Common.token_type + " " + FCD_Common.access_token);
                params.put("Authorization", FCD_Common.token_type + " " + FCD_Common.access_token);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(dashContext));
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }
    private void checkPermission() {

        // Todo Location Already on  ... start
        final LocationManager manager = (LocationManager) FCD_DashboardActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if (Objects.requireNonNull(manager).isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(FCD_DashboardActivity.this)) {
            //  Toast.makeText(FCD_DashboardActivity.this, "Gps alreadyy enabled", Toast.LENGTH_SHORT).show();

            fucedLocation();
        }
        // Todo Location Already on  ... end

        if (!hasGPSDevice(FCD_DashboardActivity.this)) {
            Toast.makeText(FCD_DashboardActivity.this, "Gps not Supported", Toast.LENGTH_SHORT).show();
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(FCD_DashboardActivity.this)) {
            Log.e("TAG", "Gps already enabled");
            Toast.makeText(FCD_DashboardActivity.this, "Gps not enabled", Toast.LENGTH_SHORT).show();
            enableLoc();
        } else {
            Log.e("TAG", "Gps already enabled");
            // Toast.makeText(FCD_DashboardActivity.this, "Gps already enabled", Toast.LENGTH_SHORT).show();
            fucedLocation();
        }
    }

    private void fucedLocation() {

        mLocationRequest = new LocationRequest();
    /*    mLocationRequest.setInterval(12000); // 1200000 two minute interval
        mLocationRequest.setFastestInterval(12000);*/
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Log.d("location ", "locationResult :" + locationResult);
            locationList.clear();
            locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.d("location ", "Location: " + location.getLatitude() + " " + location.getLongitude());
                FCD_Common.latitude = location.getLatitude();
                FCD_Common.longitude = location.getLongitude();
                //  Toast.makeText(getApplicationContext(), "" + "Latitude : " + location.getLatitude() + ", Longitude : " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                //Place current location marker
                latitude = location.getLatitude();
                longitude = location.getLongitude();


                if (check == 1) {

                    if (FCD_Common.currentTask == 0) {
                        DefaultFragment();
                    }
                    if (FCD_Common.currentTask == 1) {
                        FCD_LiveOrders liveOrders = new FCD_LiveOrders();
                        FragmentTransaction fragmentTransactionliveOrders = fragmentManager.beginTransaction();
                        fragmentTransactionliveOrders.replace(R.id.content_frame, liveOrders);
                        fragmentTransactionliveOrders.commit();
                        txt_toolbar.setText(R.string.live_order);

                    } else if (FCD_Common.currentTask == 2) {
                        FCD_HomeFragment homeFragment = new FCD_HomeFragment();
                        FragmentTransaction fragmentTransactionHome = FCD_DashboardActivity.fragmentManager.beginTransaction();
                        fragmentTransactionHome.replace(R.id.content_frame, homeFragment, "homeFragment");
                        fragmentTransactionHome.commit();
                        FCD_DashboardActivity.txt_toolbar.setText(R.string.home);

                    } else if (FCD_Common.currentTask == 3) {
                        FCD_HomeFragment homeFragment = new FCD_HomeFragment();
                        FragmentTransaction fragmentTransactionHome = FCD_DashboardActivity.fragmentManager.beginTransaction();
                        fragmentTransactionHome.replace(R.id.content_frame, homeFragment, "homeFragment");
                        fragmentTransactionHome.commit();
                        FCD_DashboardActivity.txt_toolbar.setText(R.string.home);

                    } else if (FCD_Common.currentTask == 4) {
                        FCD_ConfirmOrders confirmFragment = new FCD_ConfirmOrders();
                        FragmentTransaction fragmentTransactionConfirmOrder = FCD_DashboardActivity.fragmentManager.beginTransaction();
                        fragmentTransactionConfirmOrder.replace(R.id.content_frame, confirmFragment);
                        fragmentTransactionConfirmOrder.commit();
                        FCD_DashboardActivity.txt_toolbar.setText(R.string.confirm_order);

                    } else if (FCD_Common.currentTask == 5) {
                        FCD_PickedUpOrder PickedUpOrderFragment = new FCD_PickedUpOrder();
                        FragmentTransaction fragmentTransactionPickedUpOrder = FCD_DashboardActivity.fragmentManager.beginTransaction();
                        fragmentTransactionPickedUpOrder.replace(R.id.content_frame, PickedUpOrderFragment);
                        fragmentTransactionPickedUpOrder.commit();
                        FCD_DashboardActivity.txt_toolbar.setText(R.string.picked_up_order);

                    } else if (FCD_Common.currentTask == 6) {
                        FCD_orderPickedUpFromHotel orderPickedUpFromHotel = new FCD_orderPickedUpFromHotel();
                        FragmentTransaction fragmentTransactionorderPickedUpFromHotel = FCD_DashboardActivity.fragmentManager.beginTransaction();
                        fragmentTransactionorderPickedUpFromHotel.replace(R.id.content_frame, orderPickedUpFromHotel);
                        fragmentTransactionorderPickedUpFromHotel.commit();
                        FCD_DashboardActivity.txt_toolbar.setText(R.string.picked_up_order);

                    } else if (FCD_Common.currentTask == 7) {
                        FCD_ConfirmOrderFromHotel ConfirmOrderFromHotel = new FCD_ConfirmOrderFromHotel();
                        FragmentTransaction fragmentTransactionConfirmOrderFromHotel = FCD_DashboardActivity.fragmentManager.beginTransaction();
                        fragmentTransactionConfirmOrderFromHotel.replace(R.id.content_frame, ConfirmOrderFromHotel);
                        fragmentTransactionConfirmOrderFromHotel.commit();
                        FCD_DashboardActivity.txt_toolbar.setText(R.string.confirm_order);

                    } else if (FCD_Common.currentTask == 8) {
                        FCD_PickUpTaskToDeliver PickedUpOrderFragment = new FCD_PickUpTaskToDeliver();
                        FragmentTransaction fragmentTransactionPickedUpOrder = FCD_DashboardActivity.fragmentManager.beginTransaction();
                        fragmentTransactionPickedUpOrder.replace(R.id.content_frame, PickedUpOrderFragment);
                        fragmentTransactionPickedUpOrder.commit();
                        FCD_DashboardActivity.txt_toolbar.setText(R.string.picked_up_order);
                    }
                }
                check = 0;

                mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            }
        }
    };


    @SuppressLint("SetTextI18n")
    private void FindViewById() {

        img_currentTask = findViewById(R.id.img_currentTask);
        img_notification = findViewById(R.id.img_notification);

        txt_toolbar = findViewById(R.id.txt_toolbar);
        txt_toolbarOrderNo = findViewById(R.id.txt_toolbarOrderNo);
        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);

        ll_noInternetConnection = findViewById(R.id.ll_noInternetConnection);
        content_frame = findViewById(R.id.content_frame);

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.home, R.string.home);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // DefaultFragment();
        navigationView =  findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        ll_profile = header.findViewById(R.id.ll_profile);
        sw_dutyStatus = header.findViewById(R.id.sw_dutyStatus);
        txt_on = header.findViewById(R.id.txt_on);
        txt_off = header.findViewById(R.id.txt_off);
        imageView = header.findViewById(R.id.imageView);
        lt_driverName = header.findViewById(R.id.lt_driverName);
        lt_driveremail = header.findViewById(R.id.lt_driveremail);

        parentLayout = findViewById(android.R.id.content);

        sw_dutyStatus.setOnClickListener(view -> {


            if (sw_dutyStatus.isChecked()) {
                txt_on.setVisibility(View.VISIBLE);
                txt_off.setVisibility(View.GONE);
            } else {
                txt_on.setVisibility(View.GONE);
                txt_off.setVisibility(View.VISIBLE);
            }

            if (FCD_Common.orderConfirm == 1) {
                txt_on.setVisibility(View.VISIBLE);
                txt_off.setVisibility(View.GONE);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FCD_DashboardActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.attention_alert, null);
                dialogBuilder.setView(dialogView);

                Log.d("gsdfgsdfsd","dfgdfg"+FCD_Common.confirmbooking_id);
                AC_Textview txt_ok = dialogView.findViewById(R.id.txt_ok);
                AC_Textview txt_orderNo = dialogView.findViewById(R.id.txt_orderNo);

                txt_orderNo.setText(FCD_Common.confirmbooking_id);
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setCancelable(false);
                Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                txt_ok.setOnClickListener(view1 -> alertDialog.dismiss());

                alertDialog.show();

                sw_dutyStatus.setChecked(true);
            }

            if (sw_dutyStatus.isChecked()) {
                checkPermission();
                FCD_Common.dutyStarted = 1;
                FCD_HomeFragment homeFragment = (FCD_HomeFragment) getSupportFragmentManager().findFragmentByTag("homeFragment");
                if (homeFragment != null) {
                    // Log.d("dutyStarted d_if " , "dutyStarted : " + FCD_Common.dutyStarted);
                    homeFragment.DutyStartedlayoutChange();
                }
                if (FCD_Common.orderConfirm != 1) {
                    DutyUpdate();
                }

            } else {
                FCD_Common.dutyStarted = 0;
                FCD_HomeFragment homeFragment = (FCD_HomeFragment) getSupportFragmentManager().findFragmentByTag("homeFragment");
                if (homeFragment != null) {
                    //  Log.d("dutyStarted d_else " , "dutyStarted : " + FCD_Common.dutyStarted);
                    homeFragment.DutyStartedlayoutChange();
                }
                if (FCD_Common.orderConfirm != 1) {
                    DutyUpdate();
                }
            }
        });
        ll_profile.setOnClickListener(view -> {

            /*FCD_AccountDetails accountFragment = new FCD_AccountDetails();
            FragmentTransaction fragmentTransactionAccountDetails = fragmentManager.beginTransaction();
            fragmentTransactionAccountDetails.replace(R.id.content_frame, accountFragment);
            fragmentTransactionAccountDetails.commit();
            txt_toolbar.setText(R.string.profile);*/

            Intent intent = new Intent(FCD_DashboardActivity.this, FCD_AccountDetails.class);
            //Intent intent = new Intent(FCD_DashboardActivity.this, FCD_WithDrawRequest.class);
            startActivity(intent);
            drawer.closeDrawer(GravityCompat.START);
        });

        navigationView.setNavigationItemSelectedListener(this);
    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null) {
            return false;
        }
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    private void enableLoc() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        Log.d("ghghfg","fdgfd"+gso);
        // Build a GoogleSignInClient with the options specified by gso.
       // GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
      /*  locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);*/

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, locationSettingsResponse -> {
            // All location settings are satisfied. The client can initialize
            // location requests here.
            // ...

            fucedLocation();
        });

        task.addOnFailureListener(this, e -> {
            if (e instanceof ResolvableApiException) {
                try {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(FCD_DashboardActivity.this,
                            REQUEST_LOCATION);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        });
    }

    private void DefaultFragment() {

        FCD_LiveOrders liveOrderFragment = new FCD_LiveOrders();
        FragmentTransaction fragmentTransactionLiveOrder = fragmentManager.beginTransaction();
        fragmentTransactionLiveOrder.replace(R.id.content_frame, liveOrderFragment,"liveOrderFragment");
        fragmentTransactionLiveOrder.commit();
        txt_toolbar.setText(R.string.live_order);
    }

    private void HomeFragment() {

        FCD_HomeFragment homeFragment = new FCD_HomeFragment();
        FragmentTransaction fragmentTransactionhomeFragment = fragmentManager.beginTransaction();
        fragmentTransactionhomeFragment.replace(R.id.content_frame, homeFragment,"homeFragment");
        fragmentTransactionhomeFragment.commit();
        txt_toolbar.setText(R.string.home);
    }

    @Override
    protected void onResume() {
        super.onResume();

       // AccountStatus();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }

        registerReceiver(networkChangeReceiver, intentFilter);
        networkChangeReceiver.setConnectivityReceiverListener(FCD_DashboardActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(networkChangeReceiver);
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Utils.showExitDialog(FCD_DashboardActivity.this, "Are you sure want to \n exit from application");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.profile) {
            /*FCD_AccountDetails accountFragment = new FCD_AccountDetails();
            FragmentTransaction fragmentTransactionAccountDetails = fragmentManager.beginTransaction();
            fragmentTransactionAccountDetails.replace(R.id.content_frame, accountFragment);
            fragmentTransactionAccountDetails.commit();
            txt_toolbar.setText(R.string.profile);*/

            Intent intent = new Intent(FCD_DashboardActivity.this, FCD_AccountDetails.class);
            startActivity(intent);

        } else if (id == R.id.referAFriend) {
            /*FCD_ReferAFriend referAFriend = new FCD_ReferAFriend();
            FragmentTransaction fragmentTransactionreferAFriend = fragmentManager.beginTransaction();
            fragmentTransactionreferAFriend.replace(R.id.content_frame, referAFriend);
            fragmentTransactionreferAFriend.commit();
            txt_toolbar.setText(R.string.refer_a_friend);*/
            Intent intent = new Intent(FCD_DashboardActivity.this, FCD_ReferAFriend.class);
            //Intent intent = new Intent(FCD_DashboardActivity.this, FCD_WithDrawRequest.class);
            startActivity(intent);
        } else if (id == R.id.help) {
          /*  FCD_Help fcdHelp = new FCD_Help();
            FragmentTransaction fragmentTransactionfcdHelp = fragmentManager.beginTransaction();
            fragmentTransactionfcdHelp.replace(R.id.content_frame, fcdHelp);
            fragmentTransactionfcdHelp.commit();
            txt_toolbar.setText(R.string.help);*/

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FCD_DashboardActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.help_alert, null);
            dialogBuilder.setView(dialogView);

            AC_Textview txt_cancel = dialogView.findViewById(R.id.txt_cancel);
            AC_Textview txt_call = dialogView.findViewById(R.id.txt_call);
            LoaderTextView lt_helpNumber = dialogView.findViewById(R.id.lt_helpNumber);

            lt_helpNumber.setText(FCD_Common.Driverhelp);
            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setCancelable(false);
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            txt_cancel.setOnClickListener(view -> alertDialog.dismiss());



            txt_call.setOnClickListener(v -> {
                FCD_Common.phone=FCD_Common.Driverhelp;
                if (Build.VERSION.SDK_INT < 23) {
                    phoneCall();
                }else {
                    if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(dashContext),
                            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        phoneCall();
                    }else {
                        final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                        //Asking request Permissions
                        ActivityCompat.requestPermissions(FCD_DashboardActivity.this, PERMISSIONS_STORAGE, 9);
                    }
                }

        });
            alertDialog.show();

        } else if (id == R.id.today_earning) {
           /* FCD_TodaysEarnings todaysEarnings = new FCD_TodaysEarnings();
            FragmentTransaction fragmentTransactiontodaysEarnings = fragmentManager.beginTransaction();
            fragmentTransactiontodaysEarnings.replace(R.id.content_frame, todaysEarnings);
            fragmentTransactiontodaysEarnings.commit();
            txt_toolbar.setText(R.string.todays_earnings);*/

            Intent intent = new Intent(FCD_DashboardActivity.this, FCD_TodaysEarnings.class);
            startActivity(intent);

        } else if (id == R.id.weekly_earning) {
            /*FCD_WeeklyEarnings fcdWeeklyEarningsc = new FCD_WeeklyEarnings();
            FragmentTransaction fragmentTransactionfcdWeeklyEarningsc = fragmentManager.beginTransaction();
            fragmentTransactionfcdWeeklyEarningsc.replace(R.id.content_frame, fcdWeeklyEarningsc, "weeklyEarnings");
            fragmentTransactionfcdWeeklyEarningsc.commit();
            txt_toolbar.setText(R.string.weeks_earnings);*/

            Intent intent = new Intent(FCD_DashboardActivity.this, FCD_WeeklyEarnings.class);
            startActivity(intent);
        }
        else if (id == R.id.incentive) {
           /* FCD_Incentives fcdincentives = new FCD_Incentives();
            FragmentTransaction fragmentTransactionfcdincentives = fragmentManager.beginTransaction();
            fragmentTransactionfcdincentives.replace(R.id.content_frame, fcdincentives, "incentives");
            fragmentTransactionfcdincentives.commit();
            txt_toolbar.setText(R.string.incentives);*/

            Intent intent = new Intent(FCD_DashboardActivity.this, FCD_Incentives.class);
            startActivity(intent);
        }
        else if(id == R.id.wallethistory)
        {
           /* FCD_History fcdhistory = new FCD_History();
            FragmentTransaction fragmentTransactionfcdWallethistory = fragmentManager.beginTransaction();
            fragmentTransactionfcdWallethistory.replace(R.id.content_frame, fcdhistory, "History");
            fragmentTransactionfcdWallethistory.commit();
            txt_toolbar.setText(R.string.wallet_history);*/
            Intent intent = new Intent(FCD_DashboardActivity.this, FCD_RideHistory.class);
            startActivity(intent);

        }
        else if (id == R.id.login_duration) {
            /*FCD_LoginDuration loginDuration = new FCD_LoginDuration();
            FragmentTransaction fragmentTransactionloginDuration = fragmentManager.beginTransaction();
            fragmentTransactionloginDuration.replace(R.id.content_frame, loginDuration, "loginDuration");
            fragmentTransactionloginDuration.commit();
            txt_toolbar.setText(R.string.login_duration);*/
            Intent intent = new Intent(FCD_DashboardActivity.this, FCD_LoginDuration.class);
            startActivity(intent);
        } else if (id == R.id.floating_cash) {
            /*FCD_FloatingCash floatingCash = new FCD_FloatingCash();
            FragmentTransaction fragmentTransactionfloatingCash = fragmentManager.beginTransaction();
            fragmentTransactionfloatingCash.replace(R.id.content_frame, floatingCash, "floatingCash");
            fragmentTransactionfloatingCash.commit();
            txt_toolbar.setText(R.string.floating_cash);*/
            Intent intent = new Intent(FCD_DashboardActivity.this, FCD_FloatingCash.class);
            startActivity(intent);
        }
        else if (id == R.id.wallet) {
           /* RequestOrderDialog deliverdTheOrderDialog = RequestOrderDialog.newInstance();
            deliverdTheOrderDialog.show(Objects.requireNonNull(FCD_DashboardActivity.this).getSupportFragmentManager(),
                    "requestOrderDialog");
            txt_toolbar.setText(R.string.wallet);*/

           Intent intent = new Intent(FCD_DashboardActivity.this, FCD_Wallet.class);
           startActivity(intent);
           /* FCD_WithDrawRequest withdraw = new FCD_WithDrawRequest();
            FragmentTransaction fragmentTransactionliveOrders = fragmentManager.beginTransaction();
            fragmentTransactionliveOrders.replace(R.id.content_frame, withdraw);
            fragmentTransactionliveOrders.commit();
            txt_toolbar.setText(R.string.withdraw_request);*/
        }
        else if (id==R.id.logout){
            androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(Objects.requireNonNull(FCD_DashboardActivity.this));
            LayoutInflater inflater = FCD_DashboardActivity.this.getLayoutInflater();
            @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.alert_exit, null);
            AC_Textview txt_message =  dialogView.findViewById(R.id.txt_alertMessage);
            AC_BoldTextview txt_no = dialogView.findViewById(R.id.txt_no);
            AC_BoldTextview txt_yes = dialogView.findViewById(R.id.txt_yes);
            dialogBuilder.setView(dialogView);

            alertDialog = dialogBuilder.create();
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setCancelable(false);
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            txt_message.setText(FCD_DashboardActivity.this.getResources().getString(R.string.conform_logout));

            txt_no.setOnClickListener(v -> alertDialog.dismiss());

            txt_yes.setOnClickListener(v -> {
                Utils.toast(FCD_DashboardActivity.this,"Loading please wait");

                Logout();

            });

            alertDialog.show();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void Logout() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FCD_URL.URL_LOGOUT,
                ServerResponse -> {

                    Log.d("ServerResponse", "" + ServerResponse);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        //FC_Common.status = jsonResponse1.getString("success");
                        FCD_Common.success = jsonResponse1.getString("success");
                        FCD_Common.message = jsonResponse1.getString("message");
                        if (FCD_Common.success.equalsIgnoreCase("1"))
                        {
                            FCD_SharedPrefManager.deleteAllSharePrefs();// Deleting all shared preference data
                            SharedPreferences sharedPreferences = getSharedPreferences("MyLogin.txt", Context.MODE_PRIVATE);
                            sharedPreferences.edit().clear().apply();
                           // boolean loginCheck = sharedPreferences.getBoolean("FirstLogin", false);
                            FCD_Logout preferenceManager = new FCD_Logout(getApplicationContext());
                            preferenceManager.setFirstTimeLaunch(true);

                            Intent intent = new Intent(FCD_DashboardActivity.this, FCD_SplashActivity.class);
                            startActivity(intent);
                            finish();

                            alertDialog.dismiss();
                        }else
                        {
                            alertDialog.dismiss();
                            snackBar(FCD_Common.message);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar(String.valueOf(e));
                        alertDialog.dismiss();
                    }
                }, volleyError -> {
            String value = volleyError.toString();
            snackBar(value);
            alertDialog.dismiss();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                Log.d("fdgdfgfdg","sdfgsdgs"+FCD_Common.token_type+" "+FCD_Common.access_token);
                params.put("Authorization", FCD_Common.token_type+" "+FCD_Common.access_token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(FCD_DashboardActivity.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }
    @Override
    public void dutyStarted(int value) {
        FCD_Common.dutyStarted = value;
        checkPermission();
        FCD_HomeFragment homeFragment = (FCD_HomeFragment) getSupportFragmentManager().findFragmentByTag("homeFragment");
        if (homeFragment != null) {
            homeFragment.DutyStartedlayoutChange();
        }
        if (FCD_Common.dutyStarted == 1) {
            sw_dutyStatus.setChecked(true);
            DutyUpdate();
        } else {
            sw_dutyStatus.setChecked(false);
            DutyUpdate();
        }
    }

    private void DutyUpdate() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FCD_URL.URL_DRIVER_DUTY_STARTED,
                ServerResponse -> {

                    Log.d("ServerResponse", "dashboard12" + ServerResponse);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        FCD_Common.status = jsonResponse1.getString("success");
                        if (FCD_Common.status.equalsIgnoreCase("1")) {
                            DefaultFragment();
                            Toast.makeText(getApplicationContext(), "Status Updated", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("xcgsdgsdgsd", "dfhdf" + e);
                        snackBar(String.valueOf(e));
                    }
                }, volleyError -> {
            String value = volleyError.toString();
            snackBar(value);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("duty_status", String.valueOf(FCD_Common.dutyStarted));
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
        RequestQueue requestQueue = Volley.newRequestQueue(FCD_DashboardActivity.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isOnline(isConnected);
    }

    private void isOnline(boolean isConnected) {

        if (isConnected) {

            ll_noInternetConnection.setVisibility(View.GONE);
            content_frame.setVisibility(View.VISIBLE);
            content_frame.setFocusable(true);
            content_frame.setFocusableInTouchMode(true);
        } else {
            ll_noInternetConnection.setVisibility(View.VISIBLE);
            content_frame.setVisibility(View.GONE);
            content_frame.setFocusable(false);
            content_frame.setFocusableInTouchMode(false);
            UtilsStatic.noInternetConnection(ll_noInternetConnection, FCD_DashboardActivity.this);
        }


    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(FCD_DashboardActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NotNull String[] permissions, @NotNull int[] grantResults) {
        boolean permissionGranted = false;
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    //  mGoogleMap.setMyLocationEnabled(true);
                }

            } else {
              Log.d("fhdfgfdg","fdgfdg");
            }
        }
        if (requestCode == 9) {
            permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
        if(permissionGranted){
            phoneCall();
        }
    }

    private void snackBar(final String value) {
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(50);
                    bar = Snackbar.make(parentLayout, value, Snackbar.LENGTH_LONG)
                            .setAction("Dismiss", v -> bar.dismiss());
                    bar.setActionTextColor(Color.RED);
                    TextView tv = bar.getView().findViewById(R.id.snackbar_text);
                    tv.setTextColor(Color.CYAN);
                    bar.show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        timerThread.start();
    }

    @Override
    public void dutyStartedLiveOrder(int value) {
        FCD_Common.dutyStarted = value;
        if (FCD_Common.dutyStarted == 1) {
            sw_dutyStatus.setChecked(true);
        } else {
            sw_dutyStatus.setChecked(false);
        }

    }

    private void phoneCall() {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(dashContext),
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" +  FCD_Common.phone));
            startActivity(callIntent);
        } else {
            Toast.makeText(dashContext, "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }


}
