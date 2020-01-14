package com.app.jobfizzer.View.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.jobfizzer.Model.ShowProvidersResponseModel;
import com.app.jobfizzer.Utilities.ApiCall.ImageLoader;
import com.app.jobfizzer.View.activities.ProviderProfileActivity;
import com.app.jobfizzer.View.activities.ShowProvidersActivity;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.R;

import java.util.List;

/**
 * Created by karthik on 11/10/17.
 */
public class ProvidersListAdapter extends RecyclerView.Adapter<ProvidersListAdapter.MyViewHolder> {
    private Context context;
    private List<ShowProvidersResponseModel.ProviderService.AllProvider> providers;


    public ProvidersListAdapter(Context context, List<ShowProvidersResponseModel.ProviderService.AllProvider> providers) {
        this.context = context;
        this.providers = providers;
    }

    @Override
    public ProvidersListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.provider_list_view, parent, false);

        return new ProvidersListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ProvidersListAdapter.MyViewHolder holder, int position) {
        final ShowProvidersResponseModel.ProviderService.AllProvider subCategory = providers.get(position);
        holder.providerName.setText(subCategory.getName());
        holder.serviceType.setText(String.valueOf(subCategory.getMobile()));
        double val = Double.parseDouble(String.valueOf(subCategory.getDistance()));
        @SuppressLint("DefaultLocale") String disntac = String.format("%.2f", val);
        holder.distance.setText(disntac);
        try {
            holder.ratingView.setRating(subCategory.getAvgRating());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ImageLoader imageLoader = new ImageLoader(context);
        imageLoader.load(subCategory.getImage(), holder.providerPic, Utils.getProfilePicture(context));


        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(context, ProviderProfileActivity.class);
                    intent.putExtra("providerDetails", subCategory);
                    Pair<View, String> p1 = Pair.create((View) holder.providerName, "providerName");
                    Pair<View, String> p2 = Pair.create((View) holder.providerPic, "providerPic");
                    Pair<View, String> p3 = Pair.create((View) holder.rootLayout, "parentLayout");
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((ShowProvidersActivity) context, p1, p2, p3);
                    ActivityCompat.startActivity(context, intent, options.toBundle());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return providers.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView providerName, distance, serviceType;
        RatingBar ratingView;
        ImageView providerPic;
        LinearLayout rootLayout;

        MyViewHolder(View view) {
            super(view);
            providerName = view.findViewById(R.id.providerName);
            serviceType = view.findViewById(R.id.serviceType);
            distance = view.findViewById(R.id.distance);
            providerPic = view.findViewById(R.id.providerPic);
            rootLayout = view.findViewById(R.id.rootLayout);
            ratingView = view.findViewById(R.id.ratingView);
        }
    }
}
