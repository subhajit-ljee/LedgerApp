package profile.upload.dependency;

import com.sourav.ledgerproject.ActivityScope;

import dagger.BindsInstance;
import dagger.Subcomponent;
import profile.upload.CheckQRCodeService;
import profile.upload.model.UploadPDF;

@ActivityScope
@Subcomponent(modules = CheckQRModule.class)
public interface CheckQRComponent {
    void inject(CheckQRCodeService checkQRCodeService);

    @Subcomponent.Factory
    interface Factory {
        CheckQRComponent create(@BindsInstance UploadPDF uploadPDF);
    }
}
