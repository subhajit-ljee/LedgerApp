package com.sourav.ledgerproject.profile.addledger.addvoucher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sourav.ledgerproject.R;
import com.sourav.ledgerproject.profile.addledger.model.DeleteVoucherModel;
import com.sourav.ledgerproject.profile.addledger.view.DeleteVoucherListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeleteVoucherListActivity extends AppCompatActivity implements DeleteVoucherListAdapter.OnItemListener {

    private final String TAG = getClass().getCanonicalName();
    RecyclerView deletevoucherlistrecycler;
    DeleteVoucherListAdapter deleteVoucherListAdapter;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_voucher_details);
        db = FirebaseFirestore.getInstance();
        Log.d(TAG,"client_id: "+getIntent().getStringExtra("client_id"));
        db.collection("vouchers")
                .whereEqualTo("client_id",getIntent().getStringExtra("client_id"))
                .get()
                .addOnCompleteListener( task -> {
                    List<DeleteVoucherModel> deleteinfo = new ArrayList<>();
                    DeleteVoucherModel deleteVoucherModel;
                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                        deleteVoucherModel = new DeleteVoucherModel(snapshot.getString("name"), snapshot.getString("timestamp"),
                                snapshot.getString("type"), snapshot.getString("amount"));
                        deleteinfo.add(deleteVoucherModel);
                        //Log.d(TAG, "" + deleteVoucherModel);
                    }
                    Log.d(TAG, "deleteinfo list is: " + deleteinfo);
                    if(deleteinfo != null) {
                        deleteVoucherListAdapter = new DeleteVoucherListAdapter(this);
                        deleteVoucherListAdapter.setVoucher_model(deleteinfo);
                        deletevoucherlistrecycler = findViewById(R.id.delete_voucher_list_recyler);
                        deletevoucherlistrecycler.setLayoutManager(new LinearLayoutManager(this));
                        deletevoucherlistrecycler.setAdapter(deleteVoucherListAdapter);
                    }
                });
    }

    @Override
    public void onItemClick(int position) {
        db.collection("vouchers")
                .document("vouchers_"+getIntent().getStringExtra("client_id"))
                .delete()
        .addOnCompleteListener( (Void) ->{
            Log.d(TAG, "Voucher successfully deleted!");
        })
        .addOnFailureListener( e -> Log.d(TAG,"Error Deleting Voucher!"));


        Log.d(TAG,"item clicked: "+this.deleteVoucherListAdapter.getVoucher_model().get(position));
    }
}