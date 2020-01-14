package com.app.jobfizzer.View.activities;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.app.jobfizzer.Model.ShowProvidersResponseModel;
import com.app.jobfizzer.Utilities.ApiCall.ImageLoader;
import com.app.jobfizzer.Utilities.Prefhandler.AppSettings;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.View.adapters.OtherServicesAdapter;
import com.app.jobfizzer.View.fragments.ProfileFragment;
import com.app.jobfizzer.View.fragments.ReviewsFragment;
import com.app.jobfizzer.ViewModel.ProviderProfileViewModel;
import com.app.jobfizzer.R;

import java.util.ArrayList;
import java.util.List;

import static com.app.jobfizzer.Utilities.Constants.Constants.CALL_PERMISSIONS;

public class ProviderProfileActivity extends BaseActivity implements ProfileFragment.OnFragmentInteractionListener, ReviewsFragment.OnFragmentInteractionListener {

    ShowProvidersResponseModel.ProviderService.AllProvider jsonObject;
    List<ShowProvidersResponseModel.Review> reviewsArray = new ArrayList<>();
    List<ShowProvidersResponseModel.ProviderService> otherServices = new ArrayList<>();
    LinearLayout reviewParent;
    ImageLoader imageLoader;
    ImageView providerPic;
    TextView readMore;
    ScrollView scrollView;
    RecyclerView reviewsRecyclerView, reviewsRecyclerView_one;
    TextView summaryContent, distance, rating;
    TextView priceContent;
    CardView callButton;
    TextView reviewNameOne, reviewNameTwo, reviewNameThree;
    TextView reviewContentOne, reviewContentTwo, reviewContentThree;
    RatingBar ratingOne, ratingTwo, ratingThree;
    RelativeLayout rateOne, rateTwo, rateThree;
    TextView addressValue;
    AppSettings appSettings = new AppSettings(ProviderProfileActivity.this);
    ProviderProfileViewModel providerProfileViewModel;
    private TextView providerName, providerMobile;
    private Button bookNow;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_profile);
        imageLoader = new ImageLoader(this);
        providerProfileViewModel = ViewModelProviders.of(this).get(ProviderProfileViewModel.class);
        initViews();
        initListners();
        setValues();
        initAdapters();

    }

    private void initAdapters() {
        LinearLayoutManager timeLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        OtherServicesAdapter reviewsAdapter = new OtherServicesAdapter(this, otherServices);
        reviewsRecyclerView_one.setLayoutManager(timeLayoutManager);
        reviewsRecyclerView_one.setAdapter(reviewsAdapter);
    }


    private void initViews() {
        reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView);
        reviewsRecyclerView_one = findViewById(R.id.reviewsRecyclerView_one);
        bookNow = findViewById(R.id.bookNow);
        providerName = findViewById(R.id.providerName);
        rating = findViewById(R.id.rating);
        distance = findViewById(R.id.distance);
        providerMobile = findViewById(R.id.providerMobile);
        backButton = findViewById(R.id.backButton);
        providerPic = findViewById(R.id.providerPic);
        summaryContent = findViewById(R.id.summaryContent);
        priceContent = findViewById(R.id.priceContent);
        readMore = findViewById(R.id.readMore);
        scrollView = findViewById(R.id.scrollView);
        callButton = findViewById(R.id.callButton);
        reviewNameOne = findViewById(R.id.reviewNameOne);
        reviewNameTwo = findViewById(R.id.reviewNameTwo);
        reviewNameThree = findViewById(R.id.reviewNameThree);
        reviewContentOne = findViewById(R.id.reviewContentOne);
        reviewContentTwo = findViewById(R.id.reviewContentTwo);
        reviewContentThree = findViewById(R.id.reviewContentThree);
        addressValue = findViewById(R.id.addressValue);
        ratingOne = findViewById(R.id.ratingBarOne);
        ratingTwo = findViewById(R.id.ratingBarTwo);
        ratingThree = findViewById(R.id.ratingBarThree);
        rateOne = findViewById(R.id.rateOne);
        rateTwo = findViewById(R.id.rateTwo);
        rateThree = findViewById(R.id.rateThree);
        reviewParent = findViewById(R.id.reviewParent);

    }

    private void setValues() {
        List<String> stringsToCombined = new ArrayList<>();
        jsonObject = (ShowProvidersResponseModel.ProviderService.AllProvider) getIntent().getSerializableExtra("providerDetails");
        reviewsArray = jsonObject.getReviews();
        otherServices = jsonObject.getProviderServices();
        providerName.setText(jsonObject.getName());
        imageLoader.load(jsonObject.getImage(), providerPic, Utils.getProfilePicture(ProviderProfileActivity.this));
        summaryContent.setText(jsonObject.getAbout());
        rating.setText(String.valueOf(jsonObject.getAvgRating()));


        stringsToCombined.add(jsonObject.getDistance().toString());
        stringsToCombined.add(" km");
        distance.setText(getCombinedString(stringsToCombined));


        providerMobile.setText(String.valueOf(jsonObject.getMobile()));


        stringsToCombined = new ArrayList<>();
        stringsToCombined.add(getString(R.string.currency_symbol));
        stringsToCombined.add(jsonObject.getPriceperhour());
        stringsToCombined.add(" ");
        stringsToCombined.add(getString(R.string.price_per_hour));
        priceContent.setText(getCombinedString(stringsToCombined));


        stringsToCombined = new ArrayList<>();
        stringsToCombined.add(jsonObject.getAddressline1());
        stringsToCombined.add(" ");
        stringsToCombined.add(jsonObject.getAddressline2());
        stringsToCombined.add(" ");
        stringsToCombined.add(jsonObject.getCity());
        stringsToCombined.add(" ");
        stringsToCombined.add(jsonObject.getState());
        stringsToCombined.add(" ");
        stringsToCombined.add(jsonObject.getZipcode());
        addressValue.setText(getCombinedString(stringsToCombined));


        if (reviewsArray.size() > 3) {
            readMore.setVisibility(View.VISIBLE);
        } else {
            readMore.setVisibility(View.GONE);
        }


        if (reviewsArray.size() == 0) {
            reviewParent.setVisibility(View.GONE);
            rateOne.setVisibility(View.GONE);
            rateTwo.setVisibility(View.GONE);
            rateThree.setVisibility(View.GONE);
        } else if (reviewsArray.size() == 1) {
            reviewParent.setVisibility(View.VISIBLE);

            rateOne.setVisibility(View.VISIBLE);
            rateTwo.setVisibility(View.GONE);
            rateThree.setVisibility(View.GONE);
        } else if (reviewsArray.size() == 2) {
            reviewParent.setVisibility(View.VISIBLE);

            rateOne.setVisibility(View.VISIBLE);
            rateTwo.setVisibility(View.VISIBLE);
            rateThree.setVisibility(View.GONE);
        } else if (reviewsArray.size() == 3) {
            reviewParent.setVisibility(View.VISIBLE);
            rateOne.setVisibility(View.VISIBLE);
            rateTwo.setVisibility(View.VISIBLE);
            rateThree.setVisibility(View.VISIBLE);
        }
        setReviewValues();
    }

    private String getCombinedString(List<String> stringsToCombined) {
        return providerProfileViewModel.getCombinedString(stringsToCombined);
    }

    private void setReviewValues() {

        for (int i = 0; i < reviewsArray.size(); i++) {
            ShowProvidersResponseModel.Review jsonObject = reviewsArray.get(i);
            if (i == 0) {
                reviewNameOne.setText(jsonObject.getUsername());
                reviewContentOne.setText(jsonObject.getFeedback());
                ratingOne.setRating(jsonObject.getRating());
            } else if (i == 1) {
                reviewNameTwo.setText(jsonObject.getUsername());
                reviewContentTwo.setText(jsonObject.getFeedback());
                ratingTwo.setRating(jsonObject.getRating());

            } else if (i == 2) {
                reviewNameThree.setText(jsonObject.getUsername());
                reviewContentThree.setText(jsonObject.getFeedback());
                ratingThree.setRating(jsonObject.getRating());

            } else {
                break;
            }
        }
    }

    private void initListners() {

        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveConfirmBookingActivity();

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRatingACtivity();
            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPermissionGranted()) {
                    call_action(jsonObject.getMobile());
                }
            }
        });

    }

    private void moveConfirmBookingActivity() {
        Intent confirmBooking = new Intent(ProviderProfileActivity.this, ConfirmBookingActivity.class);
        confirmBooking.putExtra("providerDetails", jsonObject);
        startActivity(confirmBooking);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);

    }


    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSIONS);
                return false;
            }
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CALL_PERMISSIONS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call_action(jsonObject.getMobile());
                } else {
                    Utils.toast(findViewById(android.R.id.content), getString(R.string.permission_denied));

                }
            }
        }
    }

    public void call_action(long phnum) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phnum));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }

    private void showRatingACtivity() {
        Intent intent = new Intent(ProviderProfileActivity.this, RatingActivity.class);
        intent.putExtra("reviews", jsonObject);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAfterTransition(this);
    }

}

