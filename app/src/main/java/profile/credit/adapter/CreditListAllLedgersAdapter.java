package profile.credit.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.sourav.ledgerproject.R;

import profile.addledger.model.Ledger;
import profile.addvoucher.ShowVoucherActivity;


public class CreditListAllLedgersAdapter extends FirestoreRecyclerAdapter<Ledger, CreditListAllLedgersAdapter.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private static final String TAG = "CreditListAllLedgersAdapter";
    private Context context;
    public CreditListAllLedgersAdapter(@NonNull FirestoreRecyclerOptions<Ledger> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Ledger model) {

        if (model.getAccount_type().equals("Credit")) {
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

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ledger_list_for_debit_client,parent,false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView ledger_client_name, ledger_client_account_type, ledger_client_client_date;
        ImageButton go_for_voucher_list;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ledger_client_name = itemView.findViewById(R.id.ledger_client_name);
            ledger_client_account_type = itemView.findViewById(R.id.ledger_client_account_type);
            ledger_client_client_date = itemView.findViewById(R.id.ledger_client_creation_date);

            go_for_voucher_list = itemView.findViewById(R.id.go_for_voucher_list);
        }
    }
}
