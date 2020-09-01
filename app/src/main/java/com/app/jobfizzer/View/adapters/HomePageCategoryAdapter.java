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

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.jobfizzer.Model.CategoryResponseModel.List_category;
import com.app.jobfizzer.Utilities.ApiCall.ImageLoader;
import com.app.jobfizzer.Utilities.helpers.Interfaces.onDeleteClicked;
import com.app.jobfizzer.Utilities.helpers.Interfaces.onFavourite;
import com.app.jobfizzer.View.activities.SubCategoriesActivity;
import com.app.jobfizzer.ViewModel.HomePageViewModel;
import com.app.jobfizzer.R;

import java.util.List;

/**
 * Created by karthik on 06/10/17.
 */
public class HomePageCategoryAdapter extends RecyclerView.Adapter<HomePageCategoryAdapter.MyViewHolder> {
    private Activity context;
    private List<List_category> categories;
    private onDeleteClicked onDeleteClicked;
    private onFavourite onFavourite;

    public HomePageCategoryAdapter(Activity context, List<List_category> categories, HomePageViewModel homePageViewModel) {
        this.context = context;
        this.categories = categories;
    }

    public void onDeleteClicked(onDeleteClicked onDeleteClicked) {
        this.onDeleteClicked = onDeleteClicked;
    }

    public void onFavourite(onFavourite onFavourite) {
        this.onFavourite = onFavourite;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryImage, favorite_icon;
        LinearLayout categoryLayout;
        RelativeLayout favorite_layout;

        MyViewHolder(View view) {
            super(view);
            categoryName = view.findViewById(R.id.categoryName);
            favorite_icon = view.findViewById(R.id.favorite_icon);
            categoryImage = view.findViewById(R.id.categoryImage);
            favorite_layout = view.findViewById(R.id.favorite_layout);
            categoryLayout = view.findViewById(R.id.categoryLayout);
        }
    }

    @NonNull
    @Override
    public HomePageCategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new HomePageCategoryAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final HomePageCategoryAdapter.MyViewHolder holder, int position) {
        final List_category category = categories.get(position);
        holder.categoryName.setText(category.getCategory_name());
        ImageLoader imageLoader = new ImageLoader(context);
        imageLoader.load(category.getIcon(), holder.categoryImage, context.getResources().getDrawable(R.drawable.service_placeholder));

        holder.categoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent timeAndAddress = new Intent(context, SubCategoriesActivity.class);
                timeAndAddress.putExtra("subCategoryId", "" + category.getId());
                context.startActivity(timeAndAddress);
                context.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);

            }
        });


        if (category.getIsLiked()) {
            holder.favorite_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.filled));
        } else {
            holder.favorite_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.unfilled));
        }

        holder.favorite_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!category.getIsLiked()) {
                    onFavourite.liked(category, holder.getAdapterPosition());
                    holder.favorite_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.filled));
                } else {
                    onDeleteClicked.deleted("" + category.getId(), holder.getAdapterPosition());
                    holder.favorite_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.unfilled));
                }
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}


