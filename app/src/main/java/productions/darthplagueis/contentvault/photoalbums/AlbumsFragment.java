package productions.darthplagueis.contentvault.photoalbums;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import productions.darthplagueis.contentvault.FragmentsActivity;
import productions.darthplagueis.contentvault.databinding.AlbumsFragmentBinding;
import productions.darthplagueis.contentvault.util.recyclerview.GridSpacingItemDecoration;


public class AlbumsFragment extends Fragment {

    public static AlbumsFragment newInstance() {
        return new AlbumsFragment();
    }

    public AlbumsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AlbumsFragmentBinding albumsFragmentBinding =
                AlbumsFragmentBinding.inflate(inflater, container, false);
        ContentAlbumViewModel contentAlbumViewModel =
                FragmentsActivity.obtainAlbumViewModel(getActivity());
        albumsFragmentBinding.setAlbumViewModel(contentAlbumViewModel);

        ContentAlbumAdapter albumAdapter = new ContentAlbumAdapter(contentAlbumViewModel);
        contentAlbumViewModel.getDescDateList().observe(this, albumAdapter::setContentAlbumList);

        RecyclerView recyclerView = albumsFragmentBinding.albumRecyclerView;
        recyclerView.setAdapter(albumAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(
                2, dpToPx(16), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return albumsFragmentBinding.getRoot();
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
