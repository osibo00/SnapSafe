package productions.darthplagueis.contentvault;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import productions.darthplagueis.contentvault.photoalbums.ContentFolderViewModel;
import productions.darthplagueis.contentvault.photoalbums.view.FoldersFragment;
import productions.darthplagueis.contentvault.photodetail.DetailPhotoViewModel;
import productions.darthplagueis.contentvault.photodetail.DetailPhotoFragment;
import productions.darthplagueis.contentvault.photos.view.PhotosFragment;
import productions.darthplagueis.contentvault.photos.UserContentViewModel;
import productions.darthplagueis.contentvault.util.app.ActivityUtil;
import productions.darthplagueis.contentvault.util.theme.BottomNavigationViewUtil;
import productions.darthplagueis.contentvault.util.theme.ThemeUtil;

public class FragmentsActivity extends AppCompatActivity implements ActivityNavigator {

    public static final String ACTION_PICK_PHOTOS_TAG = "ACTION_PICK_PHOTOS_TAG";
    public static final int PICK_IMAGE_CODE_TAG = 1987;

    private PhotosFragment photosFragment;
    private FoldersFragment foldersFragment;

    private BottomNavigationView navigation;

    private UserContentViewModel contentViewModel;

    private boolean isNavigationHidden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_fragments);

        navigation = findViewById(R.id.bottom_navigation);
        BottomNavigationViewUtil.disableShiftMode(navigation);
        setNavigationListener();

        photosFragment =
                (PhotosFragment) getSupportFragmentManager().findFragmentByTag("PHOTOS_TAG");
        foldersFragment =
                (FoldersFragment) getSupportFragmentManager().findFragmentByTag("ALBUMS_TAG");

        setupPhotosFragment();

        contentViewModel = obtainContentViewModel(this);
        contentViewModel.getNewPhotoImportEvent().observe(this, aVoid -> createImportIntent());
        contentViewModel.getNewCarouselEvent().observe(this, aVoid -> createCarouselView());
        contentViewModel.getNewPhotoDetailEvent().observe(this, this::createDetailedView);

        DetailPhotoViewModel detailViewModel = obtainDetailViewModel(this);
        detailViewModel.getNewBackButtonEvent().observe(this, aVoid -> onBackPressed());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        contentViewModel.handleActivityResult(requestCode, data);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            setNavigationVisibility();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void createImportIntent() {
        Intent importPhotosIntent = new Intent(Intent.ACTION_PICK);
        importPhotosIntent.setType("image/*");
        importPhotosIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(
                importPhotosIntent, ACTION_PICK_PHOTOS_TAG), PICK_IMAGE_CODE_TAG);
    }

    @Override
    public void createDetailedView(String filePath) {
        setNavigationVisibility();
        ActivityUtil.addFragmentInActivity(getSupportFragmentManager(),
                DetailPhotoFragment.newInstance(filePath), R.id.contentFrame);
    }

    @Override
    public void createCarouselView() {
        startActivity(new Intent(this, ScrollGalleryActivity.class));
    }

    public static UserContentViewModel obtainContentViewModel(FragmentActivity activity) {
        return ViewModelProviders.of(activity, getViewModelFactory(activity))
                .get(UserContentViewModel.class);
    }

    public static DetailPhotoViewModel obtainDetailViewModel(FragmentActivity activity) {
        return ViewModelProviders.of(activity, getViewModelFactory(activity))
                .get(DetailPhotoViewModel.class);
    }

    public static ContentFolderViewModel obtainAlbumViewModel(FragmentActivity activity) {
        return ViewModelProviders.of(activity, getViewModelFactory(activity))
                .get(ContentFolderViewModel.class);
    }

    private static ViewModelFactory getViewModelFactory(FragmentActivity activity) {
        return ViewModelFactory.getINSTANCE(activity.getApplication());
    }

    private void setNavigationVisibility() {
        if (!isNavigationHidden) {
            isNavigationHidden = true;
            navigation.setVisibility(View.GONE);
        } else {
            isNavigationHidden = false;
            navigation.setVisibility(View.VISIBLE);
        }
    }

    private void setupPhotosFragment() {
        if (photosFragment == null) {
            photosFragment = PhotosFragment.newInstance();
        }
        ActivityUtil.replaceFragmentInActivity(
                getSupportFragmentManager(), photosFragment, R.id.contentFrame);
    }

    private void setupAlbumsFragment() {
        if (foldersFragment == null) {
            foldersFragment = FoldersFragment.newInstance();
        }
        ActivityUtil.replaceFragmentInActivity(
                getSupportFragmentManager(), foldersFragment, R.id.contentFrame);
    }

    private void setNavigationListener() {
        BottomNavigationView.OnNavigationItemSelectedListener listener = item -> {
            switch (item.getItemId()) {
                case R.id.navigation_photos:
                    setupPhotosFragment();
                    return true;
                case R.id.navigation_albums:
                    setupAlbumsFragment();
                    return true;
                case R.id.navigation_favorites:

                    return true;
                case R.id.navigation_search:

                    return true;
            }
            return false;
        };
        navigation.setOnNavigationItemSelectedListener(listener);
    }
}
