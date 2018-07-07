package productions.darthplagueis.contentvault.scrollingphotos.view;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;

import productions.darthplagueis.contentvault.data.UserContent;
import productions.darthplagueis.contentvault.databinding.ScrollingPhotoFragmentBinding;
import productions.darthplagueis.contentvault.util.ZoomableImageView;


public class ScrollingPhotoFragment extends Fragment {

    public static final String ARGUMENT_SCROLLING_TAG = "ARGUMENT_SCROLLING";
    public static final String VIEW_TRANSITION_NAME = "VIEW_TRANSITION_NAME";

    private ScrollingPhotoFragmentBinding photoFragmentBinding;

    public static ScrollingPhotoFragment newInstance(UserContent userContent, String transitionName) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(ARGUMENT_SCROLLING_TAG, userContent);
        arguments.putString(VIEW_TRANSITION_NAME, transitionName);
        ScrollingPhotoFragment fragment = new ScrollingPhotoFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public ScrollingPhotoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        photoFragmentBinding =
                ScrollingPhotoFragmentBinding.inflate(inflater, container, false);

        return photoFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        UserContent userContent = getArguments() != null ? getArguments().getParcelable(ARGUMENT_SCROLLING_TAG) : null;
        String viewTransitionName = getArguments() != null ? getArguments().getString(VIEW_TRANSITION_NAME) : null;
        if (viewTransitionName != null) photoFragmentBinding.scrollingPhotoImageView.setTransitionName(viewTransitionName);
        if (userContent != null) {
            Glide.with(photoFragmentBinding.getRoot())
                    .asBitmap()
                    .load(new File(userContent.getFilePath()))
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            startPostponedEnterTransition();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            photoFragmentBinding.scrollingPhotoImageView.setImageBitmap(resource);
                            startPostponedEnterTransition();
                        }
                    });
        }
    }
}
