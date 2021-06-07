package profile.credit.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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


public class CreditListAllLedgersAdapter extends FirestoreRecyclerAdapter<Ledger, CreditListAllLedgersAdapter.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private static final String TAG = "CreditListAllLedgersAdapter";
    private Context context;
    private static boolean SHOW_MENU = true;
    public CreditListAllLedgersAdapter(@NonNull FirestoreRecyclerOptions<Ledger> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Ledger model) {

        if (model.getAccount_type().equals("Creditor")) {
            holder.ledger_client_name.setText(model.getAccount_name());
            holder.ledger_client_account_type.setText(model.getAccount_type());
            holder.ledger_client_client_date.setText(model.getTimestamp());
            holder.ledger_client_opening_balance.setText(model.getOpening_balance());
            holder.ledger_number.setText(model.getLedger_number());

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
                findNavController.navigate(R.id.action_creditListAllLedgersFragment_to_voucherListFragment2, bundle);

            });

        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ledger_list_for_credit_client,parent,false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView ledger_client_name, ledger_client_account_type, ledger_client_client_date,
                ledger_client_opening_balance, ledger_number;
        ImageView go_for_voucher_list;
        ConstraintLayout second_view_content;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ledger_client_name = itemView.findViewById(R.id.ledger_client_name);
            ledger_client_account_type = itemView.findViewById(R.id.ledger_client_account_type);
            ledger_client_client_date = itemView.findViewById(R.id.ledger_client_creation_date);
            ledger_client_opening_balance = itemView.findViewById(R.id.ledger_client_opening_balance);
            ledger_number = itemView.findViewById(R.id.ledger_number);

            second_view_content = itemView.findViewById(R.id.second_view_content);
            go_for_voucher_list = itemView.findViewById(R.id.go_for_voucher_list);
        }
    }
}
