package productions.darthplagueis.contentvault;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import productions.darthplagueis.contentvault.photoalbums.view.AlbumsFragment;
import productions.darthplagueis.contentvault.photoalbums.ContentAlbumViewModel;
import productions.darthplagueis.contentvault.photos.ContentItemNavigator;
import productions.darthplagueis.contentvault.photodetail.DetailPhotoViewModel;
import productions.darthplagueis.contentvault.photodetail.DetailPhotoFragment;
import productions.darthplagueis.contentvault.photos.view.PhotosFragment;
import productions.darthplagueis.contentvault.photos.UserContentViewModel;
import productions.darthplagueis.contentvault.util.ActivityUtil;
import productions.darthplagueis.contentvault.util.BottomNavigationViewUtil;

public class FragmentsActivity extends AppCompatActivity implements ContentItemNavigator {

    public static final String ACTION_PICK_PHOTOS_TAG = "ACTION_PICK_PHOTOS_TAG";
    public static final int PICK_IMAGE_CODE_TAG = 1987;

    private PhotosFragment photosFragment;
    private AlbumsFragment albumsFragment;

    private BottomNavigationView navigation;

    private UserContentViewModel contentViewModel;

    private boolean isNavigationHidden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragments);

        navigation = findViewById(R.id.bottom_navigation);
        BottomNavigationViewUtil.disableShiftMode(navigation);
        setNavigationListener();

        photosFragment =
                (PhotosFragment) getSupportFragmentManager().findFragmentByTag("PHOTOS_TAG");
        albumsFragment =
                (AlbumsFragment) getSupportFragmentManager().findFragmentByTag("ALBUMS_TAG");

        setupPhotosFragment();

        contentViewModel = obtainContentViewModel(this);
        contentViewModel.getNewPhotoImportEvent().observe(this, aVoid -> importPhotos());
        contentViewModel.getNewPhotoDetailEvent().observe(this, s -> {
            if (s != null) openPhotoDetails(s);
        });
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
    public void openPhotoDetails(String filePath) {
//        setNavigationVisibility();
//        ActivityUtil.addFragmentInActivity(getSupportFragmentManager(),
//                DetailPhotoFragment.newInstance(filePath), R.id.contentFrame);
        Intent intent = new Intent(this, ScrollGalleryActivity.class);
        intent.putExtra("testing", filePath);
        startActivity(intent);
    }

    public static UserContentViewModel obtainContentViewModel(FragmentActivity activity) {
        return ViewModelProviders.of(activity, getViewModelFactory(activity))
                .get(UserContentViewModel.class);
    }

    public static DetailPhotoViewModel obtainDetailViewModel(FragmentActivity activity) {
        return ViewModelProviders.of(activity, getViewModelFactory(activity))
                .get(DetailPhotoViewModel.class);
    }

    public static ContentAlbumViewModel obtainAlbumViewModel(FragmentActivity activity) {
        return ViewModelProviders.of(activity, getViewModelFactory(activity))
                .get(ContentAlbumViewModel.class);
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
        if (albumsFragment == null) {
            albumsFragment = AlbumsFragment.newInstance();
        }
        ActivityUtil.replaceFragmentInActivity(
                getSupportFragmentManager(), albumsFragment, R.id.contentFrame);
    }

    private void importPhotos() {
        Intent importPhotosIntent = new Intent(Intent.ACTION_PICK);
        importPhotosIntent.setType("image/*");
        importPhotosIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(importPhotosIntent, ACTION_PICK_PHOTOS_TAG), PICK_IMAGE_CODE_TAG);
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
