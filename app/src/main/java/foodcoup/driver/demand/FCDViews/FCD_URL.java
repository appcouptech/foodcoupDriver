package foodcoup.driver.demand.FCDViews;
public class FCD_URL
{

    //URL//
    //private static final String ROOT_URL = "http://192.168.21.248/api/user";
   private static final String ROOT_URL = "https://foodcoup.appcoup.com/api/driver";
   // private static final String ROOT_URL = "https://foodcoupdemo1.appcoup.com/api/driver";
    //private static final String ROOT_URL = "https://foodcoupdemo4.appcoup.com/api/driver";

    //Headers//
    private static final String ROOT_PHP = "";
    private static final String ROOT_DUTY = "/duty";
    private static final String ROOT_LOCATION = "/location";
    private static final String ROOT_LIST = "/new";
    private static final String ROOTACCEPT = "/accept";
    private static final String ROOTORDER = "/order";
    private static final String ROOTUPDATE= "/update";
    private static final String ROOTPROFILE= "/profile";
    private static final String ROOTTODAY= "/today";
    private static final String ROOTFLOAT= "/float";
    private static final String ROOTLOGINDURATION= "/duration";
    private static final String ROOTWEEK= "/week";
    private static final String ROOTTOKEN= "/token";
    private static final String ROOTCURRENT= "/current";

    //ROOT_URL //
    public static final String URL_LOGIN= ROOT_URL+"/login"+ROOT_PHP;
    public static final String URL_LOGINOTP= ROOT_URL+"/otp"+ROOT_PHP;
    public static final String URL_DETAILUSER= ROOT_URL+"/detail"+ROOT_PHP;
    public static final String URL_EARNING= ROOT_URL+"/earning"+ROOT_PHP;
    public static final String URL_RATE= ROOT_URL+"/rate"+ROOT_PHP;
    public static final String URL_LOGOUT= ROOT_URL+"/logout"+ROOT_PHP;

    //ROOT_URL + ROOT_DUTY//
    public static final String URL_DRIVER_DUTY_STARTED= ROOT_URL+ROOT_DUTY+"/update"+ROOT_PHP;
    public static final String URL_DRIVER_LOCATION= ROOT_URL+ROOT_LOCATION+"/update"+ROOT_PHP;
    public static final String URL_LOCATION_BASED_LIST= ROOT_URL+ROOT_LIST +"/list"+ROOT_PHP;
    public static final String URL_DRIVER_STATUS_CHECK= ROOT_URL+ROOT_DUTY +"/status"+ROOT_PHP;
    public static final String URL_CONFIRM_ORDER= ROOT_URL+ROOTACCEPT +"/order"+ROOT_PHP;


    /*ROOT_URL + ROOTORDER*/
    public static final String URL_ORDERDETAIL= ROOT_URL+ROOTORDER +"/detail"+ROOT_PHP;

/*ROOT_URL + ROOTUPDATE*/
public static final String URL_UPDATESTATUS= ROOT_URL+ROOTUPDATE +"/status"+ROOT_PHP;

    /*ROOT_URL + ROOTTOKEN*/
    public static final String URL_TOKENVALIDATE= ROOT_URL+ROOTTOKEN +"/validate"+ROOT_PHP;

    /*ROOT_URL + ROOTTODAY*/
    public static final String URL_TODAYEARNING= ROOT_URL+ROOTTODAY +"/earning"+ROOT_PHP;

    /*ROOT_URL + ROOTFLOAT*/
    public static final String URL_FLOATCASH= ROOT_URL+ROOTFLOAT +"/cash"+ROOT_PHP;

    /*ROOT_URL + ROOTLOGINDURATION*/
    public static final String URL_LOGINLIST= ROOT_URL+ROOTLOGINDURATION +"/list"+ROOT_PHP;
    public static final String URL_LOGINLISTDETAIL= ROOT_URL+ROOTLOGINDURATION +"/detail"+ROOT_PHP;

    /*ROOT_URL + ROOTWEEK*/
    public static final String URL_WEEKEARNING= ROOT_URL+ROOTWEEK +"/earning"+ROOT_PHP;

    /*ROOT_URL + ROOTWEEK*/
    public static final String URL_CURRENTTASK= ROOT_URL+ROOTCURRENT +"/task"+ROOT_PHP;




}
