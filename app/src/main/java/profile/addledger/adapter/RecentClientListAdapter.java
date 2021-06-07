package profile.addledger.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.sourav.ledgerproject.R;

import profile.addclient.model.Client;

public class RecentClientListAdapter extends FirestoreRecyclerAdapter<Client, RecentClientListAdapter.ViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private static final String TAG = "RecentLedgerListAdapter";

    private Context context;
    private String flag;
    public RecentClientListAdapter(@NonNull FirestoreRecyclerOptions<Client> options, Context context, String flag) {
        super(options);
        this.context = context;
        this.flag = flag;
        Log.d(TAG, "onBindViewHolder: in RecentLedgerListAdapter");
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Client model) {
        holder.rec_client_name.setText(model.getClient_name());
        holder.rec_client_email.setText(model.getClient_email());

        Log.d(TAG, "onBindViewHolder: model: " + model);
        holder.see_ledgers.setOnClickListener( v -> {
            Bundle bundle = new Bundle();
            bundle.putString("clientid",model.getId());
            NavController controller = Navigation.findNavController((Activity) context,R.id.create_client_fragment);
            if(flag.equals("led"))
                controller.navigate(R.id.action_ledgerFragment_to_recentLedgerListFragment, bundle);
            else if(flag.equals("see"))
                controller.navigate(R.id.action_seeAllClientListFragment_to_recentLedgerListFragment, bundle);
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_clients_for_ledgers, parent, false);
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView rec_client_name, rec_client_email;
        MaterialButton see_ledgers;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rec_client_name = itemView.findViewById(R.id.rec_client_name);
            rec_client_email = itemView.findViewById(R.id.rec_client_email);

            see_ledgers = itemView.findViewById(R.id.see_ledgers);
        }
    }
}
