package com.sourav.ledgerproject.profile.addclient.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sourav.ledgerproject.R;
import com.sourav.ledgerproject.profile.model.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ViewHolder> {

    List<Client> clients = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Client client = clients.get(position);
        holder.client_name.setText(client.getClient_name());
        holder.client_email.setText(client.getClient_email());
        holder.client_id.setText(client.getClient_id());
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    public void setClient(List<Client> clients){
        this.clients = clients;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView client_name,client_email,client_id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            client_name = itemView.findViewById(R.id.client_name);
            client_email = itemView.findViewById(R.id.client_email);
            client_id = itemView.findViewById(R.id.client_id);
        }
    }
}
