package productions.darthplagueis.contentvault.photos.view;


import android.arch.lifecycle.Observer;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import productions.darthplagueis.contentvault.BR;
import productions.darthplagueis.contentvault.FragmentsActivity;
import productions.darthplagueis.contentvault.R;
import productions.darthplagueis.contentvault.data.UserContent;
import productions.darthplagueis.contentvault.databinding.PhotosFragmentBinding;
import productions.darthplagueis.contentvault.photos.LayoutManagerType;
import productions.darthplagueis.contentvault.photos.UserContentViewModel;
import productions.darthplagueis.contentvault.photos.view.dialogs.CopyDialog;
import productions.darthplagueis.contentvault.photos.view.dialogs.CreateAlbumDialog;
import productions.darthplagueis.contentvault.photos.view.dialogs.DeleteDialog;
import productions.darthplagueis.contentvault.util.DimensionsUtil;
import productions.darthplagueis.contentvault.util.StatusBarColorUtil;
import productions.darthplagueis.contentvault.util.recyclerview.GridSpacingItemDecoration;


public class PhotosFragment extends Fragment {

    public static final String DELETE_DIALOG_TAG = "DELETE_DIALOG_TAG";
    public static final String NEW_ALBUM_TAG = "NEW_ALBUM_TAG";
    public static final String COPY_DIALOG_TAG = "COPY_DIALOG_TAG";

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
                StatusBarColorUtil.setActionBarColor(getActivity(), true));

        contentViewModel.getNewSelectionDisabledEvent().observe(this, aVoid -> {
            contentAdapter.disableMultiSelection();
            StatusBarColorUtil.setActionBarColor(getActivity(), false);
        });

        contentViewModel.getNewDeletePromptEvent().observe(this, aVoid -> createDeleteDialog());

        contentViewModel.getNewCopyPromptEvent().observe(this, aVoid -> createCopyDialog());

        contentViewModel.getNewAlbumPromptEvent().observe(this, this::createNewAlbumDialog);

        contentViewModel.getNewSortingEvent().observe(this, aVoid -> createSortingPopup());

        contentViewModel.getNewRecyclerViewLayoutEvent().observe(this, aVoid ->
                createFilteringPopup());

        photosFragmentBinding.galleryRecyclerView.setAdapter(contentAdapter);
        setRecyclerViewLayoutManager(LayoutManagerType.GRID_LAYOUT_MANAGER_SPAN_FOUR);
    }

    private void createDeleteDialog() {
        DeleteDialog deleteDialog = DeleteDialog.newInstance(contentViewModel);
        deleteDialog.show(getChildFragmentManager(), DELETE_DIALOG_TAG);
    }

    private void createCopyDialog() {
        CopyDialog copyDialog = CopyDialog.newInstance(contentViewModel);
        copyDialog.show(getChildFragmentManager(), COPY_DIALOG_TAG);
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

    private void createFilteringPopup() {
        PopupMenu popupMenu = new PopupMenu(
                getContext(), photosFragmentBinding.photoActionBar.filterBtn, Gravity.END);
        popupMenu.getMenuInflater().inflate(R.menu.filter_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.grid_span_two:
                    setRecyclerViewLayoutManager(LayoutManagerType.GRID_LAYOUT_MANAGER_SPAN_TWO);
                    return true;
                case R.id.grid_span_three:
                    setRecyclerViewLayoutManager(LayoutManagerType.GRID_LAYOUT_MANAGER_SPAN_THREE);
                    return true;
                case R.id.grid_span_four:
                    setRecyclerViewLayoutManager(LayoutManagerType.GRID_LAYOUT_MANAGER_SPAN_FOUR);
                    return true;
                case R.id.grid_span_five:
                    setRecyclerViewLayoutManager(LayoutManagerType.GRID_LAYOUT_MANAGER_SPAN_FIVE);
                    return true;
                default:
                    return false;
            }
        });
        popupMenu.show();
    }

    private void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;
        RecyclerView recyclerView = photosFragmentBinding.galleryRecyclerView;
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((GridLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        RecyclerView.LayoutManager layoutManager;
        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER_SPAN_TWO:
                layoutManager = new GridLayoutManager(getContext(), 2);
                setItemDecoration(recyclerView, 2, 0, false);
                break;
            case GRID_LAYOUT_MANAGER_SPAN_THREE:
                layoutManager = new GridLayoutManager(getContext(), 3);
                setItemDecoration(recyclerView, 3, 0, false);
                break;
            case GRID_LAYOUT_MANAGER_SPAN_FOUR:
                layoutManager = new GridLayoutManager(getContext(), 4);
                setItemDecoration(recyclerView, 4, 4, false);
                break;
            case GRID_LAYOUT_MANAGER_SPAN_FIVE:
                layoutManager = new GridLayoutManager(getContext(), 5);
                setItemDecoration(recyclerView, 5, 0, false);
                break;
            default:
                layoutManager = new GridLayoutManager(getContext(), 4);
                setItemDecoration(recyclerView, 4, 4, false);
                break;
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(scrollPosition);
    }

    private void setItemDecoration(RecyclerView rV, int spanCount, int spacing, boolean includesEdge) {
        rV.addItemDecoration(
                new GridSpacingItemDecoration(
                        spanCount, DimensionsUtil.dpToPx(getContext(), spacing), includesEdge));
    }
}
