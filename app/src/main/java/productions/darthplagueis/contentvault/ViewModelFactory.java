package productions.darthplagueis.contentvault;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import productions.darthplagueis.contentvault.photoalbums.ContentFolderViewModel;
import productions.darthplagueis.contentvault.photodetail.DetailPhotoViewModel;
import productions.darthplagueis.contentvault.photos.UserContentViewModel;
import productions.darthplagueis.contentvault.scrollingphotos.ScrollingPhotoViewModel;

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

    @NonNull
    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserContentViewModel.class)) {
            return (T) new UserContentViewModel(application);
        }
        if (modelClass.isAssignableFrom(ContentFolderViewModel.class)) {
            return (T) new ContentFolderViewModel(application);
        }
        if (modelClass.isAssignableFrom(DetailPhotoViewModel.class)) {
            return (T) new DetailPhotoViewModel(application);
        }
        if (modelClass.isAssignableFrom(ScrollingPhotoViewModel.class)) {
            return (T) new ScrollingPhotoViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
