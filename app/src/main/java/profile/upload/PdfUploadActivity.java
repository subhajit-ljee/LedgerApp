package profile.upload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import javax.inject.Inject;

import profile.profilefragments.qr.CreateQrCodeFragment;
import profile.profilefragments.upload.PdfUploadFragment;
import profile.upload.dependency.UploadPdfComponent;
import profile.upload.model.PdfUploadRepository;
import profile.upload.model.UploadPDF;

public class PdfUploadActivity extends AppCompatActivity {

    private static final String TAG = "PdfUploadActivity";
    private UploadPdfComponent uploadPdfComponent;
    private CreateQrCodeFragment createQrCodeFragment;

    private EditText file_name;
    private Button btn_upload_file;
    //private TextView upload_meter;

    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Inject
    PdfUploadRepository pdfUploadRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_upload);

        file_name = findViewById(R.id.id_file_name);
        btn_upload_file = findViewById(R.id.id_upload_file_pdf);
        btn_upload_file.setOnClickListener( v -> {
            checkPermissionToReadPdf();
        });

        createQrCodeFragment = CreateQrCodeFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.input_for_qr, createQrCodeFragment).commit();
    }

    public void checkPermissionToReadPdf(){
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                        uploadFile();
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();
    }


    private void uploadFile() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent," Select Your Bill "),1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
        if(requestCode ==1 && resultCode == RESULT_OK
                && data != null && data.getData()!=null){
            Log.d(TAG, "onActivityResult: data: " + data.getData());
            //uploadPdfFile(data.getData());
            String name = null;
            Cursor c = getContentResolver().query(data.getData(),null,null,null,null);
            if(c != null && c.moveToFirst())
                name = c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            Log.d(TAG, "onActivityResult: name: " + name);
        }
    }

    private void uploadPdfFile(Uri data) {
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.alert_dialog_for_file_upload_process,null);
        //upload_meter = v.findViewById(R.id.id_upload_meter);
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(v)
                .create();
        alertDialog.show();

        StorageReference reference = storageReference.child("/uploads"+System.currentTimeMillis()+".pdf");
        reference.putFile(data).continueWithTask(task -> {
            if(!task.isSuccessful())
                throw task.getException();
            return reference.getDownloadUrl();
        }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "uploadPdfFile: successfully added");
                        Uri uri = (Uri) task.getResult();
                        String uid = UUID.randomUUID().toString();
                        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                        UploadPDF uploadPDF = new UploadPDF(uid, file_name.getText().toString().trim(), uri.toString(), date);

                        PdfUploadActivity.this.uploadPdfComponent = ((LedgerApplication) getApplication()).getAppComponent()
                                .getUploadPdfComponent().create(uploadPDF);
                        PdfUploadActivity.this.uploadPdfComponent.inject(PdfUploadActivity.this);
                        PdfUploadActivity.this.pdfUploadRepository.saveFile();

                        Thread alertThread = new Thread(()-> {

                           alertDialog.dismiss();
                        });
                        alertThread.start();

                        try {
                            alertThread.sleep(1000);
                        } catch (InterruptedException e) {
                            Log.e(TAG, "uploadPdfFile: ", e);
                        }
                        alertThread.interrupt();
                        if(alertThread.isInterrupted()){
                            Log.d(TAG, "uploadPdfFile: alertthread interrupted");
                        }
                    }
                });
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
}