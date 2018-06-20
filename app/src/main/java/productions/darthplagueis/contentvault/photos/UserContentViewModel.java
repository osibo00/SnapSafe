package productions.darthplagueis.contentvault.photos;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.ClipData;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;

import java.io.File;
import java.util.List;
import java.util.Objects;

import productions.darthplagueis.contentvault.R;
import productions.darthplagueis.contentvault.SingleLiveEvent;
import productions.darthplagueis.contentvault.data.UserContent;
import productions.darthplagueis.contentvault.data.source.content.UserContentRepository;
import productions.darthplagueis.contentvault.util.CurrentDateUtil;
import productions.darthplagueis.contentvault.util.FileManager;

import static productions.darthplagueis.contentvault.FragmentsActivity.PICK_IMAGE_CODE_TAG;

public class UserContentViewModel extends AndroidViewModel {

    public final ObservableBoolean isFabExtending = new ObservableBoolean();

    public final ObservableBoolean isFabClosing = new ObservableBoolean();

    private UserContentRepository contentRepository;

    private SingleLiveEvent<Void> newPhotoImportEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Void> newMultiSelectionEvent = new SingleLiveEvent<>();

    public UserContentViewModel(@NonNull Application application) {
        super(application);
        contentRepository = new UserContentRepository(application);
    }

    @BindingAdapter("openAnimation")
    public static void setOpenAnimation(View view, boolean isFabExtending) {
        if (isFabExtending) setAnimation(view, true);
    }

    @BindingAdapter("closeAnimation")
    public static void setCloseAnimation(View view, boolean isFabClosing) {
        if (isFabClosing) setAnimation(view, false);
    }

    public LiveData<List<UserContent>> getAllMedia() {
        return contentRepository.getAllMedia();
    }

    public LiveData<List<UserContent>> getDescDateList() {
        return contentRepository.getDescDateList();
    }

    public LiveData<List<UserContent>> getAscDateList() {
        return contentRepository.getAscDateList();
    }

    public LiveData<List<UserContent>> getAlbumOrderList() {
        return contentRepository.getAlbumOrderList();
    }

    public LiveData<List<UserContent>> getLastItemByDirectory(String albumName) {
        return contentRepository.getLastItemByDirectory(albumName);
    }

    public void insert(UserContent userContent) {
        contentRepository.insert(userContent);
    }

    public void delete(UserContent userContent) {
        contentRepository.delete(userContent);
    }

    public SingleLiveEvent<Void> getNewPhotoImportEvent() {
        return newPhotoImportEvent;
    }

    public SingleLiveEvent<Void> getNewMultiSelectionEvent() {
        return newMultiSelectionEvent;
    }

    public void importPhotos() {
        newPhotoImportEvent.call();
        extendFab();
    }

    public void selectPhotos() {
        newMultiSelectionEvent.call();
        extendFab();
    }

    public void extendFab() {
        if (!isFabExtending.get()) {
            isFabClosing.set(false);
            isFabExtending.set(true);
        } else {
            isFabClosing.set(true);
            isFabExtending.set(false);
        }
    }

    public void closeFab() {
        extendFab();
    }

    public void handleActivityResult(int requestCode, Intent data) {
        if (data != null) {
            if (requestCode == PICK_IMAGE_CODE_TAG && data.getClipData() != null) {
                ClipData clipData = data.getClipData();
                FileManager fileManager = new FileManager(getApplication());
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    Uri uri = item.getUri();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                Objects.requireNonNull(getApplication().getApplicationContext())
                                        .getContentResolver(), uri);
                        createContentEntity(fileManager.saveBitmap(bitmap));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void setAnimation(View view, boolean isExtending) {
        switch (view.getId()) {
            case R.id.photos_fab:
                if (isExtending) view.animate().rotationBy(135f);
                else view.animate().rotationBy(-135f);
                break;
            case R.id.fab_layout01:
                if (isExtending) {
                    view.animate().translationY(
                            -view.getContext().getResources().getDimension(R.dimen.dp_55));
                } else {
                    view.animate().translationY(0);
                }
                break;
            case R.id.fab_layout02:
                if (isExtending) {
                    view.animate().translationY(
                            -view.getContext().getResources().getDimension(R.dimen.dp_100));
                } else {
                    view.animate().translationY(0);
                }
                break;
            case R.id.fab_layout03:
                if (isExtending) {
                    view.animate().translationY(
                            -view.getContext().getResources().getDimension(R.dimen.dp_145));
                } else {
                    view.animate().translationY(0);
                }
                break;
            default:
                break;
        }
    }

    private void createContentEntity(File file) {
        UserContent userContent = new UserContent(file.getName(), file.getAbsolutePath(),
                file.getParentFile().getName().substring(4),
                CurrentDateUtil.getDateString(),
                CurrentDateUtil.getTimeStamp());

        insert(userContent);
    }
}
