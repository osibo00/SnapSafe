package productions.darthplagueis.contentvault.photos.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

import java.util.Objects;

import productions.darthplagueis.contentvault.R;
import productions.darthplagueis.contentvault.photos.UserContentViewModel;

public class DeleteDialog extends DialogFragment {

    private UserContentViewModel contentViewModel;

    public static DeleteDialog newInstance(UserContentViewModel viewModel) {
        DeleteDialog dialog = new DeleteDialog();
        dialog.contentViewModel = viewModel;
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = Objects.requireNonNull(getContext(), "Context must not be null.");
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.delete)
                .setMessage(R.string.delete_dialog_message)
                .setIcon(R.drawable.ic_delete_green_24dp)
                .setPositiveButton(R.string.dialog_yes, null)
                .setNegativeButton(R.string.dialog_cancel, null)
                .create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setOnShowListener(dialog -> onDialogShow(alertDialog));
        return alertDialog;
    }

    private void onDialogShow(AlertDialog dialog) {
        Button positiveBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveBtn.setOnClickListener(v -> onYesCLicked());
        Button cancelBtn = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        cancelBtn.setOnClickListener(v1 -> dismiss());
    }

    private void onYesCLicked() {
        contentViewModel.deleteSelected();
        dismiss();
    }
}

