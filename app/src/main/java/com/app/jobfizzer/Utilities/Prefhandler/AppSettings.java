package com.app.jobfizzer.Utilities.Prefhandler;

import android.content.Context;

import com.app.jobfizzer.Model.AppSettingsResponseModel.Timeslot;
import com.app.jobfizzer.Utilities.helpers.SharedHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/*
 * Created by user on 23-10-2017.
 */

@SuppressWarnings("WeakerAccess")
public class AppSettings {

    boolean isDateSeleceted;
    boolean isTodaySelected;
    boolean isTimeSelected;
    boolean isCardSelected;
    boolean isAddressSelected;
    private Context context;
    private List<Timeslot> timeSlots;
    private String selectedAddressTitle;
    private String selectedTimeText;
    private String selectedAddressId;
    private String fireBaseToken;
    private String selectedLong;
    private String isLogged;
    private String selectedTimeSlot;
    private String selectedSubCategory;
    private String selectedSubCategoryName;
    private String selectedDate;
    private String selectedCity;
    private String selectedlat;
    private String selectedAddress;
    private String userType;
    private String token;
    private String paymentType;
    private String userName;
    private String userId;
    private String userNumber;
    private String userImage;
    private String imageUploadPath;
    private String socialLogin;
    private String categoryId;

    public AppSettings(Context context) {
        this.context = context;

    }

    public boolean isAddressSelected() {

        if (SharedHelper.getKey(context, "isAddressSelected").equalsIgnoreCase("true")) {
            isAddressSelected = true;
        } else {
            isAddressSelected = false;
        }
        return isAddressSelected;
    }

    public void setAddressSelected(boolean addressSelected) {
        isAddressSelected = addressSelected;


        if (isAddressSelected) {
            SharedHelper.putKey(context, "isAddressSelected", "true");

        } else {
            SharedHelper.putKey(context, "isAddressSelected", "false");

        }

    }

    public boolean isCardSelected() {

        if (SharedHelper.getKey(context, "isCardSelected").equalsIgnoreCase("true")) {
            isCardSelected = true;
        } else {
            isCardSelected = false;
        }
        return isCardSelected;
    }

    public void setCardSelected(boolean cardSelected) {
        isCardSelected = cardSelected;

        if (isCardSelected) {
            SharedHelper.putKey(context, "isCardSelected", "true");

        } else {
            SharedHelper.putKey(context, "isCardSelected", "false");

        }


    }

    public boolean getIsDateSeleceted() {
        if (SharedHelper.getKey(context, "isDateSeleceted").equalsIgnoreCase("true")) {
            isDateSeleceted = true;
        } else {
            isDateSeleceted = false;

        }

        return isDateSeleceted;
    }

    public void setIsDateSeleceted(boolean isDateSeleceted) {


        if (isDateSeleceted) {
            SharedHelper.putKey(context, "isDateSeleceted", "true");

        } else {
            SharedHelper.putKey(context, "isDateSeleceted", "false");

        }

        this.isDateSeleceted = isDateSeleceted;
    }

    public boolean isTodaySelected() {
        if (SharedHelper.getKey(context, "isTodaySelected").equalsIgnoreCase("true")) {
            isTodaySelected = true;
        } else {
            isTodaySelected = false;

        }

        return isTodaySelected;
    }

    public void setTodaySelected(boolean todaySelected) {
        isTodaySelected = todaySelected;

        if (isTodaySelected) {
            SharedHelper.putKey(context, "isTodaySelected", "true");

        } else {
            SharedHelper.putKey(context, "isTodaySelected", "false");

        }

    }

    public boolean isTimeSelected() {

        if (SharedHelper.getKey(context, "isTimeSelected").equalsIgnoreCase("true")) {
            isTimeSelected = true;
        } else {
            isTimeSelected = false;
        }

        return isTimeSelected;
    }

    public void setTimeSelected(boolean timeSelected) {
        isTimeSelected = timeSelected;
        if (isTimeSelected) {
            SharedHelper.putKey(context, "isTimeSelected", "true");
        } else {
            SharedHelper.putKey(context, "isTimeSelected", "false");

        }
    }

    public String getCategoryId() {
        categoryId = SharedHelper.getKey(context, "categoryId");

        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        SharedHelper.putKey(context, "categoryId", categoryId);

        this.categoryId = categoryId;
    }


    public String getUserId() {
        userId = SharedHelper.getKey(context, "userId");

        return userId;
    }

    public void setUserId(String userId) {
        SharedHelper.putKey(context, "userId", userId);

        this.userId = userId;
    }

    public String getUserName() {
        userName = SharedHelper.getKey(context, "userName");
        return userName;
    }

    public void setUserName(String userName) {
        SharedHelper.putKey(context, "userName", userName);
        this.userName = userName;
    }

    public String getUserNumber() {
        userNumber = SharedHelper.getKey(context, "userNumber");
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        SharedHelper.putKey(context, "userNumber", userNumber);
        this.userNumber = userNumber;
    }

    public String getIsSocialLogin() {
        socialLogin = SharedHelper.getKey(context, "socialLogin");

        return socialLogin;
    }

    public void setIsSocialLogin(String socialLogin) {
        SharedHelper.putKey(context, "socialLogin", socialLogin);

        this.socialLogin = socialLogin;
    }

    public String getImageUploadPath() {
        imageUploadPath = SharedHelper.getKey(context, "imageUploadPath");

        return imageUploadPath;
    }

    public void setImageUploadPath(String imageUploadPath) {
        SharedHelper.putKey(context, "imageUploadPath", imageUploadPath);

        this.imageUploadPath = imageUploadPath;
    }

    public String getUserImage() {
        userImage = SharedHelper.getKey(context, "userImage");

        return userImage;
    }

