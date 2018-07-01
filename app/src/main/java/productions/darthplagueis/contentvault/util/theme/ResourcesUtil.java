package productions.darthplagueis.contentvault.util.theme;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.PopupMenu;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;

import productions.darthplagueis.contentvault.R;
import productions.darthplagueis.contentvault.util.SortingType;
import productions.darthplagueis.contentvault.util.recyclerview.LayoutSpanType;

public class ResourcesUtil {

    public static SpannableString getDialogTitle(Context context, int resourceId) {
        SpannableString title = new SpannableString(context.getResources().getString(resourceId));
        title.setSpan(new ForegroundColorSpan(
                        ContextCompat.getColor(context, R.color.md_blacktext_medium_emphasis)),
                0,
                title.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return title;
    }

    public static Drawable getColorIcon(Context context, int resourceId, int color) {
        Drawable drawable = ContextCompat.getDrawable(context, resourceId);
        if (drawable != null) {
            DrawableCompat.wrap(drawable).mutate();
            DrawableCompat.setTint(drawable, ContextCompat.getColor(context, color));
        }
        return drawable;
    }

    public static MenuItem getCheckedSpan(PopupMenu popupMenu, LayoutSpanType layoutSpanType) {
        switch (layoutSpanType) {
            default:
            case GRID_SPAN_FOUR:
                return popupMenu.getMenu().findItem(R.id.grid_span_four);
            case GRID_SPAN_TWO:
                return popupMenu.getMenu().findItem(R.id.grid_span_two);
            case GRID_SPAN_THREE:
                return popupMenu.getMenu().findItem(R.id.grid_span_three);
            case GRID_SPAN_FIVE:
                return popupMenu.getMenu().findItem(R.id.grid_span_five);
        }
    }

    public static MenuItem getCheckedTheme(PopupMenu popupMenu, boolean isDefaultTheme) {
        if (isDefaultTheme) return popupMenu.getMenu().findItem(R.id.light_theme);
        else return popupMenu.getMenu().findItem(R.id.dark_theme);
    }

    public static MenuItem getCheckedSort(PopupMenu popupMenu, SortingType sortingType) {
        switch (sortingType) {
            default:
            case SORT_DATE:
                return popupMenu.getMenu().findItem(R.id.sorting_date);
            case SORT_FOLDER:
                return popupMenu.getMenu().findItem(R.id.sorting_folder);
            case SORT_TAG:
                return popupMenu.getMenu().findItem(R.id.sorting_tag);
            case SORT_ASCENDING:
                return popupMenu.getMenu().findItem(R.id.sorting_ascending);
            case SORT_DESCENDING:
                return popupMenu.getMenu().findItem(R.id.sorting_descending);
        }
    }
}
