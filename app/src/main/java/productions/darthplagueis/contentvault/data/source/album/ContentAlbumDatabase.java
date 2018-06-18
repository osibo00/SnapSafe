package productions.darthplagueis.contentvault.data.source.album;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import productions.darthplagueis.contentvault.data.ContentAlbum;

@Database(entities = {ContentAlbum.class}, version = 1)
public abstract class ContentAlbumDatabase extends RoomDatabase {

    public abstract ContentAlbumDao contentAlbumDao();

    private static ContentAlbumDatabase INSTANCE;

    public static ContentAlbumDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ContentAlbumDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ContentAlbumDatabase.class, "content_album_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
