package foodcoup.driver.demand.FCDViews;

import android.annotation.SuppressLint;
import android.app.Activity;

@SuppressLint("Registered")
public class FCD_Common extends Activity {


    ////String Values//////

    public static int currentTask ;
    public static int dutyStarted = 0;
    public static int orderConfirm = 0;
    public static int order_id_check = 0;
    public static int count = 0;

    public static String success = "";
    public static String phone = "";
    public static String mobilenumber = "";
    public static String address_set = "";
    public static String vehicle_plate = "";
    public static String location_type_set = "";
    public static String longitude_set = "";
    public static String change_address = "intro";
    public static String status = "";
    public static String id = "";
    public static String name = "";
    public static String email = "";
    public static String mobile = "";
    public static String email_verified_at = "";
    public static String dial_code = "";
    public static String vehicle_type = "";
    public static String vehicle_description = "";
    public static String vehicle_color = "";
    public static String landmark = "";
    public static String last_login = "";
    public static String last_online = "";
    public static String device_token = "";
    public static String device_id = "";
    public static String device_type = "";
    public static String login_by = "";
    public static String social_unique_id = "";
    public static String location_address = "";
    public static String location_lat = "";
    public static String rating = "";
    public static String userotp = "";
    public static String created_at = "";
    public static String updated_at = "";
    public static String otp = "";
    public static String message = "";
    public static String XMLCODE = "XMLHttpRequest";
    public static String token_type = "";
    public static String token_type_dup = "Bearer";
    public static String access_token = "";
    public static String access_token_dup = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiNGVjMTM3YTA4NmRkNjBiMGVmMzkwZjkyYjgwM2ZhZTUzNzE3NzdkYjJmNzM0NjVlZWU3NWIyMjMwZGMyYjE5MWM2NWUxNTAyMDEyNzc2NzAiLCJpYXQiOjE1ODA3OTQyMzUsIm5iZiI6MTU4MDc5NDIzNSwiZXhwIjoxNjEyNDE2NjM1LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.KR_Qce74G8u8EARIr6w7pn7GbIo2Tz7n3_zknRHFr20rZDyxgKYRojVGNwetteO9vyJ7jdDWJI2a-62xyr6-8XcCzKwVVoeozq9_E-_G6-6zXUjtAeHlMjyW-t-LNNXnaVtJAA7fs-0IkAhQu0vGbFDnhcHj6Vgr6EFSleTaPG-T3DylD1tU1aDi_J0QA6Q2m8sK9DRFHnuZz1BMtFMpPMg6YlU1Dktc5rX2a546l6Us3lbjEJ2cYCB-SHSi7CBWSF5K3H0-NUJgWkNRjUmnwpbiO6haTuOO1zsdVLRLF0zGNOOEn3IKI7l7VF9xjYVNApeXlUMw3VXN1eDB5pxnpnqgf86a2Pd7XozhrgZOB5VARt7wJDZ1MucqoO6EpEnbuyqaYJYgQKY4ng9C6e2USjVMgES5L3rU8K3qI8-62FyEPUZ5kHAtLxIqL4V4AyzH49K7E-eWn-F7l-ZKr9C2ntR_BAOCcS7hukJ_Rz9hzajtm7FiZ6szh2b-OzZLHZPEBKUAljmyAPtNltmk-dLEUpWSW2RfM7NrKoNXHAhF5Q0Wf5JXmHwodkK00j6M4lOEjhH3m3ZLeCQ1_XjEkN9WZJxm0nxKSkW93DAqbSg0M5YsmvsB96T0_zdKXQgEOnUp-xCmSJqvmBwk2JYlUX7v81Ubk4l5N3TzB0qfp7UjvOI";
    public static String user_type = "";
    public static String otpcheck = "";
    public static String img_profile = "";
    public static String updateedtemail = "";
    public static String username = "";
    public static String usermobile = "";
    public static String useremail = "";
    public static String usergender = "";
    public static String devicetype = "android";
    public static String homeaddress = "";
    public static String Locality = "";
    public static String Address = "";
    public static String location = "";
    public static String location_lng = "";
    public static String filter_price_max = "";
    public static String filter_price_max_check = "";
    public static String filter_price_min = "";
    public static String filter_price_min_check = "";

    public static String default_address = "";

    public static String autocompletelocation = "";

    /*Driver */

    public static String DriverName = "";
    public static String DriverEmail = "";
    public static String Driverphone = "";
    public static String DriverPhoto = "";
    public static String Driverhelp = "";
    public static String Totaldistance = "";


    /*Referreal code*/

    public static String referealcode = "";

    /*DashboardActivity*/
    public static  double latitude ;
    public static  double longitude ;

    /*ConfirmOrder*/
    public static String confirmrestaurant_id = "";
    public static String confirmrestaurant_name = "";
    public static String confirmorderstatus = "";
    public static String confirmdriver_id = "";
    public static String confirmrestaurant_phone = "";
    public static String confirmrestaurant_address = "";
    public static String confirmrestaurant_latitude = "";
    public static String confirmrestaurant_longitude = "";
    public static String confirmname = "";
    public static String confirmmobile = "";
    public static String confirmmap_address = "";
    public static String confirmlatitude = "";
    public static String confirmlongitude = "";
    public static String confirmpayable = "";
    public static String confirmcurrency = "";
    public static String confirmitem = "";
    public static String confirmbooking_id = "";
    public static String confirmpayment_mode = "";
    public static String totalamt = "";

