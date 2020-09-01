package com.app.jobfizzer.View.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.jobfizzer.Model.Prediction;
import com.app.jobfizzer.R;
import com.app.jobfizzer.Utilities.helpers.Interfaces.onClickListener;
import java.util.List;


public class AutoCompleteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    onClickListener onClickListener;
    List<Prediction> Places;


    public AutoCompleteAdapter(Context context, List<Prediction> modelsArrayList) {
        this.context = context;
        Places = modelsArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.location_item, parent, false);
        RecyclerView.ViewHolder viewHolder;
        viewHolder = new Location(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        Location holder = (Location) viewHolder;


        holder.name.setText(Places.get(position).getTerms().get(0).getValue());
        holder.location.setText(Places.get(position).getDescription());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Places.size();
    }

    public void clear() {
        Places.clear();
        notifyDataSetChanged();
    }

    public void setOnClickListner(onClickListener onClickListner) {
        this.onClickListener = onClickListner;
    }



    class Location extends RecyclerView.ViewHolder {
        TextView name, location;

        Location(View view) {
            super(view);
            name = view.findViewById(R.id.place_name);
            location = view.findViewById(R.id.place_detail);
        }
    }
}