    public void setUserImage(String userImage) {
        SharedHelper.putKey(context, "userImage", userImage);

        this.userImage = userImage;
    }

    public String getPaymentType() {
        paymentType = SharedHelper.getKey(context, "paymentType");

        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        SharedHelper.putKey(context, "paymentType", paymentType);

        this.paymentType = paymentType;
    }


    public String getSelectedSubCategoryName() {
        selectedSubCategoryName = SharedHelper.getKey(context, "selectedSubCategoryName");

        return selectedSubCategoryName;
    }

    public void setSelectedSubCategoryName(String selectedSubCategoryName) {
        SharedHelper.putKey(context, "selectedSubCategoryName", selectedSubCategoryName);

        this.selectedSubCategoryName = selectedSubCategoryName;
    }


    public String getSelectedAddress() {
        selectedAddress = SharedHelper.getKey(context, "selectedAddress");

        return selectedAddress;
    }

    public void setSelectedAddress(String selectedAddress) {
        SharedHelper.putKey(context, "selectedAddress", selectedAddress);

        this.selectedAddress = selectedAddress;
    }


    public String getIsLogged() {
        isLogged = SharedHelper.getKey(context, "isLogged");

        return isLogged;
    }

    public void setIsLogged(String isLogged) {
        SharedHelper.putKey(context, "isLogged", isLogged);

        this.isLogged = isLogged;
    }


    public String getFireBaseToken() {
        fireBaseToken = SharedHelper.getKey(context, "fireBaseToken");
        return fireBaseToken;
    }

    public void setFireBaseToken(String fireBaseToken) {
        SharedHelper.putKey(context, "fireBaseToken", fireBaseToken);
        this.fireBaseToken = fireBaseToken;
    }

    public String getUserType() {
        userType = SharedHelper.getKey(context, "userType");

        return userType;
    }

    public void setUserType(String userType) {
        SharedHelper.putKey(context, "userType", userType);

        this.userType = userType;
    }

    public String getSelectedAddressId() {
        selectedAddressId = SharedHelper.getKey(context, "selectedAddressId");

        return selectedAddressId;
    }

    public void setSelectedAddressId(String selectedAddressId) {
        SharedHelper.putKey(context, "selectedAddressId", selectedAddressId);

        this.selectedAddressId = selectedAddressId;
    }

    public String getSelectedTimeText() {
        selectedTimeText = SharedHelper.getKey(context, "selectedTimeText");

        return selectedTimeText;
    }

    public void setSelectedTimeText(String selectedTimeText) {
        SharedHelper.putKey(context, "selectedTimeText", selectedTimeText);

        this.selectedTimeText = selectedTimeText;
    }


    public String getSelectedAddressTitle() {
        selectedAddressTitle = SharedHelper.getKey(context, "selectedAddressTitle");

        return selectedAddressTitle;
    }

    public void setSelectedAddressTitle(String selectedAddressTitle) {
        SharedHelper.putKey(context, "selectedAddressTitle", selectedAddressTitle);

        this.selectedAddressTitle = selectedAddressTitle;
    }


    public String getSelectedTimeSlot() {
        selectedTimeSlot = SharedHelper.getKey(context, "selectedTimeSlot");

        return selectedTimeSlot;
    }

    public void setSelectedTimeSlot(String selectedTimeSlot) {
        SharedHelper.putKey(context, "selectedTimeSlot", selectedTimeSlot);

        this.selectedTimeSlot = selectedTimeSlot;
    }

    public String getSelectedSubCategory() {
        selectedSubCategory = SharedHelper.getKey(context, "selectedSubCategory");

        return selectedSubCategory;
    }

    public void setSelectedSubCategory(String selectedSubCategory) {
        SharedHelper.putKey(context, "selectedSubCategory", selectedSubCategory);

        this.selectedSubCategory = selectedSubCategory;
    }

    public String getSelectedDate() {
        selectedDate = SharedHelper.getKey(context, "selectedDate");

        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        SharedHelper.putKey(context, "selectedDate", selectedDate);

        this.selectedDate = selectedDate;
    }

    public String getSelectedCity() {
        selectedCity = SharedHelper.getKey(context, "selectedCity");

        return selectedCity;
    }

    public void setSelectedCity(String selectedCity) {
        SharedHelper.putKey(context, "selectedCity", selectedCity);

        this.selectedCity = selectedCity;
    }

    public String getSelectedlat() {
        selectedlat = SharedHelper.getKey(context, "selectedlat");

        return selectedlat;
    }

    public void setSelectedlat(String selectedlat) {
        SharedHelper.putKey(context, "selectedlat", selectedlat);
        this.selectedlat = selectedlat;
    }

    public String getSelectedLong() {
        selectedLong = SharedHelper.getKey(context, "selectedLong");
        return selectedLong;
    }

    public void setSelectedLong(String selectedLong) {
        SharedHelper.putKey(context, "selectedLong", selectedLong);
        this.selectedLong = selectedLong;
    }

    public String getToken() {
        token = SharedHelper.getKey(context, "token");
        return token;
    }

    public void setToken(String token) {
        SharedHelper.putKey(context, "token", token);
        this.token = token;
    }

    public List<Timeslot> getTimeSlots() {

        String json = SharedHelper.getKey(context, "timeSlots");
        Type type = new TypeToken< List < Timeslot >>() {}.getType();
        timeSlots= new Gson().fromJson(json, type);

        return timeSlots;
    }

    public void setTimeSlots(List<Timeslot> timeSlots) {
        Gson gson = new Gson();
        String json = gson.toJson(timeSlots);
        SharedHelper.putKey(context, "timeSlots", json);
        this.timeSlots = timeSlots;
    }
}
