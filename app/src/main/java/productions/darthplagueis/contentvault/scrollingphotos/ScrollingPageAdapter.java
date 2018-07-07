package productions.darthplagueis.contentvault.scrollingphotos;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import productions.darthplagueis.contentvault.data.UserContent;
import productions.darthplagueis.contentvault.scrollingphotos.ScrollingPhotoViewModel;
import productions.darthplagueis.contentvault.scrollingphotos.view.ScrollingPhotoFragment;

public class ScrollingPageAdapter extends FragmentStatePagerAdapter {

    private final List<UserContent> userContentList;

    private final ScrollingPhotoViewModel photoViewModel;

    public ScrollingPageAdapter(FragmentManager fm, ScrollingPhotoViewModel photoViewModel,
                                List<UserContent> userContents) {
        super(fm);
        this.userContentList = userContents;
        this.photoViewModel = photoViewModel;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position < userContentList.size()) {
            UserContent userContent = userContentList.get(position);
            fragment = ScrollingPhotoFragment.newInstance(
                    userContent, userContent.getFileName());
            photoViewModel.currentPageAdapterPosition(position);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return userContentList.size();
    }
}
