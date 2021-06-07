package profile.myclient.repository;

import com.google.firebase.firestore.Query;

import javax.inject.Inject;

import profile.myclient.dao.MyClientListDao;

public class MyClientRepository {
    private MyClientListDao myClientListDao;

    @Inject
    public MyClientRepository(MyClientListDao myClientListDao){
        this.myClientListDao = myClientListDao;
    }

    public Query getMyClientList(){
        return myClientListDao.getMyClientList();
    }
}
