package com.sourav.ledgerproject.profile.addledger.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sourav.ledgerproject.R;
import com.sourav.ledgerproject.profile.addledger.model.VoucherLedger;

import java.util.ArrayList;
import java.util.List;

public class ShowVoucherLedgerAdapter extends RecyclerView.Adapter<ShowVoucherLedgerAdapter.ViewHolder> {

    private List<VoucherLedger> voucherList = new ArrayList<>();
    private final String TAG = getClass().getCanonicalName();
    OnItemListener onItemListener;

    public ShowVoucherLedgerAdapter(OnItemListener onItemListener){
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public ShowVoucherLedgerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ledger_list_recycler,parent,false);
        return new ViewHolder(view,onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowVoucherLedgerAdapter.ViewHolder holder, int position) {

        VoucherLedger voucherLedger = voucherList.get(position);
        holder.creation_time.setText(voucherLedger.getCreation_time());
        holder.voucher_mode.setText(voucherLedger.getVoucher_mode());
        holder.voucher_amount.setText(voucherLedger.getAmount()+".00");

    }

    @Override
    public int getItemCount() {
        return voucherList.size();
    }

    public void setList(List<VoucherLedger> data){
        voucherList.clear();
        voucherList.addAll(data);
        notifyDataSetChanged();
    }

    public List<VoucherLedger> getList(){
        return voucherList;
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

        TextView creation_time,voucher_mode,voucher_amount;
        OnItemListener onItemListener;
        public ViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            this.onItemListener = onItemListener;
            creation_time = itemView.findViewById(R.id.ledger_date_and_time);
            voucher_mode = itemView.findViewById(R.id.voucher_mode);
            voucher_amount = itemView.findViewById(R.id.ledger_client_amount);
        }

        @Override
        public boolean onLongClick(View v){
            onItemListener.onItemClick(getAdapterPosition());
            return true;
        }
    }

    public interface OnItemListener{
        void onItemClick(int position);
    }
}
