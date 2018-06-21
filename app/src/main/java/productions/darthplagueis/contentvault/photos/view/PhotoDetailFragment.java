package productions.darthplagueis.contentvault.photos.view;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

import productions.darthplagueis.contentvault.R;
import productions.darthplagueis.contentvault.databinding.PhotoDetailFragmentBinding;

import static com.google.common.base.Preconditions.checkNotNull;


public class PhotoDetailFragment extends Fragment {

    private String photoDetailFilePath;

    public PhotoDetailFragment() {
        // Required empty public constructor
    }

    public static PhotoDetailFragment newInstance(@NonNull String filePath) {
        PhotoDetailFragment fragment = new PhotoDetailFragment();
        fragment.photoDetailFilePath = filePath;
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        PhotoDetailFragmentBinding detailFragmentBinding =
                PhotoDetailFragmentBinding.inflate(inflater, container, false);

        detailFragmentBinding.getRoot().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        Glide.with(detailFragmentBinding.getRoot())
                .load(new File(photoDetailFilePath))
                .into(detailFragmentBinding.detailImageView);

        return detailFragmentBinding.getRoot();
    }
}
