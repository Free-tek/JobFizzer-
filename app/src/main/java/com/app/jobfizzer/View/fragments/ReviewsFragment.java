package com.app.jobfizzer.View.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by karthik on 11/10/17.
 */
public class ReviewsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    JSONArray reviews;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ReviewsFragment.OnFragmentInteractionListener mListener;
    private String TAG=ReviewsFragment.class.getSimpleName();

    public ReviewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            try {
                reviews=new JSONArray(mParam2);
                Utils.log(TAG,": "+reviews);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_reviews, container, false);

        RecyclerView reviewsRecyclerView = view.findViewById(R.id.reviewsRecyclerView);

        LinearLayoutManager timeLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);

        reviewsRecyclerView.setLayoutManager(timeLayoutManager);
        ReviewsAdapter reviewsAdapter= new ReviewsAdapter(getActivity(),reviews);
        reviewsRecyclerView.setAdapter(reviewsAdapter);
        return view;
    }

    public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder>{
        private Context context;
        private JSONArray reviews;

        public ReviewsAdapter(Context context, JSONArray reviews){
            this.context =context;
            this.reviews = reviews;
        }
        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView name,reviewContent;
            RatingBar ratingBar;


            MyViewHolder(View view) {
                super(view);
                name =  view.findViewById(R.id.reviewName);
                reviewContent =  view.findViewById(R.id.reviewContent);
                ratingBar =  view.findViewById(R.id.ratingBar);
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
            try {
                JSONObject review = reviews.getJSONObject(position);
                holder.reviewContent.setText(review.optString("feedback"));
                holder.ratingBar.setRating(Float.parseFloat(review.optString("rating")));
                holder.name.setText(review.optString("username"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return reviews.length();
        }
    }
    
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
