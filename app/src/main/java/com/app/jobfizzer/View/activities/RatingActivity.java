package com.app.jobfizzer.View.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.app.jobfizzer.Model.ShowProvidersResponseModel;
import com.app.jobfizzer.R;
import com.app.jobfizzer.View.adapters.ReviewsAdapter;

public class RatingActivity extends BaseActivity {
    RecyclerView ratingRecyclerView;
    ImageView backButton;
    ShowProvidersResponseModel.ProviderService.AllProvider reviews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        initViews();
        initListeners();
        getValues();
        setAdapterValues();
    }

    private void setAdapterValues() {
        LinearLayoutManager timeLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        ratingRecyclerView.setLayoutManager(timeLayoutManager);
        ReviewsAdapter reviewsAdapter = new ReviewsAdapter(this, reviews.getReviews());
        ratingRecyclerView.setAdapter(reviewsAdapter);
    }

    private void getValues() {
        reviews = (ShowProvidersResponseModel.ProviderService.AllProvider) getIntent().getSerializableExtra("reviews");
    }

    private void initListeners() {

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void initViews() {
        ratingRecyclerView = findViewById(R.id.ratingRecyclerView);
        backButton =  findViewById(R.id.backButton);
    }


}
