package com.app.jobfizzer.View.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.jobfizzer.Model.FavouriteBannerImages;
import com.app.jobfizzer.Utilities.ApiCall.ImageLoader;
import com.app.jobfizzer.Utilities.Prefhandler.AppSettings;
import com.app.jobfizzer.View.activities.SelectTimeAndAddressActivity;
import com.app.jobfizzer.View.activities.SubCategoriesActivity;
import com.app.jobfizzer.R;

import java.util.List;

/**
 * Created by karthik on 06/10/17.
 */

public class FavouriteSliderAdapter extends RecyclerView.Adapter<FavouriteSliderAdapter.MyViewHolder> {
    private Activity context;
    private List<FavouriteBannerImages> categories;
    private boolean faveList;
    private AppSettings appSettings;

    public FavouriteSliderAdapter(Activity context, List<FavouriteBannerImages> categories, boolean fave) {
        this.context = context;
        this.categories = categories;
        faveList = fave;
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

    @Override
    public FavouriteSliderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.home_slider, parent, false);
        return new FavouriteSliderAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FavouriteSliderAdapter.MyViewHolder holder, int position) {
        appSettings = new AppSettings(context);

        final FavouriteBannerImages favouriteBannerImages=categories.get(position);


        ImageLoader imageLoader=new ImageLoader(context);
        imageLoader.load(favouriteBannerImages.getBanner_logo(),holder.categoryImage, context.getResources().getDrawable(R.drawable.banner_placeholder));


        holder.categoryName.setText(favouriteBannerImages.getBanner_name());

        if (faveList) {
            final String subCate = favouriteBannerImages.getToSubCategory();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (subCate != null) {
                        if (subCate.equalsIgnoreCase("true")) {
                            Intent intent = new Intent(context, SubCategoriesActivity.class);
                            intent.putExtra("subcategoriesArray", categories.toString());
                            intent.putExtra("subCategoryId", favouriteBannerImages.getId());
                            context.startActivity(intent);
                            context.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);

                        } else {
                            Intent intent = new Intent(context, SelectTimeAndAddressActivity.class);
                            appSettings.setSelectedSubCategory(favouriteBannerImages.getSubId());
                            context.startActivity(intent);
                            context.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);

                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}