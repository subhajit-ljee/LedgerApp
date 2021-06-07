package profile.addclient.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.sourav.ledgerproject.R;


import profile.addclient.model.Client;

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
    public ClientAdapter(@NonNull FirestoreRecyclerOptions<Client> options, Context context) {
        super(options);
        if(options == null || options.getSnapshots().size() == 0)
        Log.d(TAG, "ClientAdapter: options not working");
        this.context = context;
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

        holder.itemView.setOnClickListener( v -> {

            if(holder.client_id.getText().toString().trim() != null)
                Log.d(TAG, "onLongClick: " + holder.client_id.getText().toString().trim());
            else
                Log.d(TAG,"null");

            //Intent intent = null;
            //intent = new Intent(context, CreateLedgerActivity.class);
            //intent.putExtra("clientid", model.getId());
            //intent.putExtra("clientname", model.getClient_name());
            Bundle bundle = new Bundle();
            bundle.putString("clientid",model.getId());
            bundle.putString("clientname",model.getClient_name());
            Navigation.findNavController(holder.itemView).navigate(R.id.action_clientListFragment_to_addLedgerFragment, bundle);

            //context.startActivity(intent);
        });
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        TextView client_name,client_email,client_id;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "ViewHolder: ");
            client_name = itemView.findViewById(R.id.client_name);
            client_email = itemView.findViewById(R.id.client_email);
            client_id = itemView.findViewById(R.id.client_id);

        }
    }

}
