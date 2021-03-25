package profile.deletevoucher.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.sourav.ledgerproject.R;

import profile.addledger.model.Ledger;
import profile.deletevoucher.activities.VoucherForDeletingActivity;

public class ListOfLedgerForDeletingAdapter extends FirestoreRecyclerAdapter<Ledger, ListOfLedgerForDeletingAdapter.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private Context context;

    public ListOfLedgerForDeletingAdapter(@NonNull FirestoreRecyclerOptions<Ledger> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Ledger model) {
        holder.client_name_v.setText(model.getAccount_name());
        holder.opening_balance_amount_v.setText(model.getOpening_balance());
        holder.ledger_date_and_time_v.setText(model.getTimestamp());

        holder.go_to_see_voucher_list_for_delete.setOnClickListener( v -> {
            Intent intent = new Intent(context, VoucherForDeletingActivity.class);
            intent.putExtra("client_id", model.getClient_id());
            intent.putExtra("ledger_id", model.getId());
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ledger_list_item_for_voucher,parent,false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView client_name_v, opening_balance_amount_v, ledger_date_and_time_v;
        ImageButton go_to_see_voucher_list_for_delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            client_name_v = itemView.findViewById(R.id.client_name_v);
            opening_balance_amount_v = itemView.findViewById(R.id.opening_balance_amount_v);
            ledger_date_and_time_v = itemView.findViewById(R.id.ledger_date_and_time_v);
            go_to_see_voucher_list_for_delete = itemView.findViewById(R.id.go_to_see_voucher_list);
        }
    }
}
