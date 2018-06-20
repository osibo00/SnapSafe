package productions.darthplagueis.contentvault.photos;

import android.view.View;

public interface PhotosUserActionsListener {
    boolean onLongClick(View view);

    void onUserClick();
}
