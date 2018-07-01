package productions.darthplagueis.contentvault.photos.view;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import productions.darthplagueis.contentvault.SnapSafeApplication;
import productions.darthplagueis.contentvault.data.UserContent;
import productions.darthplagueis.contentvault.databinding.PhotosFragmentBinding;
import productions.darthplagueis.contentvault.photos.UserContentAdapter;
import productions.darthplagueis.contentvault.util.SortingType;
import productions.darthplagueis.contentvault.util.app.SharedPrefsUtil;
import productions.darthplagueis.contentvault.util.theme.ResourcesUtil;
import productions.darthplagueis.contentvault.util.theme.ThemeType;
import productions.darthplagueis.contentvault.util.theme.ThemeUtil;
import productions.darthplagueis.contentvault.util.recyclerview.LayoutSpanType;
import productions.darthplagueis.contentvault.photos.UserContentViewModel;
import productions.darthplagueis.contentvault.photos.view.dialogs.CopyDialog;
import productions.darthplagueis.contentvault.photoalbums.view.dialogs.CreateFolderDialog;
import productions.darthplagueis.contentvault.photos.view.dialogs.DeleteDialog;
import productions.darthplagueis.contentvault.util.DimensionsUtil;
import productions.darthplagueis.contentvault.util.theme.StatusBarColorUtil;
import productions.darthplagueis.contentvault.util.recyclerview.GridSpacingItemDecoration;


public class PhotosFragment extends Fragment {

    public static final String DELETE_DIALOG_TAG = "DELETE_DIALOG_TAG";
    public static final String NEW_ALBUM_TAG = "NEW_ALBUM_TAG";
    public static final String COPY_DIALOG_TAG = "COPY_DIALOG_TAG";

    private UserContentAdapter contentAdapter;

    private GridSpacingItemDecoration itemDecoration = null;
    private LayoutSpanType layoutSpanType;

    private UserContentViewModel contentViewModel;

    private PhotosFragmentBinding photosFragmentBinding;

    private Context context;

    private FragmentActivity activity;

    public static PhotosFragment newInstance() {
        return new PhotosFragment();
    }

    public PhotosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        photosFragmentBinding = PhotosFragmentBinding.inflate(inflater, container, false);
        activity = getActivity();
        if (activity != null) {
            contentViewModel = FragmentsActivity.obtainContentViewModel(activity);
            photosFragmentBinding.setContentViewModel(contentViewModel);
            photosFragmentBinding.photoActionBar.setVariable(BR.toolBarViewModel, contentViewModel);
            photosFragmentBinding.photoActionBar.setVariable(BR.toolBarBackgroundColor, R.attr.appBarPrimaryColor);
        }
        return photosFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getContext();

        contentAdapter = new UserContentAdapter(contentViewModel);
        setImageSort();

        contentViewModel.getNewMultiSelectionEvent().observe(this, aVoid -> {
            contentAdapter.enableMultiSelection();
            StatusBarColorUtil.setActionBarColor(getActivity(), true);
            photosFragmentBinding.photoActionBar.setVariable(BR.toolBarBackgroundColor, R.attr.colorSecondary);
        });

        contentViewModel.getNewSelectionEnabledEvent().observe(this, aVoid -> {
            StatusBarColorUtil.setActionBarColor(getActivity(), true);
            photosFragmentBinding.photoActionBar.setVariable(BR.toolBarBackgroundColor, R.attr.colorSecondary);
        });

        contentViewModel.getNewSelectionDisabledEvent().observe(this, aVoid -> {
            contentAdapter.disableMultiSelection();
            StatusBarColorUtil.setActionBarColor(getActivity(), false);
            photosFragmentBinding.photoActionBar.setVariable(BR.toolBarBackgroundColor, R.attr.appBarPrimaryColor);
        });

        contentViewModel.getNewDeletePromptEvent().observe(this, aVoid -> createDeleteDialog());

        contentViewModel.getNewCopyPromptEvent().observe(this, aVoid -> createCopyDialog());

        contentViewModel.getNewAlbumPromptEvent().observe(this, this::createNewAlbumDialog);

        contentViewModel.getNewSortingEvent().observe(this, aVoid -> createSortingPopup());

        contentViewModel.getNewSettingsEvent().observe(this, aVoid -> createFilteringPopup());

