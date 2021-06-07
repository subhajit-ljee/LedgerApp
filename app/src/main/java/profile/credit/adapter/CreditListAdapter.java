package profile.credit.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import com.sourav.ledgerproject.R;

import profile.addclient.model.Client;

public class CreditListAdapter extends FirestoreRecyclerAdapter<Client, CreditListAdapter.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private static final String TAG = "CreditListAdapter";
    private static boolean SHOW_MENU = true;
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

        holder.itemView.setOnClickListener( v -> {
            if(SHOW_MENU) {
                holder.second_view.setVisibility(View.VISIBLE);
                SHOW_MENU = false;
            }
            else {
                holder.second_view.setVisibility(View.GONE);
                SHOW_MENU = true;
            }
            Log.d(TAG, "onBindViewHolder: SHOW_MENU: " + SHOW_MENU);
        });

        holder.go_to_view_ledger_list.setOnClickListener( v -> {
            new Handler().post( ()-> {
                Bundle bundle = new Bundle();
                bundle.putString("client_id",model.getId());
                NavController finNavController = Navigation.findNavController( (Activity) context,R.id.pro_main_fragment);
                finNavController.navigate(R.id.action_creditListFragment_to_creditListAllLedgersFragment, bundle);
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
        TextView client_list_item_name, client_list_item_address, client_list_item_id;
        ConstraintLayout second_view;
        ImageView go_to_view_ledger_list;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            client_list_item_name = itemView.findViewById(R.id.client_list_item_name);
            client_list_item_id = itemView.findViewById(R.id.client_list_item_id);
            client_list_item_address = itemView.findViewById(R.id.client_list_item_address);


            second_view = itemView.findViewById(R.id.second_view_content);
            go_to_view_ledger_list = itemView.findViewById(R.id.go_to_view_ledger_list);
        }
    }
}
