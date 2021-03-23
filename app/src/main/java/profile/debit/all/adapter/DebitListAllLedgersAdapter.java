package profile.debit.all.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.gson.internal.$Gson$Preconditions;
import com.sourav.ledgerproject.R;

import profile.addclient.model.Client;
import profile.addledger.model.Ledger;
import profile.addvoucher.ShowVoucherActivity;
import profile.debit.all.BankDetailsActivity;

public class DebitListAllLedgersAdapter extends FirestoreRecyclerAdapter<Ledger, DebitListAllLedgersAdapter.ViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private static final String TAG = "DebitListAllLedgersAdap";
    private Context context;
    public DebitListAllLedgersAdapter(@NonNull FirestoreRecyclerOptions<Ledger> options, Context context) {
        super(options);
        this.context = context;
        Log.d(TAG, "DebitListAllLedgersAdapter: size: " + options.getSnapshots().size());
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Ledger model) {

        if (model.getAccount_type().equals("Debit")) {
            holder.ledger_client_name.setText(model.getAccount_name());
            holder.ledger_client_account_type.setText(model.getAccount_type());
            holder.ledger_client_client_date.setText(model.getTimestamp());

            holder.go_for_voucher_list.setOnClickListener( v -> {
                Intent intent = new Intent(context, ShowVoucherActivity.class);

                intent.putExtra("clientid", model.getClient_id());
                intent.putExtra("ledgerid", model.getId());

                intent.putExtra("ledgername", model.getAccount_name());
                intent.putExtra("opening_balance", model.getOpening_balance());
                intent.putExtra("account_type", model.getAccount_type());
                context.startActivity(intent);
            });
        }



        holder.see_bank_details.setOnClickListener(v -> {

            Intent intent = new Intent(context, BankDetailsActivity.class);
            intent.putExtra("client_id", model.getClient_id());
            intent.putExtra("voucher_id", model.getId());
            context.startActivity(intent);

        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ledger_list_for_debit_client,parent,false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView ledger_client_name, ledger_client_account_type, ledger_client_client_date;
        Button see_bank_details;
        ImageButton go_for_voucher_list;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ledger_client_name = itemView.findViewById(R.id.ledger_client_name);
            ledger_client_account_type = itemView.findViewById(R.id.ledger_client_account_type);
            ledger_client_client_date = itemView.findViewById(R.id.ledger_client_creation_date);

            see_bank_details = itemView.findViewById(R.id.see_ledgers_details);
            go_for_voucher_list = itemView.findViewById(R.id.go_for_voucher_list);

        }
    }
}
