package productions.darthplagueis.contentvault;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.List;

import productions.darthplagueis.contentvault.data.UserContent;
import productions.darthplagueis.contentvault.scrollingphotos.ScrollingContentNavigator;
import productions.darthplagueis.contentvault.scrollingphotos.ScrollingPageAdapter;
import productions.darthplagueis.contentvault.scrollingphotos.ScrollingPhotoViewModel;
import productions.darthplagueis.contentvault.scrollingphotos.view.ScrollingItemFragment;
import productions.darthplagueis.contentvault.util.app.ActivityUtil;
import productions.darthplagueis.contentvault.util.theme.ThemeUtil;

public class ScrollGalleryActivity extends AppCompatActivity implements ScrollingContentNavigator {

    private String viewTransitionName;
    private int contentId;

    private ViewPager scrollingViewPager;

    private ScrollingItemFragment itemFragment;

    private ScrollingPhotoViewModel photoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_scroll_gallery);
        supportPostponeEnterTransition();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            viewTransitionName = extras.getString(FragmentsActivity.CONTENT_IMAGE_TRANSITION_NAME);
            contentId = extras.getInt(FragmentsActivity.USER_CONTENT_ID);
        }

        Toolbar toolbar = findViewById(R.id.scroll_gallery_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        scrollingViewPager = findViewById(R.id.scrolling_viewPager);

        photoViewModel = obtainScrollingViewModel(this);
        photoViewModel.getNewContentListEvent().observe(this, this::onContentRetrieved);
        photoViewModel.getNewItemAdapterPositionEvent().observe(this, this::onSetViewPagerItem);
        photoViewModel.getUserContentList();
    }

    public static ScrollingPhotoViewModel obtainScrollingViewModel(FragmentActivity activity) {
        return ViewModelProviders.of(activity, getViewModelFactory(activity))
                .get(ScrollingPhotoViewModel.class);
    }

    private static ViewModelFactory getViewModelFactory(FragmentActivity activity) {
        return ViewModelFactory.getINSTANCE(activity.getApplication());
    }

    @Override
    public void onContentRetrieved(List<UserContent> userContents) {
//        ScrollingPageAdapter scrollingPageAdapter =
//                new ScrollingPageAdapter(getSupportFragmentManager(), photoViewModel,
//                                userContents, viewTransitionName);
//        scrollingViewPager.setAdapter(scrollingPageAdapter);
//        for (int i = 0; i < userContents.size(); i++) {
//            if (userContents.get(i).getId() == contentId) {
//                scrollingViewPager.setCurrentItem(i);
//            }
//        }
        //setupScrollingItemFragment(userContents);
    }

    @Override
    public void onSetViewPagerItem(int position) {
        scrollingViewPager.setCurrentItem(position, true);
    }

    private void setupScrollingItemFragment(List<UserContent> userContents) {
        itemFragment =
                (ScrollingItemFragment) getSupportFragmentManager().findFragmentByTag("SCROLLING_TAG");
        if (itemFragment == null) {
            itemFragment = ScrollingItemFragment.newInstance(userContents);
            ActivityUtil.slideFragmentInActivity(
                    getSupportFragmentManager(), itemFragment, R.id.contentFrame);
        }
    }
}
