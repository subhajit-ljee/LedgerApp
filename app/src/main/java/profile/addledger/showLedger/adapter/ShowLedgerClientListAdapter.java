package profile.addledger.showLedger.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.sourav.ledgerproject.R;

import profile.addclient.model.Client;

public class ShowLedgerClientListAdapter extends FirestoreRecyclerAdapter<Client, ShowLedgerClientListAdapter.ViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private View view;

    public ShowLedgerClientListAdapter(@NonNull FirestoreRecyclerOptions<Client> options, View view) {
        super(options);
        this.view = view;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Client model) {
        holder.rec_client_name.setText(model.getClient_name());
        holder.rec_client_email.setText(model.getClient_email());

        holder.see_ledgers.setOnClickListener( v -> {
            Bundle bundle = new Bundle();
            bundle.putString("cid",model.getId());
            Navigation.findNavController(view).navigate(R.id.action_showLedgerFragment_to_showLedgerListForPrintFragment, bundle);
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_clients_for_ledgers, parent, false);
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView rec_client_name, rec_client_email;
        MaterialButton see_ledgers;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rec_client_name = itemView.findViewById(R.id.rec_client_name);
            rec_client_email = itemView.findViewById(R.id.rec_client_email);

            see_ledgers = itemView.findViewById(R.id.see_ledgers);
        }
    }
}
