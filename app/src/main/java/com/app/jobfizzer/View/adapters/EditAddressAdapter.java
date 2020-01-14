package com.app.jobfizzer.View.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.jobfizzer.Model.AddResponseModel.ListAddress;
import com.app.jobfizzer.Utilities.ConnectionUtils;
import com.app.jobfizzer.Utilities.helpers.Interfaces.onDeleteClicked;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.R;
import com.app.jobfizzer.View.activities.MapActivity;

import java.util.List;

/**
 * Created by user on 23-10-2017.
 */

public class EditAddressAdapter extends RecyclerView.Adapter<EditAddressAdapter.MyViewHolder> {
    private Activity context;
    private List<ListAddress> addresses;
    private String TAG = EditAddressAdapter.class.getSimpleName();
    onDeleteClicked onDeleted;


    public EditAddressAdapter(Activity context, List<ListAddress> addresses) {
        this.context = context;
        this.addresses = addresses;
    }

    @Override
    public EditAddressAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_items, parent, false);

        return new EditAddressAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final EditAddressAdapter.MyViewHolder holder, final int position) {

        final ListAddress listAddress = addresses.get(position);
        holder.address.setText(listAddress.getAddressLine1());
        holder.addressTitle.setText(listAddress.getTitle());

        holder.editLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectionUtils.isNetworkConnected(context)) {
                    Intent intent = new Intent(context, MapActivity.class);
                    intent.putExtra("from", "edit");
                    intent.putExtra("modelValue", listAddress);
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
                } else {
                    Utils.toast(context.findViewById(android.R.id.content), context.getResources().getString(R.string.no_internet_connection));
                }
            }
        });


        holder.deleteLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectionUtils.isNetworkConnected(context)) {
                    onDeleted.deleted("" + listAddress.getId(), holder.getAdapterPosition());
                } else {
                    Utils.toast(context.findViewById(android.R.id.content), context.getResources().getString(R.string.no_internet_connection));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    public void setOnDeleted(onDeleteClicked onDeleted) {
        this.onDeleted = onDeleted;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView address, addressTitle;
        LinearLayout deleteLay, editLay;

        MyViewHolder(View view) {
            super(view);
            address = view.findViewById(R.id.address);
            deleteLay = view.findViewById(R.id.deleteLay);
            editLay = view.findViewById(R.id.editLay);
            addressTitle = view.findViewById(R.id.addressTitle);
        }
    }
}