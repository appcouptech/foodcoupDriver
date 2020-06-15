package foodcoup.driver.FCPushNotifications;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint({"Registered", "MissingFirebaseInstanceTokenRefresh"})
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "gzvdzxvzxvxz " + remoteMessage.getFrom());
        Log.e(TAG, "gzvdzxvzxvxz " + remoteMessage.getData());
        Log.d("dfgsdfgsd","9note"+remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
            // handleNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());



            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                Log.d("dfgsdfg","fdgfd"+json);
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "zxgbzxvczxc: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
        pushNotification.putExtra("message", message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

        // play notification sound
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.playNotificationSound();


        Log.d("dfgsdfgsd","3note"+message);
    }

    /* private void handleNotification(String message) {
         if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
             // app is in foreground, broadcast the push message
 //            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

             Intent intent = new Intent( this , MainActivity.class );
             intent.putExtra("message", message);
             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             PendingIntent resultIntent = PendingIntent.getActivity( this , 0, intent,
                     PendingIntent.FLAG_ONE_SHOT);

             Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
             NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder( this)
                     .setSmallIcon(R.mipmap.ic_launcher)
                     .setContentTitle("Android Tutorial Point FCM Tutorial")
                     .setContentText(message)
                     .setAutoCancel( true )
                     .setSound(notificationSoundURI)
                     .setContentIntent(resultIntent);

             NotificationManager notificationManager =
                     (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

             notificationManager.notify(0, mNotificationBuilder.build());

         }else{
             // If the app is in background, firebase itself handles the notification
             Log.d("fdgdfgfd","fdgfd");
         }
     }*/
    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            //JSONObject data = json.getJSONObject("data");
            String order_id = json.getString("order_id");
            Log.e(TAG, "title: " +"order_id"+ order_id);
           /* FC_Common.order_id=order_id;
            Intent intent = new Intent(this, FC_OrderPickedUpActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("order_id", FC_Common.order_id);
            startActivity(intent);*/


        } catch (JSONException e) {
            Log.e(TAG, "ghdxfgbxcg" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "ghdxfgbxcg" + e.getMessage());
        }
    }
}
