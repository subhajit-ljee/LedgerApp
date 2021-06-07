package profile.addledger.editLedger.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textview.MaterialTextView;
import com.sourav.ledgerproject.R;

import profile.addledger.model.Ledger;

public class EditLedgerAdapter extends FirestoreRecyclerAdapter<Ledger, EditLedgerAdapter.ViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private static final String TAG = "EditLedgerAdapter";
    private final Context context;
    private String messagingtoken;
    private static boolean SHOW_MENU = true;

    public EditLedgerAdapter(@NonNull FirestoreRecyclerOptions<Ledger> options, Context context, String token) {
        super(options);
        this.context = context;
        messagingtoken = token;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Ledger model) {

        holder.clientname.setText(model.getAccount_name());
        holder.datetime.setText(model.getTimestamp());
        holder.opening_balance.setText(model.getOpening_balance());
        holder.client_type.setText(model.getAccount_type());
        holder.ledger_number.setText(model.getLedger_number());

        holder.main_ledger_area.setOnClickListener( v -> {

            if(SHOW_MENU){
                holder.tools_area_for_edit_ledger.setVisibility(View.VISIBLE);
                SHOW_MENU = false;
            }
            else if(!SHOW_MENU) {
                holder.tools_area_for_edit_ledger.setVisibility(View.GONE);
                SHOW_MENU = true;
            }
        });

        holder.ledger_info_image_view.setOnClickListener(v -> {

            Log.d(TAG, "onBindViewHolder: ledger name: " + model.getAccount_name());
            Log.d(TAG, "onBindViewHolder: messaging_token: " + messagingtoken);
            Bundle bundle = new Bundle();
            bundle.putString("ledgerid",model.getId());
            bundle.putString("clientid", model.getClient_id());
            bundle.putString("ledgername",model.getAccount_name());
            bundle.putString("opening_balance",model.getOpening_balance());
            bundle.putString("account_type",model.getAccount_type());
            bundle.putString("messaging_token",messagingtoken);
            bundle.putString("ledgername",model.getAccount_name());

            NavController findNAvController = Navigation.findNavController( (Activity) context, R.id.create_client_fragment );
            findNAvController.navigate(R.id.action_editLedgerFragment_to_ledgerFieldsFragment,bundle);

        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ledger_list_item_for_edit, parent, false);
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView opening_balance, datetime, clientname, client_type, ledger_number;
        ImageView ledger_info_image_view;
        ConstraintLayout main_ledger_area, tools_area_for_edit_ledger;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            clientname = itemView.findViewById(R.id.client_name_v);
            datetime = itemView.findViewById(R.id.ledger_date_and_time_v);
            opening_balance = itemView.findViewById(R.id.opening_balance_amount_v);
            client_type = itemView.findViewById(R.id.client_type_v);
            ledger_number = itemView.findViewById(R.id.ledger_number);
            ledger_info_image_view = itemView.findViewById(R.id.ledger_info_image_view);

            main_ledger_area = itemView.findViewById(R.id.main_area_for_ledger_details);
            tools_area_for_edit_ledger = itemView.findViewById(R.id.tools_area_for_edit_ledger);
        }
    }
}
