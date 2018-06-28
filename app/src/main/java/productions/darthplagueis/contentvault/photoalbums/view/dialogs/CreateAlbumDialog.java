package productions.darthplagueis.contentvault.photoalbums.view.dialogs;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import productions.darthplagueis.contentvault.R;
import productions.darthplagueis.contentvault.ViewModelFactory;
import productions.darthplagueis.contentvault.data.ContentAlbum;
import productions.darthplagueis.contentvault.data.UserContent;
import productions.darthplagueis.contentvault.databinding.NewAlbumDialogBinding;
import productions.darthplagueis.contentvault.photoalbums.ContentAlbumViewModel;
import productions.darthplagueis.contentvault.util.filemanager.FileManager;

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

        if (getActivity() != null) {
            ViewModelFactory factory = ViewModelFactory.getINSTANCE(getActivity().getApplication());
            albumViewModel = ViewModelProviders.of(getActivity(), factory).get(ContentAlbumViewModel.class);
            albumViewModel.getAllAlbums().observe(this, contentAlbums -> {
                if (contentAlbums != null) {
                    for (ContentAlbum album : contentAlbums) {
                        userAlbumList.add(album.getAlbumName());
                    }
                }
            });
        }
        Context context = Objects.requireNonNull(getContext(), "Context must not be null.");
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setView(dialogBinding.getRoot())
                .setTitle(R.string.album_dialog_title)
                .setMessage(R.string.album_dialog_message)
                .setIcon(R.drawable.ic_create_new_folder_green_24dp)
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
            if (TextUtils.isEmpty(albumTag)) {
                albumViewModel.createNewAlbum(albumName, null, userContentList);
            } else {
                albumViewModel.createNewAlbum(albumName, albumTag, userContentList);
            }
            dismiss();
        }
    }

    private boolean isValidName(TextInputLayout layout, String name) {
        Pattern specialCharacters = Pattern.compile("[^a-zA-Z0-9 ]");
        if (TextUtils.isEmpty(name)) {
            layout.setErrorEnabled(true);
            layout.setError(getString(R.string.album_dialog_empty));
            return false;
        } else if (userAlbumList.contains(name) || name.equalsIgnoreCase(FileManager.DEFAULT_DIRECTORY_TAG)) {
            layout.setErrorEnabled(true);
            layout.setError(String.format(getString(R.string.create_dialog_exists_error), name));
            return false;
        } else if (specialCharacters.matcher(name).find()) {
            layout.setErrorEnabled(true);
            layout.setError(getString(R.string.album_name_special_chars));
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
                dialogBinding.dialogTextAlbum.setErrorEnabled(false);
                dialogBinding.dialogTextAlbum.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                albumName = s.toString().trim();
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
                albumTag = s.toString().trim();
            }
        });
    }
}
