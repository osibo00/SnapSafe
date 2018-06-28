package productions.darthplagueis.contentvault.scrollingphotos.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import productions.darthplagueis.contentvault.ScrollGalleryActivity;
import productions.darthplagueis.contentvault.data.UserContent;
import productions.darthplagueis.contentvault.databinding.ScrollingViewFragmentBinding;
import productions.darthplagueis.contentvault.scrollingphotos.ScrollingItemAdapter;
import productions.darthplagueis.contentvault.scrollingphotos.ScrollingPhotoViewModel;
import productions.darthplagueis.contentvault.util.recyclerview.GridSpacingItemDecoration;

public class ScrollingItemFragment extends Fragment {

    private List<UserContent> userContentList;

    private ScrollingPhotoViewModel photoViewModel;

    private ScrollingViewFragmentBinding viewFragmentBinding;

    public static ScrollingItemFragment newInstance(List<UserContent> userContents) {
        ScrollingItemFragment fragment = new ScrollingItemFragment();
        fragment.userContentList = userContents;
        return fragment;
    }

    public ScrollingItemFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewFragmentBinding = ScrollingViewFragmentBinding.inflate(inflater, container, false);
        if (getActivity() != null) {
            photoViewModel = ScrollGalleryActivity.obtainScrollingViewModel(getActivity());
        }
        return viewFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ScrollingItemAdapter viewAdapter = new ScrollingItemAdapter(photoViewModel);
        if (userContentList != null) viewAdapter.setUserContentList(userContentList);
        RecyclerView recyclerView = viewFragmentBinding.scrollingRecyclerView;
        recyclerView.setAdapter(viewAdapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, 0, false));

        photoViewModel.getNewPagerAdapterPositionEvent().observe(this, recyclerView::smoothScrollToPosition);
    }
}
