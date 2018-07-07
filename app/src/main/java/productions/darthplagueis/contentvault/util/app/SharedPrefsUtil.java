package productions.darthplagueis.contentvault.util.app;

import android.content.Context;

import productions.darthplagueis.contentvault.SnapSafeApplication;
import productions.darthplagueis.contentvault.util.SortingType;
import productions.darthplagueis.contentvault.util.recyclerview.LayoutSpanType;

public class SharedPrefsUtil {

    public static boolean getThemePreference(Context context) {
        return context.getSharedPreferences(SnapSafeApplication.SHARED_PREFS, Context.MODE_PRIVATE)
                .getBoolean(SnapSafeApplication.IS_DEFAULT_THEME, true);
    }

    public static void saveThemePreference(Context context, boolean isDefaultTheme) {
        context.getSharedPreferences(SnapSafeApplication.SHARED_PREFS, Context.MODE_PRIVATE)
                .edit().putBoolean(SnapSafeApplication.IS_DEFAULT_THEME, isDefaultTheme)
                .apply();
    }

    public static String getSpanPreference(Context context) {
        return context.getSharedPreferences(SnapSafeApplication.SHARED_PREFS, Context.MODE_PRIVATE)
                .getString(SnapSafeApplication.SPAN_PREF, LayoutSpanType.GRID_SPAN_FOUR.name());
    }

    public static void saveSpanPreference(Context context, String layoutSpanType) {
        context.getSharedPreferences(SnapSafeApplication.SHARED_PREFS, Context.MODE_PRIVATE)
                .edit().putString(SnapSafeApplication.SPAN_PREF, layoutSpanType)
                .apply();
    }

    public static String getSortPreference(Context context) {
        return context.getSharedPreferences(SnapSafeApplication.SHARED_PREFS, Context.MODE_PRIVATE)
                .getString(SnapSafeApplication.SORT_PREF, SortingType.SORT_DATE.name());
    }

    public static void saveSortPreference(Context context, String sortingType) {
        context.getSharedPreferences(SnapSafeApplication.SHARED_PREFS, Context.MODE_PRIVATE)
                .edit().putString(SnapSafeApplication.SORT_PREF, sortingType)
                .apply();
    }

    public static String getSortOrderPreference(Context context) {
        return context.getSharedPreferences(SnapSafeApplication.SHARED_PREFS, Context.MODE_PRIVATE)
                .getString(SnapSafeApplication.SORT_ORDER, SortingType.SORT_DESCENDING.name());
    }

    public static void saveSortOrderPreference(Context context, String sortingType) {
        context.getSharedPreferences(SnapSafeApplication.SHARED_PREFS, Context.MODE_PRIVATE)
                .edit().putString(SnapSafeApplication.SORT_ORDER, sortingType)
                .apply();
    }
}
