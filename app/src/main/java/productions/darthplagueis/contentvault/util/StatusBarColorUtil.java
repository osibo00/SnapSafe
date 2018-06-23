package productions.darthplagueis.contentvault.util;

import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import productions.darthplagueis.contentvault.R;

public class StatusBarColorUtil {

    public static void setActionBarColor(FragmentActivity activity, boolean isActionToolbar) {
        if (activity != null) {
            if (!isActionToolbar) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setStatusBarColor(activity, R.color.colorSurface);
                } else {
                    setStatusBarColor(activity, R.color.md_mini_fab_color01);
                }
            } else {
                setStatusBarColor(activity, R.color.colorAccent);
            }
        }
    }

    public static void setStatusBarColor(FragmentActivity activity, int color) {
        activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, color));
    }
}
