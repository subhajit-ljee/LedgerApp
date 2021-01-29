package com.sourav.ledgerproject.profile.debit.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sourav.ledgerproject.R;
import com.sourav.ledgerproject.profile.model.AccountHolder;
import com.sourav.ledgerproject.profile.model.Client;

import java.util.List;

public class DebitListAdapter extends RecyclerView.Adapter<DebitListAdapter.ViewHolder> {

    List<AccountHolder> clientlist;

    @NonNull
    @Override
    public DebitListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_debit_list_recycler,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DebitListAdapter.ViewHolder holder, int position) {
        AccountHolder accountHolder = clientlist.get(position);
        holder.client_name.setText(accountHolder.getName());
        holder.client_address.setText(accountHolder.getAddress());
        holder.client_id.setText(accountHolder.getClient_id());
    }

    @Override
    public int getItemCount() {
        return clientlist.size();
    }

    public void setClientlist(List<AccountHolder> clientlist){
        this.clientlist = clientlist;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView client_name;
        TextView client_address;
        TextView client_id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            client_name = itemView.findViewById(R.id.client_list_item_name);
            client_address = itemView.findViewById(R.id.client_list_item_address);
            client_id = itemView.findViewById(R.id.client_list_item_id);
        }


    }
}
