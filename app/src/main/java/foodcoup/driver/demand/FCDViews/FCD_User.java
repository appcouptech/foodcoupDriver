package foodcoup.driver.demand.FCDViews;
public class FCD_User {

    private String id,name,email,mobile,dial_code,vehicle_type,vehicle_description,vehicle_plate,vehicle_color,status,
            last_login,last_online,device_token,device_id,device_type,login_by,social_unique_id,location_address,
            location_lat,rating,otp,created_at,updated_at,token_type,access_token,location_lng;


    public FCD_User(String id, String name, String email, String mobile,
                    String dial_code, String vehicle_type, String vehicle_description, String vehicle_plate, String vehicle_color, String status,
                    String last_login, String last_online, String device_token, String device_id, String device_type, String login_by,
                    String social_unique_id, String location_address, String location_lat, String rating,
                    String otp, String created_at, String updated_at, String token_type, String access_token,
                    String location_lng) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.dial_code = dial_code;
        this.vehicle_type = vehicle_type;
        this.vehicle_description = vehicle_description;
        this.vehicle_plate = vehicle_plate;
        this.vehicle_color = vehicle_color;
        this.status = status;
        this.last_login = last_login;
        this.last_online = last_online;
        this.device_token = device_token;
        this.device_id = device_id;
        this.device_type = device_type;
        this.login_by = login_by;
        this.social_unique_id = social_unique_id;
        this.location_address = location_address;
        this.location_lat = location_lat;
        this.rating = rating;
        this.otp = otp;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.token_type = token_type;
        this.access_token = access_token;
        this.location_lng = location_lng;

    }


    public String getid() { return id; }
    public String getname() { return name; }
    public String getemail() { return email; }
    public String getmobile() { return mobile; }
    public String getdial_code() { return dial_code; }
    public String getvehicle_type() { return vehicle_type; }
    public String getvehicle_description() { return vehicle_description; }
    public String getvehicle_plate() { return vehicle_plate; }
    public String getvehicle_color() { return vehicle_color; }
    public String getstatus() { return status; }
    public String getlast_login() { return last_login; }
    public String getlast_online() { return last_online; }
    public String getdevice_token() { return device_token; }
    public String getdevice_id() { return device_id; }
    public String getdevice_type() { return device_type; }
    public String getlogin_by() { return login_by; }
    public String getsocial_unique_id() { return social_unique_id; }
    public String getlocation_address() { return location_address; }
    public String getlocation_lat() { return location_lat; }
    public String getrating() { return rating; }
    public String getotp() { return otp; }
    public String getcreated_at() { return created_at; }
    public String getupdated_at() { return updated_at; }
    public String gettoken_type() { return token_type; }
    public String getaccess_token() { return access_token; }
    public String getlocation_lng() { return location_lng; }
}
