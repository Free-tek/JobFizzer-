package com.app.jobfizzer.View.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.jobfizzer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by karthik on 06/10/17.
 */
public class MainCategoriesAdapter extends RecyclerView.Adapter<MainCategoriesAdapter.MyViewHolder> {
    private Context context;
    private JSONArray categories;

    public MainCategoriesAdapter(Context context, JSONArray categories) {
        this.context = context;
        this.categories = categories;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        RecyclerView subCategories;

        MyViewHolder(View view) {
            super(view);
            categoryName = view.findViewById(R.id.categoryName);
            subCategories = view.findViewById(R.id.subCategories);
        }
    }

    @Override
    public MainCategoriesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_category_item, parent, false);

        return new MainCategoriesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MainCategoriesAdapter.MyViewHolder holder, int position) {
        try {
            JSONObject category = categories.getJSONObject(position);

            holder.categoryName.setText(category.optString("name"));
            JSONArray subCategories = new JSONArray();
            subCategories = category.optJSONArray("subCategories");
//            CategoriesAdapter categoriesAdapter = new CategoriesAdapter(context, subCategories);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            holder.subCategories.setLayoutManager(layoutManager);
//            holder.subCategories.setAdapter(categoriesAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return categories.length();
    }
}


