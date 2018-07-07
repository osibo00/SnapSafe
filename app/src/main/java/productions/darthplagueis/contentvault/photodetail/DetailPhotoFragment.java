package productions.darthplagueis.contentvault.photodetail;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.io.File;
import java.util.ArrayList;

import productions.darthplagueis.contentvault.BR;
import productions.darthplagueis.contentvault.FragmentsActivity;
import productions.darthplagueis.contentvault.data.UserContent;
import productions.darthplagueis.contentvault.databinding.PhotoDetailFragmentBinding;
import productions.darthplagueis.contentvault.scrollingphotos.ScrollingPageAdapter;
import productions.darthplagueis.contentvault.scrollingphotos.ScrollingPhotoViewModel;


public class DetailPhotoFragment extends Fragment {

    public static final String ARGUMENT_DETAIL_TAG = "ARGUMENT_DETAIL";
    public static final String ARGUMENT_LIST_TAG = "ARGUMENT_LIST";

    private ScrollingPhotoViewModel photoViewModel;

    private PhotoDetailFragmentBinding detailFragmentBinding;

    public DetailPhotoFragment() {
        // Required empty public constructor
    }

    public static DetailPhotoFragment newInstance(int position, ArrayList<UserContent> userContents) {
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_DETAIL_TAG, position);
        arguments.putParcelableArrayList(ARGUMENT_LIST_TAG, userContents);
        DetailPhotoFragment fragment = new DetailPhotoFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        setSharedElementReturnTransition(null);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        detailFragmentBinding = PhotoDetailFragmentBinding.inflate(
                inflater, container, false);
        //setImmersiveMode();
        photoViewModel = FragmentsActivity.obtainScrollingViewModel(getActivity());
        detailFragmentBinding.setVariable(BR.detailViewModel, photoViewModel);

        return detailFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int photoFilePath = getArguments().getInt(ARGUMENT_DETAIL_TAG);
        ArrayList<UserContent> userContents = getArguments().getParcelableArrayList(ARGUMENT_LIST_TAG);
        ScrollingPageAdapter pageAdapter = new ScrollingPageAdapter(getChildFragmentManager(), photoViewModel, userContents);
        detailFragmentBinding.scrollingViewPager.setAdapter(pageAdapter);
        detailFragmentBinding.scrollingViewPager.setCurrentItem(photoFilePath);
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
