package profile.addvoucher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.sourav.ledgerproject.R;

public class ApproveVoucherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_voucher);
        Intent intent  = new Intent(this,CreateVoucherActivity.class);
        intent.putExtra("voucher_approved","added");
        startActivity(intent);
    }
}