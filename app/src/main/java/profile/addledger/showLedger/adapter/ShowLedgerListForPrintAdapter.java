package profile.addledger.showLedger.adapter;

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

import profile.addledger.model.Ledger;

public class ShowLedgerListForPrintAdapter extends FirestoreRecyclerAdapter<Ledger, ShowLedgerListForPrintAdapter.ViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private View view;

    public ShowLedgerListForPrintAdapter(@NonNull FirestoreRecyclerOptions<Ledger> options, View view) {
        super(options);
        this.view = view;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Ledger model) {
        holder.my_ledger_client_name.setText(model.getAccount_name());
        holder.my_ledger_client_opening_balance.setText(model.getOpening_balance());
        holder.my_ledger_client_type.setText(model.getAccount_type());
        holder.my_ledger_client_date.setText(model.getTimestamp());

        Bundle bundle = new Bundle();
        bundle.putString("ledger_id",model.getId());
        bundle.putString("client_id",model.getClient_id());
        bundle.putString("print","print");
        holder.my_ledger_details_button.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_showLedgerListForPrintFragment_to_myLedgerFragment2, bundle));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ledger_list_for_print, parent, false);
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView my_ledger_client_name, my_ledger_client_date, my_ledger_client_opening_balance, my_ledger_client_type;
        MaterialButton my_ledger_details_button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            my_ledger_client_name = itemView.findViewById(R.id.my_ledger_client_name);
            my_ledger_client_date = itemView.findViewById(R.id.my_ledger_client_date);
            my_ledger_client_opening_balance = itemView.findViewById(R.id.my_ledger_client_opening_balance);
            my_ledger_client_type = itemView.findViewById(R.id.my_ledger_client_type);
            my_ledger_details_button = itemView.findViewById(R.id.my_ledger_details_button);
        }
    }
}
