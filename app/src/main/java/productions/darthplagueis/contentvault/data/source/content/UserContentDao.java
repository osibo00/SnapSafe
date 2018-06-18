package productions.darthplagueis.contentvault.data.source.content;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import productions.darthplagueis.contentvault.data.UserContent;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserContentDao {

    @Insert(onConflict = REPLACE)
    void insert(UserContent... userContent);

    @Query("DELETE from user_content")
    void deleteAll();

    @Query("SELECT * from user_content")
    LiveData<List<UserContent>> getAllUserContent();

    @Query("SELECT COUNT(*) from user_content")
    int countUserItemTotal();

    @Query("SELECT * from user_content ORDER BY time_stamp DESC")
    LiveData<List<UserContent>> getDescendingDateOrder();

    @Query("SELECT * from user_content ORDER BY time_stamp ASC")
    LiveData<List<UserContent>> getAscendingDateOrder();

    @Query("SELECT * from user_content ORDER BY file_directory ASC")
    LiveData<List<UserContent>> getAlbumOrder();

    @Query("SELECT * from user_content WHERE file_directory LIKE :directory")
    LiveData<List<UserContent>> getContentFromDirectory(String directory);

    @Query("SELECT * from user_content WHERE file_directory LIKE :directory" +
            " ORDER BY time_stamp DESC LIMIT 1")
    LiveData<List<UserContent>> getLastItem(String directory);

    @Query("SELECT * from user_content WHERE content_tag LIKE :tag")
    LiveData<List<UserContent>> getContentByTag(String tag);

    @Query("SELECT * from user_content WHERE import_date LIKE :date")
    LiveData<List<UserContent>> getContentByDate(String date);

    @Delete
    void delete(UserContent userContent);
}

