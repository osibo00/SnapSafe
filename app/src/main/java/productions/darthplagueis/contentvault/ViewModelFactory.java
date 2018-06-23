package productions.darthplagueis.contentvault;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import productions.darthplagueis.contentvault.photoalbums.ContentAlbumViewModel;
import productions.darthplagueis.contentvault.photodetail.DetailPhotoViewModel;
import productions.darthplagueis.contentvault.photos.UserContentViewModel;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private static volatile ViewModelFactory INSTANCE;

    private final Application application;

    public static ViewModelFactory getINSTANCE(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(application);
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyINSTANCE() {
        INSTANCE = null;
    }

    private ViewModelFactory(Application application) {
        this.application = application;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserContentViewModel.class)) {
            return (T) new UserContentViewModel(application);
        }
        if (modelClass.isAssignableFrom(ContentAlbumViewModel.class)) {
            return (T) new ContentAlbumViewModel(application);
        }
        if (modelClass.isAssignableFrom(DetailPhotoViewModel.class)) {
            return (T) new DetailPhotoViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
