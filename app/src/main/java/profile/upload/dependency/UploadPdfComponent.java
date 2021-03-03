package profile.upload.dependency;

import com.sourav.ledgerproject.ActivityScope;

import dagger.BindsInstance;
import dagger.Subcomponent;
import profile.upload.PdfUploadActivity;
import profile.upload.model.UploadPDF;

@ActivityScope
@Subcomponent(modules = UploadPdfModule.class)
public interface UploadPdfComponent {

    @Subcomponent.Factory
    interface Factory{
        UploadPdfComponent create(@BindsInstance UploadPDF uploadPDF);
    }

    void inject(PdfUploadActivity pdfUploadActivity);
}
