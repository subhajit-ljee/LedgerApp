package com.sourav.ledgerproject.profile.addledger.addvoucher;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sourav.ledgerproject.R;
import com.sourav.ledgerproject.profile.addledger.model.DeleteClientModel;
import com.sourav.ledgerproject.profile.addledger.view.DeleteClientListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeleteVoucherActivity extends AppCompatActivity {

    private final String TAG = getClass().getCanonicalName();

    RecyclerView deleteVoucherList;
    DeleteClientModel del_vouchers_info;
    List<DeleteClientModel> del_list;
    FirebaseFirestore db;
    DeleteClientListAdapter del_list_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_voucher);

        db = FirebaseFirestore.getInstance();
        db.collection("clients")
                .whereEqualTo("user_id",FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener( clienttask -> {

                    for(QueryDocumentSnapshot clientsnapshot:clienttask.getResult()) {
                        if(clientsnapshot.getString("user_id") != null) {
                            db.collection("account_details")
                                    .whereNotEqualTo("client_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .get()
                                    .addOnCompleteListener(task -> {

                                        del_list = new ArrayList<>();
                                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                            del_vouchers_info = new DeleteClientModel(snapshot.getString("client_id"), snapshot.getString("account_name"));
                                            del_list.add(del_vouchers_info);
                                        }

                                        Log.d(TAG, "del list is: " + del_list);
                                        del_list_adapter = new DeleteClientListAdapter();
                                        deleteVoucherList = findViewById(R.id.recycler_delete_voucher);
                                        deleteVoucherList.setLayoutManager(new LinearLayoutManager(this));
                                        del_list_adapter.setVouchers(del_list);
                                        deleteVoucherList.setAdapter(del_list_adapter);
                                    });
                        }else{
                            Toast.makeText(this,"Nothing to show!",Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    public void deleteVoucher(View v){
         String client_id = ((TextView)v.findViewById(R.id.voucher_client_id)).getText().toString().trim();
         Intent intent = new Intent(this,DeleteVoucherListActivity.class);
         intent.putExtra("client_id",client_id);
         startActivity(intent);
    }
}