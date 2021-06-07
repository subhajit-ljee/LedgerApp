package profile.addledger.adapter;

import android.annotation.SuppressLint;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import profile.addledger.model.Ledger;

public class RecentLedgerListAdapter extends FirestoreRecyclerAdapter<Ledger, RecentLedgerListAdapter.ViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private static final String TAG = "RecentLedgerListAdapter";
    private Context context;

    public RecentLedgerListAdapter(@NonNull FirestoreRecyclerOptions<Ledger> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull RecentLedgerListAdapter.ViewHolder holder, int position, @NonNull Ledger model) {

        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        //String now = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat modelDate = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date modeldate = modelDate.parse(model.getTimestamp());
            assert modeldate != null;
            long milis = modeldate.getTime();
            Log.d(TAG, "onBindViewHolder: milis: " + milis + ", and current time milis: " + System.currentTimeMillis() + ", and 60 days before: " + (System.currentTimeMillis() - 60*DAY_IN_MS));
            if(milis > (System.currentTimeMillis() - 60 * DAY_IN_MS) && System.currentTimeMillis() > milis){

                holder.itemView.setVisibility(View.VISIBLE);
                holder.rec_ledger_name.setText(model.getAccount_name());
                holder.rec_ledger_date.setText(model.getTimestamp());
                holder.rec_ledger_type.setText(model.getAccount_type());
                holder.recent_ledger_amount.setText(model.getOpening_balance());

                holder.see_details.setOnClickListener( v -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("userid", model.getUser_id());
                    bundle.putString("clientid",model.getClient_id());
                    bundle.putString("ledgerid", model.getId());
                    NavController controller = Navigation.findNavController((Activity) context,R.id.create_client_fragment);
                    controller.navigate(R.id.action_recentLedgerListFragment_to_ledgerDetailsFragment, bundle);
                });
            }
            else{
                holder.itemView.setVisibility(View.GONE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @NonNull
    @Override
    public RecentLedgerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_ledgers, parent, false);
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        MaterialTextView rec_ledger_name, rec_ledger_date, recent_ledger_amount, rec_ledger_type;
        MaterialButton see_details;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rec_ledger_name = itemView.findViewById(R.id.rec_ledger_name);
            rec_ledger_date = itemView.findViewById(R.id.rec_ledger_date);
            recent_ledger_amount = itemView.findViewById(R.id.recent_ledger_amount);
            rec_ledger_type = itemView.findViewById(R.id.rec_ledger_type);

            see_details = itemView.findViewById(R.id.see_details);

        }
    }


}
