package productions.darthplagueis.contentvault.data.source.content;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import productions.darthplagueis.contentvault.data.UserContent;

public class UserContentRepository {

    private UserContentDao userContentDao;

    public UserContentRepository(Application application) {
        UserContentDatabase db = UserContentDatabase.getDatabase(application);
        userContentDao = db.userContentDao();
    }

    public LiveData<List<UserContent>> getAllMedia() {
        return userContentDao.getAllUserContent();
    }

    public LiveData<List<UserContent>> getDescDateList() {
        return userContentDao.getDescendingDateOrder();
    }

    public LiveData<List<UserContent>> getAscDateList() {
        return userContentDao.getAscendingDateOrder();
    }

    public LiveData<List<UserContent>> getAlbumOrderList() {
        return userContentDao.getAlbumOrder();
    }

    public LiveData<List<UserContent>> getLastItemByDirectory(String directoryName) {
        return userContentDao.getLastItem(directoryName);
    }

    public void insert(UserContent userContent) {
        new insertAsyncTask(userContentDao).execute(userContent);
    }

    public void delete(UserContent userContent) {
        new deleteAsyncTask(userContentDao).execute(userContent);
    }

    private static class insertAsyncTask extends AsyncTask<UserContent, Void, Void> {

        private UserContentDao asyncTaskDao;

        insertAsyncTask(UserContentDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(UserContent... userContent) {
            asyncTaskDao.insert(userContent[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<UserContent, Void, Void> {

        private UserContentDao asyncTaskDao;

        deleteAsyncTask(UserContentDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(UserContent... userContent) {
            asyncTaskDao.delete(userContent[0]);
            return null;
        }
    }
}

