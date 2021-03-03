package profile.qr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sourav.ledgerproject.R;

import profile.profilefragments.qr.CreateQrCodeFragment;

public class CreateQrCodeActivity extends AppCompatActivity {

    private static final String TAG = "CreateQrCodeActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qr_code);


    }
}