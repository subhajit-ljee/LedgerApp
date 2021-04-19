package profile.addvoucher.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.sourav.ledgerproject.R;
import profile.addclient.model.Client;
import profile.addledger.ShowLedgerActivity;

public class ClientAdapterForVoucher extends FirestoreRecyclerAdapter<Client, ClientAdapterForVoucher.ViewHolder> {
    private static final String TAG = "ClientAdapterForVoucher";
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private Context context;

    public ClientAdapterForVoucher(@NonNull FirestoreRecyclerOptions<Client> options, Context context) {
        super(options);
        this.context = context;
        Log.d(TAG, "ClientAdapterForVoucher: executing.. ");
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Client model) {
        holder.client_id_v.setText(model.getId());
        holder.client_name_v.setText(model.getClient_name());
        holder.client_email_v.setText(model.getClient_email());
        holder.go_to_view_ledger_list.setOnClickListener( v -> {
            Intent intent = new Intent(context, ShowLedgerActivity.class);
            intent.putExtra("client_id",model.getId());
            context.startActivity(intent);
        });

        holder.client_list_for_show_ledger_image_text_id.setText(model.getClient_name().substring(0,1).toUpperCase());
        Log.d(TAG, "onBindViewHolder: "+model);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_list_item_for_voucher, parent,false);
        return new ClientAdapterForVoucher.ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView client_id_v, client_name_v, client_email_v, client_list_for_show_ledger_image_text_id;
        ImageButton go_to_view_ledger_list;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            client_id_v = itemView.findViewById(R.id.client_id_v);
            client_name_v = itemView.findViewById(R.id.client_name_v);
            client_email_v = itemView.findViewById(R.id.client_email_v);

            go_to_view_ledger_list = itemView.findViewById(R.id.go_to_view_ledger_list);
            client_list_for_show_ledger_image_text_id = itemView.findViewById(R.id.client_list_for_show_ledger_image_text_id);
        }
    }
}
