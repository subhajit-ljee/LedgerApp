package profile.upload.dao.impl;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import javax.inject.Inject;

import profile.upload.dao.CheckQRDao;
import profile.upload.model.UploadPDF;

public class CheckQRDaoImpl implements CheckQRDao {

    private static final String TAG = "CheckQRDaoImpl";

    private UploadPDF uploadPDF;
    private FirebaseFirestore db;
    @Inject
    public CheckQRDaoImpl(UploadPDF uploadPDF){
        this.uploadPDF = uploadPDF;
    }

    @Override
    public Query checkQRCode() {

        db = FirebaseFirestore.getInstance();
        return db.collection("client_bills")
                .whereEqualTo("id",uploadPDF.getId());

    }
}
