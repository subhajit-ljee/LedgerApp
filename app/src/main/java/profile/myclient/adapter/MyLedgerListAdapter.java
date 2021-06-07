package profile.myclient.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.sourav.ledgerproject.R;

import java.util.Objects;

import profile.addledger.model.Ledger;

public class MyLedgerListAdapter extends FirestoreRecyclerAdapter<Ledger, MyLedgerListAdapter.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private static final String TAG = "MyLedgerListAdapter";
    private final View view;
    public MyLedgerListAdapter(@NonNull FirestoreRecyclerOptions<Ledger> options, View view) {
        super(options);
        this.view = view;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyLedgerListAdapter.ViewHolder holder, int position, @NonNull Ledger model) {
        holder.my_ledger_client_name.setText(model.getAccount_name());
        holder.my_ledger_client_date.setText(model.getTimestamp());
        holder.my_ledger_client_opening_balance.setText(model.getOpening_balance());
        holder.my_ledger_client_type.setText(model.getAccount_type());
        holder.ledger_number.setText(model.getLedger_number());

        holder.my_ledger_details_button.setOnClickListener( v -> {
            Bundle bundle = new Bundle();
            bundle.putString("ledger_id",model.getId());
            bundle.putString("client_id",model.getUser_id());
            NavController finNavController;

            finNavController = Navigation.findNavController(view);
            finNavController.navigate(R.id.action_myLedgerListFragment_to_myLedgerFragment, bundle);


        });
    }

    @NonNull
    @Override
    public MyLedgerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_ledger_list_item, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView my_ledger_client_name, my_ledger_client_date, my_ledger_client_opening_balance, my_ledger_client_type, ledger_number;
        Button my_ledger_details_button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            my_ledger_client_name = itemView.findViewById(R.id.my_ledger_client_name);
            my_ledger_client_date = itemView.findViewById(R.id.my_ledger_client_date);
            my_ledger_client_opening_balance = itemView.findViewById(R.id.my_ledger_client_opening_balance);
            my_ledger_client_type = itemView.findViewById(R.id.my_ledger_client_type);
            ledger_number = itemView.findViewById(R.id.ledger_number);

            my_ledger_details_button = itemView.findViewById(R.id.my_ledger_details_button);
        }
    }
}
