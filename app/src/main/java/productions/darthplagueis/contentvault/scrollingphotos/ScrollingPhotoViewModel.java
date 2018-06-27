package productions.darthplagueis.contentvault.scrollingphotos;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import java.util.List;

import productions.darthplagueis.contentvault.SingleLiveEvent;
import productions.darthplagueis.contentvault.data.UserContent;
import productions.darthplagueis.contentvault.data.source.content.UserContentCallBack;
import productions.darthplagueis.contentvault.data.source.content.UserContentRepository;

public class ScrollingPhotoViewModel extends AndroidViewModel implements UserContentCallBack.GetUserContentCallBack {

    private SingleLiveEvent<List<UserContent>> newContentListEvent = new SingleLiveEvent<>();

    private final UserContentRepository contentRepository;

    public ScrollingPhotoViewModel(@NonNull Application application) {
        super(application);
        contentRepository = new UserContentRepository(application);
    }

    public SingleLiveEvent<List<UserContent>> getNewContentListEvent() {
        return newContentListEvent;
    }

    public void getUserContentList() {
        contentRepository.getUserContentList(this);
    }

    @Override
    public void onContentListRetrieved(List<UserContent> userContents) {
        newContentListEvent.setValue(userContents);
    }
}
