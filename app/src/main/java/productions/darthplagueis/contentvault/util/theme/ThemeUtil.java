package productions.darthplagueis.contentvault.util.theme;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import productions.darthplagueis.contentvault.R;
import productions.darthplagueis.contentvault.SnapSafeApplication;

public class ThemeUtil {

    private static ThemeType appTheme = SnapSafeApplication.appTheme;

    public static void changeTheme(Activity activity, ThemeType themeType) {
        appTheme = themeType;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void onActivityCreateSetTheme(Activity activity) {
        switch (appTheme) {
            default:
            case APP_THEME_LIGHT:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                    activity.setTheme(R.style.AppTheme_Light_27);
                    break;
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity.setTheme(R.style.AppTheme_Light_23);
                    break;
                } else {
                    activity.setTheme(R.style.AppTheme_Light);
                    break;
                }
            case APP_THEME_DARK:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                    activity.setTheme(R.style.AppTheme_Dark_27);
                    break;
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity.setTheme(R.style.AppTheme_Dark_23);
                    break;
                } else {
                    activity.setTheme(R.style.AppTheme_Dark);
                    break;
                }
        }
    }
}
