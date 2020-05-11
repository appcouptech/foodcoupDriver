package foodcoup.driver.demand.FCDViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class FCD_SharedPrefManager {
    private static final String SHARED_PREF_NAME = "Ala_shared";
    public static final String id = "id";
    public static final String name = "name";
    public static final String email = "email";
    public static final String mobile = "mobile";
    public static final String dial_code = "dial_code";
    public static final String vehicle_type = "vehicle_type";
    public static final String vehicle_description = "vehicle_description";
    public static final String vehicle_plate = "vehicle_plate";
    public static final String vehicle_color = "vehicle_color";
    public static final String status = "status";
    public static final String last_login = "last_login";
    public static final String last_online = "last_online";
    public static final String device_token = "device_token";
    public static final String device_id = "device_id";
    public static final String device_type = "device_type";
    public static final String login_by = "login_by";
    public static final String social_unique_id = "social_unique_id";
    public static final String location_address = "location_address";
    public static final String location_lat = "location_lat";
    public static final String rating = "rating";
    public static final String userotp = "userotp";
    public static final String created_at = "created_at";
    public static final String updated_at = "updated_at";
    public static final String token_type = "token_type";
    public static final String access_token = "access_token";
    public static final String location_lng = "location_lng";

    @SuppressLint("StaticFieldLeak")
    private static FCD_SharedPrefManager mInstance;
    @SuppressLint("StaticFieldLeak")
    private static Context mCtx;
    private FCD_SharedPrefManager(Context context) {
        mCtx = context;
    }
    public static synchronized FCD_SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new FCD_SharedPrefManager(context);
        }
        return mInstance;
    }
    public void userLogin(FCD_User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences.edit();
        editor1.putString(Utils.id, user.getid());
        editor1.putString(Utils.name, user.getname());
        editor1.putString(Utils.email, user.getemail());
        editor1.putString(Utils.mobile, user.getmobile());
        editor1.putString(Utils.dial_code, user.getdial_code());
        editor1.putString(Utils.vehicle_type, user.getvehicle_type());
        editor1.putString(Utils.vehicle_description, user.getvehicle_description());
        editor1.putString(Utils.vehicle_plate, user.getvehicle_plate());
        editor1.putString(Utils.vehicle_color, user.getvehicle_color());
        editor1.putString(Utils.status, user.getstatus());
        editor1.putString(Utils.last_login, user.getlast_login());
        editor1.putString(Utils.last_online, user.getlast_online());
        editor1.putString(Utils.device_token, user.getdevice_token());
        editor1.putString(Utils.device_id, user.getdevice_id());
        editor1.putString(Utils.device_type, user.getdevice_type());
        editor1.putString(Utils.login_by, user.getlogin_by());
        editor1.putString(Utils.social_unique_id, user.getsocial_unique_id());
        editor1.putString(Utils.location_address, user.getlocation_address());
        editor1.putString(Utils.location_lat, user.getlocation_lat());
        editor1.putString(Utils.rating, user.getrating());
        editor1.putString(Utils.userotp, user.getotp());
        editor1.putString(Utils.created_at, user.getcreated_at());
        editor1.putString(Utils.updated_at, user.getupdated_at());
        editor1.putString(Utils.token_type, user.gettoken_type());
        editor1.putString(Utils.access_token, user.getaccess_token());
        editor1.putString(Utils.location_lng, user.getlocation_lng());
        editor1.apply();
    }



    public FCD_User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new FCD_User(
                sharedPreferences.getString(id, null),
                sharedPreferences.getString(name, null),
                sharedPreferences.getString(email, null),
                sharedPreferences.getString(mobile, null),
        sharedPreferences.getString(dial_code, null),
        sharedPreferences.getString(vehicle_type, null),
        sharedPreferences.getString(vehicle_description, null),
        sharedPreferences.getString(vehicle_plate, null),
        sharedPreferences.getString(vehicle_color, null),
        sharedPreferences.getString(status, null),
        sharedPreferences.getString(last_login, null),
        sharedPreferences.getString(last_online, null),
        sharedPreferences.getString(device_token, null),
        sharedPreferences.getString(device_id, null),
        sharedPreferences.getString(device_type, null),
                sharedPreferences.getString(login_by, null),
                sharedPreferences.getString(social_unique_id, null),
                sharedPreferences.getString(location_address, null),
                sharedPreferences.getString(location_lat, null),
                sharedPreferences.getString(rating, null),
                sharedPreferences.getString(userotp, null),
                sharedPreferences.getString(created_at, null),
                sharedPreferences.getString(updated_at, null),
                sharedPreferences.getString(token_type, null),
                sharedPreferences.getString(access_token, null),
                sharedPreferences.getString(location_lng, null)
        );
    }
    public static void deleteAllSharePrefs(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

}