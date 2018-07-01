package productions.darthplagueis.contentvault.photoalbums.view;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import productions.darthplagueis.contentvault.FragmentsActivity;
import productions.darthplagueis.contentvault.databinding.AlbumsFragmentBinding;
import productions.darthplagueis.contentvault.photoalbums.ContentFolderAdapter;
import productions.darthplagueis.contentvault.photoalbums.ContentFolderViewModel;
import productions.darthplagueis.contentvault.util.DimensionsUtil;
import productions.darthplagueis.contentvault.util.recyclerview.GridSpacingItemDecoration;


public class FoldersFragment extends Fragment {

    private ContentFolderViewModel albumViewModel;

    private AlbumsFragmentBinding albumsFragmentBinding;

    private Context context;

    public static FoldersFragment newInstance() {
        return new FoldersFragment();
    }

    public FoldersFragment() {
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
        context = getContext();

        ContentFolderAdapter albumAdapter = new ContentFolderAdapter(albumViewModel);
        albumViewModel.getDescDateList().observe(this, albumAdapter::setContentFolderList);

        RecyclerView recyclerView = albumsFragmentBinding.albumRecyclerView;
        recyclerView.setAdapter(albumAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,
                DimensionsUtil.dpToPx(context, 12), true));
    }
}
