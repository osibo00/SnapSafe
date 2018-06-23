package productions.darthplagueis.contentvault.photos.view;


import android.arch.lifecycle.Observer;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import productions.darthplagueis.contentvault.BR;
import productions.darthplagueis.contentvault.FragmentsActivity;
import productions.darthplagueis.contentvault.R;
import productions.darthplagueis.contentvault.data.UserContent;
import productions.darthplagueis.contentvault.databinding.PhotosFragmentBinding;
import productions.darthplagueis.contentvault.photos.UserContentViewModel;
import productions.darthplagueis.contentvault.photos.view.dialogs.CreateAlbumDialog;
import productions.darthplagueis.contentvault.photos.view.dialogs.DeleteDialog;
import productions.darthplagueis.contentvault.util.StatusBarColorUtil;
import productions.darthplagueis.contentvault.util.recyclerview.GridSpacingItemDecoration;


public class PhotosFragment extends Fragment {

    public final String DELETE_DIALOG_TAG = "DELETE_DIALOG_TAG";
    public final String NEW_ALBUM_TAG = "NEW_ALBUM_TAG";

    private UserContentAdapter contentAdapter;

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
        contentViewModel = FragmentsActivity.obtainContentViewModel(Objects.requireNonNull(getActivity()));
        photosFragmentBinding.setViewmodel(contentViewModel);
        photosFragmentBinding.photoActionBar.setVariable(BR.toolBarViewModel, contentViewModel);
        return photosFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        contentAdapter = new UserContentAdapter(contentViewModel);
        contentViewModel.getDescDateList().observe(this, contentAdapter::setUserContentList);

        contentViewModel.getNewMultiSelectionEvent().observe(this, aVoid -> {
            contentAdapter.enableMultiSelection();
            StatusBarColorUtil.setActionBarColor(getActivity(), true);
        });

        contentViewModel.getNewSelectionEnabledEvent().observe(this, aVoid ->
                StatusBarColorUtil.setActionBarColor(getActivity(), true)
        );

        contentViewModel.getNewSelectionDisabledEvent().observe(this, aVoid -> {
            contentAdapter.disableMultiSelection();
            StatusBarColorUtil.setActionBarColor(getActivity(), false);
        });

        contentViewModel.getNewDeletePromptEvent().observe(this, aVoid ->
                createDeleteDialog()
        );

        contentViewModel.getNewAlbumPromptEvent().observe(this, this::createNewAlbumDialog);

        contentViewModel.getNewSortingEvent().observe(this, aVoid -> createSortingPopup());

        RecyclerView recyclerView = photosFragmentBinding.galleryRecyclerView;
        recyclerView.setAdapter(contentAdapter);

//        recyclerView.setLayoutManager(new SpannedGridLayoutManager(
//                position -> {
//                    if (position == 3) {
//                        return new SpannedGridLayoutManager.SpanInfo(2, 2);
//                    } else if (position % 10 == 0) {
//                        return new SpannedGridLayoutManager.SpanInfo(2, 2);
//                    } else {
//                        return new SpannedGridLayoutManager.SpanInfo(1, 1);
//                    }
//                }, 4, 1f));

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(
                4, dpToPx(4), false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void createDeleteDialog() {
        DeleteDialog deleteDialog = DeleteDialog.newInstance(contentViewModel);
        deleteDialog.show(getChildFragmentManager(), DELETE_DIALOG_TAG);
    }

    private void createNewAlbumDialog(List<UserContent> userContents) {
        CreateAlbumDialog albumDialog = CreateAlbumDialog.newInstance(userContents);
        albumDialog.show(getChildFragmentManager(), NEW_ALBUM_TAG);
    }

    private void createSortingPopup() {
        PopupMenu popupMenu = new PopupMenu(
                getContext(), photosFragmentBinding.photoActionBar.sortBtn, Gravity.END);
        popupMenu.getMenuInflater().inflate(R.menu.sorting_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.ascending_order:
                    contentViewModel.getAscDateList().observe(
                            this, contentAdapter::setUserContentList);
                    return true;
                case R.id.descending_order:
                    contentViewModel.getDescDateList().observe(
                            this, contentAdapter::setUserContentList);
                    return true;
                case R.id.album_orderAZ:
                    contentViewModel.getAlbumOrderAZList().observe(
                            this, contentAdapter::setUserContentList);
                    return true;
                case R.id.album_orderZA:
                    contentViewModel.getAlbumOrderZAList().observe(
                            this, contentAdapter::setUserContentList);
                    return true;
                default:
                    return false;
            }
        });
        popupMenu.show();
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
