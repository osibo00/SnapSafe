package productions.darthplagueis.contentvault.photoalbums;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.Objects;

import productions.darthplagueis.contentvault.FragmentsActivity;
import productions.darthplagueis.contentvault.databinding.AlbumsFragmentBinding;
import productions.darthplagueis.contentvault.util.DimensionsUtil;
import productions.darthplagueis.contentvault.util.recyclerview.GridSpacingItemDecoration;


public class AlbumsFragment extends Fragment {

    private ContentAlbumViewModel albumViewModel;

    private AlbumsFragmentBinding albumsFragmentBinding;

    public static AlbumsFragment newInstance() {
        return new AlbumsFragment();
    }

    public AlbumsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        albumsFragmentBinding = AlbumsFragmentBinding.inflate(
                inflater, container, false);
        albumViewModel = FragmentsActivity.obtainAlbumViewModel(getActivity());
        albumsFragmentBinding.setAlbumViewModel(albumViewModel);
        return albumsFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ContentAlbumAdapter albumAdapter = new ContentAlbumAdapter(albumViewModel);
        albumViewModel.getDescDateList().observe(this, albumAdapter::setContentAlbumList);

        RecyclerView recyclerView = albumsFragmentBinding.albumRecyclerView;
        recyclerView.setAdapter(albumAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(
                2,
                DimensionsUtil.dpToPx(Objects.requireNonNull(getContext()), 16),
                true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
