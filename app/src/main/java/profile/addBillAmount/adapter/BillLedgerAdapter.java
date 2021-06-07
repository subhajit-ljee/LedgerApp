package profile.addBillAmount.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.sourav.ledgerproject.R;

import profile.addledger.model.Ledger;

public class BillLedgerAdapter extends FirestoreRecyclerAdapter<Ledger, BillLedgerAdapter.ViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private Context context;

    public BillLedgerAdapter(@NonNull FirestoreRecyclerOptions<Ledger> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Ledger model) {

        holder.my_ledger_client_name.setText(model.getAccount_name());
        holder.my_ledger_client_date.setText(model.getTimestamp());
        holder.my_ledger_client_opening_balance.setText(model.getOpening_balance());
        holder.my_ledger_client_type.setText(model.getAccount_type());

        holder.enter_bill_amount_button.setOnClickListener( v -> {
            Bundle bundle = new Bundle();
            bundle.putString("userid",model.getUser_id());
            bundle.putString("clientid",model.getClient_id());
            bundle.putString("ledgerid",model.getId());

            NavController findNavController = Navigation.findNavController( (Activity) context, R.id.bill_main_fragment);
            findNavController.navigate(R.id.action_billChooseLedgerFragment_to_enterBillFragment, bundle);
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_ledger_list_item, parent, false);
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView my_ledger_client_name, my_ledger_client_date, my_ledger_client_opening_balance,
                my_ledger_client_type, ledger_number_text;
        Button enter_bill_amount_button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            my_ledger_client_name = itemView.findViewById(R.id.my_ledger_client_name);
            my_ledger_client_date = itemView.findViewById(R.id.my_ledger_client_date);
            my_ledger_client_opening_balance = itemView.findViewById(R.id.my_ledger_client_opening_balance);
            my_ledger_client_type = itemView.findViewById(R.id.my_ledger_client_type);
            ledger_number_text = itemView.findViewById(R.id.ledger_number_text);

            enter_bill_amount_button = itemView.findViewById(R.id.enter_bill_amount_button);
        }
    }
}
