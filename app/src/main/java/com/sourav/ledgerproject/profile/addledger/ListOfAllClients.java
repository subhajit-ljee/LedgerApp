package com.sourav.ledgerproject.profile.addledger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sourav.ledgerproject.R;
import com.sourav.ledgerproject.profile.addclient.view.ClientAdapter;
import com.sourav.ledgerproject.profile.model.Client;

import java.util.ArrayList;
import java.util.List;

public class ListOfAllClients extends AppCompatActivity {

    private static final int MY_REQUEST_CODE_PERMISSION = 1000;
    private static final int MY_RESULT_CODE_FILECHOOSER = 2000;

    private static final String TAG = "ListOfAllClients";

    Toolbar listItemToolbar;

    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_all_clients);

        db = FirebaseFirestore.getInstance();

        db.collection("clients")
                .get()
                .addOnCompleteListener( task -> {
                    List<Client> clientList = new ArrayList<>();
                    for(QueryDocumentSnapshot document:task.getResult()){
                        Client client = new Client(document.getId(),document.getString("client_name"),
                                document.getString("client_email"),document.getString("user_id"));

                        clientList.add(client);
                    }

                    Log.d(TAG,"clientlist: "+clientList);

                    if(clientList.size() > 0){
                        ClientAdapter clientAdapter = new ClientAdapter();
                        clientAdapter.setClient(clientList);
                        RecyclerView recyclerView = findViewById(R.id.list_of_all_clients);
                        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                        recyclerView.setFocusable(true);
                        recyclerView.setAdapter(clientAdapter);

                    }

                });

        listItemToolbar = findViewById(R.id.allvoucherlistpagetoolbar);
        setSupportActionBar(listItemToolbar);
    }

    public void createLedgerOption(View v){
        Intent showVoucherIntent = new Intent(this,ShowLedgerActivity.class);
        TextView client_name_view = v.findViewById(R.id.client_name);
        TextView client_id_view = v.findViewById(R.id.client_id);
        showVoucherIntent.putExtra("client_id",client_id_view.getText().toString().trim());
        showVoucherIntent.putExtra("client_name",client_name_view.getText().toString().trim());
        startActivity(showVoucherIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.bill_save_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem){

        switch (menuItem.getItemId()){
            case R.id.bill_save_item:
                uploadBill();
        }
        return true;
    }

    private void uploadBill(){

        int permisson = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permisson != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_REQUEST_CODE_PERMISSION
            );
        }

        doBrowseFile();
    }

    private void doBrowseFile()  {
        Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFileIntent.setType("*/*");
        chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFileIntent = Intent.createChooser(chooseFileIntent, "Choose a file");
        startActivityForResult(chooseFileIntent, MY_RESULT_CODE_FILECHOOSER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

        switch (requestCode) {
            case MY_REQUEST_CODE_PERMISSION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG,"Permission granted!");
                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();

                    this.doBrowseFile();
                }
                else {
                    Log.i(TAG,"Permission denied!");
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode) {
            case MY_RESULT_CODE_FILECHOOSER:
                if (resultCode == Activity.RESULT_OK ) {
                    if(data != null)  {
                        Uri fileUri = data.getData();
                        Log.i(TAG, "Uri: " + fileUri);

                        String filePath = null;
                        try {
                            filePath = BillFileUtils.getPath(this,fileUri);
                        } catch (Exception e) {
                            Log.e(TAG,"Error: " + e);
                            Toast.makeText(this, "Error: " + e, Toast.LENGTH_SHORT).show();
                        }
                        Log.d(TAG,"File name: "+filePath);
                    }
                }
                break;
        }
        

    }

    //public String getPath()  {
      //  return this.editTextPath.getText().toString();
    //}
}