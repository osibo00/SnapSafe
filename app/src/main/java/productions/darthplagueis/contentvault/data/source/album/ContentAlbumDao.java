package productions.darthplagueis.contentvault.data.source.album;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import productions.darthplagueis.contentvault.data.ContentAlbum;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ContentAlbumDao {

    @Insert(onConflict = REPLACE)
    void insert(ContentAlbum... contentAlbums);

    @Query("DELETE from content_album")
    void deleteAll();

    @Query("SELECT * from content_album")
    LiveData<List<ContentAlbum>> getAllAlbums();

    @Query("SELECT COUNT(*) from content_album")
    int countTotalAlbums();

    @Query("SELECT * from content_album ORDER BY latest_timestamp DESC")
    LiveData<List<ContentAlbum>> getDescendingDateOrder();

    @Query("SELECT file_directory from content_album")
    List<String> getAlbumNames();

    @Delete
    void delete(ContentAlbum contentAlbum);
}