    /*Home */
    public static String HometodayEarning = "0";
    public static String HometodayCurrency = "0";
    public static String HomeweekEarning = "0";
    public static String HomeloginDuration = "0";
    public static String HomefloatCash = "0";

    /*Account */
    public static String AccountEmail = "";
    public static String Accountphone = "";
    public static String Accountaddress = "";
    public static String AccountName = "";
    public static String AccountNumber = "";
    public static String AccountbankName = "";
    public static String AccountIFSC = "";
    public static String AccountpanCard = "";
    public static String AccountvehicleName = "";
    public static String AccountvehicleModel = "";
    public static String AccountvehicleNumber = "";
    public static String AccountvehicleInsurance = "";
    public static String AccountPhoto = "";
    public static String AccountDriverName = "";

    /*Earnings*/
    public static String todayTotal = "";
    public static String todayDate_check = "";
    public static String float_currency = "";
    public static String float_limit = "";
    public static String float_cash = "";
    public static String WeeklyTotal = "";
    public static String todaycurrency = "";
    public static String Weeklycurrency = "";
    public static String todayDate = "";
    public static String DateFromItem = "";
    public static String DateToItem = "";
    public static String duty_date = "";
    public static String LoginHRS = "";
    public static String Loginnet_time = "";


    /*Restaurant */
    public static String restaurantphone = "";

    /*HomeFragment*/
    public static String hotsellerrestaurant_status = "";
    public static String toppickrestaurant_status = "";
    public static String toppickrestaurant_discount = "";
    public static String toppickrestaurant_discounttype = "";
    public static String Allrestaurant_status = "";

    /*HotSeller */
    public static String AllHostseller_status = "";


    /* HotelList*/
    public static String hotelid = "";
    public static String restaurant_name = "";
    public static String favourite = "";
    public static String restaurant_cuisine = "";
    public static String restaurant_logo = "";
    public static String dish_id = "";
    public static String restaurant_phone = "";
    public static String delivery_estimation = "";
    public static String person_limit = "";
    public static String cost_limit = "";
    public static String restaurant_address = "";
    public static String currency = "";
    public static String restaurant_status = "";
    public static String availabilty = "";
    public static String availabilty_Description = "";
    public static String opens = "";
    public static String total_quantity = "";
    public static String total_price = "";
    public static String check_value = "";
    public static String gpsenabled = "";
    public static String restaurantid = "";
    public static String recent_search = "";
    public static String hometype = "";
    public static String hotelpricing = "";
    public static String addonpricing = "";
    public static String productID = "";
    public static String partnerId = "";
    public static String quantity = "";

    /* Addon */
    public static String ingredientID = "";
    public static String ingredientName = "";
    public static String PreparationType = "";
    public static String PreparationID = "";
    public static String pricingID = "";
    public static String addonID = "";
    public static String pricingName = "";
    public static String addonName = "";
    public static String addonTotal = "";
    public static String ingrdientTotal = "";

    /*Filter Type*/
    public static String typeVeg = "2";
    public static String CuisineName = "";
    public static String typePopularity = "1";
    public static String cuisinecheck = "";
    public static int minrange = 0;
    public static int maxrange = 100;

/*card offers*/
public static String paymentID = "";

/////int values/////
    public static int otplength ;

    /*Cart Items*/
    public static String Cartrestaurant_id = "";
    public static String CartProduct_id = "";

    public static String Cartrestaurant_name= "";
    public static String Cartrestaurant_address= "";
    public static String Cartrestaurant_latitude= "";
    public static String Cartrestaurant_longitude= "";
    public static String Cartrestaurant_phone= "";
    public static String Cartfree_tax= "";
    public static String Carttax_percent= "";
    public static String Cartfree_package= "";
    public static String Cartpackaging_charge= "";
    public static String Cartcuisines= "";
    public static String Cartdish_id= "";
    public static String Carttotal_quantity= "";
    public static String Cartitem_total= "";
    public static String Cartdelivery_fee= "";
    public static String Cartdistance_charge= "";
    public static String Cartdiscount= "";
    public static String Carttax= "";
    public static String Carttaxes_charges= "";
    public static String Carttotal= "";
    public static String Cartsuccess= "";
    public static String Cartis_default= "";
    public static String is_default = "";
    public static String Cartservice_availability = "";
    public static String Cartrestaurant_status = "";
    public static String Cartfavourite = "";


    /*Class Intent name*/
    public static String classname= "";
    public static String check_test= "";

    /*Search list*/

    public static String searchlist= "";
    public static String preordertime= "";
    public static String paymenttype= "";
/*Order */
public static String orderrestaurant_name= "";
public static String ordercuisine_id= "";
public static String ordername= "";
public static String orderdriver= "";
public static String orderrestaurant= "";
public static String ordermobile= "";
public static String orderid= "";
public static String ordertotal= "";
public static String orderitem= "";
public static String ordercurrency= "";
public static String restaurant_rating= "";
public static String restaurant_comments= "";
public static String driver_comments= "";
public static int restaurant_rating_int=0;
public static int driver_rating_int=0;
public static String driver_rating= "";


}
