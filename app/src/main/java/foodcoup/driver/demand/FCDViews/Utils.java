package foodcoup.driver.demand.FCDViews;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import foodcoup.driver.demand.R;

public class Utils {
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


    private static View parentLayout;
    private final Context context;
    static LayoutInflater inflater ;
    static View inflatedLayout ;
    static int i = 0;
    private static Snackbar bar;
    static AlertDialog alertDialog ;
    private static Locale myLocale;

    public Utils(Context context, Snackbar bar){
        
        this.context = context ;
        this.bar = bar ;

    }

   /* public static Locale getLocale(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyLanguage.txt", Context.MODE_PRIVATE);
        String lang = sharedPreferences.getString("language", "");
        if (lang != null) {
            switch (lang) {
                case "en":
                    lang = "en";
                    break;
                case "ta":
                    lang = "ta";
                    break;
                case "hi":
                    lang = "hi";
                    break;
                case "te":
                    lang = "te";
                    break;
            }
        }
        return new Locale(lang);
    }*/

    public static void toast (Context context ,String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    public static void snackBar (Context context ,String message){
//        Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
//                "Look at me, I'm a fancy snackbar", Snackbar.LENGTH_LONG);
//        snackBar.show();

        Snackbar.make(parentLayout.findViewById(android.R.id.content)," "+message, Snackbar.LENGTH_LONG).show();
        /*Snackbar snackbar = Snackbar.make(parentLayout.findViewById(R.id.content)," "+message, Snackbar.LENGTH_LONG);
        snackbar.show();*/
    }




    public static void SnackBar (Context context ,String message){
//        Snackbar snackbar = Snackbar.make(parentLayout.findViewById(R.id.content)," "+message, Snackbar.LENGTH_LONG);
//        snackbar.show();

        bar = Snackbar.make(parentLayout, ""+message, Snackbar.LENGTH_LONG)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bar.dismiss();
                    }
                });
        bar.setActionTextColor(Color.RED);
        TextView tv = bar.getView().findViewById(R.id.snackbar_text);
        tv.setTextColor(Color.CYAN);
        bar.show();

    }

    public static void log (Context context ,String message){
        Log.d("SMKDetails :" + context ,"" + message);
    }


    public static void showExitDialog(final Activity context , String message){


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = context.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_exit, null);
        AC_Textview txt_message = (AC_Textview) dialogView.findViewById(R.id.txt_alertMessage);
        AC_BoldTextview txt_no = (AC_BoldTextview)dialogView.findViewById(R.id.txt_no);
        AC_BoldTextview txt_yes = (AC_BoldTextview)dialogView.findViewById(R.id.txt_yes);
        dialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        txt_message.setText("" +message);

        txt_no.setOnClickListener(v -> alertDialog.dismiss());

        txt_yes.setOnClickListener(v -> {

           /* Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                context.finishAffinity();
            }
            System.exit(0);
        });

        alertDialog.show();
    }



    /*public static void LanguageSelection(final Activity activity ){

        final SharedPreferences sharedPreferences = activity.getSharedPreferences("MyLanguage.txt", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language", "en");

        String[] singleChoiceItems =activity.getResources().getStringArray(R.array.dialog_single_choice_array);
        int itemSelected = 0;
        new AlertDialog.Builder(activity)
                .setTitle("Select your Language")
                .setSingleChoiceItems(singleChoiceItems, itemSelected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int selectedIndex) {
                        if (selectedIndex == 0){
                            editor.putString("language", "en");
                            editor.apply();
                            ChangeLanguage(activity);
                        }else if (selectedIndex ==1){
                            editor.putString("language", "ta");
                            editor.apply();
                            ChangeLanguage(activity);
                        }else if (selectedIndex == 2) {
                            editor.putString("language", "hi");
                            editor.apply();
                            ChangeLanguage(activity);
                        } else if (selectedIndex == 3) {
                            editor.putString("language", "te");
                            editor.apply();
                            ChangeLanguage(activity);
                        }
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String lang = sharedPreferences.getString("language", "");
                        if (lang.length() != 0){

                        }

                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }*/

    private static void ChangeLanguage(final Activity activity) {

        SharedPreferences sharedPreferences = activity.getSharedPreferences("MyLanguage.txt", Context.MODE_PRIVATE);
        String lang = sharedPreferences.getString("language", "");

        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        config.locale = myLocale;
        activity.getBaseContext().getResources().updateConfiguration(config, activity.getBaseContext().getResources().getDisplayMetrics());

        Utils.log(activity,"" +  lang);

    }

    public static void configure(Activity activity, Configuration newConfig) {

        if (myLocale != null){
            newConfig.locale = myLocale;
            Locale.setDefault(myLocale);
            activity.getBaseContext().getResources().updateConfiguration(newConfig, activity.getBaseContext().getResources().getDisplayMetrics());
        }

    }

    public static byte[] getImageBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static void noInternetConnection(LinearLayout linearLayout, Context context){
        i= 1;
        inflater = LayoutInflater.from(context);
        View inflatedLayout= inflater.inflate(R.layout.no_internet_connection,null, false);
        linearLayout.addView(inflatedLayout);
    }

    public static void removeInternetConnection(LinearLayout linearLayout){

        if (i== 1){
        linearLayout.removeViewAt(0);
        i= 0 ;
        }
    }


    public static void playProgressBar (final Activity context){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = context.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_progress_bar, null);
        /*ImageView img_gif = (ImageView) dialogView.findViewById(R.id.img_gif);
        Glide.with(context).load(R.drawable.progress_bar_new).into(img_gif);*/
        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

    }

    public static void stopProgressBar(){

        alertDialog.dismiss();
    }

    public interface Language{
        void onLanguage(int lang);
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

}
