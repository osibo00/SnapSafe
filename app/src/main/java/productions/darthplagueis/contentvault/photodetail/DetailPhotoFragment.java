package productions.darthplagueis.contentvault.photodetail;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.io.File;

import productions.darthplagueis.contentvault.BR;
import productions.darthplagueis.contentvault.FragmentsActivity;
import productions.darthplagueis.contentvault.databinding.PhotoDetailFragmentBinding;


public class DetailPhotoFragment extends Fragment {

    public static final String ARGUMENT_DETAIL_TAG = "ARGUMENT_DETAIL";

    private DetailPhotoViewModel detailViewModel;

    private PhotoDetailFragmentBinding detailFragmentBinding;

    public DetailPhotoFragment() {
        // Required empty public constructor
    }

    public static DetailPhotoFragment newInstance(@NonNull String filePath) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_DETAIL_TAG, filePath);
        DetailPhotoFragment fragment = new DetailPhotoFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        detailFragmentBinding = PhotoDetailFragmentBinding.inflate(
                inflater, container, false);
        setImmersiveMode();
        detailViewModel = FragmentsActivity.obtainDetailViewModel(getActivity());
        detailFragmentBinding.setVariable(BR.detailViewModel, detailViewModel);

        return detailFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String photoFilePath = getArguments() != null ? getArguments().getString(ARGUMENT_DETAIL_TAG) : null;
        if (photoFilePath != null) {
            Glide.with(detailFragmentBinding.getRoot())
                    .load(new File(photoFilePath))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(detailFragmentBinding.detailImageView);
        }
    }

    private void setImmersiveMode() {
        detailFragmentBinding.getRoot().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }
}
