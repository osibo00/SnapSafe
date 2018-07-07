package productions.darthplagueis.contentvault.imagefolders;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.List;

import productions.darthplagueis.contentvault.SingleLiveEvent;
import productions.darthplagueis.contentvault.data.ContentFolder;
import productions.darthplagueis.contentvault.data.UserContent;
import productions.darthplagueis.contentvault.data.source.contentfolders.ContentFolderRepository;
import productions.darthplagueis.contentvault.data.source.contentfolders.ContentFolderCallBack;
import productions.darthplagueis.contentvault.data.source.content.UserContentRepository;
import productions.darthplagueis.contentvault.util.CurrentDateUtil;
import productions.darthplagueis.contentvault.util.filemanager.FileManager;
import productions.darthplagueis.contentvault.util.filemanager.FileManagerCallBack;

public class ContentFolderViewModel extends AndroidViewModel implements FileManagerCallBack.MoveFileCallBack,
        ContentFolderCallBack.GetContentFolderNamesCallBack {

    private SingleLiveEvent<List<String>> newFolderNamesEvent = new SingleLiveEvent<>();

    private FileManager fileManager = FileManager.newInstance(getApplication().getApplicationContext());

    private final ContentFolderRepository folderRepository;
    private final UserContentRepository contentRepository;

    public ContentFolderViewModel(@NonNull Application application) {
        super(application);
        folderRepository = new ContentFolderRepository(application);
        contentRepository = new UserContentRepository(application);
    }

    public LiveData<List<ContentFolder>> getAllAlbums() {
        return folderRepository.getAllAlbums();
    }

    public LiveData<List<ContentFolder>> getDescDateList() {
        return folderRepository.getDescDateList();
    }

    public SingleLiveEvent<List<String>> getNewFolderNamesEvent() {
        return newFolderNamesEvent;
    }

    public void insertContentFolder(ContentFolder contentFolder) {
        folderRepository.insert(contentFolder);
    }

    public void deleteContentFolder(ContentFolder contentFolder) {
        folderRepository.delete(contentFolder);
    }

    public void insertUserContent(UserContent userContent) {
        contentRepository.insert(userContent);
    }

    public void updateUserContent(UserContent userContent) {
        contentRepository.update(userContent);
    }

    public void deleteUserContent(UserContent userContent) {
        contentRepository.delete(userContent);
    }

    public void getFolderNames() {
        folderRepository.getFolderNames(this);
    }

    public void createNewFolder(String folderName, String tag, List<UserContent> itemsSelectedList) {
        if (itemsSelectedList != null) {
            for (int i = 0; i < itemsSelectedList.size(); i++) {
                UserContent itemSelected = itemsSelectedList.get(i);
                itemSelected.setContentTag(tag);
                if (i == 0) {
                    fileManager.moveFileAsync(itemSelected, folderName, true, this);
                } else {
                    fileManager.moveFileAsync(itemSelected, folderName, false, this);
                }
            }
        }
    }

    @Override
    public void onAlbumCreated(File file, UserContent userContent) {
        insertContentFolder(new ContentFolder(
                file.getParentFile().getName().substring(4),
                file.getAbsolutePath(),
                CurrentDateUtil.getDateString(),
                CurrentDateUtil.getTimeStamp(),
                userContent.getContentTag()));

        updateUserContentFields(userContent, file);
    }

    @Override
    public void onFileMoved(File file, UserContent userContent) {
        updateUserContentFields(userContent, file);
    }

    @Override
    public void onFolderNamesRetrieved(List<String> folderNames) {
        newFolderNamesEvent.setValue(folderNames);
    }

    private void updateUserContentFields(UserContent userContent, File file) {
        userContent.setFilePath(file.getAbsolutePath());
        userContent.setFileDirectory(file.getParentFile().getName().substring(4));
        updateUserContent(userContent);
    }
}
