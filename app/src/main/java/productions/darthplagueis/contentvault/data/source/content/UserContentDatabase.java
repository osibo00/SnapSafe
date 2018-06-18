package productions.darthplagueis.contentvault.data.source.content;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import productions.darthplagueis.contentvault.data.UserContent;

@Database(entities = {UserContent.class}, version = 1)
public abstract class UserContentDatabase extends RoomDatabase {

    public abstract UserContentDao userContentDao();

    private static UserContentDatabase INSTANCE;

    public static UserContentDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (UserContentDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            UserContentDatabase.class, "user_content_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
