package profile.deletevoucher.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.sourav.ledgerproject.R;

import profile.addledger.model.Ledger;
import profile.addvoucher.model.Voucher;
import profile.deletevoucher.service.SendNotificationForDeleteVoucherService;

public class VoucherListForDeleteAdapter extends FirestoreRecyclerAdapter<Voucher, VoucherListForDeleteAdapter.ViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private static final String TAG = "VoucherListForDeleteAdapter";
    private Context context;

    public VoucherListForDeleteAdapter(@NonNull FirestoreRecyclerOptions<Voucher> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Voucher model) {
        if(model.isAdded()) {
            holder.voucher_mode.setText(model.getType());
            holder.voucher_client_amount.setText(model.getAmount());
            holder.voucher_date_and_time.setText(model.getTimestamp());
            Log.d(TAG, "onBindViewHolder: model.isAdded(): " + model.isAdded());

            holder.itemView.setOnLongClickListener(v -> {
                new Handler().post(() -> {
                    AlertDialog deleteDialog = new AlertDialog.Builder(context)
                            .setPositiveButton("Delete Voucher? ", (dialog, which) -> {
                                Intent intent = new Intent(context, SendNotificationForDeleteVoucherService.class);
                                intent.putExtra("voucher_id", model.getId());
                                intent.putExtra("client_id", model.getClient_id());
                                intent.putExtra("ledger_id", model.getLedger_id());

                                SendNotificationForDeleteVoucherService.enqueueWork(context, intent);
                            }).setNegativeButton("Cancel", null)
                            .create();

                    deleteDialog.show();
                });
                return true;
            });
        } else {
            holder.currency_sign.setVisibility(View.INVISIBLE);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ledger_list_recycler, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView voucher_client_amount, voucher_date_and_time, voucher_mode, currency_sign;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            voucher_client_amount = itemView.findViewById(R.id.voucher_client_amount);
            voucher_date_and_time = itemView.findViewById(R.id.voucher_date_and_time);
            voucher_mode = itemView.findViewById(R.id.voucher_mode);
            currency_sign = itemView.findViewById(R.id.currency_sign);
        }
    }
}
