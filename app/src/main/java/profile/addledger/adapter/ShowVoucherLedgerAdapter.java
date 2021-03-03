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
//import com.sourav.ledgerproject.profile.ledger.model.VoucherLedger;
//import com.sourav.ledgerproject.profile.ledger.voucher.model.Voucher;

import java.util.ArrayList;
import java.util.List;

public class ShowVoucherLedgerAdapter/* extends FirestoreRecyclerAdapter<Voucher,ShowVoucherLedgerAdapter.ViewHolder> */{

    //private List<VoucherLedger> voucherList = new ArrayList<>();
    //private final String TAG = getClass().getCanonicalName();

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
   /* public ShowVoucherLedgerAdapter(@NonNull FirestoreRecyclerOptions<Voucher> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Voucher model) {
        holder.creation_time.setText(model.getTimestamp());
        holder.voucher_id.setText(model.getClient_id());
        holder.voucher_mode.setText(model.getType());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_list_item_for_voucher,parent,false);
        return new ViewHolder(view);
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        TextView creation_time,voucher_mode,voucher_amount,voucher_id;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            creation_time = itemView.findViewById(R.id.ledger_date_and_time);
            voucher_mode = itemView.findViewById(R.id.voucher_mode);
            voucher_amount = itemView.findViewById(R.id.ledger_client_amount);
        }

    }
    */
}
