package com.app.jobfizzer.View.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.jobfizzer.Utilities.ApiCall.ImageLoader;
import com.app.jobfizzer.Utilities.DatabaseHandler;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.R;
import com.app.jobfizzer.View.activities.SubCategoriesActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by karthik on 06/10/17.
 */
public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyViewHolder> {
    private Activity context;
    private JSONArray categories;
    private boolean likedStatus = false;
    private DatabaseHandler db;

    public CategoriesAdapter(Activity context, JSONArray categories) {
        this.context = context;
        this.categories = categories;
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

    @Override
    public CategoriesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        db = new DatabaseHandler(context);
        return new CategoriesAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final CategoriesAdapter.MyViewHolder holder, int position) {
        try {
            final JSONObject subCategory = categories.getJSONObject(position);
            Utils.log("onBindViewHolder: ", "jsonObject" + subCategory);
            holder.categoryName.setText(subCategory.optString("category_name"));


            ImageLoader imageLoader=new ImageLoader(context);
            imageLoader.load(subCategory.optString("icon"),holder.categoryImage, context.getResources().getDrawable(R.drawable.service_placeholder));



            holder.categoryImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent timeAndAddress = new Intent(context, SubCategoriesActivity.class);
                    timeAndAddress.putExtra("subcategoriesArray", categories.toString());
                    timeAndAddress.putExtra("subCategoryId", subCategory.optString("id"));
                    context.startActivity(timeAndAddress);
                    context.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);

                }
            });


//            if (db.isLiked(subCategory.optString("id"), "category")) {
//                holder.favorite_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.filled));
//
//            } else {
//                holder.favorite_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.unfilled));
//
//            }
//
//            holder.favorite_layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (!db.isLiked(subCategory.optString("id"), "category")) {
//
//                        db.insertData(subCategory.optString("id"), subCategory.optString("category_name"), subCategory.optString("icon"));
//                        holder.favorite_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.filled));
//                        Toast.makeText(context, R.string.added_favorites, Toast.LENGTH_SHORT).show();
//
//                    } else {
//                        db.deleteData(subCategory.optString("id"), "category");
//                        holder.favorite_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.unfilled));
//
//                    }
//                    EventBus.getDefault().post(new Favorite());
//                    notifyDataSetChanged();
//
//                }
//            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return categories.length();
    }
}


