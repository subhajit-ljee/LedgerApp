package profile.upload.model;

import com.google.firebase.firestore.Query;

import javax.inject.Inject;

import profile.upload.dao.CheckQRDao;

public class CheckQRRepository {

    private static final String TAG = "CheckQRRepository";
    private CheckQRDao checkQRDao;

    @Inject
    public CheckQRRepository(CheckQRDao checkQRDao){
        this.checkQRDao = checkQRDao;
    }

    public Query checkQRCode(){
        return checkQRDao.checkQRCode();
    }
}
