package com.app.jobfizzer.View.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.app.jobfizzer.Model.AppSettingsResponseModel.Timeslot;
import com.app.jobfizzer.Utilities.Prefhandler.AppSettings;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.R;

import java.util.List;

/**
 * Created by user on 23-10-2017.
 */

public class TimesAdapter extends RecyclerView.Adapter<TimesAdapter.MyViewHolder> {
    private Context context;
    private List<Timeslot> timeSlots;
    private String TAG = TimesAdapter.class.getSimpleName();

    public TimesAdapter(Context context, List<Timeslot> timeSlots) {
        this.context = context;
        this.timeSlots = timeSlots;
    }

    @Override
    public TimesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_time_slots, parent, false);

        return new TimesAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final TimesAdapter.MyViewHolder holder, final int position) {
        try {
            final AppSettings appSettings = new AppSettings(context);
            Timeslot timeslot = timeSlots.get(position);

            holder.timeSlot.setText(timeslot.getTiming());
            if (timeslot.getSelected().equalsIgnoreCase("true")) {
                Utils.setButtonColor(context, holder.timeSlot);
                holder.timeSlot.setTextColor(Color.parseColor("#ffffff"));
                appSettings.setTimeSelected(true);
                appSettings.setSelectedTimeSlot(""+timeslot.getId());
                appSettings.setSelectedTimeText(holder.timeSlot.getText().toString());

            } else {
                holder.timeSlot.setBackgroundResource(R.drawable.gray_bg);
                holder.timeSlot.setTextColor(Color.parseColor("#1d1d1d"));
            }

            holder.timeSlot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    appSettings.setTimeSelected(true);


                    for (int i = 0; i < timeSlots.size(); i++) {
                        if (i != position) {
                            timeSlots.get(i).setSelected("false");

                        } else {
                            timeSlots.get(i).setSelected("true");

                        }
                    }

                    notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        Button timeSlot;

        MyViewHolder(View view) {
            super(view);
            timeSlot = view.findViewById(R.id.timeSlot);
        }
    }
}