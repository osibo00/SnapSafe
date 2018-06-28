package productions.darthplagueis.contentvault.util.filemanager;

import java.io.File;

import productions.darthplagueis.contentvault.data.UserContent;

public interface FileManagerCallBack {

    interface SaveFileCallBack {
        void onFileSaved(File file);
    }

    interface MoveFileCallBack {
        void onAlbumCreated(File file, UserContent userContent);

        void onFileMoved(File file, UserContent userContent);
    }

    interface CopyFileCallBack {
        void onFileCopied(File file);
    }
}
