package productions.darthplagueis.contentvault.data.source;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import productions.darthplagueis.contentvault.data.ContentFolder;
import productions.darthplagueis.contentvault.data.UserContent;
import productions.darthplagueis.contentvault.data.source.contentfolders.ContentFolderDao;
import productions.darthplagueis.contentvault.data.source.content.UserContentDao;

@Database(entities = {UserContent.class, ContentFolder.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserContentDao userContentDao();

    public abstract ContentFolderDao contentAlbumDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "snapsafe_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
