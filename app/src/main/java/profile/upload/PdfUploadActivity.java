package profile.upload;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.sourav.ledgerproject.R;
import profile.profilefragments.qr.CreateQrCodeFragment;


public class PdfUploadActivity extends AppCompatActivity {

    private static final String TAG = "PdfUploadActivity";

    private CreateQrCodeFragment createQrCodeFragment;


    //private TextView upload_meter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_upload);


        createQrCodeFragment = CreateQrCodeFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.id_file_upload_activity, createQrCodeFragment).commit();
    }






                /*.addOnProgressListener( taskSnapshot -> {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    upload_meter.setText(""+(int)progress+"%");
                    if(upload_meter.getText().toString().trim().equals("100%")){
                        Thread thread = new Thread(() -> {
                            alertDialog.dismiss();
                        });
                        try {
                            thread.sleep(1000);
                        }catch (Exception e){
                            Log.e(TAG, "error generating sleep: ", e);
                        }
                        thread.start();
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "uploadPdfFile: ", e));


                 */

}