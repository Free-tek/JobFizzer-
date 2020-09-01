package com.app.jobfizzer.View.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.jobfizzer.Model.List_address;
import com.app.jobfizzer.Utilities.Prefhandler.AppSettings;
import com.app.jobfizzer.View.activities.MapActivity;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.R;

import java.util.List;

/*
 * Created by user on 23-10-2017.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {
    private Activity context;
    private List<List_address> addresses;
    private String TAG = AddressAdapter.class.getSimpleName();

    public AddressAdapter(Activity context, List<List_address> addresses) {
        this.context = context;
        this.addresses = addresses;
    }

    @Override
    public AddressAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_address, parent, false);

        return new AddressAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AddressAdapter.MyViewHolder holder, final int position) {
        final List_address address = addresses.get(position);
        final AppSettings appSettings = new AppSettings(context);
        holder.address.setText(address.getAddress_line_1());
        holder.title.setText(address.getTitle());

        if (address.getSelected().equalsIgnoreCase("true")) {
            Drawable drawable1 = context.getResources().getDrawable(R.drawable.bg_selected);
            drawable1.setColorFilter(new PorterDuffColorFilter(Utils.getPrimaryCOlor(context), PorterDuff.Mode.SRC_IN));
            holder.rootLayout.setBackground(drawable1);
            holder.selectedImage.setVisibility(View.VISIBLE);
        } else {
            Drawable drawable1 = context.getResources().getDrawable(R.drawable.bg_selected);
            drawable1.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.grey2),
                    PorterDuff.Mode.SRC_IN));
            holder.rootLayout.setBackground(drawable1);
            holder.selectedImage.setVisibility(View.GONE);
        }
        if (address.getType().equalsIgnoreCase("addaddress")) {
            holder.addAddressView.setVisibility(View.VISIBLE);
            holder.address.setVisibility(View.GONE);
            holder.location.setVisibility(View.GONE);
            holder.city.setVisibility(View.GONE);
            holder.title.setVisibility(View.GONE);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MapActivity.class);
                    intent.putExtra("from", "select");
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
                }
            });
        } else {
            holder.addAddressView.setVisibility(View.GONE);
            holder.address.setVisibility(View.VISIBLE);
            holder.location.setVisibility(View.GONE);
            holder.city.setVisibility(View.GONE);
            holder.title.setVisibility(View.VISIBLE);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    appSettings.setSelectedlat(""+address.getLatitude());
                    appSettings.setSelectedLong(""+address.getLongitude());
                    appSettings.setSelectedCity(address.getCity());
                    appSettings.setAddressSelected(true);
                    appSettings.setSelectedAddressId(""+address.getId());
                    appSettings.setSelectedAddressTitle(address.getTitle());
                    appSettings.setSelectedAddress(address.getAddress_line_1());
                    if (!address.getSelected().equalsIgnoreCase("true")) {
                        for (int i = 0; i < addresses.size(); i++) {
                            if (i != position) {
                                addresses.get(i).setSelected("false");
                            } else {
                                addresses.get(i).setSelected("true");
                            }
                        }
                    }
                    notifyDataSetChanged();
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView address, location, city, title, addresss_add_tv;
        RelativeLayout rootLayout;
        RelativeLayout rootLayout2;
        ImageView selectedImage, addresss_add;
        LinearLayout addAddressView;

        MyViewHolder(View view) {
            super(view);
            addAddressView = view.findViewById(R.id.addAddressView);
            selectedImage = view.findViewById(R.id.selectedImage);
            addresss_add = view.findViewById(R.id.addresss_add);
            addresss_add_tv = view.findViewById(R.id.address_add_tv);
            address = view.findViewById(R.id.address);
            location = view.findViewById(R.id.location);
            city = view.findViewById(R.id.city);
            title = view.findViewById(R.id.title);
            rootLayout = view.findViewById(R.id.rootLayout);
            rootLayout2 = view.findViewById(R.id.rootLayout2);
        }
    }
}