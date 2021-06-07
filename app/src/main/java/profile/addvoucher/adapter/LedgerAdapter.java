package profile.addvoucher.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.sourav.ledgerproject.R;

import java.util.UUID;

import profile.addledger.model.Ledger;
import profile.addvoucher.CreateVoucherActivity;

public class LedgerAdapter extends FirestoreRecyclerAdapter<Ledger,LedgerAdapter.ViewHolder> {

    private static final String TAG = "LedgerAdapter";

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private final Context context;

    private static boolean SHOW_MENU = true;

    public LedgerAdapter(@NonNull FirestoreRecyclerOptions<Ledger> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Ledger model) {

        String authid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        holder.clientname.setText(model.getAccount_name());
        holder.datetime.setText(model.getTimestamp());
        holder.opening_balance.setText(model.getOpening_balance());
        holder.client_type.setText(model.getAccount_type());
        holder.ledger_number.setText(model.getLedger_number());

        holder.main_ledger_area.setOnClickListener( v -> {

            if(SHOW_MENU){
                holder.tools_for_voucher_area.setVisibility(View.VISIBLE);
                SHOW_MENU = false;
            }
            else if(!SHOW_MENU) {
                holder.tools_for_voucher_area.setVisibility(View.GONE);
                SHOW_MENU = true;
            }
        });

        holder.go_to_see_voucher_list.setOnClickListener(v -> {

            Log.d(TAG, "onBindViewHolder: ledger name: " + model.getAccount_name());

            Bundle bundle = new Bundle();
            bundle.putString("ledgerid",model.getId());
            bundle.putString("clientid", model.getClient_id());
            bundle.putString("ledgername",model.getAccount_name());
            bundle.putString("opening_balance",model.getOpening_balance());
            bundle.putString("account_type",model.getAccount_type());
            bundle.putString("ledgerholder","ledgerholder");

            NavController findNAvController = Navigation.findNavController( (Activity) context, R.id.make_voucher_main_frag );
            findNAvController.navigate(R.id.action_ledgerHolderFragment_to_voucherListFragment,bundle);

        });

        holder.make_voucher_image_view.setOnClickListener( v -> {
            Intent intent = new Intent(context, CreateVoucherActivity.class);
            intent.putExtra("ledgerid", model.getId());
            intent.putExtra("vid", UUID.randomUUID().toString());

            intent.putExtra("clientid",model.getClient_id());
            intent.putExtra("ledgername",model.getAccount_name());
            intent.putExtra("opening_balance",model.getOpening_balance());
            intent.putExtra("account_type",model.getAccount_type());
            intent.putExtra("notifyfrom",authid);
            context.startActivity(intent);

        });

        holder.delete_voucher.setOnClickListener( v -> {
            Bundle bundle = new Bundle();
            bundle.putString("ledgerid",model.getId());
            bundle.putString("clientid", model.getClient_id());
            NavController findNAvController = Navigation.findNavController( (Activity) context, R.id.make_voucher_main_frag );
            findNAvController.navigate(R.id.action_ledgerHolderFragment_to_voucherListForDeleteFragment, bundle);
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ledger_list_item_for_voucher, parent, false);
        return new ViewHolder(v);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView opening_balance, datetime, clientname, client_type, ledger_number;
        ImageView go_to_see_voucher_list, make_voucher_image_view, delete_voucher;
        ConstraintLayout main_ledger_area, tools_for_voucher_area;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            clientname = itemView.findViewById(R.id.client_name_v);
            datetime = itemView.findViewById(R.id.ledger_date_and_time_v);
            opening_balance = itemView.findViewById(R.id.opening_balance_amount_v);
            client_type = itemView.findViewById(R.id.client_type_v);
            ledger_number = itemView.findViewById(R.id.ledger_number);
            make_voucher_image_view = itemView.findViewById(R.id.make_voucher_image_view);
            go_to_see_voucher_list = itemView.findViewById(R.id.go_to_see_voucher_list);
            delete_voucher = itemView.findViewById(R.id.delete_voucher);

            main_ledger_area = itemView.findViewById(R.id.main_area_for_ledger_details);
            tools_for_voucher_area = itemView.findViewById(R.id.tools_area_for_voucher);
        }
    }
}
