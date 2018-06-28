package productions.darthplagueis.contentvault.photodetail;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import productions.darthplagueis.contentvault.SingleLiveEvent;

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
