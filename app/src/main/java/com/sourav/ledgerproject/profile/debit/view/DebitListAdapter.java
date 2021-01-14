package com.sourav.ledgerproject.profile.debit.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sourav.ledgerproject.R;
import com.sourav.ledgerproject.profile.model.Client;

import java.util.List;

public class DebitListAdapter extends RecyclerView.Adapter<DebitListAdapter.ViewHolder> {

    List<Client> clientlist;

    @NonNull
    @Override
    public DebitListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_debit_list_recycler,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DebitListAdapter.ViewHolder holder, int position) {
        Client client = clientlist.get(position);
        holder.client_name.setText(client.getClient_name());
        holder.client_email.setText(client.getClient_email());
        holder.client_id.setText(client.getClient_id());
    }

    @Override
    public int getItemCount() {
        return clientlist.size();
    }

    public void setClientlist(List<Client> clientlist){
        this.clientlist = clientlist;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView client_name;
        TextView client_email;
        TextView client_id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            client_name = itemView.findViewById(R.id.client_list_item_name);
            client_email = itemView.findViewById(R.id.client_list_item_email);
            client_id = itemView.findViewById(R.id.client_list_item_id);
        }


    }
}
