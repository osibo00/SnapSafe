package productions.darthplagueis.contentvault.photoalbums;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.List;

import productions.darthplagueis.contentvault.R;
import productions.darthplagueis.contentvault.data.ContentAlbum;
import productions.darthplagueis.contentvault.databinding.ContentAlbumItemBinding;

public class ContentAlbumAdapter extends RecyclerView.Adapter<ContentAlbumAdapter.AlbumViewHolder> {

    private List<ContentAlbum> contentAlbumList;

    private ContentAlbumViewModel albumViewModel;

    private LayoutInflater layoutInflater;

    public ContentAlbumAdapter(ContentAlbumViewModel viewModel) {
        this.albumViewModel = viewModel;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) layoutInflater = LayoutInflater.from(parent.getContext());
        ContentAlbumItemBinding itemBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.content_album_item, parent, false);
        return new AlbumViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        holder.onBind(contentAlbumList.get(position));
    }

    @Override
    public int getItemCount() {
        return contentAlbumList != null ? contentAlbumList.size() : 0;
    }

    public void setContentAlbumList(List<ContentAlbum> contentAlbums) {
        contentAlbumList = contentAlbums;
        notifyDataSetChanged();
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder {

        private final ContentAlbumItemBinding itemBinding;

        AlbumViewHolder(ContentAlbumItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        void onBind(ContentAlbum item) {
            Glide.with(itemBinding.getRoot())
                    .load(new File(item.getDirectoryIcon()))
                    .thumbnail(0.5f)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(itemBinding.albumImageView);

            itemBinding.albumText.setText(item.getAlbumName());
        }
    }
}

