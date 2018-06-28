package productions.darthplagueis.contentvault.scrollingphotos.view;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

import productions.darthplagueis.contentvault.databinding.ScrollingPhotoFragmentBinding;


public class ScrollingPhotoFragment extends Fragment {

    public static final String ARGUMENT_SCROLLING_TAG = "ARGUMENT_SCROLLING_TAG";

    private ScrollingPhotoFragmentBinding scrollingFragmentBinding;

    public static ScrollingPhotoFragment newInstance(String filePath) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_SCROLLING_TAG, filePath);
        ScrollingPhotoFragment fragment = new ScrollingPhotoFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public ScrollingPhotoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        scrollingFragmentBinding =
                ScrollingPhotoFragmentBinding.inflate(inflater, container, false);

        return scrollingFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String photoFilePath = getArguments() != null ? getArguments().getString(ARGUMENT_SCROLLING_TAG) : null;
        if (photoFilePath != null) {
            Glide.with(scrollingFragmentBinding.getRoot())
                    .load(new File(photoFilePath))
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(scrollingFragmentBinding.scrollingPhotoImageView);
        }
    }
}
