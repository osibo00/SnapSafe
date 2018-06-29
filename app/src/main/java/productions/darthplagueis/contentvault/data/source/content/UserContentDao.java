package productions.darthplagueis.contentvault.data.source.content;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import productions.darthplagueis.contentvault.data.UserContent;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserContentDao {

    @Insert(onConflict = REPLACE)
    void insert(UserContent... userContents);

    @Update(onConflict = REPLACE)
    void update(UserContent... userContents);

    @Delete
    void delete(UserContent... userContents);

    @Query("DELETE from user_content")
    void deleteAll();

    @Query("SELECT COUNT(*) from user_content")
    int countUserContentTotal();

    @Query("SELECT * from user_content")
    LiveData<List<UserContent>> getAllUserContent();

    @Query("SELECT * from user_content ORDER BY time_stamp DESC")
    LiveData<List<UserContent>> getDescendingDateOrder();

    @Query("SELECT * from user_content ORDER BY time_stamp ASC")
    LiveData<List<UserContent>> getAscendingDateOrder();

    @Query("SELECT * from user_content ORDER BY file_directory ASC")
    LiveData<List<UserContent>> getAlbumOrderAZ();

    @Query("SELECT * from user_content ORDER BY file_directory DESC")
    LiveData<List<UserContent>> getAlbumOrderZA();

    @Query("SELECT * from user_content WHERE file_directory LIKE :directory")
    LiveData<List<UserContent>> getContentFromDirectory(String directory);

    @Query("SELECT * from user_content WHERE file_directory LIKE :directory" +
            " ORDER BY time_stamp DESC LIMIT 1")
    LiveData<List<UserContent>> getLastItem(String directory);

    @Query("SELECT * from user_content WHERE content_tag LIKE :tag")
    LiveData<List<UserContent>> getContentByTag(String tag);

    @Query("SELECT * from user_content WHERE import_date LIKE :date")
    LiveData<List<UserContent>> getContentByDate(String date);

    @Query("SELECT * from user_content ORDER BY time_stamp DESC")
    List<UserContent> getUserContentList();
}

