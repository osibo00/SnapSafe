package productions.darthplagueis.contentvault.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@Entity(tableName = "content_album")
public class ContentAlbum {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "file_directory")
    private String albumName;

    @NonNull
    @ColumnInfo(name = "directory_icon")
    private String directoryIcon;

    @NonNull
    @ColumnInfo(name = "creation_date")
    private String creationDate;

    @ColumnInfo(name = "latest_timestamp")
    private long latestTimestamp;

    @Nullable
    @ColumnInfo(name = "album_tag")
    private String albumTag;

    public ContentAlbum(@NonNull String albumName,
                        @NonNull String directoryIcon,
                        @NonNull String creationDate,
                        long latestTimestamp,
                        @Nullable String albumTag) {
        this.albumName = albumName;
        this.directoryIcon = directoryIcon;
        this.creationDate = creationDate;
        this.latestTimestamp = latestTimestamp;
        this.albumTag = albumTag;
    }

    @Ignore
    public ContentAlbum(@NonNull String albumName,
                        @NonNull String directoryIcon,
                        @NonNull String creationDate,
                        long latestTimestamp) {
        this(albumName, directoryIcon, creationDate, latestTimestamp, null);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getAlbumName() {
        return albumName;
    }

    @NonNull
    public String getDirectoryIcon() {
        return directoryIcon;
    }

    @NonNull
    public String getCreationDate() {
        return creationDate;
    }

    public long getLatestTimestamp() {
        return latestTimestamp;
    }

    @Nullable
    public String getAlbumTag() {
        return albumTag;
    }
}
