package productions.darthplagueis.contentvault;

import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import productions.darthplagueis.contentvault.util.BottomNavigationViewHelper;

public class FragmentsActivity extends AppCompatActivity {

    private BottomNavigationView navigation;

    private boolean isNavigationHidden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragments);

        navigation = findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        setNavigationListener();
    }

    private void setupPhotosFragment() {

    }

    private void setNavigationListener() {
        BottomNavigationView.OnNavigationItemSelectedListener listener = item -> {
            switch (item.getItemId()) {
                case R.id.navigation_photos:

                    return true;
                case R.id.navigation_albums:

                    return true;
                case R.id.navigation_favorites:

                    return true;
                case R.id.navigation_search:

                    return true;
            }
            return false;
        };
        navigation.setOnNavigationItemSelectedListener(listener);
    }
}