        photosFragmentBinding.galleryRecyclerView.setAdapter(contentAdapter);
        layoutSpanType = LayoutSpanType.valueOf(SharedPrefsUtil.getSpanPreference(context));
        setRecyclerViewLayoutManager(layoutSpanType);
    }

    private void setImageSort() {
        SortingType sortOrder = SortingType.valueOf(SharedPrefsUtil.getSortOrderPreference(context));
        SortingType sortType = SortingType.valueOf(SharedPrefsUtil.getSortPreference(context));
        if (sortOrder == SortingType.SORT_ASCENDING) {
            switch (sortType) {
                default:
                case SORT_DATE:
                    getAscDateList();
                    break;
                case SORT_FOLDER:
                    getFolderAToZList();
                    break;
                case SORT_TAG:
                    break;
            }
        } else {
            switch (sortType) {
                default:
                case SORT_DATE:
                    getDescDateList();
                    break;
                case SORT_FOLDER:
                    getFolderZToAList();
                    break;
                case SORT_TAG:
                    break;
            }
        }
    }

    private void getDescDateList() {
        contentViewModel.getDescDateList().observe(
                this, contentAdapter::setUserContentList);
    }

    private void getAscDateList() {
        contentViewModel.getAscDateList().observe(
                this, contentAdapter::setUserContentList);
    }

    private void getFolderAToZList() {
        contentViewModel.getAlbumOrderAZList().observe(
                this, contentAdapter::setUserContentList);
    }

    private void getFolderZToAList() {
        contentViewModel.getAlbumOrderZAList().observe(
                this, contentAdapter::setUserContentList);
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
        CreateFolderDialog albumDialog = CreateFolderDialog.newInstance(userContents);
        albumDialog.show(getChildFragmentManager(), NEW_ALBUM_TAG);
    }

    private void createSortingPopup() {
        PopupMenu popupMenu = new PopupMenu(
                context, photosFragmentBinding.photoActionBar.sortBtn, Gravity.END);
        popupMenu.getMenuInflater().inflate(R.menu.sorting_menu, popupMenu.getMenu());
        MenuItem menuSort = ResourcesUtil.getCheckedSort(popupMenu,
                SortingType.valueOf(SharedPrefsUtil.getSortPreference(context)));
        menuSort.setChecked(true);
        MenuItem menuSortOrder = ResourcesUtil.getCheckedSort(popupMenu,
                SortingType.valueOf(SharedPrefsUtil.getSortOrderPreference(context)));
        menuSortOrder.setChecked(true);
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.sorting_date:
                    SharedPrefsUtil.saveSortPreference(context, SortingType.SORT_DATE.name());
                    setImageSort();
                    return true;
                case R.id.sorting_folder:
                    SharedPrefsUtil.saveSortPreference(context, SortingType.SORT_FOLDER.name());
                    setImageSort();
                    return true;
                case R.id.sorting_tag:
                    return true;
                case R.id.sorting_ascending:
                    SharedPrefsUtil.saveSortOrderPreference(context, SortingType.SORT_ASCENDING.name());
                    setImageSort();
                    return true;
                case R.id.sorting_descending:
                    SharedPrefsUtil.saveSortOrderPreference(context, SortingType.SORT_DESCENDING.name());
                    setImageSort();
                    return false;
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
        MenuItem menuColumnSpan = ResourcesUtil.getCheckedSpan(popupMenu,
                LayoutSpanType.valueOf(SharedPrefsUtil.getSpanPreference(context)));
        menuColumnSpan.setChecked(true);
        MenuItem menuTheme = ResourcesUtil.getCheckedTheme(popupMenu,
                SharedPrefsUtil.getThemePreference(context));
        menuTheme.setChecked(true);
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.grid_span_two:
                    layoutSpanType = LayoutSpanType.GRID_SPAN_TWO;
                    setRecyclerViewLayoutManager(layoutSpanType);
                    SharedPrefsUtil.saveSpanPreference(context, layoutSpanType.name());
                    return true;
                case R.id.grid_span_three:
                    layoutSpanType = LayoutSpanType.GRID_SPAN_THREE;
                    setRecyclerViewLayoutManager(layoutSpanType);
                    SharedPrefsUtil.saveSpanPreference(context, layoutSpanType.name());
                    return true;
                case R.id.grid_span_four:
                    layoutSpanType = LayoutSpanType.GRID_SPAN_FOUR;
                    setRecyclerViewLayoutManager(layoutSpanType);
                    SharedPrefsUtil.saveSpanPreference(context, layoutSpanType.name());
                    return true;
                case R.id.grid_span_five:
                    layoutSpanType = LayoutSpanType.GRID_SPAN_FIVE;
                    setRecyclerViewLayoutManager(layoutSpanType);
                    SharedPrefsUtil.saveSpanPreference(context, layoutSpanType.name());
                    return true;
                case R.id.light_theme:
                    if (SnapSafeApplication.appTheme != ThemeType.APP_THEME_LIGHT) {
                        ThemeUtil.changeTheme(activity, ThemeType.APP_THEME_LIGHT);
                        SnapSafeApplication.appTheme = ThemeType.APP_THEME_LIGHT;
                        SharedPrefsUtil.saveThemePreference(context, true);
                    }
                    return true;
                case R.id.dark_theme:
                    if (SnapSafeApplication.appTheme != ThemeType.APP_THEME_DARK) {
                        ThemeUtil.changeTheme(activity, ThemeType.APP_THEME_DARK);
                        SnapSafeApplication.appTheme = ThemeType.APP_THEME_DARK;
                        SharedPrefsUtil.saveThemePreference(context, false);
                    }
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

    private void setRecyclerViewLayoutManager(LayoutSpanType layoutSpanType) {
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
        switch (layoutSpanType) {
            default:
            case GRID_SPAN_FOUR:
                layoutManager = new GridLayoutManager(context, 4);
                itemDecoration = spacingItemDecoration(4, 3, true);
                break;
            case GRID_SPAN_TWO:
                layoutManager = new GridLayoutManager(context, 2);
                itemDecoration = spacingItemDecoration(2, 4, false);
                break;
            case GRID_SPAN_THREE:
                layoutManager = new GridLayoutManager(context, 3);
                itemDecoration = spacingItemDecoration(3, 4, true);
                break;
            case GRID_SPAN_FIVE:
                layoutManager = new GridLayoutManager(context, 5);
                itemDecoration = spacingItemDecoration(5, 3, false);
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
