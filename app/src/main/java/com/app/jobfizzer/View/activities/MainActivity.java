package com.app.jobfizzer.View.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.jobfizzer.Model.AppSettingsResponseModel.Alltax;
import com.app.jobfizzer.Model.AppSettingsResponseModel.AppSettingResponseModel;
import com.app.jobfizzer.Model.AppSettingsResponseModel.MaterialDetails;
import com.app.jobfizzer.Model.AppSettingsResponseModel.StatusAppSettings;
import com.app.jobfizzer.Model.CommonResponseModel;
import com.app.jobfizzer.Model.FlagResponseModel;
import com.app.jobfizzer.Model.PayStachAccessURLResponse;
import com.app.jobfizzer.Model.PaymentMethodModel;
import com.app.jobfizzer.Model.PaymentType;
import com.app.jobfizzer.Model.StartJobEndJobDetailsResponse.Data;
import com.app.jobfizzer.Model.StartJobEndJobDetailsResponse.StartJobEndJobResponseModel;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.ImageLoader;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.app.jobfizzer.Utilities.Constants.Constants;
import com.app.jobfizzer.Utilities.Constants.UrlHelper;
import com.app.jobfizzer.Utilities.Events.Status;
import com.app.jobfizzer.Utilities.Prefhandler.AppSettings;
import com.app.jobfizzer.Utilities.Stripe.ExampleEphemeralKeyProvider;
import com.app.jobfizzer.Utilities.helpers.CustomLibraries.CircleImageView;
import com.app.jobfizzer.Utilities.helpers.CustomLibraries.NavigationTabStrip;
import com.app.jobfizzer.Utilities.helpers.FileDownloader;
import com.app.jobfizzer.Utilities.helpers.GlideHelper;
import com.app.jobfizzer.Utilities.helpers.MyWebViewClient;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.View.adapters.PaymentAdapter;
import com.app.jobfizzer.View.adapters.PaymentMiscellaneousAdapter;
import com.app.jobfizzer.View.adapters.PaymentTaxAdapter;
import com.app.jobfizzer.View.fragments.AccountsFragment;
import com.app.jobfizzer.View.fragments.BookingsFragment;
import com.app.jobfizzer.View.fragments.ChatFragment;
import com.app.jobfizzer.View.fragments.HomeFragment;
import com.app.jobfizzer.ViewModel.CommonViewModel;
import com.app.jobfizzer.ViewModel.MainScreenViewModel;
import com.app.jobfizzer.R;
import com.stripe.android.CustomerSession;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.model.Customer;
import com.stripe.android.model.CustomerSource;
import com.stripe.android.model.Source;
import com.stripe.android.model.SourceCardData;
import com.stripe.android.view.PaymentMethodsActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.app.jobfizzer.Utilities.helpers.Utils.emojiChatFilter;
import static com.app.jobfizzer.Utilities.helpers.Utils.showSnackBar;
import static com.app.jobfizzer.Utilities.helpers.Utils.specialFilter;

public class MainActivity extends BaseActivity {

    public RelativeLayout cardLayout;
    private final int REQUEST_CODE_SELECT_SOURCE = 55;
    public JSONObject userDetails = new JSONObject();
    public Boolean isCardSelected = false;
    AppSettings appSettings = new AppSettings(MainActivity.this);
    String cardID = "";
    TextView cardName, optionButton;
    boolean isPaymentEnabled = false;
    ImageLoader imageLoader;
    MainScreenViewModel mainScreenViewModel;
    CommonViewModel commonViewModel;
    private String TAG = MainActivity.class.getSimpleName();
    private Dialog dialog, paymentDialog;
    private String paymentUrl = "0";
    private ProgressDialog pd;
    private WebView webView;
    Dialog ratingDialog;
    private String pdf = null;
    private Button save;
    private ViewPager mainPager;
    private NavigationTabStrip tabStrip;
    private String[] titles;
    private Button ok;
    private Dialog couponDialog;


