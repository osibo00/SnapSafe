package productions.darthplagueis.contentvault;

import android.util.SparseArray;
import android.widget.ImageView;

import java.util.List;
import java.util.Map;

import productions.darthplagueis.contentvault.data.UserContent;

public interface ActivityNavigator {

    void createImportIntent();

    void createDetailedView(SparseArray<Map<ImageView, List<UserContent>>> mapSparseArray);

    void createCarouselView();
}
