package productions.darthplagueis.contentvault.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@Entity(tableName = "user_content")
public class UserContent implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "file_name")
    private final String fileName;

    @NonNull
    @ColumnInfo(name = "import_date")
    private final String importDate;

    @ColumnInfo(name = "time_stamp")
    private final long timeStamp;

    @NonNull
    @ColumnInfo(name = "file_path")
    private String filePath;

    @NonNull
    @ColumnInfo(name = "file_directory")
    private String fileDirectory;

    @Nullable
    @ColumnInfo(name = "content_tag")
    private String contentTag;

    @ColumnInfo(name = "is_favorite")
    private boolean isFavorite;

    public UserContent(@NonNull String fileName,
                       @NonNull String importDate,
                       long timeStamp,
                       @NonNull String filePath,
                       @NonNull String fileDirectory,
                       @Nullable String contentTag,
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
                       @NonNull String importDate,
                       long timeStamp,
                       @NonNull String filePath,
                       @NonNull String fileDirectory) {
        this(fileName, importDate, timeStamp, filePath, fileDirectory, null, false);
    }

    @Ignore
    public UserContent(@NonNull String fileName,
                       @NonNull String importDate,
                       long timeStamp,
                       @NonNull String filePath,
                       @NonNull String fileDirectory,
                       @Nullable String contentTag) {
        this(fileName, importDate, timeStamp, filePath, fileDirectory, contentTag, false);
    }

    @Ignore
    protected UserContent(Parcel in) {
        id = in.readInt();
        fileName = in.readString();
        importDate = in.readString();
        timeStamp = in.readLong();
        filePath = in.readString();
        fileDirectory = in.readString();
        contentTag = in.readString();
        isFavorite = in.readByte() != 0;
    }

    @Ignore
    public static final Creator<UserContent> CREATOR = new Creator<UserContent>() {
        @Override
        public UserContent createFromParcel(Parcel in) {
            return new UserContent(in);
        }

        @Override
        public UserContent[] newArray(int size) {
            return new UserContent[size];
        }
    };

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

    public void setFilePath(@NonNull String filePath) {
        this.filePath = filePath;
    }

    public void setFileDirectory(@NonNull String fileDirectory) {
        this.fileDirectory = fileDirectory;
    }

    public void setContentTag(@Nullable String contentTag) {
        this.contentTag = contentTag;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Ignore
    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(fileName);
        dest.writeString(importDate);
        dest.writeLong(timeStamp);
        dest.writeString(filePath);
        dest.writeString(fileDirectory);
        dest.writeString(contentTag);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
    }
}