    public void loadImage(String url, ImageView imageView, Drawable drawable) {
        imageLoader.load(url, imageView, drawable);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainScreenViewModel = ViewModelProviders.of(this).get(MainScreenViewModel.class);
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel.class);
        initValues();
        appSettingsInput();
        setValues();
        postToken();
    }


    private void setValues() {
        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mainPager.setAdapter(mainPagerAdapter);
        mainPager.setOffscreenPageLimit(3);
        Utils.setStripColor(MainActivity.this, tabStrip);
        tabStrip.setTitles(titles);
        Utils.initChatService(MainActivity.this);

        //0-bookings, 1-home, 2-chat, 3-accounts
        if (getIntent().getStringExtra(Constants.LOGIN_TYPE).equalsIgnoreCase(Constants.BOOKINGS_TAB)) {
            mainPager.setCurrentItem(0);
        } else if (getIntent().getStringExtra(Constants.LOGIN_TYPE).equalsIgnoreCase(Constants.ACCOUNTS_TAB)) {

            mainPager.setCurrentItem(3);
        } else {
            mainPager.setCurrentItem(1);
        }
        tabStrip.setViewPager(mainPager);


        mainPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                Utils.hideKeyboard(mainPager);
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {
                Utils.hideKeyboard(mainPager);
            }
        });

    }

    public void postToken() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fcm_token", appSettings.getFireBaseToken());
            jsonObject.put("os", "android");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        InputForAPI inputForAPI = new InputForAPI(MainActivity.this);
        inputForAPI.setUrl(UrlHelper.UPDATE_DEVICE_TOKEN);
        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaders(ApiCall.getHeaders(this));
        updateDeviceRequest(inputForAPI);
    }

    private void updateDeviceRequest(InputForAPI inputForAPI) {
        commonViewModel.flagUpdate(inputForAPI).observe(this,
                new Observer<FlagResponseModel>() {
                    @Override
                    public void onChanged(@Nullable FlagResponseModel flagResponseModel) {
                        if (flagResponseModel != null) {
                        }
                    }
                });
    }


    private void initValues() {
        PaymentConfiguration.init(getResources().getString(R.string.STRIPE_KEY));
        imageLoader = new ImageLoader(MainActivity.this);
        mainPager = findViewById(R.id.mainPager);
        tabStrip = findViewById(R.id.tabStrip);
        titles = new String[]{getResources().getString(R.string.bookings), getResources().getString(R.string.home), getResources().getString(R.string.chat), getResources().getString(R.string.account)};
    }

    public void appSettingsInput() {
        InputForAPI inputForAPI = new InputForAPI(MainActivity.this);
        inputForAPI.setUrl(UrlHelper.APP_SETTINGS);
        inputForAPI.setFile(null);
        inputForAPI.setHeaders(ApiCall.getHeaders(MainActivity.this));
        getAppSettings(inputForAPI);

    }

    public void getAppSettings(InputForAPI inputForAPI) {

        mainScreenViewModel.getAppSettings(inputForAPI).observe
                (this, new Observer<AppSettingResponseModel>() {
                    @Override
                    public void onChanged(@Nullable AppSettingResponseModel appSettingResponseModel) {
                        handleAppSettingsSuccessResponse(appSettingResponseModel);
                    }
                });

    }

    private void handleAppSettingsSuccessResponse(AppSettingResponseModel response) {
        try {
            if (!response.getDeleteStatus().equalsIgnoreCase("active")) {
                setTimeSlotsValues(response);
                setStatusValues(response);
            } else {
                showDialogDeleteStatus();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDialogDeleteStatus() {

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.show_delete_status);

        Window window = dialog.getWindow();

        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            window.setGravity(Gravity.CENTER);
        }
        dialog.show();

        ok = dialog.findViewById(R.id.ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appSettings.setIsLogged("false");
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
            }
        });

    }

    private void setStatusValues(AppSettingResponseModel appSettingResponseModel) {

        List<StatusAppSettings> statusAppSettings = appSettingResponseModel.getStatus();

        if (statusAppSettings != null && statusAppSettings.size() > 0) {
            StatusAppSettings status = statusAppSettings.get(0);
            String statusvalue = status.getStatus();
            if (dialog != null) {
                dialog.dismiss();
            }
            if (statusvalue.equalsIgnoreCase("Blocked")) {
                showBlocked();
            } else if (statusvalue.equalsIgnoreCase("Dispute")) {
                showDispute();
            } else if (statusvalue.equalsIgnoreCase("Completedjob")) {
                showInvoice(statusAppSettings.get(0));
            } else if (statusvalue.equalsIgnoreCase("Reviewpending")) {
                showRating(statusAppSettings.get(0));
            } else if (statusvalue.equalsIgnoreCase("Waitingforpaymentconfirmation")) {
                showWaitingforPayment();
            }
        }
    }

    private void setTimeSlotsValues(AppSettingResponseModel response) {

        if (response.getTimeslots() != null) {
            appSettings.setTimeSlots(mainScreenViewModel.getFormattedTimeslots(response.getTimeslots()));
        } else {
        }
    }


    private void launchWithCustomer() {
        Intent payIntent = PaymentMethodsActivity.newIntent(this);
        startActivityForResult(payIntent, REQUEST_CODE_SELECT_SOURCE);
    }

    private void postCardToken(InputForAPI inputForAPI) {

        commonViewModel.flagUpdate(inputForAPI).observe(this, new Observer<FlagResponseModel>() {
            @Override
            public void onChanged(@Nullable FlagResponseModel flagResponseModel) {
                appSettingsInput();
            }
        });
    }

    private void buildCardTokenInputs(String token, String booking_id) throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token);
        jsonObject.put("id", booking_id);

        InputForAPI inputForAPI = new InputForAPI(MainActivity.this);
        inputForAPI.setUrl(UrlHelper.STRIPE_PAYMENT);
        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaders(ApiCall.getHeaders(MainActivity.this));

        postCardToken(inputForAPI);

    }

    public void showCoupon(final String bookingOrderId) {
        couponDialog = new Dialog(MainActivity.this);
        couponDialog.setCancelable(false);
        couponDialog.setCanceledOnTouchOutside(false);
        couponDialog.setContentView(R.layout.view_coupons);
        couponDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = couponDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        ImageView close = couponDialog.findViewById(R.id.close);
        final ImageView confirm = couponDialog.findViewById(R.id.confirm);
        final EditText coupons = couponDialog.findViewById(R.id.coupons);
        coupons.setFilters(new InputFilter[]{emojiChatFilter});
        final JSONObject jsonObject = new JSONObject();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appSettingsInput();
                couponDialog.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (coupons.getText().toString().trim().length() != 0 && bookingOrderId != null) {
                    try {
                        jsonObject.put("couponname", "" + coupons.getText().toString().trim());
                        jsonObject.put("booking_order_id", bookingOrderId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    InputForAPI inputForAPI = new InputForAPI(MainActivity.this);
                    inputForAPI.setUrl(UrlHelper.COUPON_VERIFY);
                    inputForAPI.setFile(null);
                    inputForAPI.setJsonObject(jsonObject);
                    inputForAPI.setHeaders(ApiCall.getHeaders(MainActivity.this));
                    verifyCoupon(inputForAPI, couponDialog);
                } else {
                    confirm.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_error));
                    showSnackBar(coupons, getString(R.string.coupon_no));
                }
            }
        });
        couponDialog.show();
    }

    private void verifyCoupon(final InputForAPI inputForAPI, final Dialog dialog) {
        commonViewModel.flagUpdate(inputForAPI).observe(this, new Observer<FlagResponseModel>() {
            @Override
            public void onChanged(@Nullable FlagResponseModel flagResponseModel) {
                if (flagResponseModel != null) {
                    if (flagResponseModel.getError().equalsIgnoreCase("false")) {
                        appSettingsInput();
                        dialog.dismiss();
                    } else {
                        Utils.toast(couponDialog.findViewById(android.R.id.content), flagResponseModel.getError_message());
                    }
                }
            }
        });
    }

    private void setRecyclerViewTax(final RecyclerView recy_tax, List<Alltax> jsonArray) {

        PaymentTaxAdapter paymentAdapter = new PaymentTaxAdapter(MainActivity.this, jsonArray);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(MainActivity.this,
                        LinearLayoutManager.VERTICAL, false);

        recy_tax.setLayoutManager(linearLayoutManager);
        recy_tax.setAdapter(paymentAdapter);
        paymentAdapter.notifyDataSetChanged();
    }

    private void setRecyclerViewMiscellanous(RecyclerView recy_miscellanous, List<MaterialDetails> materialDetails) {
        PaymentMiscellaneousAdapter paymentMiscellaneousAdapter = new PaymentMiscellaneousAdapter(MainActivity.this,
                materialDetails);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        recy_miscellanous.setLayoutManager(linearLayoutManager);
        recy_miscellanous.setAdapter(paymentMiscellaneousAdapter);
        paymentMiscellaneousAdapter.notifyDataSetChanged();
    }

    public String getCombinedString(String... strings) {
        return commonViewModel.getCombinedStrings(strings);
    }

    public void showInvoice(final StatusAppSettings bookingValues) {
        dialog = new Dialog(MainActivity.this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.invoice_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        dialog.show();
        cardName = dialog.findViewById(R.id.cardName);
        optionButton = dialog.findViewById(R.id.optionButton);
        initCustomerSession();
        RecyclerView paymentMethod;
        TextView providerName, billingName, bookingId, bookingDate, bookingTime, workedHours, bookingPrice, bookingGst, bookingTotal, taxName, taxPercentage;
        Button confirm;
        CardView miscellaneous_card, tax_card;
        final String booking_id, booking_order_id;
        confirm = dialog.findViewById(R.id.confirm);
        paymentMethod = dialog.findViewById(R.id.paymentMethod);
        cardLayout = dialog.findViewById(R.id.cardLayout);
        final ImageView apply = dialog.findViewById(R.id.apply);
        final TextView coupons = dialog.findViewById(R.id.coupons);
        final TextView offPrice = dialog.findViewById(R.id.offPrice);
        RecyclerView recy_Tax, recy_miscellanous;

        miscellaneous_card = dialog.findViewById(R.id.miscellaneous_card);
        tax_card = dialog.findViewById(R.id.tax_card);
        providerName = dialog.findViewById(R.id.providerName);
        billingName = dialog.findViewById(R.id.billingName);
        bookingId = dialog.findViewById(R.id.bookingId);
        bookingDate = dialog.findViewById(R.id.bookingDate);
        bookingTime = dialog.findViewById(R.id.bookingTime);
        workedHours = dialog.findViewById(R.id.workedHours);
        bookingPrice = dialog.findViewById(R.id.bookingPrice);
        bookingTotal = dialog.findViewById(R.id.bookingTotal);
        bookingGst = dialog.findViewById(R.id.bookingGst);
        taxName = dialog.findViewById(R.id.taxName);
        taxPercentage = dialog.findViewById(R.id.taxPercentage);
        recy_Tax = dialog.findViewById(R.id.recy_Tax);
        recy_miscellanous = dialog.findViewById(R.id.recy_miscellanous);

        List<Alltax> jTax = bookingValues.getAlltax();
        getAvailablePaymentMethods(paymentMethod);
        setRecyclerViewTax(recy_Tax, jTax);

        paymentUrl = bookingValues.getPaystack_transaction_url();

        if (jTax.size() == 0) {
            tax_card.setVisibility(View.GONE);
        } else {
            tax_card.setVisibility(View.VISIBLE);
        }

        List<MaterialDetails> materialDetails = bookingValues.getMaterial_details();
        setRecyclerViewMiscellanous(recy_miscellanous, materialDetails);

        if (materialDetails.size() == 0) {
            miscellaneous_card.setVisibility(View.GONE);
        } else {
            miscellaneous_card.setVisibility(View.VISIBLE);
        }

        try {
            if (bookingValues.getWorkedMins() != null) {
                int total = Integer.parseInt(bookingValues.getWorkedMins());
                String hours = mainScreenViewModel.formatHoursAndMinutes(total);
                workedHours.setText(hours);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            bookingTotal.setText(getCombinedString(getString(R.string.currency_symbol), bookingValues.getTotalCost().toString()));
            billingName.setText(bookingValues.getUsername());
            bookingId.setText(bookingValues.getBookingOrderId());
            bookingDate.setText(bookingValues.getBookingDate());
            bookingTime.setText(bookingValues.getTiming());
            bookingPrice.setText(getCombinedString(getString(R.string.currency_symbol), bookingValues.getCost().toString()));
            providerName.setText(bookingValues.getProvidername());
            bookingGst.setText(getCombinedString(getString(R.string.currency_symbol), bookingValues.getGstCost().toString()));
            taxName.setText(bookingValues.getTaxName());
            taxPercentage.setText(getCombinedString(bookingValues.getGstPercent().toString(), getString(R.string.percentage_symbol)));
        } catch (Exception e) {
            e.printStackTrace();
        }


        booking_id = "" + bookingValues.getBookingId();
        booking_order_id = bookingValues.getBookingOrderId();

        if (bookingValues.getOff().equalsIgnoreCase("")) {
            offPrice.setText(getCombinedString(getString(R.string.minus_symbol), " ", getString(R.string.currency_symbol), getString(R.string.no_offers)));
        } else {
            offPrice.setText(getCombinedString(getString(R.string.minus_symbol), " ", getString(R.string.currency_symbol), bookingValues.getOff()));
        }

        if (bookingValues.getCouponApplied().equalsIgnoreCase("")) {
            apply.setImageResource(R.drawable.new_arrow_right);
        } else {
            apply.setImageResource(R.drawable.new_cancelled);
        }

        if (bookingValues.getCouponApplied().equalsIgnoreCase("")) {
            coupons.setText(getString(R.string.apply_coupon));

        } else {
            coupons.setText(bookingValues.getCouponApplied());
        }

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (coupons.getText().toString().equalsIgnoreCase(getString(R.string.apply_coupon))) {
                    dialog.dismiss();
                    showCoupon(booking_order_id);
                } else {
                    if (booking_id != null && !coupons.getText().toString().isEmpty()) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("couponname", "" + coupons.getText().toString().trim());
                            jsonObject.put("booking_order_id", booking_order_id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Utils.show(MainActivity.this);
                        InputForAPI inputForAPI = new InputForAPI(MainActivity.this);
                        inputForAPI.setUrl(UrlHelper.COUPON_REMOVE);
                        inputForAPI.setFile(null);
                        inputForAPI.setJsonObject(jsonObject);
                        inputForAPI.setHeaders(ApiCall.getHeaders(MainActivity.this));
                        removeCoupon(inputForAPI, coupons, apply);


                    }
                }
            }
        });

        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPaymentEnabled = true;
                launchWithCustomer();

            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.show(MainActivity.this);
                if (appSettings.isCardSelected()) {

//                    if (cardID.length() != 0) {
////                        try {
////                            buildCardTokenInputs(cardID, booking_id);
////                        } catch (JSONException e) {
////                            e.printStackTrace();
////                        }
//                    } else {
//                        Utils.dismiss();
//                        Utils.toast(dialog.findViewById(android.R.id.content), getResources().getString(R.string.please_select_any_card));
//                    }

                    if (paymentUrl.length() < 5) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("booking_id", booking_id);
//                        jsonObject.put("booking_id", booking_id);
                            jsonObject.put("amount", bookingValues.getTotalCost());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        InputForAPI inputForAPI = new InputForAPI(MainActivity.this);
                        inputForAPI.setUrl(UrlHelper.PAYSTACKACCESS);
                        inputForAPI.setFile(null);
                        inputForAPI.setJsonObject(jsonObject);
                        inputForAPI.setHeaders(ApiCall.getHeaders(MainActivity.this));

                        commonViewModel.webPayment(inputForAPI).observe(MainActivity.this, new Observer<PayStachAccessURLResponse>() {
                            @Override
                            public void onChanged(@Nullable PayStachAccessURLResponse payStachAccessURLResponse) {
                                if (payStachAccessURLResponse != null) {
                                    if (payStachAccessURLResponse.getError().equalsIgnoreCase("false")) {

                                        viewPayStatckDialog(payStachAccessURLResponse.getData().getAuthorization_url(), booking_id);
                                    } else {
                                        Utils.dismiss();
                                        Utils.toast(dialog.findViewById(android.R.id.content), payStachAccessURLResponse.getError_data());
                                    }
                                } else {
                                    Utils.dismiss();
                                }
                            }
                        });
                    } else {
                        viewPayStatckDialog(paymentUrl, booking_id);
                    }

                } else {

                    try {
                        hitPaymentMethod(booking_id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private void viewPayStatckDialog(final String authorization_url, final String book_id) {

        paymentDialog = new Dialog(this);
        paymentDialog.setCancelable(false);
        paymentDialog.setCanceledOnTouchOutside(false);
        paymentDialog.setContentView(R.layout.dialog_payment);
        paymentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = paymentDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        paymentDialog.show();

        final WebView vistaWeb = paymentDialog.findViewById(R.id.paymentView);

        // Enable Javascript

        vistaWeb.getSettings().setPluginState(WebSettings.PluginState.ON);
        vistaWeb.getSettings().setJavaScriptEnabled(true);
        vistaWeb.getSettings().setDomStorageEnabled(true);
        vistaWeb.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        vistaWeb.getSettings().setSupportMultipleWindows(false);
        vistaWeb.getSettings().setSupportZoom(false);
        vistaWeb.setVerticalScrollBarEnabled(false);
        vistaWeb.setHorizontalScrollBarEnabled(false);


        vistaWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                view.loadUrl(authorization_url);

                return true;
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Log.d(TAG, "shouldOverrideUrlLoading: "+url);
                    if (url.startsWith("https://api.paystack.co/transaction/update_log")) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getPaymentStatus(book_id);
                            }
                        },2000);

                    }
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);


                Utils.dismiss();
            }
        });
        vistaWeb.loadUrl(authorization_url);

