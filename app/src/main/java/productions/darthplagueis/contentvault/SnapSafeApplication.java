package productions.darthplagueis.contentvault;

import android.app.Application;

import productions.darthplagueis.contentvault.util.app.SharedPrefsUtil;
import productions.darthplagueis.contentvault.util.theme.ThemeType;

public class SnapSafeApplication extends Application {

    public static final String SHARED_PREFS = "SHARED_PREFS";
    public static final String IS_DEFAULT_THEME = "IS_DEFAULT_THEME";
    public static final String SPAN_PREF = "SPAN_PREFERENCE";
    public static final String SORT_PREF = "SORTING_PREFERENCE";
    public static final String SORT_ORDER = "SORTING_ORDER";

    public static ThemeType appTheme;

    @Override
    public void onCreate() {
        super.onCreate();
        boolean isDefaultTheme = SharedPrefsUtil.getThemePreference(getApplicationContext());
        if (isDefaultTheme) appTheme = ThemeType.APP_THEME_LIGHT;
        else appTheme = ThemeType.APP_THEME_DARK;
    }
}
