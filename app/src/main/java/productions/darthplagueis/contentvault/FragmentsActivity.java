package productions.darthplagueis.contentvault;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import productions.darthplagueis.contentvault.data.UserContent;
import productions.darthplagueis.contentvault.imagefolders.ContentFolderViewModel;
import productions.darthplagueis.contentvault.imagefolders.view.FoldersFragment;
import productions.darthplagueis.contentvault.images.ImagesViewPagerFragment;
import productions.darthplagueis.contentvault.photodetail.DetailPhotoViewModel;
import productions.darthplagueis.contentvault.photodetail.DetailPhotoFragment;
import productions.darthplagueis.contentvault.images.view.ImagesFragment;
import productions.darthplagueis.contentvault.images.UserContentViewModel;
import productions.darthplagueis.contentvault.scrollingphotos.ScrollingPhotoViewModel;
import productions.darthplagueis.contentvault.util.app.ActivityUtil;
import productions.darthplagueis.contentvault.util.app.PermissionsUtil;
import productions.darthplagueis.contentvault.util.theme.BottomNavigationViewUtil;
import productions.darthplagueis.contentvault.util.theme.ThemeUtil;

public class FragmentsActivity extends AppCompatActivity implements ActivityNavigator {

    public static final String ACTION_PICK_PHOTOS_TAG = "Open with...";
    public static final String USER_CONTENT_ID = "USER_CONTENT_ID";
    public static final String CONTENT_IMAGE_TRANSITION_NAME = "CONTENT_IMAGE_TRANSITION_NAME";
    public static final int PICK_IMAGE_CODE_REQUEST = 1987;

    private ImagesFragment imagesFragment;
    private FoldersFragment foldersFragment;

    private BottomNavigationView navigation;

    private UserContentViewModel contentViewModel;

    private boolean isNavigationHidden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_fragments);

        PermissionsUtil.requestWriteExternalStorage(this);

        navigation = findViewById(R.id.bottom_navigation);
        BottomNavigationViewUtil.disableShiftMode(navigation);
        setNavigationListener();

        imagesFragment =
                (ImagesFragment) getSupportFragmentManager().findFragmentByTag("PHOTOS_TAG");
        foldersFragment =
                (FoldersFragment) getSupportFragmentManager().findFragmentByTag("ALBUMS_TAG");

        //ActivityUtil.replaceFragmentInActivity(getSupportFragmentManager(), new ImagesViewPagerFragment(), R.id.contentFrame);

        setupPhotosFragment();

        contentViewModel = obtainContentViewModel(this);
        contentViewModel.getNewPhotoImportEvent().observe(this, aVoid -> createImportIntent());
        contentViewModel.getNewCarouselEvent().observe(this, aVoid -> createCarouselView());
        contentViewModel.getNewPhotoDetailEvent().observe(this, this::createDetailedView);

        DetailPhotoViewModel detailViewModel = obtainDetailViewModel(this);
        detailViewModel.getNewBackButtonEvent().observe(this, aVoid -> onBackPressed());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionsUtil.WRITE_EXTERNAL_STORAGE_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "SnapSafe needs this permission granted to function properly.", Toast.LENGTH_LONG).show();
            }
        }
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
                importPhotosIntent, ACTION_PICK_PHOTOS_TAG), PICK_IMAGE_CODE_REQUEST);
    }

    @Override
    public void createDetailedView(SparseArray<Map<ImageView, List<UserContent>>> viewSparseArray) {
        for (Map.Entry<ImageView, List<UserContent>> entry : viewSparseArray.valueAt(0).entrySet()) {
            ArrayList<UserContent> contentArrayList = Lists.newArrayList(entry.getValue());
            DetailPhotoFragment detailPhotoFragment = DetailPhotoFragment.newInstance(viewSparseArray.keyAt(0), contentArrayList);
            getSupportFragmentManager().beginTransaction()
                    .addSharedElement(entry.getKey(), ViewCompat.getTransitionName(entry.getKey()))
                    .addToBackStack(imagesFragment.getClass().getSimpleName())
                    .replace(R.id.contentFrame, detailPhotoFragment)
                    .commit();
            setNavigationVisibility();
        }
//        Intent scrollingGalleryIntent = new Intent(this, ScrollGalleryActivity.class);
//        scrollingGalleryIntent.putExtra(USER_CONTENT_ID, viewSparseArray.keyAt(0));
//        scrollingGalleryIntent.putExtra(
//                CONTENT_IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(contentView));
//
//        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                this, contentView, ViewCompat.getTransitionName(contentView));
//
//        startActivity(scrollingGalleryIntent, optionsCompat.toBundle());
    }

    @Override
    public void createCarouselView() {


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

    public static ScrollingPhotoViewModel obtainScrollingViewModel(FragmentActivity activity) {
        return ViewModelProviders.of(activity, getViewModelFactory(activity))
                .get(ScrollingPhotoViewModel.class);
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
        if (imagesFragment == null) {
            imagesFragment = ImagesFragment.newInstance();
        }
        ActivityUtil.replaceFragmentInActivity(
                getSupportFragmentManager(), imagesFragment, R.id.contentFrame);
    }

    private void setupAlbumsFragment() {
        if (foldersFragment == null) {
            foldersFragment = FoldersFragment.newInstance();
        }
        ActivityUtil.replaceFragmentBackStack(
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
