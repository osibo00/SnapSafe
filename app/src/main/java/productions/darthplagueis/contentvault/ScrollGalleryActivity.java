package productions.darthplagueis.contentvault;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import productions.darthplagueis.contentvault.data.UserContent;
import productions.darthplagueis.contentvault.scrollingphotos.ScrollingContentNavigator;
import productions.darthplagueis.contentvault.scrollingphotos.view.ScrollingPageAdapter;
import productions.darthplagueis.contentvault.scrollingphotos.ScrollingPhotoViewModel;

public class ScrollGalleryActivity extends AppCompatActivity implements ScrollingContentNavigator {

    private String string;
    private ViewPager scrollingViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_gallery);

        Intent extras = getIntent();
        if (extras != null) string = extras.getStringExtra("testing");

        scrollingViewPager = findViewById(R.id.scrolling_viewPager);

        ScrollingPhotoViewModel scrollingViewModel = obtainScrollingViewModel(this);
        scrollingViewModel.getNewContentListEvent().observe(this, this::onContentScroll);
        scrollingViewModel.getUserContentList();
    }

    public static ScrollingPhotoViewModel obtainScrollingViewModel(FragmentActivity activity) {
        return ViewModelProviders.of(activity, getViewModelFactory(activity))
                .get(ScrollingPhotoViewModel.class);
    }

    private static ViewModelFactory getViewModelFactory(FragmentActivity activity) {
        return ViewModelFactory.getINSTANCE(activity.getApplication());
    }

    @Override
    public void onContentScroll(List<UserContent> userContents) {
        ScrollingPageAdapter scrollingPageAdapter =
                new ScrollingPageAdapter(getSupportFragmentManager(), userContents);
        scrollingViewPager.setAdapter(scrollingPageAdapter);
        if (string != null) scrollingPageAdapter.lookUpContentItem(string);
    }
}
