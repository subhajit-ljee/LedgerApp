package profile.addclient.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;


import java.util.ArrayList;
import java.util.List;

import profile.addclient.model.Client;
import profile.addledger.CreateLedgerActivity;
import profile.addledger.ShowLedgerActivity;
import profile.addvoucher.CreateVoucherActivity;

public class ClientAdapter extends FirestoreRecyclerAdapter<Client,ClientAdapter.ViewHolder> {

    private static final String TAG = "ClientAdapter";
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private Context context;
    private String activityName;
    public ClientAdapter(@NonNull FirestoreRecyclerOptions<Client> options, Context context, String activityname) {
        super(options);
        if(options == null || options.getSnapshots().size() == 0)
        Log.d(TAG, "ClientAdapter: options not working");
        this.context = context;
        this.activityName = activityname;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_list_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Client model) {
        holder.client_name.setText(model.getClient_name());
        holder.client_email.setText(model.getClient_email());
        holder.client_id.setText(model.getId());
        holder.client_list_for_ledger_image_text_id.setText(model.getClient_name().substring(0,1).toUpperCase());
        holder.go_to_view_ledger_list.setOnClickListener( v -> {

            if(holder.client_id.getText().toString().trim() != null)
                Log.d(TAG, "onLongClick: " + holder.client_id.getText().toString().trim());
            else
                Log.d(TAG,"null");

            Intent intent = null;
            if(activityName.equals("SelectAndAddClient")) {
                intent = new Intent(context, CreateLedgerActivity.class);
                intent.putExtra("clientid", model.getId());
                intent.putExtra("clientname", model.getClient_name());

            }
            context.startActivity(intent);
        });
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        TextView client_name,client_email,client_id, client_list_for_ledger_image_text_id;
        ImageButton go_to_view_ledger_list;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "ViewHolder: ");
            client_name = itemView.findViewById(R.id.client_name);
            client_email = itemView.findViewById(R.id.client_email);
            client_id = itemView.findViewById(R.id.client_id);

            go_to_view_ledger_list = itemView.findViewById(R.id.go_to_view_ledger_list_for_delete);
            client_list_for_ledger_image_text_id = itemView.findViewById(R.id.client_list_for_ledger_image_text_id);
        }
    }

}
