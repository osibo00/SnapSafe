package productions.darthplagueis.contentvault.photoalbums;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.List;

import productions.darthplagueis.contentvault.data.ContentAlbum;
import productions.darthplagueis.contentvault.data.UserContent;
import productions.darthplagueis.contentvault.data.source.album.ContentAlbumRepository;
import productions.darthplagueis.contentvault.data.source.content.UserContentRepository;
import productions.darthplagueis.contentvault.photos.UserContentViewModel;
import productions.darthplagueis.contentvault.util.CurrentDateUtil;
import productions.darthplagueis.contentvault.util.FileManager;

public class ContentAlbumViewModel extends AndroidViewModel {

    private FileManager fileManager = new FileManager(getApplication());

    private ContentAlbumRepository albumRepository;
    private UserContentRepository contentRepository;

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

    public void insert(ContentAlbum contentAlbum) {
        albumRepository.insert(contentAlbum);
    }

    public void delete(ContentAlbum contentAlbum) {
        albumRepository.delete(contentAlbum);
    }

    public void insertUserContent(UserContent userContent) {
        contentRepository.insert(userContent);
    }

    public void deleteUserContent(UserContent userContent) {
        contentRepository.delete(userContent);
    }

    public void createNewAlbum(String albumName, List<UserContent> itemsSelectedList) {
        if (itemsSelectedList.size() != 0) {
            fileManager.setNewDirectoryName(albumName);
            insert(new ContentAlbum(albumName, CurrentDateUtil.getDateString(),
                    CurrentDateUtil.getTimeStamp()));
            for (UserContent item : itemsSelectedList) {
                fileManager.setCurrentDirectoryName(item.getFileDirectory());
                createContentEntity(fileManager.moveFile(item.getFileName()));
                deleteUserContent(item);
            }
        }
    }

    private void createContentEntity(File file) {
        insertUserContent(new UserContent(file.getName(), file.getAbsolutePath(),
                file.getParentFile().getName().substring(4),
                CurrentDateUtil.getDateString(),
                CurrentDateUtil.getTimeStamp()));
    }
}
