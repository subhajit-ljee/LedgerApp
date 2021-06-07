package profile.profilefragments.qrscanner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sourav.ledgerproject.R;

import profile.upload.CheckQRCodeService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QrCodeScannerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QrCodeScannerFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "QrCodeScannerFragment";

    private IntentIntegrator qrScan;

    public QrCodeScannerFragment() {
        // Required empty public constructor
    }

    public static QrCodeScannerFragment newInstance() {
        return new QrCodeScannerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qr_code_scanner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button buttonScan;
        MaterialToolbar toolbar2 = view.findViewById(R.id.toolbar2);

        toolbar2.setNavigationOnClickListener( v -> requireActivity().onBackPressed());
        buttonScan = view.findViewById(R.id.buttonScan);
        qrScan = new IntentIntegrator(requireActivity());
        buttonScan.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        qrScan.initiateScan();
    }

    @SuppressLint("InflateParams")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {

            if (result.getContents() == null) {
                Toast.makeText(requireActivity(), "Result Not Found", Toast.LENGTH_LONG).show();
            } else {

                try {

                    Intent intent = new Intent(requireActivity(), CheckQRCodeService.class);
                    intent.putExtra("qr_id",result.getContents());
                    CheckQRCodeService.enqueueWork(requireActivity(),intent);

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
}