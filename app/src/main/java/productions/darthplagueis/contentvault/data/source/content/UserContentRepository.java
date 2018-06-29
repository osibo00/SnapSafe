package productions.darthplagueis.contentvault.data.source.content;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import java.util.List;

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

    public void insert(UserContent userContent) {
        Runnable runnable = () -> userContentDao.insert(userContent);
        appExecutors.getDatabaseThread().execute(runnable);
    }

    public void update(UserContent userContent) {
        Runnable runnable = () -> userContentDao.update(userContent);
        appExecutors.getDatabaseThread().execute(runnable);
    }

    public void delete(UserContent userContent) {
        Runnable runnable = () -> userContentDao.delete(userContent);
        appExecutors.getDatabaseThread().execute(runnable);
    }

    public void deleteAll() {
        Runnable runnable = () -> userContentDao.deleteAll();
        appExecutors.getDatabaseThread().execute(runnable);
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
            int contentCount = userContentDao.countUserContentTotal();
            appExecutors.getMainThread().execute(() -> callBack.onContentCountRetrieved(contentCount));
        };
        appExecutors.getDatabaseThread().execute(runnable);
    }
}

