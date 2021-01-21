package com.sourav.ledgerproject.profile.addledger.view;

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

    @NonNull
    @Override
    public ShowVoucherLedgerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ledger_list_recycler,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowVoucherLedgerAdapter.ViewHolder holder, int position) {

        VoucherLedger voucherLedger = voucherList.get(position);
        holder.creation_time.setText(voucherLedger.getCreation_time());
        holder.voucher_mode.setText(voucherLedger.getVoucher_mode());
        holder.voucher_amount.setText(voucherLedger.getAmount());
    }

    @Override
    public int getItemCount() {
        return voucherList.size();
    }

    public void setList(List<VoucherLedger> voucherList){
        this.voucherList = voucherList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView creation_time,voucher_mode,voucher_amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            creation_time = itemView.findViewById(R.id.ledger_date_and_time);
            voucher_mode = itemView.findViewById(R.id.voucher_mode);
            voucher_amount = itemView.findViewById(R.id.ledger_client_amount);
        }
    }
}
