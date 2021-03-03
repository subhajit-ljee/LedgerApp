package profile.upload.dependency;

import dagger.Binds;
import dagger.Module;
import profile.upload.dao.PdfUploadDao;
import profile.upload.dao.impl.PdfUploadDaoImpl;

@Module
public abstract class UploadPdfModule {

    @Binds
    abstract PdfUploadDao bindPdfUploadDao(PdfUploadDaoImpl pdfUploadDaoImpl);
}
