package productions.darthplagueis.contentvault.util.app;


import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

public class PermissionsUtil {

    public static final int WRITE_EXTERNAL_STORAGE_REQUEST = 1989;

    private static final String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static void requestWriteExternalStorage(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity != null && !hasPermission(activity, PERMISSIONS)) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS, WRITE_EXTERNAL_STORAGE_REQUEST);
            }
        }
    }

    private static boolean hasPermission(Activity activity, String[] permissions) {
        return ActivityCompat.checkSelfPermission(
                activity, permissions[0]) == PackageManager.PERMISSION_GRANTED;
    }
}
