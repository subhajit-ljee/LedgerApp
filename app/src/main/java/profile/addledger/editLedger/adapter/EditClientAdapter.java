package profile.addledger.editLedger.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.sourav.ledgerproject.R;

import profile.addclient.model.Client;

public class EditClientAdapter extends FirestoreRecyclerAdapter<Client, EditClientAdapter.ViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private static final String TAG = "EditClientAdapter";
    private Context context;

    public EditClientAdapter(@NonNull FirestoreRecyclerOptions<Client> options,  Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Client model) {

        final boolean[] isOpen = {true};
        holder.main_view_content.setOnClickListener( v -> {

            if(isOpen[0]) {
                holder.second_view_content.setVisibility(View.VISIBLE);
                isOpen[0] = false;
            }
            else if (!isOpen[0]){
                holder.second_view_content.setVisibility(View.GONE);
                isOpen[0] = true;
            }

        });

        holder.client_id_v.setText(model.getId());
        holder.client_name_v.setText(model.getClient_name());
        holder.client_email_v.setText(model.getClient_email());
        holder.go_to_view_ledger_list.setOnClickListener( v -> {
            Bundle bundle = new Bundle();
            bundle.putString("client_id",model.getId());
            bundle.putString("messaging_token",model.getMesseging_token());
            Log.d(TAG, "onBindViewHolder: messaging_token: " + model.getMesseging_token());
            NavController findNavController = Navigation.findNavController( (Activity) context, R.id.create_client_fragment );
            findNavController.navigate(R.id.action_editClientListFragment_to_editLedgerFragment, bundle);
        });

        Log.d(TAG, "onBindViewHolder: "+model);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_list_item_for_voucher, parent, false);
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView client_id_v, client_name_v, client_email_v;
        ImageView go_to_view_ledger_list;
        ConstraintLayout second_view_content;
        RelativeLayout main_view_content;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            client_id_v = itemView.findViewById(R.id.client_id_v);
            client_name_v = itemView.findViewById(R.id.client_name_v);
            client_email_v = itemView.findViewById(R.id.client_email_v);

            second_view_content = itemView.findViewById(R.id.second_view_content);
            main_view_content = itemView.findViewById(R.id.main_view_content);

            go_to_view_ledger_list = itemView.findViewById(R.id.go_to_view_ledger_list_for_delete);
        }
    }
}
