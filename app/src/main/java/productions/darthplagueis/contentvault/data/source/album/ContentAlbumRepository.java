package productions.darthplagueis.contentvault.data.source.album;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import productions.darthplagueis.contentvault.data.ContentAlbum;

public class ContentAlbumRepository {

    private ContentAlbumDao contentAlbumDao;

    public ContentAlbumRepository(Application application) {
        ContentAlbumDatabase db = ContentAlbumDatabase.getDatabase(application);
        contentAlbumDao = db.contentAlbumDao();
    }

    public LiveData<List<ContentAlbum>> getAllAlbums() {
        return contentAlbumDao.getAllAlbums();
    }

    public LiveData<List<ContentAlbum>> getDescDateList() {
        return contentAlbumDao.getDescendingDateOrder();
    }

    public void insert(ContentAlbum contentAlbums) {
        new insertAsyncTask(contentAlbumDao).execute(contentAlbums);
    }

    public void delete(ContentAlbum contentAlbums) {
        new deleteAsyncTask(contentAlbumDao).execute(contentAlbums);
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
