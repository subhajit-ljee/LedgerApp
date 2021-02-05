package com.sourav.ledgerproject.profile.addledger.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sourav.ledgerproject.R;
import com.sourav.ledgerproject.profile.addledger.model.DeleteVoucherModel;

import java.util.List;

public class DeleteVoucherListAdapter extends RecyclerView.Adapter<DeleteVoucherListAdapter.ViewHolder> {

    List<DeleteVoucherModel> voucher_model;
    OnItemListener onItemListener;

    public DeleteVoucherListAdapter(OnItemListener onItemListener){
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_voucher_list_recycler,parent,false);
        return new ViewHolder(view,onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DeleteVoucherModel model = voucher_model.get(position);
        holder.voucher_name.setText(model.getVoucher_name());
        holder.voucher_date.setText(model.getVoucher_date());
        holder.voucher_type.setText(model.getVoucher_type());
        holder.voucher_amount.setText(model.getVoucher_amount());
    }

    @Override
    public int getItemCount() {
        return voucher_model.size();
    }

    public void setVoucher_model(List<DeleteVoucherModel> voucher_model){
        this.voucher_model = voucher_model;
        notifyDataSetChanged();
    }

    public List<DeleteVoucherModel> getVoucher_model(){
        return voucher_model;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        TextView voucher_name,voucher_date,voucher_type,voucher_amount;
        OnItemListener onItemListener;
        public ViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            this.onItemListener = onItemListener;
            voucher_name = itemView.findViewById(R.id.details_alert_name);
            voucher_date = itemView.findViewById(R.id.details_alert_client_timestamp);
            voucher_type = itemView.findViewById(R.id.details_alert_client_type);
            voucher_amount = itemView.findViewById(R.id.details_alert_client_amount);
        }

        @Override
        public boolean onLongClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
            return true;
        }
    }

    public interface OnItemListener{
        void onItemClick(int position);
    }
}
