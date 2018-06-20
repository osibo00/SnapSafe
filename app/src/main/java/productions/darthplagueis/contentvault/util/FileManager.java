package productions.darthplagueis.contentvault.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileManager {

    public static final String DEFAULT_DIRECTORY_TAG = "user_content_album";

    private String currentDirectoryName = DEFAULT_DIRECTORY_TAG;
    private String newDirectoryName = "";
    private String fileName = "";
    private String fileExtension = ".jpg";

    private Context context;

    private FileManager() {
    }

    public FileManager(Context context) {
        this.context = context;
    }

    public FileManager retrieveFile(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
        return this;
    }

    public void setCurrentDirectoryName(String currentDirectoryName) {
        this.currentDirectoryName = currentDirectoryName;
    }

    public void setNewDirectoryName(String newDirectoryName) {
        this.newDirectoryName = newDirectoryName;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public File saveBitmap(Bitmap bitmap) {
        File file = new File(directoryCheck(currentDirectoryName), createFileName());
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public Bitmap load() {
        File file = new File(directoryCheck(currentDirectoryName), fileName);
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public File moveFile(String fileName) {
        File file = new File(directoryCheck(currentDirectoryName), fileName);
        File newFile = new File(directoryCheck(newDirectoryName), createFileName());
        FileChannel outputChannel = null;
        FileChannel inputChannel = null;
        try {
            outputChannel = new FileOutputStream(newFile).getChannel();
            inputChannel = new FileInputStream(file).getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
            deleteFile(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputChannel != null) inputChannel.close();
                if (outputChannel != null) outputChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return newFile;
    }

    public void deleteFile(String fileName) {
        File file = new File(directoryCheck(currentDirectoryName), fileName);
        try {
            boolean isDeleted = file.delete();
            Log.i("FileManager", "deleteFile: " + isDeleted);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File directoryCheck(String directoryName) {
        File directory = context.getDir(directoryName, Context.MODE_PRIVATE);
        if (!directory.exists() && !directory.mkdirs()) {
            Log.e("FileManager", "Error creating directory " + directory);
        }
        return directory;
    }

    private String createFileName() {
        return String.valueOf(CurrentDateUtil.getTimeStamp()) + fileExtension;
    }
}
