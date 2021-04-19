package profile.addledger;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sourav.ledgerproject.BaseActivity;
import com.sourav.ledgerproject.R;

import profile.addclient.SelectAndAddClientActivity;
import profile.deletevoucher.activities.ListofClientForDeleteActivity;
import profile.profilefragments.voucher.ClientListForVoucherFragment;
import profile.upload.PdfUploadActivity;

public class ListOfAllClients extends BaseActivity {

    private static final String TAG = "ListOfAllClients";
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG,"Inside ProfileActivity onCreate()");
        bottomNavigationView = findViewById(R.id.profile_menu_nav_view);
        setUpBottomNavigation(bottomNavigationView);

    }

    @Override
    protected void setUpBottomNavigation(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Log.d(TAG, "setUpBottomNavigation: ");
            selectDrawerItem(item);
            return true;
        });
    }

    @Override
    protected int getResourceLayoutId() {
        return R.layout.activity_list_of_all_clients;
    }

    private void selectDrawerItem(MenuItem item) {

        switch (item.getItemId()){
            case R.id.addnewledger:
                makeLedger();
                Log.d(TAG, "selectDrawerItem: "+ R.id.addnewledger);
                break;
            case R.id.addnewvoucher:
                makeVoucher();
                Log.d(TAG, "selectDrawerItem: "+ R.id.addnewvoucher);
                break;
            case R.id.deletevoucher:
                deleteVoucher();
                Log.d(TAG, "selectDrawerItem: "+ R.id.deletevoucher);
                break;
            case R.id.showledger:
                showLedgerWithVoucher();
                Log.d(TAG, "selectDrawerItem: "+ R.id.showledger);
                break;
            case R.id.uploadbill:
                uploadBill();
                break;
        }

        item.setChecked(true);
    }

    @Override
    protected void showLedgerWithVoucher() {
        startActivity(new Intent(this, ListOfAllClients.class));
    }

    @Override
    protected void makeVoucher() {
        startActivity(new Intent(this, ListOfAllClients.class));
    }

    @Override
    protected void makeLedger() {
        startActivity(new Intent(this, SelectAndAddClientActivity.class));
    }

    @Override
    protected void uploadBill(){
        startActivity(new Intent(this, PdfUploadActivity.class));
    }

    @Override
    protected void deleteVoucher(){
        startActivity(new Intent(this, ListofClientForDeleteActivity.class));
    }

}