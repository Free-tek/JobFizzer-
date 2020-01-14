package com.app.jobfizzer.View.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.jobfizzer.Model.CategoryResponseModel.Banner_images;
import com.app.jobfizzer.Utilities.ApiCall.ImageLoader;
import com.app.jobfizzer.Utilities.Prefhandler.AppSettings;
import com.app.jobfizzer.R;

import java.util.List;

/**
 * Created by karthik on 06/10/17.
 */

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.MyViewHolder> {
    private Context context;
    private List<Banner_images> categories;
    private boolean faveList;
    private AppSettings appSettings;

    public SliderAdapter(Context context, List<Banner_images> categories, boolean fave) {
        this.context = context;
        this.categories = categories;
        faveList = fave;
    }

    @Override
    public SliderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.home_slider, parent, false);
        return new SliderAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SliderAdapter.MyViewHolder holder, int position) {
        appSettings = new AppSettings(context);
        Banner_images banner_images = categories.get(position);

        ImageLoader imageLoader = new ImageLoader(context);
        imageLoader.load(banner_images.getBanner_logo(), holder.categoryImage, context.getResources().getDrawable(R.drawable.banner_placeholder));


        holder.categoryName.setText(banner_images.getBanner_name());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryImage;

        MyViewHolder(View view) {
            super(view);
            categoryName = view.findViewById(R.id.categoryName);
            categoryImage = view.findViewById(R.id.categoryImage);
        }
    }
}