//        WebView mywebview = (WebView) paymentDialog.findViewById(R.id.paymentView);
//        mywebview.loadUrl(authorization_url);
    }




    private void getPaymentStatus(String book_id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("booking_id", book_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        InputForAPI inputForAPI = new InputForAPI(MainActivity.this);
        inputForAPI.setUrl(UrlHelper.PAYSTACKSTATUS);
        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaders(ApiCall.getHeaders(MainActivity.this));

        commonViewModel.getPaymentStatus(inputForAPI).observe(this, new Observer<CommonResponseModel>() {
            @Override
            public void onChanged(@Nullable CommonResponseModel commonResponseModel) {
                if(paymentDialog != null){
                    paymentDialog.dismiss();
                }
                appSettingsInput();
            }
        });
    }

    private void getAvailablePaymentMethods(RecyclerView paymentMethod) {

        InputForAPI inputForAPI = new InputForAPI(MainActivity.this);
        inputForAPI.setUrl(UrlHelper.PAYMENT_METHODS);
        inputForAPI.setFile(null);
        inputForAPI.setHeaders(ApiCall.getHeaders(MainActivity.this));
        getPaymentMethods(paymentMethod, inputForAPI);

    }

    private void getPaymentMethods(final RecyclerView paymentMethod, InputForAPI inputForAPI) {
        mainScreenViewModel.getPaymentMethods(inputForAPI).observe(this, new Observer<PaymentMethodModel>() {
            @Override
            public void onChanged(@Nullable PaymentMethodModel paymentMethodModel) {
                List<PaymentType> paymentTypes = paymentMethodModel.getPaymentTypes();
                List<PaymentType> finalpaymentTypes = new ArrayList<>();
                for (int i = 0; i < paymentTypes.size(); i++) {
                    PaymentType paymentType = paymentTypes.get(i);
                    if (i == 0) {
                        paymentType.setIsSelected("true");
                    } else {
                        paymentType.setIsSelected("false");
                    }
                    finalpaymentTypes.add(paymentType);
                }
                setRecyclerView(paymentMethod, finalpaymentTypes);
            }
        });
    }

    private void removeCoupon(final InputForAPI inputForAPI, final TextView coupons, final ImageView apply) {
        commonViewModel.flagUpdate(inputForAPI).observe(this, new Observer<FlagResponseModel>() {
            @Override
            public void onChanged(@Nullable FlagResponseModel flagResponseModel) {
                coupons.setText(getString(R.string.apply_coupon));
                apply.setImageResource(R.drawable.new_cancelled);

                if (flagResponseModel != null) {
                    if (flagResponseModel.getError().equalsIgnoreCase("false")) {
                        appSettingsInput();
                    } else {
                        Utils.toast(dialog.findViewById(android.R.id.content), flagResponseModel.getError_message());
                    }
                }
            }
        });

    }

    private void setRecyclerView(final RecyclerView paymentMethod, List<PaymentType> payment_types) {

        PaymentAdapter paymentAdapter = new PaymentAdapter(MainActivity.this, payment_types, cardLayout);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        paymentMethod.setLayoutManager(linearLayoutManager);
        paymentMethod.setAdapter(paymentAdapter);
        paymentAdapter.notifyDataSetChanged();
    }

    private void hitPaymentMethod(String booking_id) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("method", appSettings.getPaymentType());
        jsonObject.put("id", booking_id);
        InputForAPI inputForAPI = new InputForAPI(MainActivity.this);
        inputForAPI.setUrl(UrlHelper.PAYMENT);
        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaders(ApiCall.getHeaders(MainActivity.this));
        confirmPaymentMethod(inputForAPI);

    }

    public void confirmPaymentMethod(InputForAPI inputForAPI) {
        commonViewModel.flagUpdate(inputForAPI).observe(this, new Observer<FlagResponseModel>() {
            @Override
            public void onChanged(@Nullable FlagResponseModel flagResponseModel) {
                dialog.dismiss();
                appSettingsInput();
            }
        });

    }

    public void showWaitingforPayment() {

        try {
            dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog = new Dialog(MainActivity.this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.view_payment_confirmation);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.BOTTOM);
        dialog.show();
        ImageView payConfImage = dialog.findViewById(R.id.payConfImage);
        Drawable drawable1 = getResources().getDrawable(R.drawable.text_circular);
        drawable1.setColorFilter(new PorterDuffColorFilter(Utils.getPrimaryCOlor(MainActivity.this),
                PorterDuff.Mode.SRC_IN));
        payConfImage.setBackground(drawable1);
    }


    public void showThanks() {

        dialog = new Dialog(MainActivity.this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.show_thanks);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        dialog.show();

        final CircleImageView confirmed = dialog.findViewById(R.id.paymentConfirmed);
        final ImageView paymentWaiting = dialog.findViewById(R.id.paymentWaiting);
        final TextView payConfText = dialog.findViewById(R.id.payConfText);
        Button confirm = dialog.findViewById(R.id.confirm);
        paymentWaiting.setVisibility(View.VISIBLE);
        confirmed.setVisibility(View.GONE);
        payConfText.setText(getResources().getString(R.string.thanks_for));

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appSettingsInput();
                EventBus.getDefault().post(new Status());
                dialog.dismiss();
                ratingDialog.dismiss();
            }
        });

    }


    private void showRating(final StatusAppSettings jsonObject) {
        try {
            ratingDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ratingDialog = new Dialog(MainActivity.this);
        ratingDialog.setCancelable(false);
        ratingDialog.setCanceledOnTouchOutside(false);
        ratingDialog.setContentView(R.layout.view_rating_old);
        ratingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = ratingDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ratingDialog.show();

        final TextView getReceipts = ratingDialog.findViewById(R.id.getReceipts);
        final RatingBar ratingBar = ratingDialog.findViewById(R.id.ratingBar);
        final TextView booking_id = ratingDialog.findViewById(R.id.booking_id);

        Button submit = ratingDialog.findViewById(R.id.submit);
        final LinearLayout topLayout = ratingDialog.findViewById(R.id.topLayout);
        final EditText feedBackText = ratingDialog.findViewById(R.id.feedBackText);
        final LinearLayout commentSection = ratingDialog.findViewById(R.id.commentSection);


        feedBackText.setFilters(new InputFilter[]{specialFilter});

        booking_id.setText(jsonObject.getBookingOrderId());

        JSONObject beforeObject = new JSONObject();
        try {
            beforeObject.put("booking_id", "" + jsonObject.getBookingId());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        InputForAPI inputForAPI = new InputForAPI(MainActivity.this);
        inputForAPI.setUrl(UrlHelper.BEFORE_AFTER_IMAGE);
        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(beforeObject);
        inputForAPI.setHeaders(ApiCall.getHeaders(this));
        getRatingDetails(inputForAPI);


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (commentSection.getVisibility() != View.VISIBLE) {
                    mainScreenViewModel.animateUp(commentSection, topLayout);
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    ratingDialog.dismiss();
                    buildRatingInputs(ratingBar.getRating(), "" + jsonObject.getBookingId(),
                            feedBackText.getText().toString(), "" + jsonObject.getProviderId());

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        getReceipts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pdf != null) {
                    ratingDialog.hide();
                    showReceipt();
                }
            }
        });
    }

    private void handleRatingDetailsResponse(StartJobEndJobResponseModel response) {
        final ImageView after_image = ratingDialog.findViewById(R.id.after_image);
        final ImageView before_image = ratingDialog.findViewById(R.id.before_image);
        final TextView before_tv = ratingDialog.findViewById(R.id.before_tv);
        final TextView after_tv = ratingDialog.findViewById(R.id.after_tv);
        final LinearLayout before_image_ln = ratingDialog.findViewById(R.id.before_image_ln);
        final LinearLayout after_image_ln = ratingDialog.findViewById(R.id.after_image_ln);
        final LinearLayout image_ln = ratingDialog.findViewById(R.id.image_ln);

        Data data = response.getData();
        if (data != null) {
            String start_image = data.getStartImage();
            String end_image = data.getEndImage();

            try {
                if (start_image != null && !start_image.isEmpty() && !start_image.equalsIgnoreCase("null")) {
                    Log.e(TAG, "start_image: " + start_image);
                    image_ln.setVisibility(View.VISIBLE);
                    before_image_ln.setVisibility(View.VISIBLE);
                    before_image.setVisibility(View.VISIBLE);
                    before_tv.setVisibility(View.VISIBLE);
                    loadImageplaceholder(start_image, before_image,
                            ContextCompat.getDrawable(MainActivity.this, R.drawable.service_ph));
                } else {
                    image_ln.setVisibility(View.VISIBLE);
                    before_image_ln.setVisibility(View.VISIBLE);
                    before_image.setVisibility(View.VISIBLE);
                    before_tv.setVisibility(View.VISIBLE);
                    loadImageplaceholder(start_image, before_image,
                            ContextCompat.getDrawable(MainActivity.this, R.drawable.service_ph));
                }

                if (end_image != null && !end_image.isEmpty() && !end_image.equalsIgnoreCase("null")) {
                    image_ln.setVisibility(View.VISIBLE);
                    after_image_ln.setVisibility(View.VISIBLE);
                    after_tv.setVisibility(View.VISIBLE);
                    after_image.setVisibility(View.VISIBLE);
                    loadImageplaceholder(end_image, after_image,
                            ContextCompat.getDrawable(MainActivity.this, R.drawable.service_ph));

                } else {
                    image_ln.setVisibility(View.VISIBLE);
                    after_image_ln.setVisibility(View.VISIBLE);
                    after_image.setVisibility(View.VISIBLE);
                    after_tv.setVisibility(View.VISIBLE);
                    loadImageplaceholder(end_image, after_image,
                            ContextCompat.getDrawable(MainActivity.this, R.drawable.service_ph));
                }
            } catch (Exception e) {
                e.printStackTrace();

            }

        }
        try {

            pdf = response.getInvoicelink().get(0).getInvoicelink();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadImageplaceholder(String url, ImageView imageView, Drawable drawable) {
        GlideHelper.setImage(url, imageView, drawable);
    }

    private void getRatingDetails(InputForAPI inputForAPI) {

        mainScreenViewModel.getRatingDetails(inputForAPI).observe(this, new Observer<StartJobEndJobResponseModel>() {
            @Override
            public void onChanged(@Nullable StartJobEndJobResponseModel startJobEndJobResponseModel) {

                if (startJobEndJobResponseModel != null) {
                    if (!startJobEndJobResponseModel.getError().equalsIgnoreCase("true")) {
                        handleRatingDetailsResponse(startJobEndJobResponseModel);
                    } else {
                        Utils.toast(findViewById(android.R.id.content), startJobEndJobResponseModel.getErrorMessage());
                    }
                }
            }
        });
    }


    private void buildRatingInputs(float rating, String booking_id, String s, String provider_id)
            throws JSONException {

        JSONObject jsonObject = new JSONObject();
        int f_rating = Math.round(rating);
        jsonObject.put("booking_id", booking_id);
        jsonObject.put("rating", "" + f_rating);
        jsonObject.put("id", provider_id);
        jsonObject.put("feedback", s);

        InputForAPI inputForAPI = new InputForAPI(MainActivity.this);
        inputForAPI.setUrl(UrlHelper.REVIEW);
        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaders(ApiCall.getHeaders(this));

        postValues(inputForAPI);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void showReceipt() {
        final Dialog receiptDialog = new Dialog(MainActivity.this);
        receiptDialog.setCancelable(true);
        receiptDialog.setCanceledOnTouchOutside(false);
        receiptDialog.setContentView(R.layout.view_receipt);
        Window window = receiptDialog.getWindow();
        if (window != null) {
            receiptDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        }
        receiptDialog.show();


        receiptDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                receiptDialog.dismiss();
                try {
                    ratingDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        webView = receiptDialog.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setAllowFileAccess(true);


        save = receiptDialog.findViewById(R.id.saveButton);
        save.setVisibility(View.GONE);
        pd = new ProgressDialog(MainActivity.this);
        pd.setMessage(getString(R.string.please_wait));
        pd.setCancelable(false);
        pd.show();

        if (pdf != null) {
            String fullurl = "https://docs.google.com/gview?embedded=true&url=" + pdf;
            MyWebViewClient myWebViewClient = new MyWebViewClient(pd, save, webView);
            webView.setWebViewClient(myWebViewClient);
            webView.loadUrl(fullurl);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pdf != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.STORAGE_PERMISSIONS);
                    } else {
                        new PdfDownload(MainActivity.this).execute();
                    }
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void stringToPdf() {
        boolean appFolder, receiptFolder, pdfFile;
        File filepath = Environment.getExternalStorageDirectory();
        final File zoeFolder = new File(filepath.getAbsolutePath(),
                getResources().getString(R.string.app_name)).getAbsoluteFile();
        if (!zoeFolder.exists()) {
            appFolder = zoeFolder.mkdir();
        }
        File newFolder = new File(zoeFolder,
                getResources().getString(R.string.app_name) + "_Receipts").getAbsoluteFile();
        if (!newFolder.exists()) {
            receiptFolder = newFolder.mkdir();
        }
        Random r = new Random();
        int Low = 1000;
        int High = 10000000;
        int randomImageNo = r.nextInt(High - Low) + Low;
        String camera_captureFile = String.valueOf("PDF_" + randomImageNo);
        final File file = new File(newFolder, camera_captureFile + ".pdf");


        try {
            pdfFile = file.createNewFile();
            Log.e(TAG, "stringToPdf: " + file.getAbsolutePath());
            if (pdfFile) {
                FileDownloader.downloadFile(pdf, file);
            }
        } catch (IOException e) {
            Log.i(TAG, "IOException" + e.getLocalizedMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.STORAGE_PERMISSIONS: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new PdfDownload(MainActivity.this).execute();
                } else {
                    Utils.toast(findViewById(android.R.id.content), getResources().getString(R.string.storage_permission_error));
                }
                break;
            }

        }
    }

    private void postValues(InputForAPI inputForAPI) {

        commonViewModel.flagUpdate(inputForAPI).observe(this, new Observer<FlagResponseModel>() {
            @Override
            public void onChanged(@Nullable FlagResponseModel flagResponseModel) {
                showThanks();
            }
        });
    }

    public void showBlocked() {
        dialog = new Dialog(MainActivity.this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.view_blocked);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStatus(Status event) {
        appSettingsInput();
    }

    public void showDispute() {
        dialog = new Dialog(MainActivity.this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.view_blocked);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        dialog.show();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Status event) {

        appSettingsInput();

    }


    private String buildCardString(@NonNull SourceCardData data) {
        return commonViewModel.getCombinedStrings(data.getBrand(), " ", getString(R.string.ending_in), " ", data.getLast4());
    }

    public void initCustomerSession() {
        CustomerSession.initCustomerSession(
                new ExampleEphemeralKeyProvider(
                        new ExampleEphemeralKeyProvider.ProgressListener() {
                            @Override
                            public void onStringResponse(String string) {
                                Log.e(TAG, "onStringResponse: " + string);

                                if (string.startsWith("Error: ")) {
                                    Log.e(TAG, "onStringResponse: " + string);
                                }
                            }
                        }, MainActivity.this));

        CustomerSession.getInstance().retrieveCurrentCustomer(
                new CustomerSession.CustomerRetrievalListener() {
                    @Override
                    public void onCustomerRetrieved(@NonNull Customer customer) {
                        CustomerSource sourcee = customer.getSourceById(customer.getDefaultSource());
                        String selectedSource;
                        if (sourcee != null) {
                            selectedSource = sourcee.toString();
                            Log.e("RETROFIT_TAG", "selectedSource1: " + selectedSource);
                            setStripeCard(selectedSource);
                        }
                    }

                    @Override
                    public void onError(int errorCode, @Nullable String errorMessage) {
                        Utils.log("chek", "error: ");
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_SOURCE && resultCode == RESULT_OK) {
            Utils.log("RETROFIT_TAG", "REQUEST_CODE_SELECT_SOURCE: ");
            String selectedSource = data.getStringExtra(PaymentMethodsActivity.EXTRA_SELECTED_PAYMENT);
            setStripeCard(selectedSource);
        }
    }

    private void setStripeCard(String selectedSource) {
        Source source = Source.fromString(selectedSource);
        if (source != null && Source.CARD.equals(source.getType())) {
            SourceCardData sourceCardData = (SourceCardData) source.getSourceTypeModel();
            if (sourceCardData != null) {
                cardID = source.getId();
                cardName.setText(buildCardString(sourceCardData));
                optionButton.setText(getResources().getString(R.string.change));

            } else {
                cardID = "";
                cardName.setText("");
                optionButton.setText(getResources().getString(R.string.add_card_text));
            }
        }
    }


    private class PdfDownload extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;

        private PdfDownload(Activity activity) {
            progressDialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            stringToPdf();
            return null;
        }

        protected void onPostExecute(Void result) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            Utils.toast(findViewById(android.R.id.content), getResources().getString(R.string.receipt_downloaded));

        }
    }

    public class MainPagerAdapter extends FragmentStatePagerAdapter {


        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return BookingsFragment.newInstance(getResources().getString(R.string.bookings), "Fragment");
                case 1:
                    return HomeFragment.newInstance(getResources().getString(R.string.home), "Fragment");
                case 2:
                    return ChatFragment.newInstance(getResources().getString(R.string.chat), "Fragment");
                case 3:
                    return AccountsFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
