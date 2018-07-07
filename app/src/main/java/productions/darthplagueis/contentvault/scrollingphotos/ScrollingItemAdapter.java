package productions.darthplagueis.contentvault.scrollingphotos;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.List;

import productions.darthplagueis.contentvault.R;
import productions.darthplagueis.contentvault.data.UserContent;
import productions.darthplagueis.contentvault.databinding.ScrollingContentItemBinding;

public class ScrollingItemAdapter extends RecyclerView.Adapter<ScrollingItemAdapter.ScrollingViewHolder> {

    private List<UserContent> userContentList;

    private LayoutInflater layoutInflater;

    private final ScrollingPhotoViewModel photoViewModel;

    public ScrollingItemAdapter(ScrollingPhotoViewModel photoViewModel) {
        this.photoViewModel = photoViewModel;
    }

    @NonNull
    @Override
    public ScrollingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) layoutInflater = LayoutInflater.from(parent.getContext());
        ScrollingContentItemBinding contentItemBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.scrolling_content_item, parent, false);
        return new ScrollingViewHolder(contentItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ScrollingViewHolder holder, int position) {
        holder.onBind(userContentList.get(position));
    }

    @Override
    public int getItemCount() {
        return userContentList != null ? userContentList.size() : 0;
    }

    public void setUserContentList(List<UserContent> newList) {
        userContentList = newList;
        notifyDataSetChanged();
    }

    class ScrollingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ScrollingContentItemBinding binding;

        ScrollingViewHolder(ScrollingContentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.scrollingItemLayout.setOnClickListener(this);
        }

        void onBind(UserContent userContent) {
            Glide.with(binding.getRoot())
                    .load(new File(userContent.getFilePath()))
                    .thumbnail(0.25f)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.scrollingItemImageView);
        }

        @Override
        public void onClick(View v) {
            photoViewModel.currentItemAdapterPosition(getAdapterPosition());
        }
    }
}
