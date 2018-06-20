package productions.darthplagueis.contentvault;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import productions.darthplagueis.contentvault.photos.view.PhotosFragment;
import productions.darthplagueis.contentvault.photos.UserContentViewModel;
import productions.darthplagueis.contentvault.util.ActivityUtil;
import productions.darthplagueis.contentvault.util.BottomNavigationViewUtil;

public class FragmentsActivity extends AppCompatActivity {

    public static final String ACTION_PICK_PHOTOS_TAG = "ACTION_PICK_PHOTOS_TAG";
    public static final int PICK_IMAGE_CODE_TAG = 1987;

    private BottomNavigationView navigation;

    private UserContentViewModel viewModel;

    private boolean isNavigationHidden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragments);

        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }

        navigation = findViewById(R.id.bottom_navigation);
        BottomNavigationViewUtil.disableShiftMode(navigation);
        setNavigationListener();

        setupPhotosFragment();

        viewModel = obtainViewModel(this);
        viewModel.getNewPhotoImportEvent().observe(this, aVoid -> importPhotos());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        viewModel.handleActivityResult(requestCode, data);
    }

    public static UserContentViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getINSTANCE(activity.getApplication());
        UserContentViewModel viewModel =
                ViewModelProviders.of(activity, factory).get(UserContentViewModel.class);
        return viewModel;
    }

    private void setupPhotosFragment() {
        PhotosFragment photosFragment =
                (PhotosFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (photosFragment == null) {
            photosFragment = PhotosFragment.newInstance();
            ActivityUtil.replaceFragmentInActivity(
                    getSupportFragmentManager(), photosFragment, R.id.contentFrame);
        }
    }

    private void setNavigationListener() {
        BottomNavigationView.OnNavigationItemSelectedListener listener = item -> {
            switch (item.getItemId()) {
                case R.id.navigation_photos:
                    setupPhotosFragment();
                    return true;
                case R.id.navigation_albums:

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

    private void importPhotos() {
        Intent importPhotosIntent = new Intent(Intent.ACTION_PICK);
        importPhotosIntent.setType("image/*");
        importPhotosIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(importPhotosIntent, ACTION_PICK_PHOTOS_TAG), PICK_IMAGE_CODE_TAG);
    }
}
