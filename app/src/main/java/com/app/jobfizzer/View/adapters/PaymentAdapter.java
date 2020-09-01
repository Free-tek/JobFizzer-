package com.app.jobfizzer.View.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.jobfizzer.Model.PaymentType;
import com.app.jobfizzer.Utilities.Prefhandler.AppSettings;
import com.bumptech.glide.Glide;
import com.app.jobfizzer.R;

import java.util.List;

/**
 * Created by user on 23-10-2017.
 */

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.MyViewHolder> {
    private Context context;
    private List<PaymentType> addresses;
    private String TAG = PaymentAdapter.class.getSimpleName();
    RelativeLayout cardLayout;

    public PaymentAdapter(Context context, List<PaymentType> addresses, RelativeLayout cardLayout) {
        this.context = context;
        this.addresses = addresses;
        this.cardLayout = cardLayout;
    }

    @Override
    public PaymentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_items, parent, false);

        return new PaymentAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final PaymentAdapter.MyViewHolder holder, final int position) {


        PaymentType paymentType = addresses.get(position);
        final AppSettings appSettings = new AppSettings(context);

        holder.paymentName.setText(paymentType.getPaymentName());

        if (paymentType.getIsSelected().equalsIgnoreCase("true")) {
            appSettings.setPaymentType(paymentType.getPaymentName());
            holder.selected_border.setVisibility(View.VISIBLE);
            holder.selected_round.setVisibility(View.VISIBLE);

            if (paymentType.getPaymentName().equalsIgnoreCase("card")) {
                cardLayout.setVisibility(View.INVISIBLE);
                appSettings.setCardSelected(true);
                Glide.with(context).load(context.getResources().getDrawable(R.drawable.blue_card)).into(holder.paymentIcon);
            } else {
                cardLayout.setVisibility(View.INVISIBLE);
                appSettings.setCardSelected(false);
                Glide.with(context).load(context.getResources().getDrawable(R.drawable.blue_cash)).into(holder.paymentIcon);
            }

            if (paymentType.getPaymentName().equalsIgnoreCase("cash")) {
                cardLayout.setVisibility(View.INVISIBLE);
                appSettings.setCardSelected(false);
                Glide.with(context).load(context.getResources().getDrawable(R.drawable.blue_cash)).into(holder.paymentIcon);
            }

        } else {
            holder.selected_border.setVisibility(View.GONE);
            holder.selected_round.setVisibility(View.GONE);


            if (paymentType.getPaymentName().equalsIgnoreCase("card")) {
                Glide.with(context).load(context.getResources().getDrawable(R.drawable.grey_card)).into(holder.paymentIcon);
            } else {
                Glide.with(context).load(context.getResources().getDrawable(R.drawable.grey_cash)).into(holder.paymentIcon);
            }

            if (paymentType.getPaymentName().equalsIgnoreCase("cash")) {
                Glide.with(context).load(context.getResources().getDrawable(R.drawable.grey_cash)).into(holder.paymentIcon);
            }
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < addresses.size(); i++) {
                    addresses.get(i).setIsSelected("false");
                }
                addresses.get(position).setIsSelected("true");
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView paymentName;
        ImageView selected_border, paymentIcon;
        FrameLayout selected_round;


        MyViewHolder(View view) {
            super(view);
            paymentName = view.findViewById(R.id.paymentName);
            selected_border = view.findViewById(R.id.selected_border);
            paymentIcon = view.findViewById(R.id.paymentIcon);
            selected_round = (FrameLayout) view.findViewById(R.id.selected_round);
        }
    }
}