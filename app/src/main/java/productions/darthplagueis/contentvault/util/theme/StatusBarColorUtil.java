package productions.darthplagueis.contentvault.util.theme;

import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import productions.darthplagueis.contentvault.R;
import productions.darthplagueis.contentvault.SnapSafeApplication;

public class StatusBarColorUtil {

    public static void setActionBarColor(FragmentActivity activity, boolean isActionToolbar) {
        ThemeType currentTheme = SnapSafeApplication.appTheme;
        if (activity != null) {
            if (!isActionToolbar) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    switch (currentTheme) {
                        default:
                        case APP_THEME_LIGHT:
                            setStatusBarColor(activity, R.color.colorSurfaceLight);
                            break;
                        case APP_THEME_DARK:
                            setStatusBarColor(activity, R.color.colorDarkPrimaryDark);
                            break;
                    }
                } else {
                    switch (currentTheme) {
                        default:
                        case APP_THEME_LIGHT:
                            setStatusBarColor(activity, R.color.colorPrimaryDark);
                            break;
                        case APP_THEME_DARK:
                            setStatusBarColor(activity, R.color.colorDarkPrimaryDark);
                            break;
                    }
                }
            } else {
                switch (currentTheme) {
                    default:
                    case APP_THEME_LIGHT:
                        setStatusBarColor(activity, R.color.colorPrimary);
                        break;
                    case APP_THEME_DARK:
                        setStatusBarColor(activity, R.color.colorDarkSecondary);
                        break;
                }
            }
        }
    }

    public static void setStatusBarColor(FragmentActivity activity, int color) {
        activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, color));
    }
}
