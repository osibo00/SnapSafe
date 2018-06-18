package productions.darthplagueis.contentvault.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@Entity(tableName = "user_content")
public class UserContent {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "file_name")
    private final String fileName;

    @NonNull
    @ColumnInfo(name = "file_path")
    private final String filePath;

    @NonNull
    @ColumnInfo(name = "file_directory")
    private final String fileDirectory;

    @NonNull
    @ColumnInfo(name = "import_date")
    private final String importDate;

    @ColumnInfo(name = "time_stamp")
    private final long timeStamp;

    @Nullable
    @ColumnInfo(name = "content_tag")
    private String contentTag;

    @ColumnInfo(name = "is_favorite")
    private boolean isFavorite;

    public UserContent(@NonNull String fileName,
                       @NonNull String filePath,
                       @NonNull String fileDirectory,
                       @NonNull String importDate,
                       @Nullable String contentTag,
                       long timeStamp,
                       boolean isFavorite) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileDirectory = fileDirectory;
        this.importDate = importDate;
        this.timeStamp = timeStamp;
        this.contentTag = contentTag;
        this.isFavorite = isFavorite;
    }

    @Ignore
    public UserContent(@NonNull String fileName,
                       @NonNull String filePath,
                       @NonNull String fileDirectory,
                       @NonNull String importDate,
                       long timeStamp) {
        this(fileName, filePath, fileDirectory, importDate,
                null, timeStamp, false);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getFileName() {
        return fileName;
    }

    @NonNull
    public String getFilePath() {
        return filePath;
    }

    @NonNull
    public String getFileDirectory() {
        return fileDirectory;
    }

    @NonNull
    public String getImportDate() {
        return importDate;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    @Nullable
    public String getContentTag() {
        return contentTag;
    }

    public boolean isFavorite() {
        return isFavorite;
    }
}

