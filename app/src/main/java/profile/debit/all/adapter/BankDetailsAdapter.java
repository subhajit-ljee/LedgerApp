package profile.debit.all.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.sourav.ledgerproject.R;

import profile.addledger.model.BankDetails;
import profile.addledger.model.Ledger;

public class BankDetailsAdapter extends FirestoreRecyclerAdapter<Ledger, BankDetailsAdapter.ViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public BankDetailsAdapter(@NonNull FirestoreRecyclerOptions<Ledger> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Ledger model) {
        holder.bank_name.setText(model.getBankDetails().getBank_name());
        holder.bank_account_number.setText(model.getBankDetails().getAccount_number());
        holder.pan.setText(model.getBankDetails().getPan_or_it_no());
        holder.branch_name.setText(model.getBankDetails().getBranch_name());
        holder.bank_ifsc.setText(model.getBankDetails().getBank_ifsc());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_bank_details_list_recycler, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView bank_name, bank_account_number, bank_ifsc, branch_name, pan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bank_name = itemView.findViewById(R.id.bank_name_id);
            bank_account_number = itemView.findViewById(R.id.bank_account_number);
            bank_ifsc = itemView.findViewById(R.id.bank_ifsc_number);
            branch_name = itemView.findViewById(R.id.bank_branch_name);
            pan = itemView.findViewById(R.id.pan);
        }
    }
}
