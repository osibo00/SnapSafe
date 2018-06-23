package productions.darthplagueis.contentvault.photos.view.dialogs;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import productions.darthplagueis.contentvault.R;
import productions.darthplagueis.contentvault.ViewModelFactory;
import productions.darthplagueis.contentvault.data.ContentAlbum;
import productions.darthplagueis.contentvault.data.UserContent;
import productions.darthplagueis.contentvault.databinding.NewAlbumDialogBinding;
import productions.darthplagueis.contentvault.photoalbums.ContentAlbumViewModel;
import productions.darthplagueis.contentvault.photos.UserContentViewModel;
import productions.darthplagueis.contentvault.util.CurrentDateUtil;

public class CreateAlbumDialog extends DialogFragment {

    private List<UserContent> userContentList;
    private List<String> userAlbumList = new ArrayList<>();

    private String albumName;
    private String albumTag;

    private ContentAlbumViewModel albumViewModel;

    private NewAlbumDialogBinding dialogBinding;

    public static CreateAlbumDialog newInstance(List<UserContent> userContents) {
        CreateAlbumDialog dialog = new CreateAlbumDialog();
        dialog.userContentList = userContents;
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setViews();

        ViewModelFactory factory = ViewModelFactory.getINSTANCE(
                Objects.requireNonNull(getActivity()).getApplication());
        albumViewModel = ViewModelProviders.of(getActivity(), factory).get(ContentAlbumViewModel.class);
        albumViewModel.getAllAlbums().observe(this, contentAlbums -> {
            if (contentAlbums != null) {
                for (ContentAlbum album : contentAlbums) {
                    userAlbumList.add(album.getAlbumName());
                }
            }
        });

        AlertDialog alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                .setView(dialogBinding.getRoot())
                .setTitle(R.string.album_dialog_title)
                .setMessage(R.string.album_dialog_message)
                .setPositiveButton(R.string.positive_btn_create, null)
                .setNegativeButton(R.string.dialog_cancel, null)
                .create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setOnShowListener(dialog -> onDialogShow(alertDialog));
        return alertDialog;
    }

    private void setViews() {
        dialogBinding = NewAlbumDialogBinding.inflate(
                LayoutInflater.from(getContext()), null, false);
        addTextWatcher();
    }

    private void onDialogShow(AlertDialog dialog) {
        Button positiveBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveBtn.setOnClickListener(v -> onCreateClicked());
        Button cancelBtn = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        cancelBtn.setOnClickListener(v1 -> dismiss());
    }

    private void onCreateClicked() {
        if (isValidName(dialogBinding.dialogTextAlbum, albumName)) {
            albumViewModel.createNewAlbum(albumName, userContentList);
            dismiss();
        }
    }

    private boolean isValidName(TextInputLayout layout, String name) {
        if (TextUtils.isEmpty(name)) {
            layout.setErrorEnabled(true);
            layout.setError(getString(R.string.album_dialog_empty));
            return false;
        } else if (userAlbumList.contains(name)) {
            layout.setErrorEnabled(true);
            layout.setError(String.format(getString(R.string.create_dialog_exists_error), name));
            return false;
        }

        layout.setErrorEnabled(false);
        layout.setError("");
        return true;
    }

    private void addTextWatcher() {
        dialogBinding.dialogEditAlbum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                albumName = s.toString();
                dialogBinding.dialogTextTag.setVisibility(View.VISIBLE);
            }
        });
        dialogBinding.dialogEditTag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                albumTag = s.toString();
            }
        });
    }
}
