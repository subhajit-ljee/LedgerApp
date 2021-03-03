package profile.addvoucher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.sourav.ledgerproject.R;

import org.w3c.dom.Text;

import profile.addvoucher.model.Voucher;

public class VoucherListAdapter extends FirestoreRecyclerAdapter<Voucher, VoucherListAdapter.ViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private static final String TAG = "VoucherListAdapter";
    private Context context;
    private String activityname;
    public VoucherListAdapter(@NonNull FirestoreRecyclerOptions<Voucher> options, Context context, String activityname) {
        super(options);
        this.context = context;
        this.activityname = activityname;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Voucher model) {
        holder.voucher_client_amount.setText(model.getAmount());
        holder.voucher_mode.setText(model.getType());
        holder.voucher_date_and_time.setText(model.getTimestamp());

        holder.itemView.setOnLongClickListener( v -> {

            return true;
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ledger_list_recycler, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView voucher_date_and_time, voucher_mode, voucher_client_amount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            voucher_client_amount = itemView.findViewById(R.id.voucher_client_amount);
            voucher_mode = itemView.findViewById(R.id.voucher_mode);
            voucher_date_and_time = itemView.findViewById(R.id.voucher_date_and_time);
        }
    }


}
