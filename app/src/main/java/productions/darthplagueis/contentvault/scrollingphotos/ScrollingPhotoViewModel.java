package productions.darthplagueis.contentvault.scrollingphotos;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import productions.darthplagueis.contentvault.SingleLiveEvent;
import productions.darthplagueis.contentvault.data.UserContent;
import productions.darthplagueis.contentvault.data.source.content.UserContentCallBack;
import productions.darthplagueis.contentvault.data.source.content.UserContentRepository;

public class ScrollingPhotoViewModel extends AndroidViewModel implements UserContentCallBack.GetUserContentCallBack {

    private SingleLiveEvent<List<UserContent>> newContentListEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Integer> newPagerAdapterPositionEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Integer> newItemAdapterPositionEvent = new SingleLiveEvent<>();

    private final UserContentRepository contentRepository;

    public ScrollingPhotoViewModel(@NonNull Application application) {
        super(application);
        contentRepository = new UserContentRepository(application);
    }

    @Override
    public void onContentListRetrieved(List<UserContent> userContents) {
        newContentListEvent.setValue(userContents);
    }

    public SingleLiveEvent<List<UserContent>> getNewContentListEvent() {
        return newContentListEvent;
    }

    public SingleLiveEvent<Integer> getNewPagerAdapterPositionEvent() {
        return newPagerAdapterPositionEvent;
    }

    public SingleLiveEvent<Integer> getNewItemAdapterPositionEvent() {
        return newItemAdapterPositionEvent;
    }

    public void getUserContentList() {
        contentRepository.getUserContentList(this);
    }

    public void currentPageAdapterPosition(int position) {
        newPagerAdapterPositionEvent.setValue(position);
    }

    public void currentItemAdapterPosition(int adapterPosition) {
        newItemAdapterPositionEvent.setValue(adapterPosition);
    }
}
