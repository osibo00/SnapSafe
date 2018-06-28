package productions.darthplagueis.contentvault.data.source.content;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import productions.darthplagueis.contentvault.data.UserContent;
import productions.darthplagueis.contentvault.util.AppExecutors;

public class UserContentRepository implements UserContentCallBack {

    private LiveData<List<UserContent>> userContentList;

    private AppExecutors appExecutors;

    private UserContentDao userContentDao;

    public UserContentRepository(Application application) {
        UserContentDatabase db = UserContentDatabase.getDatabase(application);
        userContentDao = db.userContentDao();
        appExecutors = new AppExecutors();
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

    public void getUserContentList(GetUserContentCallBack callBack) {
        Runnable runnable = () -> {
            List<UserContent> userContents = userContentDao.getUserContentList();
            appExecutors.getMainThread().execute(() -> callBack.onContentListRetrieved(userContents));
        };
        appExecutors.getDatabaseThread().execute(runnable);
    }

    public void getUserContentCount(GetUserContentCountCallBack callBack) {
        Runnable runnable = () -> {
            int contentCount = userContentDao.countUserItemTotal();
            appExecutors.getMainThread().execute(() -> callBack.onContentCountRetrieved(contentCount));
        };
        appExecutors.getDatabaseThread().execute(runnable);
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
}

