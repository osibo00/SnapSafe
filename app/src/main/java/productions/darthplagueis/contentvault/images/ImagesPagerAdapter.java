package productions.darthplagueis.contentvault.images;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class ImagesPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;

    public ImagesPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragmentList = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public float getPageWidth(int position) {
        if (position != 0) {
            return 0.8f;
        } else {
            return super.getPageWidth(position);
        }
    }
}
