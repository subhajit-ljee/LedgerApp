package profile.qrscanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sourav.ledgerproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import profile.upload.CheckQRCodeService;

public class QRCodeScanActivity extends AppCompatActivity implements View.OnClickListener{


    private static final String TAG = "QRCodeScanActivity";

    private Button buttonScan;
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_code_scan);

        buttonScan = findViewById(R.id.buttonScan);

        qrScan = new IntentIntegrator(this);

        buttonScan.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {

            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {

                try {

                    Intent intent = new Intent(this, CheckQRCodeService.class);
                    intent.putExtra("qr_id",result.getContents());
                    CheckQRCodeService.enqueueWork(this,intent);

                    Log.d(TAG, "onActivityResult: exception block: " + result.getContents());

                } catch (Exception e) {
                    e.printStackTrace();
                    getLayoutInflater().inflate(R.layout.fragment_error_adding_ledger,null);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        qrScan.initiateScan();
    }
}