package foodcoup.driver.demand.FCDPojo.FCDLiveOrders;

public class LiveOrdersObject {

    private String d_image ;
    private String d_name ;

    private int id ;
    private String status ;
    private int driver_id ;
    private String restaurant_name ;
    private String restaurant_phone ;
    private String restaurant_address ;
    private double restaurant_latitude ;
    private double restaurant_longitude ;
    private String name ;
    private String mobile ;
    private String map_address ;
    private int distance ;
    private String booking_id ;
    private String payment_mode ;


    public String getD_image() {
        return d_image;
    }

    public void setD_image(String d_image) {
        this.d_image = d_image;
    }

    public String getD_name() {
        return d_name;
    }

    public void setD_name(String d_name) {
        this.d_name = d_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(int driver_id) {
        this.driver_id = driver_id;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getRestaurant_phone() {
        return restaurant_phone;
    }

    public void setRestaurant_phone(String restaurant_phone) {
        this.restaurant_phone = restaurant_phone;
    }

    public String getRestaurant_address() {
        return restaurant_address;
    }

    public void setRestaurant_address(String restaurant_address) {
        this.restaurant_address = restaurant_address;
    }

    public double getRestaurant_latitude() {
        return restaurant_latitude;
    }

    public void setRestaurant_latitude(double restaurant_latitude) {
        this.restaurant_latitude = restaurant_latitude;
    }

    public double getRestaurant_longitude() {
        return restaurant_longitude;
    }

    public void setRestaurant_longitude(double restaurant_longitude) {
        this.restaurant_longitude = restaurant_longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMap_address() {
        return map_address;
    }

    public void setMap_address(String map_address) {
        this.map_address = map_address;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }


    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }
}
