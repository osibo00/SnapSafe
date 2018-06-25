package productions.darthplagueis.contentvault.data.source.content;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import productions.darthplagueis.contentvault.data.UserContent;

public class UserContentRepository {

    private LiveData<List<UserContent>> userContentList;

    private UserContentDao userContentDao;

    public UserContentRepository(Application application) {
        UserContentDatabase db = UserContentDatabase.getDatabase(application);
        userContentDao = db.userContentDao();
        userContentList = userContentDao.getDescendingDateOrder();
    }

    public LiveData<List<UserContent>> getAllMedia() {
        return userContentDao.getAllUserContent();
    }

    public LiveData<List<UserContent>> getDescDateList() {
        return userContentList;
    }

    public LiveData<List<UserContent>> getAscDateList() {
        return userContentDao.getAscendingDateOrder();
    }

    public LiveData<List<UserContent>> getAlbumOrderAZList() {
        return userContentDao.getAlbumOrderAZ();
    }

    public LiveData<List<UserContent>> getAlbumOrderZAList() {
        return userContentDao.getAlbumOrderZA();
    }

    public LiveData<List<UserContent>> getLastItemByDirectory(String directoryName) {
        return userContentDao.getLastItem(directoryName);
    }

    public int getItemCount() {
        try {
            return new countAsyncTask(userContentDao).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void insert(UserContent userContent) {
        new insertAsyncTask(userContentDao).execute(userContent);
    }

    public void update(UserContent userContent) {
        new updateAsyncTask(userContentDao).execute(userContent);
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

    private static class updateAsyncTask extends AsyncTask<UserContent, Void, Void> {

        private UserContentDao asyncTaskDao;

        updateAsyncTask(UserContentDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(UserContent... userContent) {
            asyncTaskDao.updateUserContent(userContent[0]);
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

    private static class countAsyncTask extends AsyncTask<Void, Void, Integer> {

        private UserContentDao asyncTaskDao;

        countAsyncTask(UserContentDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return asyncTaskDao.countUserItemTotal();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }
    }
}

