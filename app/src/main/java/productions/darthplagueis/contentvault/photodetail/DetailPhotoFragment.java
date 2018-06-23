package productions.darthplagueis.contentvault.photodetail;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.io.File;

import productions.darthplagueis.contentvault.BR;
import productions.darthplagueis.contentvault.FragmentsActivity;
import productions.darthplagueis.contentvault.databinding.PhotoDetailFragmentBinding;


public class DetailPhotoFragment extends Fragment {

    private String photoDetailFilePath;

    private PhotoDetailFragmentBinding detailFragmentBinding;

    public DetailPhotoFragment() {
        // Required empty public constructor
    }

    public static DetailPhotoFragment newInstance(@NonNull String filePath) {
        DetailPhotoFragment fragment = new DetailPhotoFragment();
        fragment.photoDetailFilePath = filePath;
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        detailFragmentBinding =
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DetailPhotoViewModel detailViewModel =
                FragmentsActivity.obtainDetailViewModel(getActivity());
        detailFragmentBinding.setVariable(BR.detailViewModel, detailViewModel);
    }
}
