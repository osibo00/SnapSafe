package productions.darthplagueis.contentvault.photos.view;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import productions.darthplagueis.contentvault.BR;
import productions.darthplagueis.contentvault.FragmentsActivity;
import productions.darthplagueis.contentvault.R;
import productions.darthplagueis.contentvault.data.UserContent;
import productions.darthplagueis.contentvault.databinding.PhotosFragmentBinding;
import productions.darthplagueis.contentvault.photos.UserContentAdapter;
import productions.darthplagueis.contentvault.util.recyclerview.LayoutManagerType;
import productions.darthplagueis.contentvault.photos.UserContentViewModel;
import productions.darthplagueis.contentvault.photos.view.dialogs.CopyDialog;
import productions.darthplagueis.contentvault.photoalbums.view.dialogs.CreateAlbumDialog;
import productions.darthplagueis.contentvault.photos.view.dialogs.DeleteDialog;
import productions.darthplagueis.contentvault.util.DimensionsUtil;
import productions.darthplagueis.contentvault.util.StatusBarColorUtil;
import productions.darthplagueis.contentvault.util.recyclerview.GridSpacingItemDecoration;


public class PhotosFragment extends Fragment {

    public static final String DELETE_DIALOG_TAG = "DELETE_DIALOG_TAG";
    public static final String NEW_ALBUM_TAG = "NEW_ALBUM_TAG";
    public static final String COPY_DIALOG_TAG = "COPY_DIALOG_TAG";

    public static PhotosFragment newInstance() {
        return new PhotosFragment();
    }

    private UserContentAdapter contentAdapter;

    private GridSpacingItemDecoration itemDecoration = null;
    private LayoutManagerType layoutManagerType = LayoutManagerType.GRID_SPAN_FOUR;

    private UserContentViewModel contentViewModel;

    private PhotosFragmentBinding photosFragmentBinding;

    private Context context;

    public PhotosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        photosFragmentBinding = PhotosFragmentBinding.inflate(inflater, container, false);
        if (getActivity() != null) {
            contentViewModel = FragmentsActivity.obtainContentViewModel(getActivity());
            photosFragmentBinding.setContentViewModel(contentViewModel);
            photosFragmentBinding.photoActionBar.setVariable(BR.toolBarViewModel, contentViewModel);
        }
        return photosFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getContext();

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

        contentViewModel.getNewSettingsEvent().observe(this, aVoid ->
                createFilteringPopup());

        photosFragmentBinding.galleryRecyclerView.setAdapter(contentAdapter);
        setRecyclerViewLayoutManager(LayoutManagerType.GRID_SPAN_FOUR);
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
                context, photosFragmentBinding.photoActionBar.sortBtn, Gravity.END);
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
                context, photosFragmentBinding.photoActionBar.settingsBtn, Gravity.END);
        popupMenu.getMenuInflater().inflate(R.menu.photos_more_vert_menu, popupMenu.getMenu());
        MenuItem menuItem = getCheckedItem(popupMenu, layoutManagerType);
        menuItem.setChecked(true);
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.grid_span_two:
                    setRecyclerViewLayoutManager(LayoutManagerType.GRID_SPAN_TWO);
                    layoutManagerType = LayoutManagerType.GRID_SPAN_TWO;
                    return true;
                case R.id.grid_span_three:
                    setRecyclerViewLayoutManager(LayoutManagerType.GRID_SPAN_THREE);
                    layoutManagerType = LayoutManagerType.GRID_SPAN_THREE;
                    return true;
                case R.id.grid_span_four:
                    setRecyclerViewLayoutManager(LayoutManagerType.GRID_SPAN_FOUR);
                    layoutManagerType = LayoutManagerType.GRID_SPAN_FOUR;
                    return true;
                case R.id.grid_span_five:
                    setRecyclerViewLayoutManager(LayoutManagerType.GRID_SPAN_FIVE);
                    layoutManagerType = LayoutManagerType.GRID_SPAN_FIVE;
                    return true;
                case R.id.app_settings:
                    return true;
                case R.id.app_about:
                    return true;
                default:
                    return false;
            }
        });
        popupMenu.show();
    }

    private MenuItem getCheckedItem(PopupMenu popupMenu, LayoutManagerType layoutManagerType) {
        switch (layoutManagerType) {
            case GRID_SPAN_TWO:
                return popupMenu.getMenu().findItem(R.id.grid_span_two);
            case GRID_SPAN_THREE:
                return popupMenu.getMenu().findItem(R.id.grid_span_three);
            case GRID_SPAN_FOUR:
                return popupMenu.getMenu().findItem(R.id.grid_span_four);
            case GRID_SPAN_FIVE:
                return popupMenu.getMenu().findItem(R.id.grid_span_five);
            default:
                return popupMenu.getMenu().findItem(R.id.grid_span_four);
        }
    }

    private void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;
        RecyclerView recyclerView = photosFragmentBinding.galleryRecyclerView;
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((GridLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        if (itemDecoration != null) {
            recyclerView.removeItemDecoration(itemDecoration);
        }
        RecyclerView.LayoutManager layoutManager;
        switch (layoutManagerType) {
            case GRID_SPAN_TWO:
                layoutManager = new GridLayoutManager(context, 2);
                itemDecoration = spacingItemDecoration(2, 4, false);
                break;
            case GRID_SPAN_THREE:
                layoutManager = new GridLayoutManager(context, 3);
                itemDecoration = spacingItemDecoration(3, 4, true);
                break;
            case GRID_SPAN_FOUR:
                layoutManager = new GridLayoutManager(context, 4);
                itemDecoration = spacingItemDecoration(4, 3, true);
                break;
            case GRID_SPAN_FIVE:
                layoutManager = new GridLayoutManager(context, 5);
                itemDecoration = spacingItemDecoration(5, 3, false);
                break;
            default:
                layoutManager = new GridLayoutManager(context, 4);
                itemDecoration = spacingItemDecoration(4, 3, true);
                break;
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.scrollToPosition(scrollPosition);
    }

    private GridSpacingItemDecoration spacingItemDecoration(int spanCount, int spacing, boolean includesEdge) {
        return new GridSpacingItemDecoration(
                spanCount, DimensionsUtil.dpToPx(context, spacing), includesEdge);
    }
}
