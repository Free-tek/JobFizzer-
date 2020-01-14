package com.app.jobfizzer.View.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.jobfizzer.Model.ShowProvidersResponseModel;
import com.app.jobfizzer.R;

import java.util.List;

/**
 * Created by yuvaraj on 18/12/17.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder> {
    private Context context;
    private List<ShowProvidersResponseModel.Review> reviews;

    public ReviewsAdapter(Context context, List<ShowProvidersResponseModel.Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, reviewContent;
        RatingBar ratingBar;


        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.reviewName);
            reviewContent = view.findViewById(R.id.reviewContent);
            ratingBar = view.findViewById(R.id.ratingBar);
        }
    }

    @Override
    public ReviewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reviews, parent, false);

        return new ReviewsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.MyViewHolder holder, int position) {
        ShowProvidersResponseModel.Review review = reviews.get(position);
        holder.reviewContent.setText(review.getFeedback());
        holder.ratingBar.setRating(review.getRating());
        holder.name.setText(review.getUsername());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}
