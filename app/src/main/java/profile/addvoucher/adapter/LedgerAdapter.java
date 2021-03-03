package profile.addvoucher.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.sourav.ledgerproject.R;

import java.util.concurrent.atomic.AtomicInteger;

import profile.addledger.ShowLedgerActivity;
import profile.addledger.model.Ledger;
import profile.addvoucher.CreateVoucherActivity;
import profile.addvoucher.ShowVoucherActivity;

public class LedgerAdapter extends FirestoreRecyclerAdapter<Ledger,LedgerAdapter.ViewHolder> {

    private static final String TAG = "LedgerAdapter";

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private Context context;
    private String activityName;
    private boolean toolbarflag = false;
    public LedgerAdapter(@NonNull FirestoreRecyclerOptions<Ledger> options, Context context, String activityName) {
        super(options);
        this.context = context;
        this.activityName = activityName;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Ledger model) {
        holder.clientname.setText(model.getAccount_name());
        holder.datetime.setText(model.getTimestamp());
        holder.opening_balance.setText(model.getOpening_balance());

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, ShowVoucherActivity.class);
            intent.putExtra("ledgerid",model.getId());
            intent.putExtra("clientid",model.getClient_id());
            intent.putExtra("ledgername",model.getAccount_name());
            intent.putExtra("opening_balance",model.getOpening_balance());
            intent.putExtra("account_type",model.getAccount_type());
            Log.d(TAG, "onBindViewHolder: "+model.getOpening_balance());
            context.startActivity(intent);

        });

        holder.itemView.setOnLongClickListener(v -> {

            //holder.check_ledger_todo.setVisibility(View.VISIBLE);

            if(holder.check_ledger_todo.getVisibility() == View.VISIBLE) {
                holder.check_ledger_todo.setVisibility(View.INVISIBLE);
                return false;
            }
            else if(holder.check_ledger_todo.getVisibility() == View.INVISIBLE) {
                holder.check_ledger_todo.setVisibility(View.VISIBLE);
                return true;
            }
            return true;
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ledger_list_item_for_voucher, parent, false);
        return new ViewHolder(v);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView opening_balance, datetime, clientname;
        ImageView check_ledger_todo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            clientname = itemView.findViewById(R.id.client_name_v);
            datetime = itemView.findViewById(R.id.ledger_date_and_time_v);
            opening_balance = itemView.findViewById(R.id.opening_balance_amount_v);
            check_ledger_todo = itemView.findViewById(R.id.check_ledger_todo);
        }
    }
}
