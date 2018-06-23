package productions.darthplagueis.contentvault.photos;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.ClipData;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    public final ObservableBoolean isActionState = new ObservableBoolean();

    public final ObservableField<String> photoActionBarText =
            new ObservableField<>(getApplication().getString(R.string.zero_selected));

    private List<UserContent> itemsSelectedList = new ArrayList<>(0);

    private SingleLiveEvent<List<UserContent>> newAlbumPromptEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<String> newPhotoDetailEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Void> newPhotoImportEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Void> newMultiSelectionEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Void> newSelectionEnabledEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Void> newSelectionDisabledEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Void> newDeletePromptEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Void> newSortingEvent = new SingleLiveEvent<>();

    private FileManager fileManager = new FileManager(getApplication());

    private UserContentRepository contentRepository;

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

    public LiveData<List<UserContent>> getAlbumOrderAZList() {
        return contentRepository.getAlbumOrderAZList();
    }

    public LiveData<List<UserContent>> getAlbumOrderZAList() {
        return contentRepository.getAlbumOrderZAList();
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

    public SingleLiveEvent<List<UserContent>> getNewAlbumPromptEvent() {
        return newAlbumPromptEvent;
    }

    public SingleLiveEvent<String> getNewPhotoDetailEvent() {
        return newPhotoDetailEvent;
    }

    public SingleLiveEvent<Void> getNewPhotoImportEvent() {
        return newPhotoImportEvent;
    }

    public SingleLiveEvent<Void> getNewMultiSelectionEvent() {
        return newMultiSelectionEvent;
    }

    public SingleLiveEvent<Void> getNewSelectionEnabledEvent() {
        return newSelectionEnabledEvent;
    }

    public SingleLiveEvent<Void> getNewSelectionDisabledEvent() {
        return newSelectionDisabledEvent;
    }

    public SingleLiveEvent<Void> getNewDeletePromptEvent() {
        return newDeletePromptEvent;
    }

    public SingleLiveEvent<Void> getNewSortingEvent() {
        return newSortingEvent;
    }

    public void importPhotos() {
        newPhotoImportEvent.call();
        extendFab();
    }

    public void selectPhotos() {
        newMultiSelectionEvent.call();
        isActionState.set(true);
        itemsSelectedList.clear();
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

    public void enableMultiSelection() {
        newSelectionEnabledEvent.call();
        isActionState.set(true);
        itemsSelectedList.clear();
    }

    public void disableMultiSelection() {
        newSelectionDisabledEvent.call();
        isActionState.set(false);
        photoActionBarText.set(getApplication().getString(R.string.zero_selected));
    }

    public void totalItemsSelected(int amountSelected) {
        photoActionBarText.set(String.valueOf(amountSelected) +
                getApplication().getString(R.string.items_selected));
    }

    public void loadDetailView(UserContent userContent) {
        newPhotoDetailEvent.setValue(userContent.getFilePath());
    }

    public void contentSelected(UserContent userContent) {
        if (itemsSelectedList.contains(userContent)) {
            itemsSelectedList.remove(userContent);
        } else {
            itemsSelectedList.add(userContent);
        }
    }

    public void presentDeletePrompt() {
        if (itemsSelectedList.size() > 0) {
            newDeletePromptEvent.call();
            disableMultiSelection();
        }
    }

    public void presentAlbumPrompt() {
        if (itemsSelectedList.size() > 0) {
            newAlbumPromptEvent.setValue(itemsSelectedList);
            disableMultiSelection();
        }
    }

    public void createSortPopup() {
        newSortingEvent.call();
    }

    public void deleteSelected() {
        if (itemsSelectedList.size() != 0) {
            for (UserContent item : itemsSelectedList) {
                fileManager.setCurrentDirectoryName(item.getFileDirectory());
                fileManager.deleteFile(item.getFileName());
                delete(item);
            }
            itemsSelectedList.clear();
        }
    }

    public void handleActivityResult(int requestCode, Intent data) {
        if (data != null && requestCode == PICK_IMAGE_CODE_TAG) {
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    Uri uri = item.getUri();
                    convertToBitmap(uri);
                }
            } else {
                Uri uri = data.getData();
                convertToBitmap(uri);
            }
        }
    }

    private void convertToBitmap(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                    (getApplication().getApplicationContext()).getContentResolver(), uri);
            createContentEntity(fileManager.saveBitmap(bitmap));
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            e.getLocalizedMessage();
        }
    }

    private void createContentEntity(File file) {
        insert(new UserContent(file.getName(), file.getAbsolutePath(),
                file.getParentFile().getName().substring(4),
                CurrentDateUtil.getDateString(),
                CurrentDateUtil.getTimeStamp()));
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
}
