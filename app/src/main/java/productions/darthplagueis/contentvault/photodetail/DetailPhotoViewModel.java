package productions.darthplagueis.contentvault.photodetail;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import java.util.List;

import productions.darthplagueis.contentvault.SingleLiveEvent;
import productions.darthplagueis.contentvault.data.UserContent;
import productions.darthplagueis.contentvault.util.FileManager;

public class DetailPhotoViewModel extends AndroidViewModel {

    private SingleLiveEvent<Void> newBackButtonEvent = new SingleLiveEvent<>();

    public DetailPhotoViewModel(@NonNull Application application) {
        super(application);
    }

    public SingleLiveEvent<Void> getNewBackButtonEvent() {
        return newBackButtonEvent;
    }

    public void onBackButtonPressed() {
        newBackButtonEvent.call();
    }
}
