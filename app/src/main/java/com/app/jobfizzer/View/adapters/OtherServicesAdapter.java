package com.app.jobfizzer.View.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.jobfizzer.Model.ShowProvidersResponseModel;
import com.app.jobfizzer.R;

import java.util.List;

/**
 * Created by yuvaraj on 18/12/17.
 */

public class OtherServicesAdapter extends RecyclerView.Adapter<OtherServicesAdapter.MyViewHolder> {
    private Context context;
    private List<ShowProvidersResponseModel.ProviderService> reviews;

    public OtherServicesAdapter(Context context, List<ShowProvidersResponseModel.ProviderService> reviews) {
        this.context = context;
        this.reviews = reviews;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView serviceName, priceValue;

        MyViewHolder(View view) {
            super(view);
            serviceName = view.findViewById(R.id.serviceName);
            priceValue = view.findViewById(R.id.priceValue);

        }
    }

    @Override
    public OtherServicesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_other_services, parent, false);

        return new OtherServicesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OtherServicesAdapter.MyViewHolder holder, int position) {
        ShowProvidersResponseModel.ProviderService review = reviews.get(position);
        holder.serviceName.setText(review.getSubCategoryName());
        holder.priceValue.setText(review.getDisplayPricePerhour(context));

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}
