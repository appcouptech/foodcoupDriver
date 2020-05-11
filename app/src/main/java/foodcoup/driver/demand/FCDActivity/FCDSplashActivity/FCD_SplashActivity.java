package foodcoup.driver.demand.FCDActivity.FCDSplashActivity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import foodcoup.driver.demand.FCDActivity.FCDDashboardActivity.FCD_DashboardActivity;
import foodcoup.driver.demand.FCDActivity.FCDLoginActivity.FCD_LoginActivity;
import foodcoup.driver.demand.FCDUtils.NetworkChecking.NetworkChangeReceiver;
import foodcoup.driver.demand.FCDViews.FCD_Common;
import foodcoup.driver.demand.FCDViews.FCD_SharedPrefManager;
import foodcoup.driver.demand.FCDViews.FCD_URL;
import foodcoup.driver.demand.FCDViews.FCD_User;
import foodcoup.driver.demand.FCDViews.Utils;
import foodcoup.driver.demand.R;

public class FCD_SplashActivity extends AppCompatActivity implements NetworkChangeReceiver.ConnectivityReceiverListener {
    Boolean loginCheck;
    String versionName="";
    int versionCode;
    TextView nonet;
    ImageView imgnonet;
    Snackbar bar;
    LinearLayout linnonet ,ll_noInternetConnection;
    NetworkChangeReceiver networkChangeReceiver ;
    IntentFilter intentFilter ;
    FCD_User user;
    final static int REQUEST_LOCATION = 199;
    double latitude; // latitude
    double longitude;
    private GoogleSignInClient mGoogleSignInClient;
    FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationRequest;
    List<Location> locationList = new ArrayList<>();
    FrameLayout frame_main;
    TextView txt_poweredBy ;
    View parentLayout;
    LocationManager locationManager ;
    boolean GpsStatus ;
    private Context context;
    private Handler handler;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcd__splash);
        context = getApplicationContext();
        //checkPermission();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        nonet=findViewById(R.id.nonet);
        frame_main=findViewById(R.id.frame_main);
        ll_noInternetConnection =  findViewById(R.id.ll_noInternetConnection);
        linnonet = findViewById(R.id.linnonet);
        imgnonet = findViewById(R.id.imgnonet);
        txt_poweredBy = findViewById(R.id.txt_poweredBy);
        parentLayout = findViewById(android.R.id.content);
        networkChangeReceiver = new NetworkChangeReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        user = FCD_SharedPrefManager.getInstance(FCD_SplashActivity.this).getUser();
        FCD_Common.token_type = String.valueOf(user.gettoken_type());
        FCD_Common.access_token = String.valueOf(user.getaccess_token());
        FCD_Common.vehicle_plate = String.valueOf(user.getvehicle_plate());
        FCD_Common.vehicle_color = String.valueOf(user.getvehicle_color());
        FCD_Common.vehicle_type = String.valueOf(user.getvehicle_type());
        FCD_Common.vehicle_description = String.valueOf(user.getvehicle_description());
        FCD_Common.name = String.valueOf(user.getname());
        FCD_Common.mobile = String.valueOf(user.getmobile());
        FCD_Common.email = String.valueOf(user.getemail());
        FCD_Common.email = String.valueOf(user.getemail());
        FCD_Common.location_lng = String.valueOf(user.getlocation_lng());
        FCD_Common.device_id = String.valueOf(user.getdevice_id());
        FCD_Common.token_type = String.valueOf(user.gettoken_type());
        FCD_Common.access_token = String.valueOf(user.getaccess_token());


        boolean isConnected = NetworkChangeReceiver.isConnected(this);
        isOnline(isConnected);

        LocationManager lm = (LocationManager)FCD_SplashActivity.this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {

            assert lm != null;
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!gps_enabled){
                Log.d("Dfhdfgfdgfdg","dfgfdgfdgdf");
                FCD_Common.gpsenabled="false";
            }
            else
            {
                Log.d("Dfhdfgfdgfdg","24435dfgfdgfdgdf");
                FCD_Common.gpsenabled="true";
            }
        } catch(NullPointerException ex) {
            ex.printStackTrace();
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }

      //  AccessCheck();
        Log.d("fghdfhdfgh","dfgdfgdf");

    }
    @Override
    protected void onResume() {
        super.onResume();
        boolean isConnected = NetworkChangeReceiver.isConnected(this);
        isOnline(isConnected);
        Log.d("fghdfhdfgh","svxcvxcv");
        LocationManager lm = (LocationManager)FCD_SplashActivity.this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {

            assert lm != null;
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!gps_enabled){
                Log.d("Dfhdfgfdgfdg","dfgfdgfdgdf");
                FCD_Common.gpsenabled="false";
            }
            else
            {
                Log.d("Dfhdfgfdgfdg","24435dfgfdgfdgdf");
                FCD_Common.gpsenabled="true";
            }
        } catch(NullPointerException ex) {
            ex.printStackTrace();
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        AccessCheck();
        registerReceiver(networkChangeReceiver, intentFilter);
        networkChangeReceiver.setConnectivityReceiverListener(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isOnline(isConnected);

        CurrentTask();
        Log.d("connection Splash","True");
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void CurrentTask() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FCD_URL.URL_CURRENTTASK,
                ServerResponse -> {

                    Log.d("ServerResponse", "Splash" + ServerResponse);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        FCD_Common.status = jsonResponse1.getString("success");

                        if (FCD_Common.status.equalsIgnoreCase("1")) {
                            FCD_Common.currentTask = jsonResponse1.getInt("current_task");

                            AccessCheck();
                        }
                        else {
                                Intent intent = new Intent(FCD_SplashActivity.this, FCD_LoginActivity.class);
                                startActivity(intent);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("xcgsdgsdgsd", "dfhdf" + e);
                    }
                }, volleyError -> {

        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                Log.d("fdgdfgfdg", "sdfgsdgs" + FCD_Common.token_type + " " + FCD_Common.access_token);
                params.put("Authorization", FCD_Common.token_type + " " + FCD_Common.access_token);
                params.put("X-Requested-With", FCD_Common.XMLCODE);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(FCD_SplashActivity.this));
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void AccessCheck() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FCD_URL.URL_TOKENVALIDATE,
                ServerResponse -> {

                    Log.d("ServerResponse", "Splash" + ServerResponse);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        FCD_Common.status = jsonResponse1.getString("success");

                        if (FCD_Common.status.equalsIgnoreCase("1")) {
                            if (FCD_Common.gpsenabled.equalsIgnoreCase("true")){
                            Utils.removeInternetConnection(ll_noInternetConnection);
                            ll_noInternetConnection.setVisibility(View.GONE);

                          /*  Thread timerThread = new Thread() {
                                @SuppressLint("HardwareIds")
                                public void run() {
                                    try {
                                        sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                }
                            };
                            timerThread.start();*/

                                try {

                                    @SuppressLint("HardwareIds") String token = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                                    Log.d("sdgsdgsd", "dfdf" + token);
                                    FCD_Common.device_id = Settings.Secure.getString(FCD_SplashActivity.this.getContentResolver(),
                                            Settings.Secure.ANDROID_ID);


                                    Log.d("xgdfgdfgfd", "device_id: " + FCD_Common.device_id);

                                    Utils.log(FCD_SplashActivity.this, "dghdfgdf" + "sdfsdfgsd");
                                    PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                                    versionName = packageInfo.versionName;
                                    versionCode = packageInfo.versionCode;
                                    SharedPreferences sharedPreferences = getSharedPreferences("MyLogin.txt", Context.MODE_PRIVATE);
                                    loginCheck = sharedPreferences.getBoolean("FirstLogin", false);
                                    Utils.log(FCD_SplashActivity.this, " loginCheck: " + loginCheck);

                                    if (loginCheck) {
                                        if (FCD_Common.gpsenabled.equalsIgnoreCase("true")) {
                                            if (FCD_Common.token_type.equalsIgnoreCase("") || FCD_Common.access_token.equalsIgnoreCase("")) {
                                                Log.d("sdgsdgsdgsd", "grtdfgb");
                                                Intent intent = new Intent(FCD_SplashActivity.this, FCD_LoginActivity.class);
                                                intent.putExtra("gpsenabled", FCD_Common.gpsenabled);
                                                startActivity(intent);
                                            } else {

                                                Log.d("sdgsdgsdgsd", "46nbv");
                                                Intent intent = new Intent(FCD_SplashActivity.this, FCD_DashboardActivity.class);
                                                startActivity(intent);

                                            }
                                        } else {
                                            snackBar(getResources().getString(R.string.current_loc));
                                            CheckGpsStatus();
                                        }


                                        Log.d("sdgsdgsdgsd", "1243sdfsdfsdfsd");
                                    } else {
                                        if (FCD_Common.gpsenabled.equalsIgnoreCase("true")) {
                                            Intent intent = new Intent(FCD_SplashActivity.this, FCD_LoginActivity.class);
                                            startActivity(intent);
                                        } else {
                                            snackBar(getResources().getString(R.string.current_loc));
                                            CheckGpsStatus();
                                        }
                                        Log.d("sdgsdgsdgsd", "sdfsdfsdfsd");
                                    }
                                } catch (PackageManager.NameNotFoundException e1) {
                                    e1.printStackTrace();
                                    Log.d("dgdssdvsd", "sdfsd" + e1);
                                }
                        }
                            else {
                                snackBar(getResources().getString(R.string.current_loc));
                                CheckGpsStatus();
                            }

                    }
                        else {
                            if (FCD_Common.gpsenabled.equalsIgnoreCase("true")) {
                                Intent intent = new Intent(FCD_SplashActivity.this, FCD_LoginActivity.class);
                                startActivity(intent);
                            }
                            else {
                                snackBar(getResources().getString(R.string.current_loc));
                                CheckGpsStatus();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("xcgsdgsdgsd", "dfhdf" + e);
                    }
                }, volleyError -> {

        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                Log.d("fdgdfgfdg", "sdfgsdgs" + FCD_Common.token_type + " " + FCD_Common.access_token);
                params.put("Authorization", FCD_Common.token_type + " " + FCD_Common.access_token);
                params.put("X-Requested-With", FCD_Common.XMLCODE);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(FCD_SplashActivity.this));
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    private void isOnline(boolean isConnected) {

        if (isConnected){


            frame_main.setVisibility(View.VISIBLE);
            ll_noInternetConnection.setVisibility(View.GONE);

        }
        else {
            frame_main.setVisibility(View.GONE);
            ll_noInternetConnection.setVisibility(View.VISIBLE);
            Utils.noInternetConnection(ll_noInternetConnection,FCD_SplashActivity.this);
        }
    }

    private void enableLoc() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
      /* locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);*/

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, locationSettingsResponse -> {

        });
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(FCD_SplashActivity.this,
                                REQUEST_LOCATION);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
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

    public void CheckGpsStatus(){
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(GpsStatus == true) {
            AccessCheck();
           // textview.setText("GPS Is Enabled");
        } else {
           Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent1);
           // textview.setText("GPS Is Disabled");
        }
    }

}
