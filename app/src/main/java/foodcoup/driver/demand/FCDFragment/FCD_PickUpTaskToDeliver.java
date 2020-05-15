package foodcoup.driver.demand.FCDFragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import foodcoup.driver.demand.FCDActivity.FCDDashboardActivity.FCD_DashboardActivity;
import foodcoup.driver.demand.FCDPojo.WriteDatabase_Driver;
import foodcoup.driver.demand.FCDUtils.BottomSheet.BottomSheetDeliverd.DeliverdTheOrderDialog;
import foodcoup.driver.demand.FCDUtils.HttpConnection;
import foodcoup.driver.demand.FCDUtils.Loader.LoaderTextView;
import foodcoup.driver.demand.FCDUtils.PathJSONParser;
import foodcoup.driver.demand.FCDViews.AC_Edittext;
import foodcoup.driver.demand.FCDViews.AC_Textview;
import foodcoup.driver.demand.FCDViews.FCD_Common;
import foodcoup.driver.demand.FCDViews.FCD_SharedPrefManager;
import foodcoup.driver.demand.FCDViews.FCD_URL;
import foodcoup.driver.demand.FCDViews.FCD_User;
import foodcoup.driver.demand.FCDViews.Utils;
import foodcoup.driver.demand.R;

import static foodcoup.driver.demand.FCDActivity.FCDDashboardActivity.FCD_DashboardActivity.fragmentManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class FCD_PickUpTaskToDeliver extends Fragment implements OnMapReadyCallback {

    private LinearLayout ll_changeAmt, ll_customer, mBottomSheet, ll_direction, ll_paymentSuccessful;
    private AC_Textview txt_collectCashFromCustomerBtn, txt_deliverComplete, txt_collectFromCustomer;
    private ImageView player_col, player_expand;
    private ScrollView sv_pickedUp;
    private LoaderTextView lt_orderNo,lt_customerName, lt_customerAddress, lt_currency, lt_item, lt_restaurantName, lt_restaurantAddress ,lt_distance;
   // DatabaseReference myRef;
    private GoogleMap mGoogleMap;
    private LocationRequest mLocationRequest;
    private final static int REQUEST_LOCATION = 199;
    private Location location; // location
    private double latitude; // latitude
    private double longitude;
    private Marker mCurrLocationMarker, destinationMarker;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastLocation;
    private double CURRENT_latitude, CURRENT_longitude;
    private GoogleSignInClient mGoogleSignInClient;
    private List<Location> locationList = new ArrayList<>();
    private int userZoomLevel = 0;
    private float zoom;
    private LatLngBounds bounds;
    private LatLng latLng;
    private Polyline polyline;
    private Context context;
    private AlertDialog alertDialog;
    private AC_Edittext edt_price;
    DatabaseReference mDatabase;
    private Handler handler;
    public FCD_PickUpTaskToDeliver() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fcd__pick_up_task_to_deliver, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        FCD_User user = FCD_SharedPrefManager.getInstance(context).getUser();
        FCD_Common.id = String.valueOf(user.getid());
        FCD_Common.token_type = String.valueOf(user.gettoken_type());
        FCD_Common.access_token = String.valueOf(user.getaccess_token());
        FindViewById(view);

        FCD_Common.currentTask = 8;


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getActivity()));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            Log.d("checking map", "map Created");
        }

        getActivity().setFinishOnTouchOutside(true);

        // Todo Location Already on  ... start
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (Objects.requireNonNull(manager).isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(getActivity())) {
            Toast.makeText(getActivity(), "Gps already enabled", Toast.LENGTH_SHORT).show();
        }
        // Todo Location Already on  ... end

        if (!hasGPSDevice(getActivity())) {
            Toast.makeText(getActivity(), "Gps not Supported", Toast.LENGTH_SHORT).show();
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(getActivity())) {
            Log.e("TAG", "Gps already enabled");
            Toast.makeText(getActivity(), "Gps not enabled", Toast.LENGTH_SHORT).show();
            enableLoc();
        } else {
            Log.e("TAG", "Gps already enabled");
            Toast.makeText(getActivity(), "Gps already enabled", Toast.LENGTH_SHORT).show();
        }

        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        // bottomSheetBehavior.setPeekHeight(100);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint({"SwitchIntDef", "SetTextI18n"})
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        player_col.setVisibility(View.GONE);
                        player_expand.setVisibility(View.VISIBLE);

                        ll_direction.setOnClickListener(view13 -> {
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            sv_pickedUp.scrollTo(0, 0);
                            player_col.setVisibility(View.VISIBLE);
                            player_expand.setVisibility(View.GONE);
                        });

                        txt_collectCashFromCustomerBtn.setOnClickListener(view1 -> {
                            if (FCD_Common.confirmpayment_mode.equalsIgnoreCase("CASH"))
                            {
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                            LayoutInflater inflater = getLayoutInflater();
                            @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.confirmation_alert, null);
                            dialogBuilder.setView(dialogView);

                            AC_Textview txt_alertDesc = dialogView.findViewById(R.id.txt_alertDesc);
                            AC_Textview txt_alertDesc1 = dialogView.findViewById(R.id.txt_alertDesc1);
                            AC_Textview txt_cancel = dialogView.findViewById(R.id.txt_cancel);
                            AC_Textview txt_confirm = dialogView.findViewById(R.id.txt_confirm);

                            alertDialog = dialogBuilder.create();
                            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            alertDialog.setCancelable(false);
                            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                            txt_alertDesc.setText(R.string.are_you_sure_collect_the_cash);
                            txt_alertDesc1.setVisibility(View.VISIBLE);
                            txt_alertDesc1.setText("" + FCD_Common.confirmcurrency + " " + FCD_Common.confirmpayable);
                            txt_cancel.setOnClickListener(view112 -> alertDialog.dismiss());

                            txt_confirm.setOnClickListener(view11 -> {
                                mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                                ConfirmOrder();
                            });
                            alertDialog.show();
                            }
                            else if(FCD_Common.confirmpayment_mode.equalsIgnoreCase("CARD"))
                            {
                                ConfirmOrder();
                            }
                        });

                        txt_deliverComplete.setOnClickListener(view12 -> {

                            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            sv_pickedUp.scrollTo(0, 0);
                            player_col.setVisibility(View.VISIBLE);
                            player_expand.setVisibility(View.GONE);

                            DeliverdTheOrderDialog deliverdTheOrderDialog = DeliverdTheOrderDialog.newInstance();
                            deliverdTheOrderDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),
                                    "deliverdTheOrderDialog");

                           /* new Handler().postDelayed(() -> {

                                DeliverdTheOrderDialog deliverdTheOrderDialog = DeliverdTheOrderDialog.newInstance();
                                deliverdTheOrderDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),
                                        "deliverdTheOrderDialog");

                            }, 300);*/


                        });
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {

                        sv_pickedUp.scrollTo(0, 0);
                        player_col.setVisibility(View.VISIBLE);
                        player_expand.setVisibility(View.GONE);
                    }
                    break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        ll_customer.setOnClickListener(v -> {
            FCD_Common.phone = FCD_Common.confirmmobile;
            if (Build.VERSION.SDK_INT < 23) {
                phoneCall();
            } else {
                if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                        Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    phoneCall();
                } else {
                    final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                    //Asking request Permissions
                    ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE, 9);
                }
            }
        });

        OrderDetail();

        edt_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence txt, int i, int i1, int i2) {


                String searchvalue = edt_price.getText().toString().toLowerCase(Locale.getDefault());

                double d1 = Double.parseDouble(FCD_Common.confirmpayable);
                double d2 = Double.parseDouble(searchvalue);
                int retval = Double.compare(d1, d2);

                if (retval > 0) {
                    edt_price.setText(FCD_Common.confirmpayable);
                    Log.d("dfhdfgfdgfdgfd", "d1 is greater than d2");
                } else if (retval < 0) {
                    Log.d("dfhdfgfdgfdgfd", "d1 is less than d2");
                    txt_collectCashFromCustomerBtn.setText(getResources().getString(R.string.collect) + " " + FCD_Common.confirmcurrency + " " +
                            searchvalue + " " + getResources().getString(R.string.from_customer));
                    FCD_Common.totalamt = edt_price.getText().toString();
                } else {

                    Log.d("dfhdfgfdgfdgfd", "d1 is equal than d2");
                    txt_collectCashFromCustomerBtn.setText(getResources().getString(R.string.collect) + " " + FCD_Common.confirmcurrency + " " +
                            searchvalue + " " + getResources().getString(R.string.from_customer));
                    FCD_Common.totalamt = edt_price.getText().toString();
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

        Log.d("fgjhfghfg", "fghfghfg" + FCD_Common.confirmorderstatus);

      /*  myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                // Log.d("Value is: " + value);
                Log.d("dfgdfgfdg","dfgfdgfdg"+value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //  Log.w(TAG, "Failed to read value.", error.toException());
                Log.d("dfgdfgfdg","dfgfdgfdg"+error.toException());
            }
        });*/

    }

    @SuppressLint("SetTextI18n")
    private void FindViewById(View view) {

        ll_changeAmt = view.findViewById(R.id.ll_changeAmt);
        lt_orderNo = view.findViewById(R.id.lt_orderNo);
        ll_direction = view.findViewById(R.id.ll_direction);
        ll_paymentSuccessful = view.findViewById(R.id.ll_paymentSuccessful);
        txt_collectFromCustomer = view.findViewById(R.id.txt_collectFromCustomer);
        txt_collectCashFromCustomerBtn = view.findViewById(R.id.txt_collectCashFromCustomerBtn);
        lt_distance = view.findViewById(R.id.lt_distance);
        txt_deliverComplete = view.findViewById(R.id.txt_deliverComplete);
        edt_price = view.findViewById(R.id.edt_totprice);
        sv_pickedUp = view.findViewById(R.id.sv_pickedUp);
        mBottomSheet = view.findViewById(R.id.bottom_sheet_deliver);
        player_col = view.findViewById(R.id.player_col);
        player_expand = view.findViewById(R.id.player_expand);
        lt_customerName = view.findViewById(R.id.lt_customerName);
        lt_customerAddress = view.findViewById(R.id.lt_customerAddress);
        lt_currency = view.findViewById(R.id.lt_currency);
        ll_customer = view.findViewById(R.id.ll_customer);
        lt_item = view.findViewById(R.id.lt_item);
        lt_restaurantName = view.findViewById(R.id.lt_restaurantName);
        lt_restaurantAddress = view.findViewById(R.id.lt_restaurantAddress);

        FCD_DashboardActivity.txt_toolbarOrderNo.setVisibility(View.GONE);
        FCD_DashboardActivity.txt_toolbarOrderNo.setText("" + 123456789);
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

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(Objects.requireNonNull(getActivity()), gso);

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
      /*  locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);*/

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(Objects.requireNonNull(getActivity()), locationSettingsResponse -> {
            // All location settings are satisfied. The client can initialize
            // location requests here.
            // ...

        });

        task.addOnFailureListener(getActivity(), e -> {
            if (e instanceof ResolvableApiException) {
                try {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(getActivity(),
                            REQUEST_LOCATION);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //Location Permission already granted
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;

        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(false);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(12000);
        mLocationRequest.setFastestInterval(12000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            } else {
                checkLocationPermission();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
    }


    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Log.d("location ", "locationResult :" + locationResult);
            locationList.clear();
            locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.d("location ", "Location: " + location.getLatitude() + " " + location.getLongitude());
             //    Toast.makeText(getActivity(), "" + "Latitude : " + location.getLatitude() + ", Longitude : " + location.getLongitude(), Toast.LENGTH_SHORT).show();

                FCD_Common.latitude = location.getLatitude();
                FCD_Common.longitude = location.getLongitude();


                //OrderDetail();

                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                    Log.d("zoomLevel", "marker remove");
                }

                //Place current location marker
                latitude = location.getLatitude();
                longitude = location.getLongitude();

               /* CURRENT_latitude = latitude;
                CURRENT_longitude = longitude;*/

                latLng = new LatLng(latitude, longitude);
                Log.d("zoom", " latLng: " + latLng);



                handler = new Handler();
                int delay = 35000; //milliseconds
                handler.postDelayed(new Runnable() {
                    public void run() {
                        //do something
                        mDatabase = FirebaseDatabase.getInstance().getReference();

                        Log.d("dfgdfgsd","dgdsfg"+FCD_Common.device_id);
                        Log.d("dfgdfgsd","dgdsfg"+FCD_Common.confirmdriver_id);
                        Log.d("dfgdfgsd","dgdsfg"+FCD_Common.confirmlatitude);
                        Log.d("dfgdfgsd","dgdsfg"+FCD_Common.confirmlongitude);
                        writeNewPost(FCD_Common.confirmdriver_id, FCD_Common.device_id,
                                String.valueOf(FCD_Common.latitude),String.valueOf(FCD_Common.longitude));

                        // ItemViewList();
                        handler.postDelayed(this, delay);
                    }
                }, delay);

                int height = (int) FCD_DashboardActivity.dashContext.getResources().getDimension(R.dimen.bitmap_iconSize);
                int width = (int) FCD_DashboardActivity.dashContext.getResources().getDimension(R.dimen.bitmap_iconSize);
                try {


                    Bitmap b = getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_driver_marker));
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title("Current Location");
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                    //  markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                    mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
                    //move map camera
                    CameraPosition googlePlex = CameraPosition.builder()
                            .target(latLng)
                            .zoom(17)
                            .build();

                    mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));
                    if (bounds != null) {
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
                    }
                    animateMarker(latLng, false);
                    DestinationUserMarkerPlace();
                } catch (IllegalStateException ex) {
                    ex.printStackTrace();
                }
            }
        }
    };

    private void writeNewPost(String userId, String username, String title, String body) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        //String key = mDatabase.child("posts").push().getKey();
        WriteDatabase_Driver post = new WriteDatabase_Driver(userId, username, title, body);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        /*childUpdates.put("/dev/" + key, postValues);
        childUpdates.put("/drivers/" + userId + "/" + key, postValues);*/
       /* childUpdates.put("/foodcoup/" +  userId,postValues);
        childUpdates.put("/dev/" +  userId,postValues);
        childUpdates.put("/drivers/" + userId + "/" , postValues);*/
        /*childUpdates.put("/foodcoup/" +  userId,postValues);
        childUpdates.put("/foodcoup/"+"/dev/"+"/drivers/" +  userId,postValues);*/
        childUpdates.put("/foodcoup/"+"/dev/"+"/drivers/" + userId + "/" , postValues);

        mDatabase.updateChildren(childUpdates);
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }

    private void DestinationUserMarkerPlace() {

        if (destinationMarker != null) {
            destinationMarker.remove();
        }

        int height1 = (int) FCD_DashboardActivity.dashContext.getResources().getDimension(R.dimen.bitmap_iconSize);
        int width1 = (int) FCD_DashboardActivity.dashContext.getResources().getDimension(R.dimen.bitmap_iconSize);
     /*   BitmapDrawable bitmapdraw1 = (BitmapDrawable) getResources().getDrawable(R.drawable.to_location);
        Bitmap b1 = bitmapdraw1.getBitmap();*/
        Bitmap b1 = getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_customer_marker));
        Bitmap smallMarker1 = Bitmap.createScaledBitmap(b1, width1, height1, false);

        try {


        double c_lat = Double.parseDouble(FCD_Common.confirmlatitude);
        double c_lng = Double.parseDouble(FCD_Common.confirmlongitude);
        LatLng point = new LatLng(c_lat, c_lng);

        MarkerOptions markerOptions1 = new MarkerOptions();
        markerOptions1.position(point);
        markerOptions1.title("Deliver Location");
        markerOptions1.icon(BitmapDescriptorFactory.fromBitmap(smallMarker1));
        destinationMarker = mGoogleMap.addMarker(markerOptions1);

        if (polyline != null) {
            polyline.remove();
        }

        String url = getMapsApiDirectionsUrl();
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);

        mGoogleMap.setOnCameraIdleListener(() -> {
            zoom = mGoogleMap.getCameraPosition().zoom;
            bounds = mGoogleMap.getProjection().getVisibleRegion().latLngBounds;
            Log.d("zoomLevel", " zoom bounds: " + bounds);
            Log.d("zoomLevel", " zoom float: " + zoom);

            //use zoomLevel value..
            userZoomLevel = Math.round(zoom);

        });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private String getMapsApiDirectionsUrl() {

        // Origin of route
        String str_origin = "origin=" + latitude + "," + longitude;

        // Destination of route
        String str_dest = "destination=" + FCD_Common.confirmlatitude + "," + FCD_Common.confirmlongitude;


        // Sensor enabled
        String sensor = "sensor=true";
        String mode = "mode=driving";

        //Key
        String key = "key=" + FCD_DashboardActivity.dashContext.getResources().getString(R.string.google_maps_key);

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        Log.d("location", "gdgdfgs" + url);

        return url;
    }

    private void animateMarker(final LatLng toPosition, final boolean hideMarke) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mGoogleMap.getProjection();
        Point startPoint = proj.toScreenLocation(mCurrLocationMarker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 5000;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                mCurrLocationMarker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarke) {
                        mCurrLocationMarker.setVisible(false);
                    } else {
                        mCurrLocationMarker.setVisible(true);
                    }
                }
            }
        });
    }

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new android.app.AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
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
                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    //  mGoogleMap.setMyLocationEnabled(true);
                }

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
            }
            if (requestCode == 9) {
                permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            }
            if (permissionGranted) {
                phoneCall();
            } else {
                Toast.makeText(getActivity(), "You don't assign permission.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = new ArrayList<>();
            routes.clear();

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = new ArrayList<>();
            points.clear();
            PolylineOptions polyLineOptions = new PolylineOptions();
            List<HashMap<String, String>> path = new ArrayList<>();
            path.clear();
            String distance = "";
            String duration = "";

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {


                path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(Objects.requireNonNull(point.get("lat")));
                    double lng = Double.parseDouble(Objects.requireNonNull(point.get("lng")));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(10);
                polyLineOptions.color(Color.BLACK);
            }

            if (polyLineOptions != null) {
                polyline = mGoogleMap.addPolyline(polyLineOptions);
                Log.d("map", " Distance : " + distance + " , duration : " + duration);
                lt_distance.setText(""+ distance);
            } else {
                Toast.makeText(getActivity(), "Manage API Account", Toast.LENGTH_SHORT).show();
            }

        }
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

                            FCD_Common.totalamt = FCD_Common.confirmpayable;
                            lt_orderNo.setText(FCD_Common.confirmbooking_id);
                            lt_restaurantName.setText(FCD_Common.confirmrestaurant_name);
                            lt_restaurantAddress.setText(FCD_Common.confirmrestaurant_address);
                            lt_item.setText(FCD_Common.confirmitem);
                            lt_customerName.setText(FCD_Common.confirmname);
                            lt_customerAddress.setText(FCD_Common.confirmmap_address);
                            edt_price.setText(FCD_Common.confirmpayable);

                            lt_currency.setText(FCD_Common.confirmcurrency + " " + FCD_Common.confirmpayable);
                            if (FCD_Common.confirmpayment_mode.equalsIgnoreCase("CASH"))
                            {
                                txt_collectCashFromCustomerBtn.setText(getResources().getString(R.string.collect) + " " + FCD_Common.confirmcurrency + " " +
                                        FCD_Common.confirmpayable + " " + getResources().getString(R.string.from_customer));
                            }
                            if (FCD_Common.confirmpayment_mode.equalsIgnoreCase("CARD"))
                            {
                                txt_collectCashFromCustomerBtn.setText(getResources().getString(R.string.reached_customer_location));
                            }

                            if (FCD_Common.confirmorderstatus.equalsIgnoreCase("REACHED")) {
                                txt_collectFromCustomer.setVisibility(View.VISIBLE);
                                ll_paymentSuccessful.setVisibility(View.GONE);
                                txt_deliverComplete.setVisibility(View.VISIBLE);

                                txt_collectCashFromCustomerBtn.setVisibility(View.GONE);
                                if (FCD_Common.confirmpayment_mode.equalsIgnoreCase("CASH"))
                                {
                                    ll_changeAmt.setVisibility(View.VISIBLE);
                                }
                                if (FCD_Common.confirmpayment_mode.equalsIgnoreCase("CARD"))
                                {
                                    ll_changeAmt.setVisibility(View.GONE);
                                }
                            }
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
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                Log.d("getParams: ", "" + params);
                params.put("Authorization", FCD_Common.token_type + " " + FCD_Common.access_token);
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
            callIntent.setData(Uri.parse("tel:" + FCD_Common.phone));
            getActivity().startActivity(callIntent);
        } else {
            Toast.makeText(getActivity(), "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }

    private void ConfirmOrder() {
      //  Utils.playProgressBar(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FCD_URL.URL_UPDATESTATUS,
                ServerResponse -> {

                    Log.d("ServerResponse", "PickedUp" + ServerResponse);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        FCD_Common.status = jsonResponse1.getString("success");
                        if (FCD_Common.status.equalsIgnoreCase("1")) {

                        //    Utils.stopProgressBar();

                            txt_collectFromCustomer.setVisibility(View.VISIBLE);
                            ll_paymentSuccessful.setVisibility(View.GONE);
                            txt_deliverComplete.setVisibility(View.VISIBLE);
                            txt_collectCashFromCustomerBtn.setVisibility(View.GONE);

                            if (FCD_Common.confirmpayment_mode.equalsIgnoreCase("CASH"))
                            {
                                alertDialog.dismiss();
                                ll_changeAmt.setVisibility(View.VISIBLE);
                            }
                            if (FCD_Common.confirmpayment_mode.equalsIgnoreCase("CARD"))
                            {
                                ll_changeAmt.setVisibility(View.GONE);
                            }

                        }
                        else {
                            Utils.toast(getActivity(),getResources().getString(R.string.enable_location));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("xcgsdgsdgsd", "dfhdf" + e);
                    }
                }, volleyError -> {
            String value = volleyError.toString();

            // snackBar(value);
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("order_id", FCD_Common.orderid);
                params.put("order_status", "REACHED");
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

}
