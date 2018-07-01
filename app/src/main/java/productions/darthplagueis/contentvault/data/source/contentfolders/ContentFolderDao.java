package productions.darthplagueis.contentvault.data.source.contentfolders;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import productions.darthplagueis.contentvault.data.ContentFolder;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ContentFolderDao {

    @Insert(onConflict = REPLACE)
    void insert(ContentFolder... contentFolders);

    @Delete
    void delete(ContentFolder... contentFolders);

    @Query("DELETE from content_folders")
    void deleteAll();

    @Query("SELECT COUNT(*) from content_folders")
    int countTotalAlbums();

    @Query("SELECT * from content_folders")
    LiveData<List<ContentFolder>> getAllAlbums();

    @Query("SELECT * from content_folders ORDER BY latest_timestamp DESC")
    LiveData<List<ContentFolder>> getDescendingDateOrder();

    @Query("SELECT file_directory from content_folders")
    List<String> getAlbumNames();
}
