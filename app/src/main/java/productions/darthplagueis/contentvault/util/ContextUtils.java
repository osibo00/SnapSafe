package productions.darthplagueis.contentvault.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.StyleRes;

public class ContextUtils {

    @ColorInt
    public static int resolveColor(final Context context, @StyleRes final int style, @AttrRes final int attr, @ColorInt final int fallback) {
        final TypedArray ta = obtainTypedArray(context, style, attr);
        try {
            return ta.getColor(0, fallback);
        } finally {
            ta.recycle();
        }
    }

    @ColorInt
    public static int resolveColor(final Context context, @AttrRes final int attr, @ColorInt final int fallback) {
        return resolveColor(context, 0, attr, fallback);
    }

    private static TypedArray obtainTypedArray(final Context context, @StyleRes final int style, @AttrRes final int attr) {
        final int[] tempArray = getTempArray();
        tempArray[0] = attr;
        return context.obtainStyledAttributes(style, tempArray);
    }

    private static final ThreadLocal<int[]> TEMP_ARRAY = new ThreadLocal<>();

    private static int[] getTempArray() {
        int[] tempArray = TEMP_ARRAY.get();
        if (tempArray == null) {
            tempArray = new int[1];
            TEMP_ARRAY.set(tempArray);
        }
        return tempArray;
    }
}
