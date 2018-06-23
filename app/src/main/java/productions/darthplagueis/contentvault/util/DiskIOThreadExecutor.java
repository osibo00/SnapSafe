package productions.darthplagueis.contentvault.util;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DiskIOThreadExecutor implements Executor {

    private final Executor diskIO;

    public DiskIOThreadExecutor newInstance() {
        return new DiskIOThreadExecutor();
    }

    private DiskIOThreadExecutor() {
        diskIO = Executors.newSingleThreadExecutor();
    }

    @Override
    public void execute(@NonNull Runnable command) {
        diskIO.execute(command);
    }

    public Executor getDiskIO() {
        return diskIO;
    }
}
