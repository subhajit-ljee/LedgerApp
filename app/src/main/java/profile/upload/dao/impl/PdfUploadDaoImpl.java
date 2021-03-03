package profile.upload.dao.impl;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

import profile.upload.dao.PdfUploadDao;
import profile.upload.model.UploadPDF;

public class PdfUploadDaoImpl implements PdfUploadDao {
    private static final String TAG = "PdfUploadDaoImpl";
    private UploadPDF uploadPDF;
    private FirebaseFirestore db;
    @Inject
    public PdfUploadDaoImpl(UploadPDF uploadPDF){
        this.uploadPDF = uploadPDF;
        Log.d(TAG, "PdfUploadDaoImpl: UploadPdf: " + uploadPDF);
    }
    @Override
    public void saveFile() {
        db = FirebaseFirestore.getInstance();
        db.collection("client_bills").document(uploadPDF.getId())
        .set(uploadPDF);
    }
}
