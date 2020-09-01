package com.app.jobfizzer.View.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.jobfizzer.Model.AppSettingsResponseModel.Alltax;
import com.app.jobfizzer.Model.AppSettingsResponseModel.MaterialDetails;
import com.app.jobfizzer.Model.FlagResponseModel;
import com.app.jobfizzer.Model.ViewBookingsResponseModel.AllBooking;
import com.app.jobfizzer.Model.ViewBookingsResponseModel.Invoicedetail;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.ImageLoader;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.app.jobfizzer.Utilities.Constants.Status;
import com.app.jobfizzer.Utilities.Constants.UrlHelper;
import com.app.jobfizzer.Utilities.helpers.CustomLibraries.CircleImageView;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.View.adapters.PaymentMiscellaneousAdapter;
import com.app.jobfizzer.View.adapters.PaymentTaxAdapter;
import com.app.jobfizzer.ViewModel.CommonViewModel;
import com.app.jobfizzer.ViewModel.DetailedBookingViewModel;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.app.jobfizzer.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DetailedBookingActivity extends BaseActivity {
    AllBooking bookingValues;
    ImageView backButton;
    LinearLayout billLayout;
    RelativeLayout topBillingLayout;
    RelativeLayout activity_detailed_booking;
    Button statusBackground;
    CircleImageView profilePic;
    CommonViewModel commonViewModel;
    TextView billingName, bookingId, bokkingDate, bookingAddress, pricePerhour, serviceName, providerName;
    ImageView mapData;
    TextView totalCostandTime, hoursValue;
    TextView statusText;
    View viewOne, viewTwo, viewThree, viewFour, viewFive, viewSix, viewSeven, viewEight, viewNine, viewTen, viewEleven;
    LinearLayout layOne, layTwo, layThree, layFour, layFive, laySix;
    TextView textOne, textTwo, textThree, textFour, textFive, textSix;
    ImageView timeOne, timeTwo, timeThree, timeFour, timeFive, timeSix;
    String statusOne = "", statusTwo = "", statusThree = "", statusFour = "", statusFive = "", statusSix = "";

    DetailedBookingViewModel detailedBookingViewModel;
    TextView bookingGst, taxName, taxPercentage;
    ImageLoader imageLoader;
    private String TAG = DetailedBookingActivity.class.getSimpleName();
    private SpeedDialView speedDialView;
    ImageView vimg_btn_receipt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_booking);
        imageLoader = new ImageLoader(this);
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel.class);
        initViews();
        setValues();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        statusBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (statusBackground.getText().toString().equalsIgnoreCase(getResources().getString(R.string.track))) {
                    trackPage();
                } else {
                    showCancelDialog("" + bookingValues.getId());
                }
            }
        });

//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showCancelDialog("" + bookingValues.getId());
//
//            }
//        });

