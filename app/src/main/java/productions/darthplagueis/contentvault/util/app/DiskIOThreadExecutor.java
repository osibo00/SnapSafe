package productions.darthplagueis.contentvault.util.app;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DiskIOThreadExecutor implements Executor {

    private final Executor diskIO;

    public DiskIOThreadExecutor() {
        diskIO = Executors.newSingleThreadExecutor();
    }

    @Override
    public void execute(@NonNull Runnable command) {
        diskIO.execute(command);
    }
}
