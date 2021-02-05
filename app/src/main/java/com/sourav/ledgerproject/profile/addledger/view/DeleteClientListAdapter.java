package com.sourav.ledgerproject.profile.addledger.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sourav.ledgerproject.R;
import com.sourav.ledgerproject.profile.addledger.model.DeleteClientModel;

import java.util.List;

public class DeleteClientListAdapter extends RecyclerView.Adapter<DeleteClientListAdapter.ViewHolder> {

    private final String TAG = getClass().getCanonicalName();
    List<DeleteClientModel> vouchers;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.delete_voucher_list_recycler,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DeleteClientModel delvouchers = vouchers.get(position);

        holder.vouchername.setText(delvouchers.getClient_name());
        holder.voucherclientid.setText(delvouchers.getClient_id());
    }

    public void setVouchers(List<DeleteClientModel> vouchers){
        this.vouchers = vouchers;
        Log.d(TAG,"voucher is: "+vouchers);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return vouchers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView vouchername,voucherclientid;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vouchername = itemView.findViewById(R.id.vouchername);
            voucherclientid = itemView.findViewById(R.id.voucher_client_id);
        }
    }
}
