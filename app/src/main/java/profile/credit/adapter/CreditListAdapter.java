package profile.credit.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.sourav.ledgerproject.R;

import profile.addclient.model.Client;
import profile.credit.CreditListAllLedgersActivity;

public class CreditListAdapter extends FirestoreRecyclerAdapter<Client, CreditListAdapter.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private static final String TAG = "CreditListAdapter";
    private Context context;

    public CreditListAdapter(@NonNull FirestoreRecyclerOptions<Client> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Client model) {
        holder.client_list_item_id.setText(model.getId());
        holder.client_list_item_name.setText(model.getClient_name());
        holder.client_list_item_address.setText(model.getClient_email());
        holder.client_list_image_text_id.setText(model.getClient_name().substring(0,1).toUpperCase());
        holder.go_to_view_ledger_list.setOnClickListener( v -> {
            new Handler().post( ()-> {
                Intent intent = new Intent(context, CreditListAllLedgersActivity.class);
                intent.putExtra("client_id",model.getId());
                context.startActivity(intent);
            });
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_debit_list_recycler, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView client_list_item_name, client_list_item_address, client_list_item_id, client_list_image_text_id;
        ImageButton go_to_view_ledger_list;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            client_list_item_name = itemView.findViewById(R.id.client_list_item_name);
            client_list_item_id = itemView.findViewById(R.id.client_list_item_id);
            client_list_item_address = itemView.findViewById(R.id.client_list_item_address);

            client_list_image_text_id = itemView.findViewById(R.id.client_list_image_text_id);

            go_to_view_ledger_list = itemView.findViewById(R.id.go_to_view_ledger_list);
        }
    }
}
