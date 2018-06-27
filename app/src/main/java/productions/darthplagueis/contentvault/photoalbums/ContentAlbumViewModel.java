package productions.darthplagueis.contentvault.photoalbums;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.util.List;

import productions.darthplagueis.contentvault.data.ContentAlbum;
import productions.darthplagueis.contentvault.data.UserContent;
import productions.darthplagueis.contentvault.data.source.album.ContentAlbumRepository;
import productions.darthplagueis.contentvault.data.source.content.UserContentRepository;
import productions.darthplagueis.contentvault.util.CurrentDateUtil;
import productions.darthplagueis.contentvault.util.FileManager;
import productions.darthplagueis.contentvault.util.FileManagerCallBack;

public class ContentAlbumViewModel extends AndroidViewModel implements FileManagerCallBack.MoveFileCallBack {

    private FileManager fileManager = new FileManager(getApplication());

    private final ContentAlbumRepository albumRepository;
    private final UserContentRepository contentRepository;

    public ContentAlbumViewModel(@NonNull Application application) {
        super(application);
        albumRepository = new ContentAlbumRepository(application);
        contentRepository = new UserContentRepository(application);
    }

    public LiveData<List<ContentAlbum>> getAllAlbums() {
        return albumRepository.getAllAlbums();
    }

    public LiveData<List<ContentAlbum>> getDescDateList() {
        return albumRepository.getDescDateList();
    }

    public List<String> getAlbumNames() {
        return albumRepository.getAlbumNames();
    }

    public void insertContentAlbum(ContentAlbum contentAlbum) {
        albumRepository.insert(contentAlbum);
    }

    public void deleteContentAlbum(ContentAlbum contentAlbum) {
        albumRepository.delete(contentAlbum);
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

    public void createNewAlbum(String albumName, String tag, List<UserContent> itemsSelectedList) {
        if (itemsSelectedList != null) {
            for (int i = 0; i < itemsSelectedList.size(); i++) {
                UserContent itemSelected = itemsSelectedList.get(i);
                itemSelected.setContentTag(tag);
                if (i == 0) {
                    fileManager.moveFileAsync(itemSelected, albumName, true, this);
                } else {
                    fileManager.moveFileAsync(itemSelected, albumName, false, this);
                }
            }
        }
    }

    @Override
    public void onAlbumCreated(File file, UserContent userContent) {
        insertContentAlbum(new ContentAlbum(
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

    private void updateUserContentFields(UserContent userContent, File file) {
        userContent.setFilePath(file.getAbsolutePath());
        userContent.setFileDirectory(file.getParentFile().getName().substring(4));
        updateUserContent(userContent);
    }
}
