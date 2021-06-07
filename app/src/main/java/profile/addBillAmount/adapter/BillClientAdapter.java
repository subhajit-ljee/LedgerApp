package profile.addBillAmount.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textview.MaterialTextView;
import com.sourav.ledgerproject.R;

import profile.addclient.model.Client;

public class BillClientAdapter extends FirestoreRecyclerAdapter<Client, BillClientAdapter.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private Context context;

    public BillClientAdapter(@NonNull FirestoreRecyclerOptions<Client> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Client model) {

        holder.client_name_b.setText(model.getClient_name());
        holder.client_id_b.setText(model.getId());
        holder.client_email_b.setText(model.getClient_email());

        boolean[] isopen = {true};

        holder.main_content_view.setOnClickListener( v -> {

            if(isopen[0]){
                holder.second_content_view.setVisibility(View.VISIBLE);
                isopen[0] = false;
            }else{
                holder.second_content_view.setVisibility(View.GONE);
                isopen[0] = true;
            }
        });

        holder.go_for_bill_entry.setOnClickListener( v -> {
            NavController findNavController = Navigation.findNavController((Activity) context, R.id.bill_main_fragment);
            Bundle bundle = new Bundle();
            bundle.putString("clientid",model.getId());
            bundle.putString("userid",model.getUser_id());
            findNavController.navigate(R.id.action_billChooseClientFragment_to_billChooseLedgerFragment, bundle);
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_client_list, parent, false);
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView client_name_b, client_id_b, client_email_b;
        ConstraintLayout second_content_view;
        RelativeLayout main_content_view;
        ImageView go_for_bill_entry;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            client_name_b = itemView.findViewById(R.id.client_name_b);
            client_id_b = itemView.findViewById(R.id.client_id_b);
            client_email_b = itemView.findViewById(R.id.client_email_b);

            main_content_view = itemView.findViewById(R.id.main_view_content);
            second_content_view = itemView.findViewById(R.id.second_view_content);

            go_for_bill_entry = itemView.findViewById(R.id.go_for_bill_entry);
        }
    }
}
