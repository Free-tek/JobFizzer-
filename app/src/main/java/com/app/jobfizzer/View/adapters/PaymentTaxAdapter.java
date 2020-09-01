package com.app.jobfizzer.View.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.jobfizzer.Model.AppSettingsResponseModel.Alltax;

import com.app.jobfizzer.R;

import java.util.List;

public class PaymentTaxAdapter extends RecyclerView.Adapter<PaymentTaxAdapter.MyViewHolder> {

    private Context context;
    private List<Alltax> j_Tax;
    private String TAG = PaymentTaxAdapter.class.getSimpleName();

    public PaymentTaxAdapter(Context context, List<Alltax> j_Tax) {
        this.context = context;
        this.j_Tax = j_Tax;
    }

    @Override
    public PaymentTaxAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_tax_items, parent, false);

        return new PaymentTaxAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PaymentTaxAdapter.MyViewHolder holder, final int position) {


        Alltax alltax=j_Tax.get(position);

        holder.txt_taxName.setText(alltax.getTaxname());
        holder.txt_taxPercentage.setText(alltax.getTaxAmount()+ "%");
        holder.txt_bookingGst.setText(context.getString(R.string.currency_symbol) + alltax.getTaxTotalamount());
    }

    @Override
    public int getItemCount() {
        return j_Tax.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_taxName, txt_taxPercentage, txt_bookingGst;

        MyViewHolder(View view) {
            super(view);
            txt_taxName = view.findViewById(R.id.taxName);
            txt_taxPercentage = view.findViewById(R.id.taxPercentage);
            txt_bookingGst = view.findViewById(R.id.bookingGst);

        }
    }
}