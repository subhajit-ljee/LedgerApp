package profile.deletevoucher.adapter;

import android.content.Context;
import android.content.Intent;
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
import profile.deletevoucher.activities.ListOfLedgerForDeletingActivity;

public class ListOfClientForDeleteAdapter extends FirestoreRecyclerAdapter<Client, ListOfClientForDeleteAdapter.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private Context context;

    public ListOfClientForDeleteAdapter(@NonNull FirestoreRecyclerOptions<Client> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Client model) {
        holder.client_name.setText(model.getClient_name());
        holder.client_email.setText(model.getClient_email());
        holder.client_id.setText(model.getId());

        holder.go_to_view_ledger_list_for_delete.setOnClickListener( v -> {
            Intent intent = new Intent(context, ListOfLedgerForDeletingActivity.class);
            intent.putExtra("client_id",model.getId());

            context.startActivity(intent);

        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_list_item, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView client_name, client_email, client_id;
        ImageButton go_to_view_ledger_list_for_delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            client_name = itemView.findViewById(R.id.client_name);
            client_email = itemView.findViewById(R.id.client_email);
            client_id = itemView.findViewById(R.id.client_id);
            go_to_view_ledger_list_for_delete = itemView.findViewById(R.id.go_to_view_ledger_list_for_delete);
        }
    }
}
