package productions.darthplagueis.contentvault.data.source.contentfolders;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import java.util.List;

import productions.darthplagueis.contentvault.data.ContentFolder;
import productions.darthplagueis.contentvault.data.source.AppDatabase;
import productions.darthplagueis.contentvault.util.app.AppExecutors;

public class ContentFolderRepository implements ContentFolderCallBack {

    private AppExecutors appExecutors;

    private ContentFolderDao contentFolderDao;

    public ContentFolderRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        contentFolderDao = db.contentAlbumDao();
        appExecutors = new AppExecutors();
    }

    public LiveData<List<ContentFolder>> getAllAlbums() {
        return contentFolderDao.getAllAlbums();
    }

    public LiveData<List<ContentFolder>> getDescDateList() {
        return contentFolderDao.getDescendingDateOrder();
    }

    public void insert(ContentFolder contentFolder) {
        Runnable runnable = () -> contentFolderDao.insert(contentFolder);
        appExecutors.getDatabaseThread().execute(runnable);
    }

    public void delete(ContentFolder contentFolder) {
        Runnable runnable = () -> contentFolderDao.delete(contentFolder);
        appExecutors.getDatabaseThread().execute(runnable);
    }

    public void deleteAll() {
        Runnable runnable = () -> contentFolderDao.deleteAll();
        appExecutors.getDatabaseThread().execute(runnable);
    }

    public void getFolderNames(GetContentFolderNamesCallBack callBack) {
        Runnable runnable = () -> {
            List<String> folderNames = contentFolderDao.getAlbumNames();
            appExecutors.getMainThread().execute(() -> callBack.onFolderNamesRetrieved(folderNames));
        };
        appExecutors.getDatabaseThread().execute(runnable);
    }
}
