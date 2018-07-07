package productions.darthplagueis.contentvault.images;

import android.animation.Animator;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import productions.darthplagueis.contentvault.R;
import productions.darthplagueis.contentvault.SingleLiveEvent;
import productions.darthplagueis.contentvault.data.UserContent;
import productions.darthplagueis.contentvault.data.source.content.UserContentCallBack;
import productions.darthplagueis.contentvault.data.source.content.UserContentRepository;
import productions.darthplagueis.contentvault.util.ContextUtils;
import productions.darthplagueis.contentvault.util.CurrentDateUtil;
import productions.darthplagueis.contentvault.util.filemanager.FileManager;
import productions.darthplagueis.contentvault.util.filemanager.FileManagerCallBack;

import static productions.darthplagueis.contentvault.FragmentsActivity.PICK_IMAGE_CODE_REQUEST;

public class UserContentViewModel extends AndroidViewModel implements FileManagerCallBack.SaveFileCallBack,
        FileManagerCallBack.CopyFileCallBack, UserContentCallBack.GetUserContentCountCallBack {

    public final ObservableBoolean isFabExtending = new ObservableBoolean();

    public final ObservableBoolean isFabClosing = new ObservableBoolean();

    public final ObservableBoolean isActionState = new ObservableBoolean();

    public final ObservableField<String> photoActionBarText =
            new ObservableField<>(getApplication().getString(R.string.zero_selected));

    private static boolean allowFabMovement;

    private List<UserContent> itemsSelectedList = new ArrayList<>(0);

    private SparseArray<ImageView> viewSparseArray = new SparseArray<>(0);

    private SparseArray<Map<ImageView, List<UserContent>>> sparseArray = new SparseArray<>(0);

    private SingleLiveEvent<List<UserContent>> newFolderPromptEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<SparseArray<Map<ImageView, List<UserContent>>>> newPhotoDetailEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Void> newCarouselEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Void> newSortingEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Void> newSettingsEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Void> newPhotoImportEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Void> newMultiSelectionEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Void> newSelectionEnabledEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Void> newSelectionDisabledEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Void> newDeletePromptEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Void> newCopyPromptEvent = new SingleLiveEvent<>();

    private final FileManager fileManager = FileManager.newInstance(getApplication().getApplicationContext());

    private final UserContentRepository contentRepository;

    public UserContentViewModel(@NonNull Application application) {
        super(application);
        contentRepository = new UserContentRepository(application);
    }

    @BindingAdapter("openAnimation")
    public static void setOpenAnimation(View view, boolean isFabExtending) {
        if (allowFabMovement) {
            if (isFabExtending) setAnimation(view, true);
        }
    }

    @BindingAdapter("closeAnimation")
    public static void setCloseAnimation(View view, boolean isFabClosing) {
        if (allowFabMovement) {
            if (isFabClosing) setAnimation(view, false);
        }
    }

    @BindingAdapter({"appPrimaryBackground"})
    public static void setAppPrimaryBackground(View v, int resId) {
        final Context context = v.getContext();
        final int color = ContextUtils.resolveColor(context, resId, Color.BLACK);
        v.setBackgroundColor(color);
    }

    @Override
    public void onFileSaved(File file) {
        createContentEntity(file);
    }

    @Override
    public void onFileCopied(File file) {
        createContentEntity(file);
    }

    @Override
    public void onContentCountRetrieved(int count) {
        if (count > 0) newCarouselEvent.call();
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

    public void insert(UserContent userContent) {
        contentRepository.insert(userContent);
    }

    public void delete(UserContent userContent) {
        contentRepository.delete(userContent);
    }

    public SingleLiveEvent<List<UserContent>> getNewFolderPromptEvent() {
        return newFolderPromptEvent;
    }

    public SingleLiveEvent<SparseArray<Map<ImageView, List<UserContent>>>> getNewPhotoDetailEvent() {
        return newPhotoDetailEvent;
    }

    public SingleLiveEvent<Void> getNewCarouselEvent() {
        return newCarouselEvent;
    }

    public SingleLiveEvent<Void> getNewSettingsEvent() {
        return newSettingsEvent;
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

    public SingleLiveEvent<Void> getNewCopyPromptEvent() {
        return newCopyPromptEvent;
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

    public void loadDetailView(int position, List<UserContent> userContents, ImageView imageView) {
        Map<ImageView, List<UserContent>> userContentListMap = new HashMap<>();
        userContentListMap.put(imageView, userContents);
        sparseArray.clear();
        sparseArray.append(position, userContentListMap);
        newPhotoDetailEvent.setValue(sparseArray);
    }

    public void useCarouselView() {
        contentRepository.getUserContentCount(this);
    }

    public void createSortPopup() {
        newSortingEvent.call();
    }

    public void createSettingsPopup() {
        newSettingsEvent.call();
    }

    public void extendFab() {
        allowFabMovement = true;
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

    public void totalItemsSelected(int amountSelected) {
        photoActionBarText.set(String.valueOf(amountSelected) +
                getApplication().getString(R.string.items_selected));
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

    public void presentCopyPrompt() {
        if (itemsSelectedList.size() > 0) {
            newCopyPromptEvent.call();
            disableMultiSelection();
        }
    }

    public void presentAlbumPrompt() {
        if (itemsSelectedList.size() > 0) {
            newFolderPromptEvent.setValue(itemsSelectedList);
            disableMultiSelection();
        }
    }

    public void deleteSelected() {
        if (itemsSelectedList.size() != 0) {
            for (UserContent itemSelected : itemsSelectedList) {
                fileManager.deleteFileAsync(itemSelected);
                delete(itemSelected);
            }
        }
    }

    public void copySelected() {
        if (itemsSelectedList.size() != 0) {
            for (UserContent itemSelected : itemsSelectedList) {
                fileManager.copyFileAsync(itemSelected, this);
            }
        }
    }

    public void handleActivityResult(int requestCode, Intent data) {
        if (data != null && requestCode == PICK_IMAGE_CODE_REQUEST) {
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
            fileManager.saveBitmapAsync(bitmap, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createContentEntity(File file) {
        insert(new UserContent(file.getName(), CurrentDateUtil.getDateString(),
                CurrentDateUtil.getTimeStamp(), file.getAbsolutePath(),
                file.getParentFile().getName().substring(4)));
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
                    view.animate().translationY(0).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            allowFabMovement = false;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                }
                break;
            default:
                break;
        }
    }
}
