package com.app.jobfizzer.View.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.jobfizzer.Utilities.Events.onSelected;
import com.app.jobfizzer.Model.DateModel;
import com.app.jobfizzer.Utilities.Prefhandler.AppSettings;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by user on 23-10-2017.
 */

public class DatesAdapter extends RecyclerView.Adapter<DatesAdapter.MyViewHolder> {
    private Context context;
    private List<DateModel> dates;
    private String TAG = DatesAdapter.class.getSimpleName();

    public DatesAdapter(Context context, List<DateModel> dates) {
        this.context = context;
        this.dates = dates;
    }

    @Override
    public DatesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_date_slots, parent, false);

        return new DatesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DatesAdapter.MyViewHolder holder, final int position) {

        final AppSettings appSettings = new AppSettings(context);

        final DateModel singledate = dates.get(position);

        holder.date.setText(singledate.getDate());
        holder.day.setText(singledate.getDay());
        if (singledate.getSelected().equalsIgnoreCase("true")) {
            if (position == 0) {
                appSettings.setTodaySelected(true);
            } else {
                appSettings.setTodaySelected(false);
            }
            appSettings.setIsDateSeleceted(true);
            appSettings.setSelectedDate(singledate.getFullDate());
            holder.date.setTextColor(Color.parseColor("#ffffff"));
            Drawable drawable1 = context.getResources().getDrawable(R.drawable.text_circular);
            drawable1.setColorFilter(new PorterDuffColorFilter(Utils.getPrimaryCOlor(context), PorterDuff.Mode.SRC_IN));
            holder.date.setBackground(drawable1);

        } else {


            Drawable drawable1 = context.getResources().getDrawable(R.drawable.text_circular);
            drawable1.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.white),
                    PorterDuff.Mode.SRC_IN));
            holder.date.setBackground(drawable1);
            holder.date.setTextColor(context.getResources().getColor(R.color.grey));

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                appSettings.setTimeSelected(false);
                if (singledate.getSelected().equalsIgnoreCase("true")) {
                    if (position == 0) {
                        appSettings.setTodaySelected(true);
                    } else {
                        appSettings.setTodaySelected(false);
                    }
                } else {
                    if (position == 0) {
                        appSettings.setTodaySelected(true);
                    } else {
                        appSettings.setTodaySelected(false);
                    }
                    for (int i = 0; i < dates.size(); i++) {
                        if (i != position) {
                            dates.get(i).setSelected("false");
                        } else {
                            dates.get(i).setSelected("true");
                        }
                    }
                    EventBus.getDefault().post(new onSelected("no"));

                }
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView day;
        TextView date;

        MyViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.date);
            day = view.findViewById(R.id.day);
        }
    }
}