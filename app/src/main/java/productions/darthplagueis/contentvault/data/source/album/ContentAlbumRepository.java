package productions.darthplagueis.contentvault.data.source.album;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import productions.darthplagueis.contentvault.data.ContentAlbum;
import productions.darthplagueis.contentvault.util.AppExecutors;
import productions.darthplagueis.contentvault.util.DiskIOThreadExecutor;

public class ContentAlbumRepository implements ContentFolderCallBack {

    private AppExecutors appExecutors;

    private ContentAlbumDao contentAlbumDao;

    public ContentAlbumRepository(Application application) {
        ContentAlbumDatabase db = ContentAlbumDatabase.getDatabase(application);
        contentAlbumDao = db.contentAlbumDao();
        appExecutors = new AppExecutors();
    }

    public LiveData<List<ContentAlbum>> getAllAlbums() {
        return contentAlbumDao.getAllAlbums();
    }

    public LiveData<List<ContentAlbum>> getDescDateList() {
        return contentAlbumDao.getDescendingDateOrder();
    }

    public void insert(ContentAlbum contentAlbum) {
        Runnable runnable = () -> contentAlbumDao.insert(contentAlbum);
        appExecutors.getDatabaseThread().execute(runnable);
    }

    public void delete(ContentAlbum contentAlbum) {
        Runnable runnable = () -> contentAlbumDao.delete(contentAlbum);
        appExecutors.getDatabaseThread().execute(runnable);
    }

    public void deleteAll() {
        Runnable runnable = () -> contentAlbumDao.deleteAll();
        appExecutors.getDatabaseThread().execute(runnable);
    }

    public void getFolderNames(GetContentFolderNamesCallBack callBack) {
        Runnable runnable = () -> {
            List<String> folderNames = contentAlbumDao.getAlbumNames();
            appExecutors.getMainThread().execute(() -> callBack.onFolderNamesRetrieved(folderNames));
        };
        appExecutors.getDatabaseThread().execute(runnable);
    }
}
