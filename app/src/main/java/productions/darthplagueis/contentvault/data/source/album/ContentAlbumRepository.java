package productions.darthplagueis.contentvault.data.source.album;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import productions.darthplagueis.contentvault.data.ContentAlbum;
import productions.darthplagueis.contentvault.util.AppExecutors;
import productions.darthplagueis.contentvault.util.DiskIOThreadExecutor;

public class ContentAlbumRepository {

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

    public List<String> getAlbumNames() {
        return contentAlbumDao.getAlbumNames();
    }

    public void insert(ContentAlbum contentAlbums) {
        new insertAsyncTask(contentAlbumDao).execute(contentAlbums);
    }

    public void delete(ContentAlbum contentAlbums) {
        new deleteAsyncTask(contentAlbumDao).execute(contentAlbums);
    }

    public void deleteAll() {
        Runnable runnable = () -> contentAlbumDao.deleteAll();
        appExecutors.getDatabaseThread().execute(runnable);
    }

    private static class insertAsyncTask extends AsyncTask<ContentAlbum, Void, Void> {

        private ContentAlbumDao asyncTaskDao;

        insertAsyncTask(ContentAlbumDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(ContentAlbum... contentAlbums) {
            asyncTaskDao.insert(contentAlbums[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<ContentAlbum, Void, Void> {

        private ContentAlbumDao asyncTaskDao;

        deleteAsyncTask(ContentAlbumDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(ContentAlbum... contentAlbums) {
            asyncTaskDao.delete(contentAlbums[0]);
            return null;
        }
    }
}
