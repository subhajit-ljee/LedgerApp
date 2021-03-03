package profile.profilefragments.qr;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.sourav.ledgerproject.R;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static android.content.Context.WINDOW_SERVICE;


public class CreateQrCodeFragment extends Fragment {

    private ImageView qrCodeImg;
    private EditText dataQr;
    private Button qrButton;

    Bitmap bitmap;
    QRGEncoder qrgEncoder;

    public CreateQrCodeFragment() {

    }


    public static CreateQrCodeFragment newInstance() {
        CreateQrCodeFragment fragment = new CreateQrCodeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_qr_code, container, false);
        dataQr = view.findViewById(R.id.valueForQr);
        qrButton = view.findViewById(R.id.idDataQr);
        qrCodeImg = view.findViewById(R.id.idImageQr);

        qrButton.setOnClickListener( v -> {
            if (TextUtils.isEmpty(dataQr.getText().toString())) {
                Toast.makeText(getActivity(), "Enter some text to generate QR Code", Toast.LENGTH_SHORT).show();
            } else {
                WindowManager manager = getActivity().getWindowManager();

                Point point = new Point();
                Display display = manager.getDefaultDisplay();
                display.getSize(point);

                int width = point.x;
                int height = point.y;

                int dimen = (width < height)? width:height;
                dimen = dimen * 3 / 4;

                qrgEncoder = new QRGEncoder(dataQr.getText().toString(), null, QRGContents.Type.TEXT, dimen);
                try {

                    bitmap = qrgEncoder.getBitmap();
                    qrCodeImg.setImageBitmap(bitmap);

                } catch (Exception e) {
                    Log.e("Tag", e.toString());
                }
            }
        });
        return view;
    }
}