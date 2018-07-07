package productions.darthplagueis.contentvault.imagefolders;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.io.File;
import java.util.List;

import productions.darthplagueis.contentvault.R;
import productions.darthplagueis.contentvault.data.ContentFolder;
import productions.darthplagueis.contentvault.databinding.ContentAlbumItemBinding;

public class ContentFolderAdapter extends RecyclerView.Adapter<ContentFolderAdapter.AlbumViewHolder> {

    private List<ContentFolder> contentFolderList;

    private ContentFolderViewModel albumViewModel;

    private LayoutInflater layoutInflater;

    public ContentFolderAdapter(ContentFolderViewModel viewModel) {
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
        holder.onBind(contentFolderList.get(position));
    }

    @Override
    public int getItemCount() {
        return contentFolderList != null ? contentFolderList.size() : 0;
    }

    public void setContentFolderList(List<ContentFolder> contentFolders) {
        contentFolderList = contentFolders;
        notifyDataSetChanged();
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder {

        private final ContentAlbumItemBinding itemBinding;

        AlbumViewHolder(ContentAlbumItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        void onBind(ContentFolder item) {
            Glide.with(itemBinding.getRoot())
                    .load(new File(item.getDirectoryIcon()))
                    .thumbnail(0.25f)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(itemBinding.albumImageView);

            itemBinding.albumText.setText(item.getFolderName());
        }
    }
}

