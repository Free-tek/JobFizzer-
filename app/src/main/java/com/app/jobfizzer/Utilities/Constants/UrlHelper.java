package com.app.jobfizzer.Utilities.Constants;

import com.app.jobfizzer.BuildConfig;

/**
 * Created by user on 23-10-2017.
 */

public class UrlHelper {

    public static String SOCKET_URL = BuildConfig.SOCKET_URL;
    private static String BASE = BuildConfig.BASE_URL;
    //        public static String BASE_URL = BASE + "uber_test/public/api/";
    public static String BASE_URL = BASE + "uberdoo/public/api/";
    public static String SIGN_UP = BASE_URL + "signup";
    public static String SIGN_IN = BASE_URL + "userlogin";
    public static String FORGOT_PASSWORD = BASE_URL + "forgotpassword";
    public static String RESET_PASSWORD = BASE_URL + "resetpassword";
    public static String HOME_DASH_BOARD_NEW = BASE_URL + "homedashboardnew";
    public static String APP_SETTINGS = BASE_URL + "appsettings";
    public static String LIST_ADDRESS = BASE_URL + "listaddress";
    public static String ADD_ADDRESS = BASE_URL + "addaddress";
    public static String LIST_PROVIDERS = BASE_URL + "listprovider";
    public static String NEW_BOOKINGS = BASE_URL + "newbooking";
    public static String VIEW_BOOKINGS = BASE_URL + "view_bookings";
    public static String GET_PROVIDER_LOCATION = BASE_URL + "getproviderlocation";
    public static String VIEW_PROFILE = BASE_URL + "viewprofile";
    public static String UPDATE_DEVICE_TOKEN = BASE_URL + "updatedevicetoken";
    public static String REVIEW = BASE_URL + "review";
    public static String PAYMENT = BASE_URL + "pay";
    public static String PAYMENT_METHODS = BASE_URL + "list_payment_methods";
    public static String STRIPE_PAYMENT = BASE_URL + "stripe";
    public static String CHANGE_PASSWORD = BASE_URL + "changepassword";
    public static String LIST_SUB_CATEGORY = BASE_URL + "list_subcategory";
    //    public static String UPLOAD_IMAGE = BASE + "uber_test/public/admin/imageupload";
    public static String UPLOAD_IMAGE = BASE_URL + "imageupload";
    public static String CANCEL_BOOKING = BASE_URL + "cancelbyuser";
    public static String CANCEL_REQUEST = BASE_URL + "cancel_request";
    public static String ABOUT_US = BASE + "uberdoo/public/admin/showPag";
    public static String TERMS = BASE + "uberdoo/public/admin/terms";
    public static String UPDATE_PROFILE = BASE_URL + "updateprofile";
    public static String DELETE_ADDRESS = BASE_URL + "deleteaddress";
    public static String UPDATE_ADDRESS = BASE_URL + "updateaddress";
    public static String SOCIAL_LOGIN = BASE_URL + "sociallogin";
    public static String COUPON_VERIFY = BASE_URL + "couponverify";
    public static String COUPON_REMOVE = BASE_URL + "couponremove";
    public static String BEFORE_AFTER_IMAGE = BASE_URL + "startjobendjobdetails";
    public static String LOG_OUT = BASE_URL + "logout";
    public static String VERIFY_OTP = BASE_URL + "forgotpasswordotpcheck";
    public static String PAYSTACKACCESS = BASE_URL + "paystack_access";
    public static String PAYSTACKSTATUS = BASE_URL + "paystack_payment_status";

    public static String FAQ = BASE + "uberdoo/public/admin/faq";
    public static String HOME_DASH_BOARD = BASE_URL + "homedashboard";


    //CHAT_SOCKET/API
    public static String CHAT_SOCKET = BuildConfig.CHAT_SOCKET_URL;

    //CHAT APi
    public static String CHAT_MESSAGES = CHAT_SOCKET + "userchatlist";
    public static String CHAT_LISTS = CHAT_SOCKET + "providermsglist";


    //SOCKET EMITTERS
    public static String GET_ONLINE = "get_user_online";
    public static String SEND_MESSAGE = "sendmessagefromuser";

    public static String IS_OFFLINE = "isOffline";
    public static String IS_ONLINE = "isOnline";
    public static String SEND_DELIVERED = "send_delivered";

    //SOCKET LISTENER
    public static String RECEVICE_MESSAGE = "recievemessage";
}