package foodcoup.driver.demand.FCDActivity.FCDLoginActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialPickerConfig;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsClient;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import foodcoup.driver.demand.FCDActivity.FCDDashboardActivity.FCD_DashboardActivity;
import foodcoup.driver.demand.FCDActivity.FCDSplashActivity.FCD_SplashActivity;
import foodcoup.driver.demand.FCDUtils.CountryPicker.CountryCodePicker;
import foodcoup.driver.demand.FCDUtils.NetworkChecking.NetworkChangeReceiver;
import foodcoup.driver.demand.FCDViews.AC_BoldTextview;
import foodcoup.driver.demand.FCDViews.AC_Textview;
import foodcoup.driver.demand.FCDViews.FCD_Common;
import foodcoup.driver.demand.FCDViews.FCD_SharedPrefManager;
import foodcoup.driver.demand.FCDViews.FCD_URL;
import foodcoup.driver.demand.FCDViews.FCD_User;
import foodcoup.driver.demand.FCDViews.Utils;
import foodcoup.driver.demand.R;

public class FCD_LoginActivity extends AppCompatActivity implements NetworkChangeReceiver.ConnectivityReceiverListener {

    public static final int RC_HINT = 1000;
    EditText txtotp,txt_countrywithnumber;
    CredentialsClient mCredentialsClient;
    LinearLayout ll_noInternetConnection;
    View parentLayout;
    NetworkChangeReceiver networkChangeReceiver;
    Snackbar bar;
    TextView txtsignin,counttime,resendotp,txtotpverify,txt_number;
    private LinearLayout ll_login ,ll_recentOtp ;
    ProgressBar progressbar;
    CountryCodePicker countrypicker;
    LinearLayout lin_country;
    IntentFilter intentFilter;
    String selected_country_code;
    InputMethodManager inputMgr;
    LinearLayout ll_loginMain ,ll_field;
    FCD_User user;
    View view_otp ;
    Context context;
    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcd__login);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        context = FCD_LoginActivity.this ;

        user = FCD_SharedPrefManager.getInstance(FCD_LoginActivity.this).getUser();
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
       // FCD_Common.device_id = String.valueOf(user.getdevice_id());
        FCD_Common.token_type = String.valueOf(user.gettoken_type());
        FCD_Common.access_token = String.valueOf(user.getaccess_token());


        lin_country = findViewById(R.id.lin_country);
        txtotp = findViewById(R.id.txtotp);
        txt_number = findViewById(R.id.txt_number);
        txtotpverify = findViewById(R.id.txtotpverify);
        view_otp= findViewById(R.id.view_otp);

        txtsignin = findViewById(R.id.txtsignin);
        ll_recentOtp = findViewById(R.id.ll_recentOtp);
        ll_login = findViewById(R.id.ll_login);
        ll_loginMain = findViewById(R.id.ll_loginMain);
        ll_field = findViewById(R.id.ll_field);
        counttime=findViewById(R.id.counttime);
        txt_countrywithnumber=findViewById(R.id.txt_countrywithnumber);
        resendotp=findViewById(R.id.resendotp);
        networkChangeReceiver = new NetworkChangeReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        ll_noInternetConnection = findViewById(R.id.ll_noInternetConnection);
        countrypicker = findViewById(R.id.countrypicker);
        selected_country_code = countrypicker.getDefaultCountryCode();
        Log.d("fghdgdfgdfg","dgsfgsdfs"+selected_country_code);

        @SuppressLint("HardwareIds") String token = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("sdgsdgsd", "dfdf" + token);
        FCD_Common.device_id = Settings.Secure.getString(FCD_LoginActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        return;
                    }
                    FCD_Common.devicetoken = task.getResult().getToken();
                    Log.d("sdgsdgsdgsd",""+FCD_Common.devicetoken);
                    // Log and toast
                    @SuppressLint({"StringFormatInvalid", "LocalSuppress"})
                    String msg = getString(R.string.app_name, FCD_Common.devicetoken);
                });
        countrypicker.setOnCountryChangeListener(() -> {
            //Alert.showMessage(RegistrationActivity.this, countrypicker.getSelectedCountryCodeWithPlus());
            selected_country_code= countrypicker.getSelectedCountryCode();
            Log.d("dfgdfgdfg","dfgdf"+selected_country_code);
        });
        progressbar = findViewById(R.id.progressbar);

        Log.d("dfgdfgfd","fgdfgfdg");
        boolean isConnected = NetworkChangeReceiver.isConnected(this);
        isOnline(isConnected);

        Intent intent1 = getIntent();
        Bundle bundle = intent1.getExtras();

        if (bundle != null)
        {
            FCD_Common.gpsenabled = (String) bundle.get("gpsenabled");
        }

        inputMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        parentLayout = findViewById(android.R.id.content);
        HintRequest hintRequest = new HintRequest.Builder()
                .setHintPickerConfig(new CredentialPickerConfig.Builder()
                        .setShowCancelButton(true)
                        .build())
                .setPhoneNumberIdentifierSupported(true)
                .build();
        mCredentialsClient = Credentials.getClient(this);
        PendingIntent intent = mCredentialsClient.getHintPickerIntent(hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(),
                    RC_HINT, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
        //requestStoragePermission();

        txt_number.setOnClickListener(v -> {
            txt_number.setVisibility(View.GONE);
            ll_recentOtp.setVisibility(View.GONE);
            //resendotp.setVisibility(View.GONE);
            txtotp.setVisibility(View.GONE);
            counttime.setVisibility(View.GONE);
            lin_country.setVisibility(View.VISIBLE);
            countrypicker.setVisibility(View.VISIBLE);
            ll_login.setVisibility(View.VISIBLE);
            //txtsignin.setVisibility(View.VISIBLE);
        });

        ll_login.setOnClickListener(v -> {
            inputMgr.hideSoftInputFromWindow(ll_login.getWindowToken(), 0);
            //lin_country.setVisibility(View.GONE);
            counttime.setVisibility(View.GONE);
            ll_recentOtp.setVisibility(View.GONE);
            //String test = countrypicker.getDefaultCountryCode();
            //Log.d("fghdgdfgdfg","dgsfgsdfs"+test);
            Log.d("vxdfdfsd", "24343passss");
            if (txt_number.getVisibility() == View.VISIBLE)
            {
                Log.d("vxdfdfsd", "passss");

                if (txt_number.getText().toString().equalsIgnoreCase("")) {
                    counttime.setVisibility(View.GONE);
                    String value="Phone Number cannot Be Empty";

                    snackBar(value);
                }
                else
                {
                    FCD_Common.mobilenumber=txt_number.getText().toString();
                    loginExecute();
                }

                //maxID = 0;
            }
            else if (txt_countrywithnumber.getVisibility()== View.VISIBLE) {
                Log.d("vxdfdfsd", "fail");
                if (txt_countrywithnumber.getText().toString().equalsIgnoreCase("")) {
                    counttime.setVisibility(View.GONE);
                    String value="Phone Number cannot Be Empty";
                    snackBar(value);
                }
                else
                {

                    FCD_Common.mobilenumber="+"+selected_country_code+" "+txt_countrywithnumber.getText().toString();
                    loginExecute();
                }
            }
            else {
                Log.d("vxdfdfsd", "dfghdghfail");
            }


        });

        ll_loginMain.setOnClickListener(view -> {
            inputMgr.hideSoftInputFromWindow(ll_loginMain.getWindowToken(), 0);
        });

        ll_field.setOnClickListener(view -> {
            inputMgr.hideSoftInputFromWindow(ll_field.getWindowToken(), 0);
        });

        ll_recentOtp.setOnClickListener(v -> {
            txtotp.setText("");
            txtotp.setHint(getResources().getString(R.string.otp));
            inputMgr.hideSoftInputFromWindow(ll_recentOtp.getWindowToken(), 0);
           // lin_country.setVisibility(View.GONE);
            counttime.setVisibility(View.GONE);
            ll_recentOtp.setVisibility(View.GONE);

            Log.d("vxdfdfsd", "24343passss");
            if (txt_number.getVisibility() == View.VISIBLE)
            {
                Log.d("vxdfdfsd", "passss");

                if (txt_number.getText().toString().equalsIgnoreCase("")) {
                    counttime.setVisibility(View.GONE);
                    String value="Phone Number cannot Be Empty";
                    ll_recentOtp.setVisibility(View.VISIBLE);
                    snackBar(value);
                }
                else
                {
                    FCD_Common.mobilenumber=txt_number.getText().toString();
                    loginExecute();
                }

                //maxID = 0;
            }
            else if (txt_countrywithnumber.getVisibility()== View.VISIBLE) {
                Log.d("vxdfdfsd", "fail");
                if (txt_countrywithnumber.getText().toString().equalsIgnoreCase("")) {
                    counttime.setVisibility(View.GONE);
                    String value="Phone Number cannot Be Empty";
                    ll_recentOtp.setVisibility(View.VISIBLE);
                    snackBar(value);
                }
                else
                {
                    FCD_Common.mobilenumber="+"+selected_country_code+" "+txt_countrywithnumber.getText().toString();
                    // Toast.makeText(getApplicationContext(),"dfgdfg"+FC_Common.mobilenumber,Toast.LENGTH_LONG).show();
                    loginExecute();


                }
            }
            else {
                Log.d("vxdfdfsd", "dfghdghfail");
            }

        });
        txtotp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.d("dgddsfd","gsdgsdf"+ FCD_Common.otplength);
                if(txtotp.getText().toString().length()>= FCD_Common.otplength)     //size as per your requirement
                {
                    inputMgr.hideSoftInputFromWindow(txtotp.getWindowToken(), 0);
                    FCD_Common.otpcheck = txtotp.getText().toString().trim();
                    if (FCD_Common.otpcheck.equalsIgnoreCase(FCD_Common.otp))
                    {

                        inputMgr.hideSoftInputFromWindow(txtotp.getWindowToken(), 0);
                        Log.d("fgghfghfghfg","dgdfgdfgfdg"+ FCD_Common.user_type);
                        Log.d("fgghfghfghfg","dgdfgdfgfdg"+ FCD_Common.mobilenumber);

                            loginCheck();

                    }
                    else
                    {
                        String value = "OTP Mismatches or Wrong OTp";
                        snackBar(value);
                        Log.d("fgghfghfghfg","32423421dgdfgdfgfdg");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void loginExecute() {
        Utils.playProgressBar(FCD_LoginActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FCD_URL.URL_LOGINOTP,
                ServerResponse -> {

                    Log.d("ServerResponse", "Login" + ServerResponse);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        FCD_Common.status = jsonResponse1.getString("success");
                        FCD_Common.message = jsonResponse1.getString("message");
                        FCD_Common.user_type = jsonResponse1.getString("user_type");
                        FCD_Common.otp = jsonResponse1.getString("otp");
                        FCD_Common.otplength= FCD_Common.otp.length();
                        if (FCD_Common.status.equalsIgnoreCase("1")) {


                            Utils.stopProgressBar();
                            counttime.setVisibility(View.VISIBLE);
                            txtotp.setVisibility(View.VISIBLE);
                            view_otp.setVisibility(View.VISIBLE);
                            ll_login.setVisibility(View.GONE);
                            if (txt_number.getVisibility() == View.VISIBLE)
                            {
                                lin_country.setVisibility(View.GONE);
                                txt_number.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                lin_country.setVisibility(View.VISIBLE);
                                txt_number.setVisibility(View.GONE);
                            }

                            new CountDownTimer(10000, 1000) {

                                @SuppressLint("SetTextI18n")
                                public void onTick(long millisUntilFinished) {
                                    counttime.setText("Seconds Remaining: " + millisUntilFinished / 1000);
                                    if (txt_number.getVisibility() == View.VISIBLE)
                                    {
                                        lin_country.setVisibility(View.GONE);
                                        txt_number.setVisibility(View.VISIBLE);
                                    }
                                    else
                                    {
                                        lin_country.setVisibility(View.VISIBLE);
                                        txt_number.setVisibility(View.GONE);
                                    }
                                }

                                public void onFinish() {

                                    //txtotpverify.setVisibility(View.GONE);
                                    counttime.setVisibility(View.GONE);
                                    ll_login.setVisibility(View.GONE);
                                    ll_recentOtp.setVisibility(View.VISIBLE);
                                    txtotp.setVisibility(View.INVISIBLE);
                                    view_otp.setVisibility(View.INVISIBLE);
                                    if (txt_number.getVisibility() == View.VISIBLE)
                                    {
                                        lin_country.setVisibility(View.GONE);
                                        txt_number.setVisibility(View.VISIBLE);
                                    }
                                    else
                                    {
                                        lin_country.setVisibility(View.VISIBLE);
                                        txt_number.setVisibility(View.GONE);
                                    }

                                }
                            }.start();
                        } else {
                            Utils.stopProgressBar();
                            progressbar.setVisibility(View.GONE);
                            snackBar(FCD_Common.message);
                            counttime.setVisibility(View.GONE);
                            if (txt_number.getVisibility() == View.VISIBLE)
                            {
                                lin_country.setVisibility(View.GONE);
                                txt_number.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                lin_country.setVisibility(View.VISIBLE);
                                txt_number.setVisibility(View.GONE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utils.stopProgressBar();
                        Log.d("fdhbdfghdf", "xzvxzcxzc" + e);
                        snackBar(String.valueOf(e));
                        counttime.setVisibility(View.GONE);
                        if (txt_number.getVisibility() == View.VISIBLE)
                        {
                            lin_country.setVisibility(View.GONE);
                            txt_number.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            lin_country.setVisibility(View.VISIBLE);
                            txt_number.setVisibility(View.GONE);
                        }
                    }
                }, volleyError -> {
            String value = volleyError.toString();
            snackBar(value);
            Utils.stopProgressBar();
            counttime.setVisibility(View.GONE);
            if (txt_number.getVisibility() == View.VISIBLE)
            {
                lin_country.setVisibility(View.GONE);
                txt_number.setVisibility(View.VISIBLE);
            }
            else
            {
                lin_country.setVisibility(View.VISIBLE);
                txt_number.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Log.d("fgjfghfghfg","dfhgfhfghf"+ FCD_Common.mobilenumber);
                params.put("mobile", FCD_Common.mobilenumber);
                params.put("device_token", FCD_Common.devicetoken);
                params.put("device_id", FCD_Common.device_id);
                Log.d("getParams: ", "" + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }
    private void loginCheck() {
        Utils.playProgressBar(FCD_LoginActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FCD_URL.URL_LOGIN,
                ServerResponse -> {

                    Log.d("ServerResponse", "LoginActivity" + ServerResponse);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        FCD_Common.status = jsonResponse1.getString("success");


                        if (FCD_Common.status.equalsIgnoreCase("1")) {

                            FCD_Common.token_type = jsonResponse1.getString("token_type");
                            FCD_Common.access_token = jsonResponse1.getString("access_token");
                            Utils.stopProgressBar();
                            GetUserDetails();

                        } else {
                            Utils.stopProgressBar();
                            //Utils.showExitDialog(FCD_LoginActivity.this, "Your number is not registered \n Please try again");
                            progressbar.setVisibility(View.GONE);
                            snackBar(FCD_Common.message);

                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(context));
                            LayoutInflater inflater = getLayoutInflater();
                            @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.alert_exit, null);
                            dialogBuilder.setView(dialogView);

                            AC_Textview txt_alertMessage = dialogView.findViewById(R.id.txt_alertMessage);
                            txt_alertMessage.setVisibility(View.VISIBLE);
                            txt_alertMessage.setText(getResources().getString(R.string.registerdriver));
                            AC_BoldTextview txt_no = dialogView.findViewById(R.id.txt_no);
                            AC_BoldTextview txt_yes = dialogView.findViewById(R.id.txt_yes);
                            txt_yes.setText(getResources().getString(R.string.register));

                            final AlertDialog alertDialog = dialogBuilder.create();
                            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            alertDialog.setCancelable(false);
                            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



                            txt_no.setOnClickListener(view12 -> alertDialog.dismiss());

                            txt_yes.setOnClickListener(view1 -> {

                                String url = "http://www.google.com";
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                                alertDialog.dismiss();
                            });
                            alertDialog.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("fdhbdfghdf", "dfhdf" + e);
                        snackBar(String.valueOf(e));
                        Utils.stopProgressBar();
                    }
                }, volleyError -> {
            String value = volleyError.toString();
            Utils.stopProgressBar();
            snackBar(value);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Log.d("fgjfghfghfg","dfhgfhfghf"+ FCD_Common.mobilenumber);
                params.put("mobile", FCD_Common.mobilenumber);
                params.put("otp", FCD_Common.otp);
                params.put("device_token", FCD_Common.devicetoken);
                params.put("device_id", FCD_Common.device_id);
                Log.d("getParams: ", "" + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_HINT) {
            if (data != null) {
                //Credential cred = data.getParcelableExtra(Credential.EXTRA_KEY);
                Credential cred = data.getParcelableExtra(Credential.EXTRA_KEY);
                Log.d("fdgdfgdfg","sdgsd"+cred);
                if (cred != null) {
                    final String unformattedPhone = cred.getId();
                    String formattedPhone;
                    String code = Locale.getDefault().getCountry();
                    Log.d("fdgdfgdfg","fdgdfgd : "+code+"-"+unformattedPhone);
                    formattedPhone = PhoneNumberUtils.formatNumber(unformattedPhone,Locale.getDefault().getCountry());
                    // formattedPhone = PhoneNumberUtils.formatNumber(unformattedPhone);
                    txt_number.setText("" +formattedPhone);

                    Log.d("dghdfhdfh","13223fdhdfghfd");
                    txt_number.setVisibility(View.VISIBLE);
                    countrypicker.setVisibility(View.GONE);
                    lin_country.setVisibility(View.GONE);

                }
                else
                {
                    Log.d("dghdfhdfh","fdhdfghfd");
                    txt_number.setVisibility(View.GONE);
                    countrypicker.setVisibility(View.VISIBLE);
                    lin_country.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    private void isOnline(boolean isConnected) {

        if (!isConnected) {

            Log.d("dffhdfhfd","dfgdfgdfg");
            /* Utils.toast(context, "Enable Network Connection");*/
            ll_noInternetConnection.setVisibility(View.VISIBLE);
            Utils.noInternetConnection(ll_noInternetConnection, context);
            //  progressbar.setVisibility(View.VISIBLE);
        } else {
            Log.d("dffhdfhfd","vnvcn");
            Utils.removeInternetConnection(ll_noInternetConnection);
            ll_noInternetConnection.setVisibility(View.GONE);
            progressbar.setVisibility(View.GONE);
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
    public void onBackPressed() {

        Utils.showExitDialog(FCD_LoginActivity.this, "Are you sure want to \n exit from application");
    }



    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(networkChangeReceiver, intentFilter);
        networkChangeReceiver.setConnectivityReceiverListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isOnline(isConnected);
    }
    private void GetUserDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FCD_URL.URL_DETAILUSER,
                ServerResponse -> {

                    Log.d("ServerResponse", "LoginGetUser" + ServerResponse);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        //FC_Common.status = jsonResponse1.getString("success");

                        FCD_Common.status = jsonResponse1.getString("status");

                        Log.d("fdhdfhfd","dfgfd"+ FCD_Common.id);

                        if (FCD_Common.status.equalsIgnoreCase("approved"))
                        {
                            FCD_Common.id = jsonResponse1.getString("id");
                            FCD_Common.name = jsonResponse1.getString("name");
                            FCD_Common.email = jsonResponse1.getString("email");
                            FCD_Common.mobile = jsonResponse1.getString("mobile");
                            FCD_Common.dial_code = jsonResponse1.getString("dial_code");
                            FCD_Common.vehicle_type = jsonResponse1.getString("vehicle_type");
                            FCD_Common.vehicle_description = jsonResponse1.getString("vehicle_description");
                            FCD_Common.vehicle_plate = jsonResponse1.getString("vehicle_plate");
                            FCD_Common.vehicle_color = jsonResponse1.getString("vehicle_color");
                            FCD_Common.last_login = jsonResponse1.getString("last_login");
                            FCD_Common.last_online = jsonResponse1.getString("last_online");
                            FCD_Common.device_token = jsonResponse1.getString("device_token");
                            FCD_Common.device_id = jsonResponse1.getString("device_id");
                            FCD_Common.device_type = jsonResponse1.getString("device_type");
                            FCD_Common.login_by = jsonResponse1.getString("login_by");
                            FCD_Common.social_unique_id = jsonResponse1.getString("social_unique_id");
                            FCD_Common.location_address = jsonResponse1.getString("location_address");
                            FCD_Common.location_lat = jsonResponse1.getString("location_lat");
                            FCD_Common.rating = jsonResponse1.getString("rating");
                            FCD_Common.userotp = jsonResponse1.getString("otp");
                            FCD_Common.created_at = jsonResponse1.getString("created_at");
                            FCD_Common.updated_at = jsonResponse1.getString("updated_at");
                            FCD_Common.token_type = jsonResponse1.getString("token_type");
                            FCD_Common.access_token = jsonResponse1.getString("access_token");
                            FCD_Common.location_lng = jsonResponse1.getString("location_lng");

                            FCD_User user = new FCD_User(jsonResponse1.getString("id"),
                                    jsonResponse1.getString("name"),
                                    jsonResponse1.getString("email"),
                                    jsonResponse1.getString("mobile"),
                                    jsonResponse1.getString("dial_code"),
                                    jsonResponse1.getString("vehicle_type"),
                                    jsonResponse1.getString("vehicle_description"),
                                    jsonResponse1.getString("vehicle_plate"),
                                    jsonResponse1.getString("vehicle_color"),
                                    jsonResponse1.getString("status"),
                                    jsonResponse1.getString("last_login"),
                                    jsonResponse1.getString("last_online"),
                                    jsonResponse1.getString("device_token"),
                                    jsonResponse1.getString("device_id"),
                                    jsonResponse1.getString("device_type"),
                                    jsonResponse1.getString("login_by"),
                                    jsonResponse1.getString("social_unique_id"),
                                    jsonResponse1.getString("location_address"),
                                    jsonResponse1.getString("location_lat"),
                                    jsonResponse1.getString("rating"),
                                    jsonResponse1.getString("otp"),
                                    jsonResponse1.getString("created_at"),
                                    jsonResponse1.getString("updated_at"),
                                    jsonResponse1.getString("token_type"),
                                    jsonResponse1.getString("access_token"),
                                    jsonResponse1.getString("location_lng")
                            );

                            FCD_SharedPrefManager.getInstance(context).userLogin(user);
                            SharedPreferences sharedPreferences = context.getSharedPreferences("MyLogin.txt", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("FirstLogin", true);
                            editor.apply();
                            SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(context);
                            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                            editor1.putString(Utils.id, FCD_Common.id);
                            editor1.putString(Utils.name, FCD_Common.name);
                            editor1.putString(Utils.email, FCD_Common.email);
                            editor1.putString(Utils.mobile, FCD_Common.mobile);
                            editor1.putString(Utils.dial_code, FCD_Common.dial_code);
                            editor1.putString(Utils.vehicle_type, FCD_Common.vehicle_type);
                            editor1.putString(Utils.vehicle_description, FCD_Common.vehicle_description);
                            editor1.putString(Utils.vehicle_plate, FCD_Common.vehicle_plate);
                            editor1.putString(Utils.vehicle_color, FCD_Common.vehicle_color);
                            editor1.putString(Utils.status, FCD_Common.status);
                            editor1.putString(Utils.last_login, FCD_Common.last_login);
                            editor1.putString(Utils.last_online, FCD_Common.last_online);
                            editor1.putString(Utils.device_token, FCD_Common.device_token);
                            editor1.putString(Utils.device_id, FCD_Common.device_id);
                            editor1.putString(Utils.device_type, FCD_Common.device_type);
                            editor1.putString(Utils.login_by, FCD_Common.login_by);
                            editor1.putString(Utils.social_unique_id, FCD_Common.social_unique_id);
                            editor1.putString(Utils.location_address, FCD_Common.location_address);
                            editor1.putString(Utils.location_lat, FCD_Common.location_lat);
                            editor1.putString(Utils.rating, FCD_Common.rating);
                            editor1.putString(Utils.userotp, FCD_Common.userotp);
                            editor1.putString(Utils.created_at, FCD_Common.created_at);
                            editor1.putString(Utils.updated_at, FCD_Common.updated_at);
                            editor1.putString(Utils.token_type, FCD_Common.token_type);
                            editor1.putString(Utils.access_token, FCD_Common.access_token);
                            editor1.putString(Utils.location_lng, FCD_Common.location_lng);
                            editor1.apply();

                            txtotp.setText("");
                            txtotp.setHint(getResources().getString(R.string.otp));

                                Intent intent=new Intent(context, FCD_DashboardActivity.class);
                                startActivity(intent);

                        }
                        else {
                            Utils.showExitDialog(FCD_LoginActivity.this, "Your Application Still in Processing \n Not Approved");
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
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("fdgdfgfdg","sdfgsdgs"+ FCD_Common.token_type+" "+ FCD_Common.access_token);
                params.put("Authorization", FCD_Common.token_type+" "+ FCD_Common.access_token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }
}
