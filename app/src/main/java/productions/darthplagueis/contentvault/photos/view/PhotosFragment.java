package productions.darthplagueis.contentvault.photos.view;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import productions.darthplagueis.contentvault.FragmentsActivity;
import productions.darthplagueis.contentvault.R;
import productions.darthplagueis.contentvault.databinding.PhotosFragmentBinding;
import productions.darthplagueis.contentvault.photos.UserContentViewModel;
import productions.darthplagueis.contentvault.util.recyclerview.GridSpacingItemDecoration;
import productions.darthplagueis.contentvault.util.recyclerview.SpannedGridLayoutManager;


public class PhotosFragment extends Fragment {

    private UserContentViewModel contentViewModel;

    private PhotosFragmentBinding photosFragmentBinding;

    public PhotosFragment() {
        // Required empty public constructor
    }

    public static PhotosFragment newInstance() {
        return new PhotosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        photosFragmentBinding = PhotosFragmentBinding.inflate(inflater, container, false);
        contentViewModel = FragmentsActivity.obtainViewModel(Objects.requireNonNull(getActivity()));
        photosFragmentBinding.setViewmodel(contentViewModel);

        Toolbar toolbar = photosFragmentBinding.galleryToolbar;
        inflater.inflate(R.layout.photo_action_toolbar, toolbar);

        return photosFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        UserContentAdapter contentAdapter = new UserContentAdapter(contentViewModel);
        contentViewModel.getDescDateList().observe(this, contentAdapter::setUserContentList);
        contentViewModel.getNewMultiSelectionEvent().observe(this, aVoid ->
                contentAdapter.enableMultiSelection());

        RecyclerView recyclerView = photosFragmentBinding.galleryRecyclerView;
        recyclerView.setAdapter(contentAdapter);
        recyclerView.setLayoutManager(new SpannedGridLayoutManager(
                position -> {
                    if (position == 0) {
                        return new SpannedGridLayoutManager.SpanInfo(2, 2);
                    } else if (position % 10 == 0) {
                        return new SpannedGridLayoutManager.SpanInfo(2, 2);
                    } else {
                        return new SpannedGridLayoutManager.SpanInfo(1, 1);
                    }
                }, 3, 1f));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(
                3, dpToPx(4), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
