package profile.myclient.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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

import profile.addclient.model.MyClient;


public class MyClientListAdapter extends FirestoreRecyclerAdapter<MyClient,MyClientListAdapter.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     *
     */

    private static final String TAG = "MyClientListAdapter";
    private static boolean SHOW_MENU = true;
    private final Context context;
    public MyClientListAdapter(@NonNull FirestoreRecyclerOptions<MyClient> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull MyClient model) {



        holder.client_name.setText(model.getMy_client_name());
        holder.client_id.setText(model.getMy_client_id());
        holder.client_email.setText(model.getMy_client_email());

        Log.d(TAG, "onBindViewHolder: name: " + model.getMy_client_name() + " , id: " + model.getMy_client_id() + " , email: " + model.getMy_client_email());
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

        NavController findNavController = Navigation.findNavController((Activity) context,R.id.pro_main_fragment);
        Bundle ledgerInfoBundle = new Bundle();
        ledgerInfoBundle.putString("cid",model.getMy_client_id());
        holder.details.setOnClickListener( v -> new Handler().post(() -> findNavController.navigate(R.id.action_myLedgerMainFragment_to_myLedgerListFragment,ledgerInfoBundle)));

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_client_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView client_name, client_id, client_email;
        ImageView details;
        ConstraintLayout second_view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            client_name = itemView.findViewById(R.id.my_client_name);
            client_id = itemView.findViewById(R.id.my_client_id);
            client_email = itemView.findViewById(R.id.my_client_email);

            second_view = itemView.findViewById(R.id.second_view_content);
            details = second_view.findViewById(R.id.see_details_img);
        }
    }
}
