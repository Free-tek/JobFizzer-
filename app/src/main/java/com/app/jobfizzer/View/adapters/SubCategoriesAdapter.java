package com.app.jobfizzer.View.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.jobfizzer.Model.SubCategoryResponseModel.List_subcategory;
import com.app.jobfizzer.Utilities.ApiCall.ImageLoader;
import com.app.jobfizzer.Utilities.Prefhandler.AppSettings;
import com.app.jobfizzer.Utilities.helpers.Interfaces.onDeleteClicked;
import com.app.jobfizzer.Utilities.helpers.Interfaces.onSubFavourite;
import com.app.jobfizzer.View.activities.SelectTimeAndAddressActivity;
import com.app.jobfizzer.R;

import java.util.List;

/**
 * Created by karthik on 07/10/17.
 */
public class SubCategoriesAdapter extends RecyclerView.Adapter<SubCategoriesAdapter.MyViewHolder> {
    private Activity context;
    private List<List_subcategory> subCategories;
    private onDeleteClicked onDeleteClicked;
    private onSubFavourite onSubFavourite;

    public SubCategoriesAdapter(Activity context, List<List_subcategory> subCategories) {
        this.context = context;
        this.subCategories = subCategories;
    }

    public void onDeleteClicked(onDeleteClicked onDeleteClicked) {
        this.onDeleteClicked = onDeleteClicked;
    }

    public void onFavourite(onSubFavourite onSubFavourite) {
        this.onSubFavourite = onSubFavourite;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView subCategoryName;
        ImageView subCategoryImage, favourite_icon;
        LinearLayout subCategoryLayout;
        RelativeLayout favourite_layout;

        MyViewHolder(View view) {
            super(view);
            subCategoryName = view.findViewById(R.id.subCategoryName);
            subCategoryImage = view.findViewById(R.id.subCategoryImage);
            subCategoryLayout = view.findViewById(R.id.subCategoryLayout);
            favourite_layout = view.findViewById(R.id.favourite_layout);
            favourite_icon = view.findViewById(R.id.favourite_icon);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_sub_category, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final AppSettings appSettings = new AppSettings(context);
        final List_subcategory list_subcategory = subCategories.get(position);
        holder.subCategoryName.setText(list_subcategory.getSub_category_name());
        ImageLoader imageLoader = new ImageLoader(context);
        imageLoader.load(list_subcategory.getIcon(), holder.subCategoryImage, context.getResources().getDrawable(R.drawable.service_placeholder));

        holder.subCategoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appSettings.setSelectedSubCategory("" + list_subcategory.getId());
                appSettings.setSelectedSubCategoryName(list_subcategory.getSub_category_name());
                Intent timeAndAddress = new Intent(context, SelectTimeAndAddressActivity.class);
                context.startActivity(timeAndAddress);
                context.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
            }
        });

        if (list_subcategory.getIsLiked()) {
            holder.favourite_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.filled));
        } else {
            holder.favourite_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.unfilled));
        }

        holder.favourite_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!list_subcategory.getIsLiked()) {
                    onSubFavourite.liked(list_subcategory, holder.getAdapterPosition());
                    holder.favourite_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.filled));
                } else {
                    onDeleteClicked.deleted("" + list_subcategory.getId(), holder.getAdapterPosition());
                    holder.favourite_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.unfilled));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return subCategories.size();
    }
}