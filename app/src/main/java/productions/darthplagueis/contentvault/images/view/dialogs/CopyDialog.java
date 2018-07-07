package productions.darthplagueis.contentvault.images.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

import java.util.Objects;

import productions.darthplagueis.contentvault.R;
import productions.darthplagueis.contentvault.SnapSafeApplication;
import productions.darthplagueis.contentvault.images.UserContentViewModel;
import productions.darthplagueis.contentvault.util.theme.ResourcesUtil;
import productions.darthplagueis.contentvault.util.theme.ThemeType;

public class CopyDialog extends DialogFragment {

    private UserContentViewModel contentViewModel;

    public static CopyDialog newInstance(UserContentViewModel viewModel) {
        CopyDialog dialog = new CopyDialog();
        dialog.contentViewModel = viewModel;
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = Objects.requireNonNull(getContext(), "Context must not be null.");
        Drawable drawable;
        if (SnapSafeApplication.appTheme == ThemeType.APP_THEME_LIGHT) {
            drawable = ResourcesUtil.getColorIcon(context, R.drawable.ic_content_copy_black_24dp, R.color.colorPrimary);
        } else {
            drawable = ResourcesUtil.getColorIcon(context, R.drawable.ic_content_copy_black_24dp, R.color.colorDarkSecondary);
        }
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(ResourcesUtil.getDialogTitle(context, R.string.copy))
                .setMessage(R.string.make_copies)
                .setIcon(drawable)
                .setPositiveButton(R.string.dialog_yes, null)
                .setNegativeButton(R.string.dialog_cancel, null)
                .create();
        alertDialog.setCanceledOnTouchOutside(true);
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
        contentViewModel.copySelected();
        dismiss();
    }
}
