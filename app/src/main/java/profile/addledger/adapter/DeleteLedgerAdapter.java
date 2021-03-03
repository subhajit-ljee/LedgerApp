package profile.addledger.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.sourav.ledgerproject.R;

import profile.addledger.model.Ledger;

public class DeleteLedgerAdapter extends FirestoreRecyclerAdapter<Ledger,DeleteLedgerAdapter.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public DeleteLedgerAdapter(@NonNull FirestoreRecyclerOptions<Ledger> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Ledger model) {
        holder.ledger_name.setText(model.getAccount_name());
        holder.ledger_id.setText(model.getId());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.delete_ledger_list_recycler,parent,false);
        return new ViewHolder(v);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView ledger_name, ledger_id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ledger_name = itemView.findViewById(R.id.del_ledger_name);
            ledger_id = itemView.findViewById(R.id.del_ledger_client_id);
        }
    }
}
