package com.app.jobfizzer.View.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.jobfizzer.Model.ViewBookingsResponseModel.AllBooking;
import com.app.jobfizzer.Utilities.ApiCall.ImageLoader;
import com.app.jobfizzer.View.activities.DetailedBookingActivity;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.R;

import java.util.List;

/**
 * Created by karthik on 07/10/17.
 */

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.MyViewHolder> {
    public View parentView;
    private Activity context;
    private List<AllBooking> subCategories;
    private String TAG = BookingsAdapter.class.getSimpleName();

    public BookingsAdapter(Activity context, List<AllBooking> subCategories, View parentView) {
        this.context = context;
        this.subCategories = subCategories;
        this.parentView = parentView;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final AllBooking allBooking = subCategories.get(position);


        String providerName, prvoider_pic;
        providerName = allBooking.getProvidername();
        prvoider_pic = allBooking.getImage();
        holder.serviceName.setText(allBooking.getSubCategoryName());
        holder.serviceTiming.setText(allBooking.getTiming());
        holder.providerName.setText(providerName);
        holder.serviceDate.setText(allBooking.getBookingDate());
        ImageLoader imageLoader = new ImageLoader(context);
        imageLoader.load(prvoider_pic, holder.providerPic, Utils.getProfilePicture(context));

        String status = allBooking.getStatus();
        int colorvalue = 0;
        String statusValue = "";
        Drawable statusIcon = null;
        if (status.equalsIgnoreCase("Pending")) {
            statusValue = context.getResources().getString(R.string.pending);
            colorvalue = context.getResources().getColor(R.color.status_orange);
            statusIcon = context.getResources().getDrawable(R.drawable.new_pending);
        } else if (status.equalsIgnoreCase("Rejected")) {
            statusValue = context.getResources().getString(R.string.rejected);
            colorvalue = context.getResources().getColor(R.color.red);
            statusIcon = context.getResources().getDrawable(R.drawable.new_cancelled);
        } else if (status.equalsIgnoreCase("Accepted")) {
            statusValue = context.getResources().getString(R.string.accepted);
            colorvalue = context.getResources().getColor(R.color.green);
            statusIcon = context.getResources().getDrawable(R.drawable.new_accepted);
        } else if (status.equalsIgnoreCase("CancelledbyUser")) {
            statusValue = context.getResources().getString(R.string.cancelled) + " " + context.getResources().getString(R.string.by_user);
            colorvalue = context.getResources().getColor(R.color.red);
            statusIcon = context.getResources().getDrawable(R.drawable.new_cancelled);
        } else if (status.equalsIgnoreCase("CancelledbyProvider")) {
            statusValue = context.getResources().getString(R.string.cancelled) + " " + context.getResources().getString(R.string.by_provider);
            colorvalue = context.getResources().getColor(R.color.red);
            statusIcon = context.getResources().getDrawable(R.drawable.new_cancelled);
        } else if (status.equalsIgnoreCase("StarttoCustomerPlace")) {
            statusValue = context.getResources().getString(R.string.on_the_way);
            colorvalue = context.getResources().getColor(R.color.status_orange);
            statusIcon = context.getResources().getDrawable(R.drawable.new_start_to_place);
        } else if (status.equalsIgnoreCase("Startedjob")) {
            statusValue = context.getResources().getString(R.string.job_started);
            colorvalue = context.getResources().getColor(R.color.status_orange);
            statusIcon = context.getResources().getDrawable(R.drawable.new_started_job);
        } else if (status.equalsIgnoreCase("Completedjob")) {
            statusValue = context.getResources().getString(R.string.completed);
            colorvalue = context.getResources().getColor(R.color.green);
            statusIcon = context.getResources().getDrawable(R.drawable.new_complete_job);
        } else if (status.equalsIgnoreCase("Waitingforpaymentconfirmation")) {
            statusValue = context.getResources().getString(R.string.waiting_for);
            colorvalue = context.getResources().getColor(R.color.status_orange);
            statusIcon = context.getResources().getDrawable(R.drawable.new_pay_conifrmation);
        } else if (status.equalsIgnoreCase("Reviewpending")) {
            statusValue = context.getResources().getString(R.string.review_pending);
            colorvalue = context.getResources().getColor(R.color.ratingColor);
            statusIcon = context.getResources().getDrawable(R.drawable.new_review);
        } else if (status.equalsIgnoreCase("Finished")) {
            statusValue = context.getResources().getString(R.string.finished);
            colorvalue = context.getResources().getColor(R.color.green);
            statusIcon = context.getResources().getDrawable(R.drawable.new_finished);
        }

        holder.statusText.setText(statusValue);
        holder.statusText.setTextColor(colorvalue);
        holder.statusIcon.setImageDrawable(statusIcon);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailedBooking = new Intent(context, DetailedBookingActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("bookingValues", allBooking);
                detailedBooking.putExtras(bundle);

                context.startActivity(detailedBooking);
                context.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
            }
        });
    }


    @Override
    public int getItemCount() {
        return subCategories.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView serviceName, serviceTiming, statusText, serviceDate;
        ImageView statusIcon;
        ImageView providerPic;
        TextView providerName;

        MyViewHolder(View view) {
            super(view);
            statusText = view.findViewById(R.id.statusText);
            providerPic = view.findViewById(R.id.providerPic);
            Utils.setIconColour(context, providerPic.getDrawable());
            providerName = view.findViewById(R.id.providerName);
            serviceName = view.findViewById(R.id.serviceName);
            serviceTiming = view.findViewById(R.id.serviceTiming);
            serviceDate = view.findViewById(R.id.serviceDate);
            statusIcon = view.findViewById(R.id.statusIcon);
        }
    }
}
