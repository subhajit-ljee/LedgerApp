package profile.debit.all.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.sourav.ledgerproject.R;

import profile.addledger.model.Ledger;

public class DebitListAllLedgersAdapter extends FirestoreRecyclerAdapter<Ledger, DebitListAllLedgersAdapter.ViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private static final String TAG = "DebitListAllLedgersAdap";
    private Context context;
    private static boolean SHOW_MENU = true;
    public DebitListAllLedgersAdapter(@NonNull FirestoreRecyclerOptions<Ledger> options, Context context) {
        super(options);
        this.context = context;
        Log.d(TAG, "DebitListAllLedgersAdapter: size: " + options.getSnapshots().size());
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Ledger model) {

        if (model.getAccount_type().equals("Debitor")) {
            holder.ledger_client_name.setText(model.getAccount_name());
            holder.ledger_client_account_type.setText(model.getAccount_type());
            holder.ledger_client_client_date.setText(model.getTimestamp());

            holder.itemView.setOnClickListener( v -> {
                if(SHOW_MENU) {
                    holder.second_view_content.setVisibility(View.VISIBLE);
                    SHOW_MENU = false;
                }
                else {
                    holder.second_view_content.setVisibility(View.GONE);
                    SHOW_MENU = true;
                }
            });

            holder.go_for_voucher_list.setOnClickListener( v -> {
                Bundle bundle = new Bundle();

                bundle.putString("clientid", model.getClient_id());
                bundle.putString("ledgerid", model.getId());

                bundle.putString("ledgername", model.getAccount_name());
                bundle.putString("opening_balance", model.getOpening_balance());
                bundle.putString("account_type", model.getAccount_type());

                NavController findNavController = Navigation.findNavController( (Activity) context, R.id.pro_main_fragment);
                findNavController.navigate(R.id.action_debitListAllLedgersFragment_to_voucherListFragment3, bundle);
            });
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ledger_list_for_debit_client,parent,false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView ledger_client_name, ledger_client_account_type, ledger_client_client_date;
        ImageButton go_for_voucher_list;
        ConstraintLayout second_view_content;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ledger_client_name = itemView.findViewById(R.id.ledger_client_name);
            ledger_client_account_type = itemView.findViewById(R.id.ledger_client_account_type);
            ledger_client_client_date = itemView.findViewById(R.id.ledger_client_creation_date);

            second_view_content = itemView.findViewById(R.id.second_view_content);
            go_for_voucher_list = itemView.findViewById(R.id.go_for_voucher_list);

        }
    }
}
