package productions.darthplagueis.contentvault.util.app;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

public class AppExecutors {

    private final Executor diskIO;

    private final Executor databaseThread;

    private final Executor mainThread;

    AppExecutors(Executor diskIO, Executor databaseThread, Executor mainThread) {
        this.diskIO = diskIO;
        this.databaseThread = databaseThread;
        this.mainThread = mainThread;
    }

    public AppExecutors() {
        this(new DiskIOThreadExecutor(), new DatabaseThreadExecutor(), new MainThreadExecutor());
    }

    public Executor getDiskIO() {
        return diskIO;
    }

    public Executor getDatabaseThread() {
        return databaseThread;
    }

    public Executor getMainThread() {
        return mainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
