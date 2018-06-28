package productions.darthplagueis.contentvault.photos;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.List;

import productions.darthplagueis.contentvault.R;
import productions.darthplagueis.contentvault.data.UserContent;
import productions.darthplagueis.contentvault.databinding.UserContentItemBinding;


public class UserContentAdapter extends RecyclerView.Adapter<UserContentAdapter.UserContentViewHolder> {

    private List<UserContent> userContentList;
    private SparseBooleanArray itemStateArray = new SparseBooleanArray();

    private LayoutInflater layoutInflater;

    private boolean isMultiSelection;
    private int amountSelected;

    private final UserContentViewModel contentViewModel;

    public UserContentAdapter(UserContentViewModel contentViewModel) {
        this.contentViewModel = contentViewModel;
    }

    @NonNull
    @Override
    public UserContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) layoutInflater = LayoutInflater.from(parent.getContext());
        UserContentItemBinding itemBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.user_content_item, parent, false);
        return new UserContentViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserContentViewHolder holder, int position) {
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

    public void enableMultiSelection() {
        if (getItemCount() != 0) {
            isMultiSelection = true;
            notifyDataSetChanged();
        }
    }

    public void disableMultiSelection() {
        isMultiSelection = false;
        itemStateArray.clear();
        amountSelected = 0;
        notifyDataSetChanged();
    }

    class UserContentViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {

        private final UserContentItemBinding binding;

        UserContentViewHolder(UserContentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.photoParentLayout.setOnClickListener(this);
            binding.photoParentLayout.setOnLongClickListener(this);
        }

        void onBind(UserContent userContent) {
            Glide.with(binding.getRoot())
                    .load(new File(userContent.getFilePath()))
                    .thumbnail(0.5f)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(binding.photoImageView);

            displaySelectionStatus();

            // For Testing.
            binding.textView.setText(userContent.getFileDirectory());
        }

        @Override
        public void onClick(View v) {
            itemSelection();
        }

        @Override
        public boolean onLongClick(View v) {
            if (!isMultiSelection) {
                enableMultiSelection();
                contentViewModel.enableMultiSelection();
            } else {
                disableMultiSelection();
                contentViewModel.disableMultiSelection();
            }
            return true;
        }

        void displaySelectionStatus() {
            if (!isMultiSelection) {
                binding.photoCheckBox.setVisibility(View.GONE);
            } else {
                binding.photoCheckBox.setVisibility(View.VISIBLE);
                boolean isItemSelected = itemStateArray.get(
                        getAdapterPosition(), false);
                if (!isItemSelected) {
                    binding.photoCheckBox.setChecked(false);
                } else {
                    binding.photoCheckBox.setChecked(true);
                }
            }
        }

        void itemSelection() {
            int position = getAdapterPosition();
            if (isMultiSelection) {
                boolean isItemSelected = itemStateArray.get(position);
                if (isItemSelected) {
                    binding.photoCheckBox.setChecked(false);
                    itemStateArray.delete(position);
                    amountSelected--;
                } else {
                    binding.photoCheckBox.setChecked(true);
                    itemStateArray.put(position, true);
                    amountSelected++;
                }
                contentViewModel.totalItemsSelected(amountSelected);
                contentViewModel.contentSelected(userContentList.get(position));
            } else {
                contentViewModel.loadDetailView(userContentList.get(position));
            }
        }
    }
}
