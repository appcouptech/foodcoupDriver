package foodcoup.driver.demand.FCDPojo;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class WriteDatabase_Driver {
    /*public String device_id;
    public String confirmdriver_id;
    public String confirmlatitude;
    public String confirmlongitude;

    public WriteDatabase_Driver() {
    }

    public WriteDatabase_Driver(String device_id, String confirmdriver_id, String confirmlatitude,String confirmlongitude) {
        this.device_id = device_id;
        this.confirmdriver_id = confirmdriver_id;
        this.confirmlatitude = confirmlatitude;
        this.confirmlongitude = confirmlongitude;
    }*/
    public String uid;
    public String author;
    public String title;
    public String body;

    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public WriteDatabase_Driver() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public WriteDatabase_Driver(String uid, String author, String title, String body) {
        this.uid = uid;
        this.author = author;
        this.title = title;
        this.body = body;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("driverid", uid);
        result.put("device_id", author);
        result.put("latitude", title);
        result.put("longitude", body);
        result.put("stars", stars);

        return result;
    }


}