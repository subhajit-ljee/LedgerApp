package profile.upload.model;

import javax.inject.Inject;

import profile.upload.dao.PdfUploadDao;

public class PdfUploadRepository {

    private static final String TAG = "PdfUploadRepository";
    private PdfUploadDao pdfUploadDao;

    @Inject
    public PdfUploadRepository(PdfUploadDao pdfUploadDao){
        this.pdfUploadDao = pdfUploadDao;
    }

    public void saveFile(){
        pdfUploadDao.saveFile();
    }
}