//        chat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(DetailedBookingActivity.this, ChatActivity.class);
//                intent.putExtra("bookingValues", bookingValues);
//                startActivity(intent);
//                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
//            }
//        });
/*
        if (fabWithLabelViewCancel != null && fabWithLabelViewChat != null && fabWithLabelViewTrack != null) {
            fabWithLabelViewCancel.setSpeedDialActionItem(fabWithLabelViewCancel
                    .getSpeedDialActionItemBuilder().setFabImageTintColor(ResourcesCompat
                            .getColor(getResources(),
                                    R.color.blue, getTheme()))
                    .setFabBackgroundColor(ResourcesCompat.getColor(getResources(),
                            R.color.white, getTheme())).create());

            fabWithLabelViewChat.setSpeedDialActionItem(fabWithLabelViewChat
                    .getSpeedDialActionItemBuilder().setFabImageTintColor(ResourcesCompat
                            .getColor(getResources(),
                                    R.color.blue, getTheme()))
                    .setFabBackgroundColor(ResourcesCompat.getColor(getResources(),
                            R.color.white, getTheme())).create());

            fabWithLabelViewTrack.setSpeedDialActionItem(fabWithLabelViewTrack
                    .getSpeedDialActionItemBuilder().setFabImageTintColor(ResourcesCompat
                            .getColor(getResources(),
                                    R.color.blue, getTheme()))
                    .setFabBackgroundColor(ResourcesCompat.getColor(getResources(),
                            R.color.white, getTheme())).create());

        }*/

        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                switch (actionItem.getId()) {
                    case R.id.fab_cancel:
                        showCancelDialog("" + bookingValues.getId());
                        speedDialView.close();
                        return true;
                    case R.id.fab_track:
                        trackPage();
                        speedDialView.close();
                        return true;
                    case R.id.fab_chat:
                        Intent intent = new Intent(DetailedBookingActivity.this, ChatActivity.class);
                        intent.putExtra("userImage", bookingValues.getImage());
                        intent.putExtra("userName", bookingValues.getProvidername());
                        intent.putExtra("userID", "" + bookingValues.getProviderId());
                        intent.putExtra("booking_id", "" + bookingValues.getId());
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
                        speedDialView.close();
                        return true;
                }
                return false;
            }
        });

        vimg_btn_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showViewReceipt();
            }
        });
    }

    private void addFabCancel() {
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_cancel, R.drawable.new_cancelled)
                        .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()))
                        .setFabImageTintColor(Utils.getPrimaryCOlor(DetailedBookingActivity.this))
                        .setLabel(getString(R.string.cancel))
                        .setLabelColor(ResourcesCompat.getColor(getResources(), R.color.text_colour, getTheme()))
                        .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()))
                        .setLabelClickable(true)
                        .create());
    }

    private void addFabTrack() {
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_track, R.drawable.track_marker)
                        .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()))
                        .setFabImageTintColor(Utils.getPrimaryCOlor(DetailedBookingActivity.this))
                        .setLabel(getString(R.string.track))
                        .setLabelColor(ResourcesCompat.getColor(getResources(), R.color.text_colour, getTheme()))
                        .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()))
                        .setLabelClickable(true)
                        .create());
    }

    private void addFabChat() {
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_chat, R.drawable.chat)
                        .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()))
                        .setFabImageTintColor(Utils.getPrimaryCOlor(DetailedBookingActivity.this))
                        .setLabel(getString(R.string.chat))
                        .setLabelColor(ResourcesCompat.getColor(getResources(), R.color.text_colour, getTheme()))
                        .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()))
                        .setLabelClickable(true)
                        .create());
    }

    private void removeFabChat() {
        speedDialView.removeActionItemById(R.id.fab_chat);
    }

    private void removeFabCancel() {
        speedDialView.removeActionItemById(R.id.fab_cancel);
    }

    private void removeFabTrack() {
        speedDialView.removeActionItemById(R.id.fab_track);
    }

    private void removeFabIcon() {
        speedDialView.setVisibility(View.GONE);
    }

    private void showViewReceipt() {
        final Dialog dialog = new Dialog(DetailedBookingActivity.this);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
//        dialog.setContentView(R.layout.receipt_dialog);
        dialog.setContentView(R.layout.invoice_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        dialog.show();

        TextView txt_close, providerName, billingName, bookingId, bookingDate,
                bookingTime, workedHours, bookingPrice, bookingGst, bookingTotal,
                header, invoice;
        RecyclerView recy_Tax, recy_miscellanous;

        LinearLayout payment_header, coupons_header;

        CardView miscellaneous_card, tax_card;

        recy_Tax = dialog.findViewById(R.id.recy_Tax);
        recy_miscellanous = dialog.findViewById(R.id.recy_miscellanous);
        miscellaneous_card = dialog.findViewById(R.id.miscellaneous_card);
        tax_card = dialog.findViewById(R.id.tax_card);
        txt_close = dialog.findViewById(R.id.confirm);
        providerName = dialog.findViewById(R.id.providerName);
        billingName = dialog.findViewById(R.id.billingName);
        bookingId = dialog.findViewById(R.id.bookingId);
        bookingDate = dialog.findViewById(R.id.bookingDate);
        workedHours = dialog.findViewById(R.id.workedHours);
        bookingTime = dialog.findViewById(R.id.bookingTime);
        bookingPrice = dialog.findViewById(R.id.bookingPrice);
        bookingTotal = dialog.findViewById(R.id.bookingTotal);


        header = dialog.findViewById(R.id.header);
        payment_header = dialog.findViewById(R.id.payment_header);
        coupons_header = dialog.findViewById(R.id.coupons_header);
        invoice = dialog.findViewById(R.id.invoice);

        header.setText(R.string.billing_details);
        txt_close.setText(R.string.close);

        payment_header.setVisibility(View.GONE);
        coupons_header.setVisibility(View.GONE);
        invoice.setVisibility(View.GONE);


        txt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        List<Invoicedetail> invoicedetail = bookingValues.getInvoicedetails();

        try {
//            jTax = bookingValues.getJSONArray("invoicedetails");
            providerName.setText(invoicedetail.get(0).getProvidername());
            billingName.setText(invoicedetail.get(0).getUsername());
            bookingId.setText(invoicedetail.get(0).getBookingOrderId());
            bookingDate.setText(invoicedetail.get(0).getBookingDate());
            bookingTime.setText(invoicedetail.get(0).getTiming());
            bookingPrice.setText(invoicedetail.get(0).getCost());
            bookingTotal.setText(getString(R.string.currency_symbol) + invoicedetail.get(0).getTotalCost());
            bookingDate.setText(invoicedetail.get(0).getBookingDate());

            if (invoicedetail.get(0).getWorkedMins() != null) {
                int total = Integer.parseInt(String.valueOf(invoicedetail.get(0).getWorkedMins()));
                String hours = detailedBookingViewModel.formatHoursAndMinutes(total);
                workedHours.setText(hours);
            }

            List<Alltax> jTaxreceipt = invoicedetail.get(0).getAlltax();
            List<MaterialDetails> materialDetails = invoicedetail.get(0).getMaterialDetails();

            if (jTaxreceipt.size() == 0) {
                tax_card.setVisibility(View.GONE);
            } else {
                tax_card.setVisibility(View.VISIBLE);
            }
            if (materialDetails.size() == 0) {
                miscellaneous_card.setVisibility(View.GONE);
            } else {
                miscellaneous_card.setVisibility(View.VISIBLE);
            }
            setRecyclerViewTax(recy_Tax, jTaxreceipt);

            setRecyclerViewMiscellanous(recy_miscellanous, materialDetails);

        } catch (Exception e) {
            Log.d("json_value_EXE", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    private void setRecyclerViewTax(final RecyclerView recy_tax, List<Alltax> invoicedetail) {
        PaymentTaxAdapter paymentAdapter = new PaymentTaxAdapter(DetailedBookingActivity.this,
                invoicedetail);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(DetailedBookingActivity.this, LinearLayoutManager.VERTICAL, false);
        recy_tax.setLayoutManager(linearLayoutManager);
        recy_tax.setAdapter(paymentAdapter);
        paymentAdapter.notifyDataSetChanged();
    }

    private void setRecyclerViewMiscellanous(RecyclerView recy_miscellanous, List<MaterialDetails> materialDetails) {
        PaymentMiscellaneousAdapter paymentMiscellaneousAdapter = new PaymentMiscellaneousAdapter(DetailedBookingActivity.this,
                materialDetails);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailedBookingActivity.this, LinearLayoutManager.VERTICAL, false);
        recy_miscellanous.setLayoutManager(linearLayoutManager);
        recy_miscellanous.setAdapter(paymentMiscellaneousAdapter);
        paymentMiscellaneousAdapter.notifyDataSetChanged();
    }

    private void showCancelDialog(final String id) {
        final Dialog dialog = new Dialog(DetailedBookingActivity.this);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.cancel_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.show();
        TextView yesButton, noButton;
        yesButton = dialog.findViewById(R.id.yesButton);
        noButton = dialog.findViewById(R.id.noButton);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                buildCancelRequestInput(id);

            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void buildCancelRequestInput(String id) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        InputForAPI inputForAPI = new InputForAPI(DetailedBookingActivity.this);
        inputForAPI.setUrl(UrlHelper.CANCEL_BOOKING);
        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaders(ApiCall.getHeaders(this));
        cancelRequest(inputForAPI);


    }


    private void cancelRequest(InputForAPI inputForAPI) {
        commonViewModel.flagUpdate(inputForAPI).observe(this, new Observer<FlagResponseModel>() {
            @Override
            public void onChanged(@Nullable FlagResponseModel flagResponseModel) {
                finish();
            }
        });
    }

    private void trackPage() {
        Intent tracking = new Intent(DetailedBookingActivity.this, TrackingActivity.class);
        tracking.putExtra("bookingValues", bookingValues);

        startActivity(tracking);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
    }


    private void initViews() {

        backButton = findViewById(R.id.backButton);
        billingName = findViewById(R.id.billingName);
        hoursValue = findViewById(R.id.hoursValue);
        bookingGst = findViewById(R.id.bookingGst);
        statusBackground = findViewById(R.id.statusBackground);
        totalCostandTime = findViewById(R.id.totalCostandTime);
        bookingId = findViewById(R.id.bookingId);
        bokkingDate = findViewById(R.id.bokkingDate);
        bookingAddress = findViewById(R.id.bookingAddress);
        pricePerhour = findViewById(R.id.pricePerhour);
        serviceName = findViewById(R.id.serviceName);
        billLayout = findViewById(R.id.billLayout);
        profilePic = findViewById(R.id.profilePic);
        taxName = findViewById(R.id.taxName);
        taxPercentage = findViewById(R.id.taxPercentage);
        mapData = findViewById(R.id.mapData);
        statusText = findViewById(R.id.statusText);
        activity_detailed_booking = findViewById(R.id.activity_detailed_booking);
        topBillingLayout = findViewById(R.id.topBillingLayout);
//        cancel = findViewById(R.id.cancel);
        //  chat = findViewById(R.id.chat);
        activity_detailed_booking.setBackgroundColor(getResources().getColor(R.color.transparent));
        textOne = findViewById(R.id.textOne);
        textTwo = findViewById(R.id.textTwo);
        textThree = findViewById(R.id.textThree);
        textFour = findViewById(R.id.textFour);
        textFive = findViewById(R.id.textFive);
        textSix = findViewById(R.id.textSix);
        layOne = findViewById(R.id.layOne);
        layTwo = findViewById(R.id.layTwo);
        layThree = findViewById(R.id.layThree);
        layFour = findViewById(R.id.layFour);
        layFive = findViewById(R.id.layFive);
        laySix = findViewById(R.id.laySix);

        timeOne = findViewById(R.id.timeOne);
        timeTwo = findViewById(R.id.timeTwo);
        timeThree = findViewById(R.id.timeThree);
        timeFour = findViewById(R.id.timeFour);
        timeFive = findViewById(R.id.timeFive);
        timeSix = findViewById(R.id.timeSix);


        viewOne = findViewById(R.id.viewOne);
        viewTwo = findViewById(R.id.viewTwo);
        viewThree = findViewById(R.id.viewThree);
        viewFour = findViewById(R.id.viewFour);
        viewFive = findViewById(R.id.viewFive);
        viewSix = findViewById(R.id.viewSix);
        viewSeven = findViewById(R.id.viewSeven);
        viewEight = findViewById(R.id.viewEight);
        viewNine = findViewById(R.id.viewNine);
        viewTen = findViewById(R.id.viewTen);
        viewEleven = findViewById(R.id.viewEleven);
        detailedBookingViewModel = ViewModelProviders.of(this).get(DetailedBookingViewModel.class);

        vimg_btn_receipt = findViewById(R.id.id_imgbtn_receipt);

        speedDialView = findViewById(R.id.speedDial);

        addFabChat();
        addFabTrack();
        addFabCancel();

    }

    public String getCombinedString(String s, String s1) {
        return detailedBookingViewModel.getCombinedStrings(s, s1);
    }


    private void setValues() {
        Bundle bundle = getIntent().getExtras();
        bookingValues = (AllBooking) bundle.getSerializable("bookingValues");
        Utils.log(TAG, "" + bookingValues.getStatus());
//        bookingValues = (AllBooking) getIntent().getSerializableExtra("bookingValues");
        String showBill = bookingValues.getShowBillFlag();
        if (showBill.equalsIgnoreCase("0")) {
            topBillingLayout.setVisibility(View.GONE);
            billLayout.setVisibility(View.GONE);
            totalCostandTime.setVisibility(View.GONE);
            billLayout.setVisibility(View.GONE);
            bookingGst.setVisibility(View.GONE);
            taxName.setVisibility(View.GONE);
            taxPercentage.setVisibility(View.GONE);
        } else {
            billLayout.setVisibility(View.VISIBLE);
            topBillingLayout.setVisibility(View.VISIBLE);
            totalCostandTime.setVisibility(View.VISIBLE);
            int total = Integer.parseInt(bookingValues.getWorkedMins());
            String hours = detailedBookingViewModel.formatHoursAndMinutes(total);
            totalCostandTime.setText(getString(R.string.currency_symbol) + bookingValues.getTotalCost());
            hoursValue.setText(hours);
            pricePerhour.setText(getString(R.string.currency_symbol) + " " + bookingValues.getCost());
        }

        billingName.setText(bookingValues.getUsername());
        bookingId.setText(bookingValues.getBookingOrderId());
        bokkingDate.setText(bookingValues.getBookingDate() + " " + bookingValues.getTiming());
        bookingAddress.setText(bookingValues.getAddressLine1());
        serviceName.setText(bookingValues.getSubCategoryName());
        String status = bookingValues.getStatus();

        statusText.setText(status.toUpperCase());

        if (bookingValues.getPendingTime() == null) {
            statusOne = getResources().getString(R.string.service_booked);
        } else {
            statusOne = getCombinedString(getResources().getString(R.string.service_booked), detailedBookingViewModel.getConvertedTime(bookingValues.getPendingTime()));
        }

        if (bookingValues.getAcceptedTime() == null) {
            statusTwo = getResources().getString(R.string.service_accepted);
        } else {
            statusTwo = getCombinedString(getResources().getString(R.string.service_accepted), detailedBookingViewModel.getConvertedTime(bookingValues.getAcceptedTime()));
        }
        if (bookingValues.getStarttoCustomerPlaceTime() == null) {
            statusThree = getResources().getString(R.string.start_to_customer_place);
        } else {
            statusThree = getCombinedString(getResources().getString(R.string.start_to_customer_place), detailedBookingViewModel.getConvertedTime(bookingValues.getStarttoCustomerPlaceTime()));
        }

        if (bookingValues.getStartjobTimestamp() == null) {
            statusFour = getResources().getString(R.string.provider_arrived);
            statusFive = getResources().getString(R.string.job_started);
        } else {
            statusFour = getCombinedString(getResources().getString(R.string.provider_arrived), detailedBookingViewModel.getConvertedTime(bookingValues.getStartjobTimestamp()));
            statusFive = getCombinedString(getResources().getString(R.string.job_started), detailedBookingViewModel.getConvertedTime(bookingValues.getStartjobTimestamp()));
        }

        if (bookingValues.getEndjobTimestamp() == null) {
            statusSix = getResources().getString(R.string.job_completed);
        } else {
            statusSix = getCombinedString(getResources().getString(R.string.job_completed), detailedBookingViewModel.getConvertedTime(bookingValues.getEndjobTimestamp()));
        }


        int colorvalue = 0;

        if (detailedBookingViewModel.isEqual(status, Status.PENDING)) {
            statusBackground.setVisibility(View.GONE);

            setBooked();
            colorvalue = getResources().getColor(R.color.light_violet);
            statusText.setText(getResources().getString(R.string.pending));

            removeFabIcon();

        } else if (detailedBookingViewModel.isEqual(status, Status.REJECTED)) {
            statusBackground.setVisibility(View.GONE);

            statusTwo = getCombinedString(getResources().getString(R.string.service_rejected), detailedBookingViewModel.getConvertedTime(bookingValues.getRejectedTime()));
            setAccepted();
            colorvalue = getResources().getColor(R.color.red);
            statusText.setText(getResources().getString(R.string.rejected));
            removeFabIcon();

        } else if (detailedBookingViewModel.isEqual(status, Status.ACCEPTED)) {
            statusText.setText(getResources().getString(R.string.accepted));

            statusBackground.setVisibility(View.GONE);

            setAccepted();
            colorvalue = getResources().getColor(R.color.light_violet);

            removeFabTrack();
            addFabChat();
            addFabCancel();


        } else if (detailedBookingViewModel.isEqual(status, Status.CANCELLED_BY_USER)) {
            statusBackground.setVisibility(View.GONE);

            if (bookingValues.getCancelledbyUserTime() == null) {
                statusThree = getResources().getString(R.string.cancelled_by_user);

            } else {
                statusThree = getCombinedString(getResources().getString(R.string.cancelled_by_user), detailedBookingViewModel.getConvertedTime(bookingValues.getCancelledbyUserTime()));
            }

            setStartToPlace();
            colorvalue = getResources().getColor(R.color.red);
            statusText.setText(getResources().getString(R.string.cancelled));

            removeFabIcon();

        } else if (detailedBookingViewModel.isEqual(status, Status.CANCELLED_BY_PROVIDER)) {
            statusBackground.setVisibility(View.GONE);

            if (bookingValues.getCancelledbyProviderTime() == null) {
                statusThree = getResources().getString(R.string.cancelled_by_provider);


            } else {
                statusThree = getCombinedString(getResources().getString(R.string.cancelled_by_provider), bookingValues.getCancelledbyProviderTime());
            }

            setStartToPlace();
            colorvalue = getResources().getColor(R.color.red);
            statusText.setText(getResources().getString(R.string.cancelled));

            removeFabIcon();

        } else if (detailedBookingViewModel.isEqual(status, Status.START_TO_CUSTOMER_PLACE)) {
            statusBackground.setText(getResources().getString(R.string.track));

            statusBackground.setVisibility(View.VISIBLE);

            setStartToPlace();

            colorvalue = getResources().getColor(R.color.light_violet);
            Utils.log(TAG, "status: " +
                    "" + getResources().getString(R.string.on_the_way));
            statusText.setText(getResources().getString(R.string.on_the_way));

            addFabCancel();
            addFabChat();
            addFabTrack();

        } else if (detailedBookingViewModel.isEqual(status, Status.STARTED_JOB)) {
            statusBackground.setVisibility(View.GONE);

            setJobStarted();
            colorvalue = getResources().getColor(R.color.light_violet);
            statusText.setText(getResources().getString(R.string.working));

            removeFabCancel();
            addFabChat();
            removeFabTrack();

        } else if (detailedBookingViewModel.isEqual(status, Status.COMPLETED_JOB)) {
            statusBackground.setVisibility(View.GONE);

            setJobCompleted();
            colorvalue = getResources().getColor(R.color.green);
            statusText.setText(getResources().getString(R.string.completed));

            removeFabIcon();

        } else if (detailedBookingViewModel.isEqual(status, Status.WATIGIN_FOR_PAYMENT_CONFIRMATION)) {
            statusBackground.setVisibility(View.GONE);

            colorvalue = getResources().getColor(R.color.status_orange);
            statusBackground.setVisibility(View.GONE);
            setJobCompleted();
            removeFabIcon();

        } else if (detailedBookingViewModel.isEqual(status, Status.REVIEW_PENDING)) {
            statusBackground.setVisibility(View.GONE);

            colorvalue = getResources().getColor(R.color.ratingColor);
            statusBackground.setVisibility(View.GONE);
            setJobCompleted();
            removeFabIcon();

        } else if (detailedBookingViewModel.isEqual(status, Status.FINISHED)) {
            statusBackground.setVisibility(View.GONE);

            setJobCompleted();
            colorvalue = getResources().getColor(R.color.green);
            statusText.setText(getResources().getString(R.string.completed));

            removeFabIcon();
        }

        statusText.setTextColor(colorvalue);


        loadStaticMap("" + bookingValues.getBoookingLatitude(), "" + bookingValues.getBookingLongitude());

    }


    public void setBooked() {
        layOne.setVisibility(View.VISIBLE);
        layTwo.setVisibility(View.GONE);
        layThree.setVisibility(View.GONE);
        layFour.setVisibility(View.GONE);
        layFive.setVisibility(View.GONE);
        laySix.setVisibility(View.GONE);
        textOne.setText(statusOne);
        textTwo.setText(statusTwo);
        textThree.setText(statusThree);
        textFour.setText(statusFour);
        textFive.setText(statusFive);
        textSix.setText(statusSix);

        viewOne.setVisibility(View.INVISIBLE);
        viewTwo.setVisibility(View.INVISIBLE);
        viewThree.setVisibility(View.INVISIBLE);
        viewFour.setVisibility(View.INVISIBLE);
        viewFive.setVisibility(View.INVISIBLE);
        viewSix.setVisibility(View.INVISIBLE);
        viewSeven.setVisibility(View.INVISIBLE);
        viewEight.setVisibility(View.INVISIBLE);
        viewNine.setVisibility(View.INVISIBLE);
        viewTen.setVisibility(View.INVISIBLE);
        viewEleven.setVisibility(View.INVISIBLE);

        timeOne.setImageDrawable(getResources().getDrawable(R.drawable.circle_violeet_filled));

    }

    public void setAccepted() {
        layOne.setVisibility(View.VISIBLE);
        layTwo.setVisibility(View.VISIBLE);
        layThree.setVisibility(View.GONE);
        layFour.setVisibility(View.GONE);
        layFive.setVisibility(View.GONE);
        laySix.setVisibility(View.GONE);
        textOne.setText(statusOne);
        textTwo.setText(statusTwo);
        textThree.setText(statusThree);
        textFour.setText(statusFour);
        textFive.setText(statusFive);
        textSix.setText(statusSix);

        viewOne.setVisibility(View.VISIBLE);
        viewTwo.setVisibility(View.VISIBLE);
        viewThree.setVisibility(View.INVISIBLE);
        viewFour.setVisibility(View.INVISIBLE);
        viewFive.setVisibility(View.INVISIBLE);
        viewSix.setVisibility(View.INVISIBLE);
        viewSeven.setVisibility(View.INVISIBLE);
        viewEight.setVisibility(View.INVISIBLE);
        viewNine.setVisibility(View.INVISIBLE);
        viewTen.setVisibility(View.INVISIBLE);
        viewEleven.setVisibility(View.INVISIBLE);

        timeTwo.setImageDrawable(getResources().getDrawable(R.drawable.circle_violeet_filled));
    }

    public void setStartToPlace() {
        layOne.setVisibility(View.VISIBLE);
        layTwo.setVisibility(View.VISIBLE);
        layThree.setVisibility(View.VISIBLE);
        layFour.setVisibility(View.GONE);
        layFive.setVisibility(View.GONE);
        laySix.setVisibility(View.GONE);
        textOne.setText(statusOne);
        textTwo.setText(statusTwo);
        textThree.setText(statusThree);
        textFour.setText(statusFour);
        textFive.setText(statusFive);
        textSix.setText(statusSix);


        viewOne.setVisibility(View.VISIBLE);
        viewTwo.setVisibility(View.VISIBLE);
        viewThree.setVisibility(View.VISIBLE);
        viewFour.setVisibility(View.VISIBLE);
        viewFive.setVisibility(View.INVISIBLE);
        viewSix.setVisibility(View.INVISIBLE);
        viewSeven.setVisibility(View.INVISIBLE);
        viewEight.setVisibility(View.INVISIBLE);
        viewNine.setVisibility(View.INVISIBLE);
        viewTen.setVisibility(View.INVISIBLE);
        viewEleven.setVisibility(View.INVISIBLE);

        timeThree.setImageDrawable(getResources().getDrawable(R.drawable.circle_violeet_filled));
    }


    public void setProviderArrived() {
        layOne.setVisibility(View.VISIBLE);
        layTwo.setVisibility(View.VISIBLE);
        layThree.setVisibility(View.VISIBLE);
        layFour.setVisibility(View.VISIBLE);
        layFive.setVisibility(View.GONE);
        laySix.setVisibility(View.GONE);
        textOne.setText(statusOne);
        textTwo.setText(statusTwo);
        textThree.setText(statusThree);
        textFour.setText(statusFour);
        textFive.setText(statusFive);
        textSix.setText(statusSix);


        viewOne.setVisibility(View.VISIBLE);
        viewTwo.setVisibility(View.VISIBLE);
        viewThree.setVisibility(View.VISIBLE);
        viewFour.setVisibility(View.VISIBLE);
        viewFive.setVisibility(View.VISIBLE);
        viewSix.setVisibility(View.INVISIBLE);
        viewSeven.setVisibility(View.INVISIBLE);
        viewEight.setVisibility(View.INVISIBLE);
        viewNine.setVisibility(View.INVISIBLE);
        viewTen.setVisibility(View.INVISIBLE);
        viewEleven.setVisibility(View.INVISIBLE);

        timeFour.setImageDrawable(getResources().getDrawable(R.drawable.circle_violeet_filled));
    }

    public void setJobStarted() {
        layOne.setVisibility(View.VISIBLE);
        layTwo.setVisibility(View.VISIBLE);
        layThree.setVisibility(View.VISIBLE);
        layFour.setVisibility(View.VISIBLE);
        layFive.setVisibility(View.VISIBLE);
        laySix.setVisibility(View.GONE);

        textOne.setText(statusOne);
        textTwo.setText(statusTwo);
        textThree.setText(statusThree);
        textFour.setText(statusFour);
        textFive.setText(statusFive);
        textSix.setText(statusSix);


        viewOne.setVisibility(View.VISIBLE);
        viewTwo.setVisibility(View.VISIBLE);
        viewThree.setVisibility(View.VISIBLE);
        viewFour.setVisibility(View.VISIBLE);
        viewFive.setVisibility(View.VISIBLE);
        viewSix.setVisibility(View.VISIBLE);
        viewSeven.setVisibility(View.VISIBLE);
        viewEight.setVisibility(View.VISIBLE);
        viewNine.setVisibility(View.INVISIBLE);
        viewTen.setVisibility(View.INVISIBLE);
        viewEleven.setVisibility(View.INVISIBLE);

        timeFive.setImageDrawable(getResources().getDrawable(R.drawable.circle_violeet_filled));
    }


    public void setJobCompleted() {
        layOne.setVisibility(View.VISIBLE);
        layTwo.setVisibility(View.VISIBLE);
        layThree.setVisibility(View.VISIBLE);
        layFour.setVisibility(View.VISIBLE);
        layFive.setVisibility(View.VISIBLE);
        laySix.setVisibility(View.VISIBLE);
        textOne.setText(statusOne);
        textTwo.setText(statusTwo);
        textThree.setText(statusThree);
        textFour.setText(statusFour);
        textFive.setText(statusFive);
        textSix.setText(statusSix);
        viewOne.setVisibility(View.VISIBLE);
        viewTwo.setVisibility(View.VISIBLE);
        viewThree.setVisibility(View.VISIBLE);
        viewFour.setVisibility(View.VISIBLE);
        viewFive.setVisibility(View.VISIBLE);
        viewSix.setVisibility(View.VISIBLE);
        viewSeven.setVisibility(View.VISIBLE);
        viewEight.setVisibility(View.VISIBLE);
        viewNine.setVisibility(View.VISIBLE);
        viewTen.setVisibility(View.VISIBLE);
        viewEleven.setVisibility(View.INVISIBLE);

        timeSix.setImageDrawable(getResources().getDrawable(R.drawable.circle_violeet_filled));
    }


    private void loadStaticMap(String lat, String longg) {
        imageLoader.load(detailedBookingViewModel.getStaticMapUrl(lat, longg), mapData, Utils.getMapRequest(DetailedBookingActivity.this));
    }


}
