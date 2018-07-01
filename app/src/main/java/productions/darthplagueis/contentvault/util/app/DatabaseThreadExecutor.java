package productions.darthplagueis.contentvault.util.app;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DatabaseThreadExecutor implements Executor {

    private final Executor databaseThread;

    public DatabaseThreadExecutor() {
        databaseThread = Executors.newSingleThreadExecutor();
    }

    @Override
    public void execute(@NonNull Runnable command) {
        databaseThread.execute(command);
    }
}
