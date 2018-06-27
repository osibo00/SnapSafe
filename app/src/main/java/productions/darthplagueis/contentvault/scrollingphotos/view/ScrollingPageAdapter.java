package productions.darthplagueis.contentvault.scrollingphotos.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import productions.darthplagueis.contentvault.data.UserContent;
import productions.darthplagueis.contentvault.scrollingphotos.view.ScrollingPhotoFragment;

public class ScrollingPageAdapter extends FragmentStatePagerAdapter {

    private final List<UserContent> userContentList;

    public ScrollingPageAdapter(FragmentManager fm, List<UserContent> userContents) {
        super(fm);
        this.userContentList = userContents;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position < userContentList.size()) {
            fragment = ScrollingPhotoFragment.newInstance(userContentList.get(position).getFilePath());
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return userContentList.size();
    }

    public void lookUpContentItem(String filePath) {
        for (UserContent item : userContentList) {
            if (filePath == item.getFilePath()) {
                getItem(userContentList.indexOf(item));
            }
        }
    }
}
