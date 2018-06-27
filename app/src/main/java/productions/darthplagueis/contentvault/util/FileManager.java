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

import productions.darthplagueis.contentvault.data.UserContent;

public class FileManager implements FileManagerCallBack {

    public static final String DEFAULT_DIRECTORY_TAG = "snapsafe_content";

    private String currentDirectoryName = DEFAULT_DIRECTORY_TAG;
    private String newDirectoryName = "";
    private String fileExtension = ".jpg";

    private Context context;

    private AppExecutors appExecutors;

    private FileManager() {
    }

    public FileManager(Context context) {
        this.context = context;
        appExecutors = new AppExecutors();
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

    public void saveBitmapAsync(Bitmap bitmap, SaveFileCallBack callBack) {
        Runnable runnable = () -> {
            File file = new File(directoryCheck(DEFAULT_DIRECTORY_TAG), createFileName());
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
            appExecutors.getMainThread().execute(() -> callBack.onFileSaved(file));
        };
        appExecutors.getDiskIO().execute(runnable);
    }

    public Bitmap load(String fileName) {
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
            file.delete();
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

    public void moveFileAsync(UserContent userContent, String directoryName, boolean isAlbumCreation, MoveFileCallBack callBack) {
        Runnable runnable = () -> {
            File file = new File(userContent.getFilePath());
            File newFile = new File(directoryCheck(directoryName), createFileName());
            FileChannel outputChannel = null;
            FileChannel inputChannel = null;
            try {
                outputChannel = new FileOutputStream(newFile).getChannel();
                inputChannel = new FileInputStream(file).getChannel();
                inputChannel.transferTo(0, inputChannel.size(), outputChannel);
                inputChannel.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputChannel != null) inputChannel.close();
                    if (outputChannel != null) outputChannel.close();
                    boolean isDeleted = file.delete();
                    Log.i("FileManager", "Delete: file deleted=" + isDeleted + " file exists=" + file.exists());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.i("FileManager", "moveFileAsync: newFile exists=" + newFile.exists());
            appExecutors.getMainThread().execute(() -> {
                if (isAlbumCreation) {
                    callBack.onAlbumCreated(newFile, userContent);
                } else {
                    callBack.onFileMoved(newFile, userContent);
                }
            });
        };
        appExecutors.getDiskIO().execute(runnable);
    }

    public File copyFile(String fileName) {
        File file = new File(directoryCheck(currentDirectoryName), fileName);
        File newFile = new File(directoryCheck(DEFAULT_DIRECTORY_TAG), createFileName());
        FileChannel outputChannel = null;
        FileChannel inputChannel = null;
        try {
            outputChannel = new FileOutputStream(newFile).getChannel();
            inputChannel = new FileInputStream(file).getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
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

    public void copyFileAsync(UserContent userContent, CopyFileCallBack callBack) {
        Runnable runnable = () -> {
            File file = new File(userContent.getFilePath());
            File newFile = new File(directoryCheck(DEFAULT_DIRECTORY_TAG), createFileName());
            FileChannel outputChannel = null;
            FileChannel inputChannel = null;
            try {
                outputChannel = new FileOutputStream(newFile).getChannel();
                inputChannel = new FileInputStream(file).getChannel();
                inputChannel.transferTo(0, inputChannel.size(), outputChannel);
                inputChannel.close();
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
            appExecutors.getMainThread().execute(() -> callBack.onFileCopied(newFile));
        };
        appExecutors.getDiskIO().execute(runnable);
    }

    public void deleteFileAsync(UserContent userContent) {
        Runnable runnable = () -> {
            File file = new File(userContent.getFilePath());
            try {
                boolean isDeleted = file.delete();
                Log.i("FileManager", "deleteFileAsync: " + isDeleted);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        appExecutors.getDiskIO().execute(runnable);
    }

    private File directoryCheck(String directoryName) {
        File directory = context.getDir(directoryName, Context.MODE_PRIVATE);
        if (!directory.exists() && !directory.mkdirs()) {
            Log.e("FileManager", "Error creating directory: " + directory);
        }
        return directory;
    }

    private String createFileName() {
        return String.valueOf(CurrentDateUtil.getTimeStamp()) + fileExtension;
    }
}
